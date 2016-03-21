import json
import logging
import re
from ManifestParser import ManifestParser
from ConfigParser import ConfigParser

class Compare():
    """compare info with definitions in specified manifest file for cloud foundry (manifest.yml) or configuration file for Spring (application.yml)"""
    def __init__(self, use_config_file, file_path):
        self.use_config_file = use_config_file
        self.file_path = file_path
        if use_config_file:
            self.parser = ConfigParser(file_path)
        else:
            self.parser = ManifestParser(file_path)

    def get_services_name_and_its_plans_name(self):
        """return dict of service name and its plans name: {service_name:[plans_name]}""" 
        return self.parser.get_services_name_and_its_plans_name()

    def get_services_name(self):
        """return list [services_name]"""
        return self.parser.get_services_name()

    def get_services_id_and_name(self):
        """return dict {service_id:service_name}"""
        return self.parser.get_services_id_and_name()

    def get_service_and_plan_id_from_name(self, target_service_name, target_plan_name):
        """return tuple (service_id, plan_id)"""
        return self.parser.get_service_and_plan_id_from_name(target_service_name, target_plan_name)

    def service_catalog_info_should_match_configuration(self, cf_service_info_path, config_service_id):
        cf_service_plan_info_str = open(cf_service_info_path, 'r').read()
        cf_service_info_split = re.split("GET /v2/service_plans", cf_service_plan_info_str)
        cf_service_info = self.parse_cf_service_info(cf_service_info_split[0])
        configured_service_info = self.parser.get_configured_service_info(config_service_id)
        for propertyname in configured_service_info.keys():
            if cf_service_info[propertyname] != configured_service_info[propertyname]:
                raise AssertionError('info [%s] in service: %r, not match in broker manifest configuration: %r' % (propertyname, cf_service_info[propertyname], configured_service_info[propertyname]))
            else:
                logging.info('info [%s] in service: %r, match in broker manifest configuration: %r' % (propertyname, cf_service_info[propertyname], configured_service_info[propertyname]))
        cf_plans_info = sorted(self.parse_cf_plans_info(cf_service_info_split[1]), key=lambda k: k['name'])  
        configured_plans_info = sorted(self.parser.get_configured_plans_info(config_service_id), key=lambda k: k['name'])  
        for cf_plan_info, configured_plan_info in zip(cf_plans_info, configured_plans_info):
            for propertyname in configured_plan_info.keys():
                if cf_plan_info[propertyname] != configured_plan_info[propertyname]:
                    raise AssertionError('info [%s] in service plan[%s]: %r, not match plan[%s] in broker manifest configuration: %r' % (propertyname, cf_plan_info['name'], cf_plan_info[propertyname], configured_plan_info['name'], configured_plan_info[propertyname]))
                else:
                    logging.info('info [%s] in service plan[%s]: %r, match plan[%s] in broker manifest configuration: %r' % (propertyname, cf_plan_info['name'], cf_plan_info[propertyname], configured_plan_info['name'], configured_plan_info[propertyname]))


    def service_key_info_should_match_configuration(self, cf_service_key_info, service_name, plan_name):
        cf_service_key_info = '{' + cf_service_key_info.split('{', 1)[1]
        config_credentials = self.parser.get_configured_credential_info(service_name, plan_name)
        if cmp(json.loads(cf_service_key_info), config_credentials) != 0:
            raise AssertionError('service key for service [%s] plan [%s]: %r, not match its configuration: %r' % (service_name, plan_name, json.loads(cf_service_key_info), config_credentials))
        else:
            logging.info('service key for service [%s] plan [%s]: %r, match its configuration: %r' % (service_name, plan_name, json.loads(cf_service_key_info), config_credentials))    

    def bound_app_env_credential_info_should_match_configuration(self, cf_app_env, service_name, plan_name, service_instance_name):
        cf_app_env = cf_app_env.split(' "VCAP_SERVICES": ', 1)[1]
        cf_app_env = cf_app_env.split('\n }', 1)[0] + '}'
        # application may be bound to multiple service instances
        for service_instance_credential in json.loads(cf_app_env)[service_name]: 
            if service_instance_credential['name'] == service_instance_name:
                cf_credential_info = service_instance_credential['credentials']
        config_credentials = self.parser.get_configured_credential_info(service_name, plan_name)
        if cmp(cf_credential_info, config_credentials) != 0:
            raise AssertionError('credentials info in the bound application env for service [%s] plan [%s]: %r, not match its configuration: %r' % (service_name, plan_name, cf_credential_info, config_credentials))
        else:
            logging.info('credentials info in the bound application env for service [%s] plan [%s]: %r, match its configuration: %r' % (service_name, plan_name, cf_credential_info, config_credentials))

    def parse_cf_plans_info(self, cf_plans_info_str):
        plans_info = []
        cf_plan_info_json = re.findall(r'(\{.*\})', cf_plans_info_str, re.M | re.S)[0] 
        cf_plan_info = json.loads(cf_plan_info_json)
        plans_number = cf_plan_info['total_results']
        for plan_index in xrange(0, plans_number - 1):
            plan_info = cf_plan_info['resources'][plan_index]['entity']
            plan_info['extra'] = json.loads(plan_info['extra'])
            plans_info.append(plan_info)
        return plans_info

    def parse_cf_service_info(self, cf_service_info_str):
        cf_service_info = {}
        cf_service_info_json = re.findall(r'(\{.*\})', cf_service_info_str, re.M | re.S)[0] 
        cf_service_info = json.loads(cf_service_info_json)['resources'][0]['entity']
        cf_service_info['extra'] = json.loads(cf_service_info['extra'])
        return cf_service_info