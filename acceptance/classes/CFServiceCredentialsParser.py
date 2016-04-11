import json
import logging
import re

class CFServiceCredentialsParser():
    """compare info with definitions in specified manifest file for cloud foundry (manifest.yml) or configuration file for Spring (application.yml)"""
    ROBOT_LIBRARY_SCOPE = "GLOBAL"

    def parse_service_key_info(self, cf_service_key_info_str):
        cf_service_key_info = re.findall(r'(\{.*\})', cf_service_key_info_str, re.M | re.S)[0]
        return json.loads(cf_service_key_info)

    def parse_app_env_credential_info(self, cf_app_env, service_name, service_instance_name):
        cf_app_env_sys_list = re.findall('System-Provided:(.*)User-Provided:', cf_app_env, re.M | re.S)
        if len(cf_app_env_sys_list) != 0:
            cf_app_env_sys = cf_app_env_sys_list[0]
        else:
            cf_app_env_sys = re.findall('System-Provided:(.*)No user-defined env', cf_app_env, re.M | re.S)[0]
        cf_services_credentials_str = re.findall(r'\{\s*"VCAP_SERVICES":(.*)\}\s*\{\s*"VCAP_APPLICATION":.*\}', cf_app_env_sys, re.M | re.S)[0]
        cf_service_credentials = json.loads(cf_services_credentials_str)[service_name]
        return next(credential['credentials'] for credential in cf_service_credentials if credential['name'] == service_instance_name)