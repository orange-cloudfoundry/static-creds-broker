import logging
import yaml
import json
import re

class Staticreds(object):
    ROBOT_LIBRARY_SCOPE = "GLOBAL"

    def __init__(self, manifest_path):
        self.manifest_path = manifest_path
        self.services_plans_id_and_name = self.get_services_plans_id_and_name()

    # return dict of service id and name with its plans id and name: {service_id:(service_name, {plan_id:plan_name})}
    def get_services_plans_id_and_name(self):
        services = {}  
        with open(self.manifest_path, 'r') as f:
            doc = yaml.load(f)
            manifest_env = doc['applications'][0]['env']
            id_pattern = r'((?!_PLAN_)(?!_CREDENTIALS).)+'
            service_name_pattern = r'^SERVICES_(?P<serviceid>%s)_NAME$' %id_pattern
            plan_name_pattern = r'^SERVICES_(?P<serviceid>%s)_PLAN_(?P<planid>%s)_NAME$' %(id_pattern, id_pattern)
            for propertyname, propertyvalue in manifest_env.iteritems():
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
        return services

    # return dict of service name and its plans name: {service_name:[plans_name]}
    def get_services_name_and_its_plans_name(self):
        services_plans_name = {}
        for service_tuple in self.services_plans_id_and_name.values(): # [(service_name, {plan_id:plan_name})]
            services_plans_name[service_tuple[0]] = service_tuple[1].values()
        return services_plans_name

    def get_services_name(self):
        return dict(self.services_plans_id_and_name.values()).keys()

    def get_services_id_and_name(self):
        services_id_and_name = {}
        for service_id, service_tuple in self.services_plans_id_and_name.iteritems():
            services_id_and_name[service_id] = service_tuple[0]
        return services_id_and_name

    def service_catalog_info_should_match_manifest(self, cf_service_info_path, manifest_service_id):
        cf_service_info = open(cf_service_info_path, 'r').read()
        with open(self.manifest_path, 'r') as f:
            doc = yaml.load(f)
            manifest_env = doc['applications'][0]['env']

            # the correspond property name between in cf_service_info and in manifest_env (only for the properties whose value is string)
            propertyname_correspond = {'label':'SERVICES_' + manifest_service_id + '_NAME', 'description':'SERVICES_' + manifest_service_id + '_DESCRIPTION', 'bindable':'SERVICES_' + manifest_service_id + '_BINDEABLE'}
            property_defaultvalue = {'SERVICES_'+manifest_service_id+'_BINDEABLE':'true'}
            for propertyname_service, propertyname_manifest in propertyname_correspond.iteritems():
                manifest_env_value = manifest_env.get(propertyname_manifest, property_defaultvalue.get(propertyname_manifest));
                cf_service_info_value = re.findall(r'"%s": (.*?),' %propertyname_service, cf_service_info)[0]
                if cf_service_info_value.replace('"', '').lower() != str(manifest_env_value).replace('"', '').lower():
                    raise AssertionError('info [%s] in service: %r, not match [%s] in broker manifest configuration: %r' % (propertyname_service, cf_service_info_value, propertyname_manifest, manifest_env_value))
                else:
                    logging.info('info [%s] in service: %r, match [%s] in broker manifest configuration: %r' % (propertyname_service, cf_service_info_value, propertyname_manifest, manifest_env_value))

            # compare tags info which is a string array with default value: empty list
            propertyname_manifest = 'SERVICES_' + manifest_service_id + '_TAGS'
            cf_service_info_tags = re.findall(r'"tags": \[(.*?)\],', cf_service_info, re.M | re.S)[0].replace('\n', '')
            manifest_env_value = manifest_env.get(propertyname_manifest, '');
            if cf_service_info_tags.replace('"', '').replace(' ', '') != manifest_env_value.replace('"', '').replace(' ', ''):
                raise AssertionError('info [tags] in service: %r, not match [%s] in broker manifest configuration: %r' % (cf_service_info_tags, propertyname_manifest, manifest_env_value))
            else:
                logging.info('info [tags] in service: %r, match [%s] in broker manifest configuration: %r' % (cf_service_info_tags, propertyname_manifest, manifest_env_value))

            # for service metadata properties which appears in the extra of service_info as json string
            propertyname_correspond_metadata = {'displayName':'SERVICES_' + manifest_service_id + '_METADATA_DISPLAYNAME', 
                'imageUrl':'SERVICES_' + manifest_service_id + '_METADATA_IMAGEURL', 
                'supportUrl':'SERVICES_' + manifest_service_id + '_METADATA_SUPPORTURL', 
                'documentationUrl':'SERVICES_' + manifest_service_id + '_METADATA_DOCUMENTATIONURL', 
                'providerDisplayName':'SERVICES_' + manifest_service_id + '_METADATA_PROVIDERDISPLAYNAME', 
                'longDescription':'SERVICES_' + manifest_service_id + '_METADATA_LONGDESCRIPTION'}
            property_metadata_defaultvalue = {'SERVICES_' + manifest_service_id + '_METADATA_DISPLAYNAME':manifest_env.get('SERVICES_' + manifest_service_id + '_NAME'), 
                'SERVICES_' + manifest_service_id + '_METADATA_IMAGEURL':'',
                'SERVICES_' + manifest_service_id + '_METADATA_SUPPORTURL':'',
                'SERVICES_' + manifest_service_id + '_METADATA_DOCUMENTATIONURL':'',
                'SERVICES_' + manifest_service_id + '_METADATA_PROVIDERDISPLAYNAME':'',
                'SERVICES_' + manifest_service_id + '_METADATA_LONGDESCRIPTION':''}
            cf_service_info_metadata_str = re.findall(r'"extra": "(.*?)",\s*\n', cf_service_info)[0].replace('\\"','"')
            cf_service_info_metadata = json.loads(cf_service_info_metadata_str)
            for propertyname_service, propertyname_manifest in propertyname_correspond_metadata.iteritems():
                manifest_env_value = manifest_env.get(propertyname_manifest, property_metadata_defaultvalue.get(propertyname_manifest));
                if cf_service_info_metadata[propertyname_service] != manifest_env_value :
                    raise AssertionError('info [%s] in service: %r, not match [%s] in broker manifest configuration: %r' % (propertyname_service, cf_service_info_metadata[propertyname_service], propertyname_manifest, manifest_env_value))
                else:
                    logging.info('info [%s] in service: %r, match [%s] in broker manifest configuration: %r' % (propertyname_service, cf_service_info_metadata[propertyname_service], propertyname_manifest, manifest_env_value))

            manifest_plans_id_and_name = self.services_plans_id_and_name[manifest_service_id][1]
            cf_plan_info = re.split("GET /v2/service_plans", cf_service_info)[1]
            
            for manifest_plan_id in manifest_plans_id_and_name.keys():
                # the correspond property name between in cf_plan_info and in manifest_env (only for the properties whose value is string)
                propertyname_correspond = {#'name':'SERVICES_%s_PLAN_%s_NAME' %(manifest_service_id, manifest_plan_id), 
                    'description':'SERVICES_%s_PLAN_%s_DESCRIPTION' %(manifest_service_id, manifest_plan_id),
                    'free':'SERVICES_%s_PLAN_%s_FREE' %(manifest_service_id, manifest_plan_id)}
                property_defaultvalue = {#'SERVICES_%s_PLAN_%s_NAME' %(manifest_service_id, manifest_plan_id):'default', 
                    'SERVICES_%s_PLAN_%s_DESCRIPTION' %(manifest_service_id, manifest_plan_id):'Default plan', 
                    'SERVICES_%s_PLAN_%s_FREE' %(manifest_service_id, manifest_plan_id):'true'}
                manifest_plan_info = {}
                for propertyname_plan, propertyname_manifest in propertyname_correspond.iteritems():
                    manifest_env_value = manifest_env.get(propertyname_manifest, property_defaultvalue.get(propertyname_manifest));
                    manifest_plan_info[propertyname_plan] = str(manifest_env_value)
                for cf_plan_info_index in xrange(0,len(manifest_plans_id_and_name.keys())):
                    cf_plan_info_name = re.findall(r'"name": (.*?),', cf_plan_info)[cf_plan_info_index]
                    if cf_plan_info_name.replace('"', '') != manifest_plans_id_and_name[manifest_plan_id].replace('"', ''):
                        continue
                    for propertyname_plan, manifest_env_value in manifest_plan_info.iteritems():
                        cf_plan_info_value = re.findall(r'"%s": (.*?),' %propertyname_plan, cf_plan_info)[cf_plan_info_index] 
                        if cf_plan_info_value.replace('"', '').lower() != manifest_env_value.replace('"', '').lower():
                            raise AssertionError('info [%s] in service plan(%s): %r, not match [%s] in broker manifest configuration: %r' % (propertyname_plan, cf_plan_info_name, cf_plan_info_value, propertyname_correspond[propertyname_plan], manifest_env_value))
                        else:
                            logging.info('info [%s] in service plan(%s): %r, match [%s] in broker manifest configuration: %r' % (propertyname_plan, cf_plan_info_name, cf_plan_info_value, propertyname_correspond[propertyname_plan], manifest_env_value))
                        # compare PLAN_METADATA info which is in format json with default value: empty list
                    plan_metadata_propertyname = 'SERVICES_%s_PLAN_%s_METADATA' %(manifest_service_id, manifest_plan_id)
                    cf_plan_info_metadata = re.findall(r'"extra": "(.*?)",\s*\n', cf_plan_info)[cf_plan_info_index].replace('\\"','"')
                    if plan_metadata_propertyname not in manifest_env:
                        plan_metadata_empty = {"bullets":None,"costs":None,"displayName":None}
                        if cmp(json.loads(cf_plan_info_metadata), plan_metadata_empty) != 0:
                            raise AssertionError('info [%s] in service: %r, not match default broker manifest configuration: %r' % (plan_metadata_propertyname, json.loads(cf_plan_info_metadata), plan_metadata_empty))
                        else:
                            logging.info('info [%s] in service: %r, match default broker manifest configuration: %r' % (plan_metadata_propertyname, json.loads(cf_plan_info_metadata), plan_metadata_empty))
                    elif cmp(json.loads(cf_plan_info_metadata), json.loads(manifest_env[plan_metadata_propertyname])) != 0:
                        raise AssertionError('info [%s] in service: %r, not match broker manifest configuration: %r' % (plan_metadata_propertyname, json.loads(cf_plan_info_metadata), json.loads(manifest_env[plan_metadata_propertyname])))
                    else:
                        logging.info('info [%s] in service: %r, match broker manifest configuration: %r' % (plan_metadata_propertyname, json.loads(cf_plan_info_metadata), json.loads(manifest_env[plan_metadata_propertyname])))