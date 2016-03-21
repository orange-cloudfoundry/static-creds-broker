import logging
import yaml
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
        pass

    def get_configured_service_info(self, config_service_id):
        pass

    def get_configured_plans_info(self, config_service_id):
        pass