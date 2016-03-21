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
        configured_catalog_info = {}
        # the correspond property name between service info retrived from cloud foundry and in manifest_env definition
        propertyname_correspond = {
            'label':'SERVICES_' + config_service_id + '_NAME', 
            'description':'SERVICES_' + config_service_id + '_DESCRIPTION', 
            'bindable':'SERVICES_' + config_service_id + '_BINDEABLE', 
            'tags': 'SERVICES_' + config_service_id + '_TAGS'
        }
        property_defaultvalue = {'bindable':True, 'tags':''}
        for propertyname_service, propertyname_manifest in propertyname_correspond.iteritems():
            manifest_env_property_value = self.manifest_env.get(propertyname_manifest, property_defaultvalue.get(propertyname_service));
            if propertyname_service == 'bindable':
                configured_catalog_info[propertyname_service] = bool(manifest_env_property_value)
            elif propertyname_service == 'tags':
                configured_catalog_info[propertyname_service] = filter(None, manifest_env_property_value.split(','))
            else:
                configured_catalog_info[propertyname_service] = manifest_env_property_value

        metadata_propertyname_correspond = {
            'displayName':'SERVICES_' + config_service_id + '_METADATA_DISPLAYNAME', 
            'imageUrl':'SERVICES_' + config_service_id + '_METADATA_IMAGEURL', 
            'supportUrl':'SERVICES_' + config_service_id + '_METADATA_SUPPORTURL', 
            'documentationUrl':'SERVICES_' + config_service_id + '_METADATA_DOCUMENTATIONURL', 
            'providerDisplayName':'SERVICES_' + config_service_id + '_METADATA_PROVIDERDISPLAYNAME', 
            'longDescription':'SERVICES_' + config_service_id + '_METADATA_LONGDESCRIPTION'
        }
        metadata_property_defaultvalue = {'displayName':self.manifest_env.get('SERVICES_' + config_service_id + '_NAME'),
            'imageUrl':'', 'supportUrl':'', 'documentationUrl':'', 'providerDisplayName':'', 'longDescription':''}
        metadata_info = {}
        for propertyname_metadata, propertyname_manifest in metadata_propertyname_correspond.iteritems():
            manifest_env_property_value = self.manifest_env.get(propertyname_manifest, metadata_property_defaultvalue.get(propertyname_metadata));
            metadata_info[propertyname_metadata] = manifest_env_property_value
        configured_catalog_info['extra'] = metadata_info
        return configured_catalog_info

    def get_configured_plans_info(self, config_service_id):
        manifest_plans_info = []
        manifest_plans_id_and_name = self.services_plans_id_and_name[config_service_id][1]
        for plan_id, plan_name in manifest_plans_id_and_name.iteritems():
            manifest_plan_info = {}
            manifest_plan_info['name'] = plan_name
            # the correspond property name between in cf_plan_info and in manifest_env 
            propertyname_correspond = { 
                'description':'SERVICES_%s_PLAN_%s_DESCRIPTION' %(config_service_id, plan_id),
                'free':'SERVICES_%s_PLAN_%s_FREE' %(config_service_id, plan_id)}
            property_defaultvalue = { 
                'description':'plan ' + plan_name, 
                'free':True}
            for propertyname_plan, propertyname_manifest in propertyname_correspond.iteritems():
                manifest_env_value = self.manifest_env.get(propertyname_manifest, property_defaultvalue.get(propertyname_plan));
                manifest_plan_info[propertyname_plan] = manifest_env_value
                if propertyname_plan == 'free':
                    manifest_plan_info[propertyname_plan] = bool(manifest_env_value)
            plan_metadata_propertyname = 'SERVICES_%s_PLAN_%s_METADATA' %(config_service_id, plan_id)
            if plan_metadata_propertyname not in self.manifest_env:
                manifest_plan_info['extra'] = {"bullets":None,"costs":None,"displayName":None}
            else:
                manifest_plan_info['extra'] = self.manifest_env[plan_metadata_propertyname]
            manifest_plans_info.append(manifest_plan_info)
        return manifest_plans_info