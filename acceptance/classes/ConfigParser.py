import logging
import yaml
import json
from Parser import Parser

class ConfigParser(Parser):
    def __init__(self, file_path):
        with open(file_path, 'r') as f:
            doc = yaml.load(f)
            self.service_config = doc['services']
        super(ConfigParser, self).__init__()
        
    def get_services_plans_id_and_name(self): 
        """return dict of service id and name with its plans id and name: {service_id:(service_name, {plan_id:plan_name})}"""
        services = {} 
        for service_id, service_properties in self.service_config.items():
            plans = {}
            if service_properties.get('PLAN') == None:
                plans['0'] = 'default'
            else:
                for plan_id, plan_properties in service_properties.get('PLAN').items():
                    plans[plan_id] = plan_properties.get('NAME', plan_id)
            services[service_id] = (service_properties.get('NAME'), plans)
        return services

    def get_configured_credential_info(self, service_name, plan_name): 
        credentials = {}
        service_id, plan_id = self.get_service_and_plan_id_from_name(service_name, plan_name)
        assert self.service_config.get(service_id) != None
        service_credentials = self.service_config.get(service_id).get('CREDENTIALS')
        if type(service_credentials) is str:
            service_credentials = json.loads(service_credentials)
        if service_credentials:
            credentials.update(service_credentials)
        plans_properties = self.service_config.get(service_id).get('PLAN')
        if plans_properties:
            plan_credentials = plans_properties.get(plan_id).get('CREDENTIALS')
            if type(plan_credentials) is str:
                plan_credentials = json.loads(plan_credentials)
            if plan_credentials:
                credentials.update(plan_credentials)
        return credentials

    def get_configured_service_info(self, config_service_id):
        assert self.service_config.get(config_service_id) != None
        service_properties_definition = self.service_config.get(config_service_id)
        config_service_propertyname = ['NAME', 'DESCRIPTION', 'BINDEABLE', 'TAGS']
        meta_properties_definition = self.service_config.get(config_service_id).get('METADATA')
        config_meta_propertyname = ['DISPLAYNAME', 'IMAGEURL', 'SUPPORTURL', 'DOCUMENTATIONURL', 'PROVIDERDISPLAYNAME', 'LONGDESCRIPTION']
        return self.get_configured_service_info_pattern(service_properties_definition, config_service_propertyname, meta_properties_definition, config_meta_propertyname)

    def get_configured_plans_info(self, config_service_id):
        assert self.service_config.get(config_service_id) != None
        assert self.services_plans_id_and_name.get(config_service_id) != None
        assert self.services_plans_id_and_name[config_service_id][1] != None
        plans_properties_definition  = {}
        config_plans_propertyname ={}
        config_plans_id_and_name = self.services_plans_id_and_name[config_service_id][1]
        for plan_id in config_plans_id_and_name.keys():
            propertyname_correspond = ['DESCRIPTION', 'FREE', 'METADATA']
            plans_properties_definition[plan_id] = self.service_config.get(config_service_id).get('PLAN', {}).get(plan_id, {})
            config_plans_propertyname[plan_id] = propertyname_correspond
        return self.get_configured_plans_info_pattern(config_plans_id_and_name, plans_properties_definition, config_plans_propertyname)