from Parser import Parser

class ConfigParser(Parser):
    def __init__(self, file_path):
        super(ConfigParser, self).__init__(True, file_path)
        
    def get_services_plans_id_and_name(self): 
        pass
        
    def get_configured_credential_info(self, service_name, plan_name): 
        pass

    def get_configured_service_info(self, config_service_id):
        pass

    def get_configured_plans_info(self, config_service_id):
        pass