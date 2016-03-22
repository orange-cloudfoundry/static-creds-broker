import logging
import yaml
import json
import re
from Parser import Parser

class ManifestParser(Parser):
    def __init__(self, file_path):
        with open(file_path, 'r') as f:
            doc = yaml.load(f)
            self.manifest_env = doc['applications'][0]['env']
        super(ManifestParser, self).__init__()

    def get_services_plans_id_and_name(self):
        services = {}  
        service_id_pattern = r'((?!_PLAN_)(?!_CREDENTIALS).)+'
        plan_id_pattern = r'((?!_CREDENTIALS).)+'
        service_name_pattern = r'^SERVICES_(?P<serviceid>%s)_NAME$' %service_id_pattern
        plan_name_pattern = r'^SERVICES_(?P<serviceid>%s)_PLAN_(?P<planid>%s)_NAME$' %(service_id_pattern, plan_id_pattern)
        plan_propertyname_pattern = r'DESCRIPTION$|METADATA$|FREE$|CREDENTIALS'
        plan_property_pattern = r'^SERVICES_(?P<serviceid>%s)_PLAN_(?P<planid>%s)_%s$' %(service_id_pattern, plan_id_pattern, plan_propertyname_pattern)
        for propertyname, propertyvalue in self.manifest_env.iteritems():
            service_name_match_result = re.match(service_name_pattern, propertyname)
            if service_name_match_result:
                service_id = service_name_match_result.group('serviceid')
                service_name = propertyvalue
                service_plans = services.get(service_id, (None, {}))[1]
                services[service_id] = (service_name, service_plans)
            else:
                plan_name_match_result = re.match(plan_name_pattern, propertyname)
                if plan_name_match_result:
                    service_id = plan_name_match_result.group('serviceid')
                    plan_id = plan_name_match_result.group('planid')
                    plan_name = propertyvalue
                    service = services.get(service_id, (None, {}))
                    service[1][plan_id] = plan_name
                    services[service_id] = service
        # add plan default name for services which not no plan defined, assign it id: '0'
        for service_value in services.values():
            if len(service_value[1]) == 0:
                service_value[1]['0'] = 'default'
        # add plan default name for plans defined without name
        for propertyname, propertyvalue in self.manifest_env.iteritems():
            plan_property_match_result = re.match(plan_property_pattern, propertyname)
            if plan_property_match_result:
                service_id = plan_property_match_result.group('serviceid')
                plan_id = plan_property_match_result.group('planid')
                service = services.get(service_id)
                if service != None and service[1] != None and service[1][plan_id] == None:
                    service[1][plan_id] = plan_id
                    services[service_id] = service
        return services

    def get_configured_credential_info(self, service_name, plan_name):
        manifest_credentials = {}
        service_id, plan_id = self.get_service_and_plan_id_from_name(service_name, plan_name)
        service_credential_json = 'SERVICES_%s_CREDENTIALS' %service_id
        plan_credential_json = 'SERVICES_%s_PLAN_%s_CREDENTIALS' %(service_id, plan_id)
        service_credential_json_value = self.manifest_env.get(service_credential_json)
        if service_credential_json_value:
            manifest_credentials.update(json.loads(service_credential_json_value))
        plan_credential_json_value = self.manifest_env.get(plan_credential_json)
        if plan_credential_json_value:
            manifest_credentials.update(json.loads(plan_credential_json_value))

        service_credential_property_pattern = r'^SERVICES_%s_CREDENTIALS_(?P<credentialProperty>.+)$' %service_id
        plan_credential_property_pattern = r'^SERVICES_%s_PLAN_%s_CREDENTIALS_(?P<credentialProperty>.+)$' %(service_id, plan_id)
        service_credential_properties  = {}
        plan_credential_properties = {}
        for propertyname, propertyvalue in self.manifest_env.iteritems():
            service_credential_property_match_result = re.match(service_credential_property_pattern, propertyname)
            if service_credential_property_match_result:
                credentialProperty = service_credential_property_match_result.group('credentialProperty')
                service_credential_properties[credentialProperty] = propertyvalue
            plan_credential_property_match_result = re.match(plan_credential_property_pattern, propertyname)
            if plan_credential_property_match_result:
                credentialProperty = plan_credential_property_match_result.group('credentialProperty')
                plan_credential_properties[credentialProperty] = propertyvalue
        manifest_credentials.update(service_credential_properties)
        manifest_credentials.update(plan_credential_properties)
        return manifest_credentials

    def get_configured_service_info(self, config_service_id):
        service_properties_definition = self.manifest_env
        config_service_propertyname = ['SERVICES_' + config_service_id + '_NAME', 
            'SERVICES_' + config_service_id + '_DESCRIPTION', 
            'SERVICES_' + config_service_id + '_BINDEABLE', 
            'SERVICES_' + config_service_id + '_TAGS']
        meta_properties_definition = self.manifest_env
        config_meta_propertyname = ['SERVICES_' + config_service_id + '_METADATA_DISPLAYNAME', 
            'SERVICES_' + config_service_id + '_METADATA_IMAGEURL', 
            'SERVICES_' + config_service_id + '_METADATA_SUPPORTURL', 
            'SERVICES_' + config_service_id + '_METADATA_DOCUMENTATIONURL', 
            'SERVICES_' + config_service_id + '_METADATA_PROVIDERDISPLAYNAME', 
            'SERVICES_' + config_service_id + '_METADATA_LONGDESCRIPTION']
        return self.get_configured_service_info_pattern(service_properties_definition, config_service_propertyname, meta_properties_definition, config_meta_propertyname)

    def get_configured_plans_info(self, config_service_id):
        assert self.services_plans_id_and_name.get(config_service_id) != None
        assert self.services_plans_id_and_name[config_service_id][1] != None
        plans_properties_definition  = {}
        config_plans_propertyname ={}
        config_plans_id_and_name = self.services_plans_id_and_name[config_service_id][1]
        for plan_id in config_plans_id_and_name.keys():
            propertyname_correspond = ['SERVICES_%s_PLAN_%s_DESCRIPTION' %(config_service_id, plan_id),
                'SERVICES_%s_PLAN_%s_FREE' %(config_service_id, plan_id),
                'SERVICES_%s_PLAN_%s_METADATA' %(config_service_id, plan_id)]
            plans_properties_definition[plan_id] = self.manifest_env
            config_plans_propertyname[plan_id] = propertyname_correspond
        return self.get_configured_plans_info_pattern(config_plans_id_and_name, plans_properties_definition, config_plans_propertyname)