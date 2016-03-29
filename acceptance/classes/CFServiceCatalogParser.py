import json
import logging
import re

class CFServiceCatalogParser():
    """compare info with definitions in specified manifest file for cloud foundry (manifest.yml) or configuration file for Spring (application.yml)"""
    ROBOT_LIBRARY_SCOPE = "TEST SUITE"

    def __init__(self, cf_service_info_path):
        self.service_name = cf_service_info_path.replace(".txt","")
        cf_service_complete_info_str = open(cf_service_info_path, 'r').read()
        if 'FAILED' in cf_service_complete_info_str:
            raise AssertionError('Service %s not appear in the marketplace.' %(self.service_name))
        if cf_service_complete_info_str.count("GET /v2/spaces/") != 1:
            cf_service_complete_info_split = re.split("GET /v2/spaces/", cf_service_complete_info_str)
            cf_service_complete_info_str = cf_service_complete_info_split[len(cf_service_complete_info_split) - 1]
        cf_service_plan_info_split = re.split("GET /v2/service_plans", cf_service_complete_info_str)
        self.cf_service_info = self.parse_cf_service_info(cf_service_plan_info_split[0])
        self.cf_plans_info = self.parse_cf_plans_info(cf_service_plan_info_split[1])

    def get_cloud_foundry_service_basic_catalog_info(self):
        return self.cf_service_info

    def get_cloud_foundry_service_metadata_catalog_info(self):
        return json.loads(self.cf_service_info['extra'])

    def get_cloud_foundry_service_plan_catalog_info(self, plan_name):
        return next(cf_plan_info for cf_plan_info in self.cf_plans_info if cf_plan_info['name'] == plan_name)
        
    def parse_cf_plans_info(self, cf_plans_info_str):
        plans_info = []
        cf_plan_info_json = re.findall(r'(\{.*\})', cf_plans_info_str, re.M | re.S)[0] 
        cf_plan_info = json.loads(cf_plan_info_json)
        plans_number = cf_plan_info['total_results']
        for plan_index in xrange(0, plans_number):
            plan_info = cf_plan_info['resources'][plan_index]['entity']
            plan_info['extra'] = json.loads(plan_info['extra'])
            plans_info.append(plan_info)
        return plans_info

    def parse_cf_service_info(self, cf_service_info_str):
        cf_service_info = {}
        cf_service_info_json = re.findall(r'(\{.*\})', cf_service_info_str, re.M | re.S)[0] 
        cf_service_info = json.loads(cf_service_info_json)['resources'][0]['entity']
        return cf_service_info