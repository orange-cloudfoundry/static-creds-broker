import logging
import yaml
import json

class Staticreds(object):
    ROBOT_LIBRARY_SCOPE = "GLOBAL"

    def __init__(self, manifest_path):
        self.manifest_path = manifest_path

    def service_entity_info_should_correspond_to_broker_manifest(self, service_info): 
        with open(self.manifest_path, 'r') as f:
            doc = yaml.load(f)
            manifest_env = doc["applications"][0]["env"]

            # the correspond property name between in service_info and in manifest_env (only for the properties whose value is string)
            propertyname_correspond = {'label':'SERVICES_ID_NAME', 'description':'SERVICES_ID_DESCRIPTION', 'bindable':'SERVICES_ID_BINDEABLE'}
            property_defaultvalue = {'SERVICES_ID_BINDEABLE':True}
            for propertyname_service, propertyname_manifest in propertyname_correspond.iteritems():
                manifest_env_value = manifest_env.get(propertyname_manifest, property_defaultvalue.get(propertyname_manifest));
                if service_info[propertyname_service] != manifest_env_value:
                    raise AssertionError('info [%s] in service: %r, not correspond to [%s] in broker manifest configuration: %r' % (propertyname_service, service_info[propertyname_service], propertyname_manifest, manifest_env_value))

            # compare tags info which is a string array with default value: empty list
            if "SERVICES_ID_TAGS" not in manifest_env:
                if service_info['tags']:
                    raise AssertionError('info [SERVICES_ID_TAGS] in service: %r, not correspond to default broker manifest configuration: []' % (service_info['tags']))
            elif cmp(service_info['tags'], manifest_env["SERVICES_ID_TAGS"].split(',')) != 0:
                raise AssertionError('info [SERVICES_ID_TAGS] in service: %r, not correspond to broker manifest configuration: %r' % (service_info['tags'], manifest_env["SERVICES_ID_TAGS"]))

            # for service metadata properties which appears in the extra of service_info as json string
            propertyname_correspond_metadata = {'displayName':'SERVICES_ID_METADATA_DISPLAYNAME', 'imageUrl':'SERVICES_ID_METADATA_IMAGEURL', 'supportUrl':'SERVICES_ID_METADATA_SUPPORTURL', 'documentationUrl':'SERVICES_ID_METADATA_DOCUMENTATIONURL', 'providerDisplayName':'SERVICES_ID_METADATA_PROVIDERDISPLAYNAME', 'longDescription':'SERVICES_ID_METADATA_LONGDESCRIPTION'}
            property_metadata_defaultvalue = {'SERVICES_ID_METADATA_DISPLAYNAME':manifest_env.get("SERVICES_ID_NAME"), 'SERVICES_ID_METADATA_IMAGEURL':'','SERVICES_ID_METADATA_SUPPORTURL':'','SERVICES_ID_METADATA_DOCUMENTATIONURL':'','SERVICES_ID_METADATA_PROVIDERDISPLAYNAME':'','SERVICES_ID_METADATA_LONGDESCRIPTION':''}
            service_info_metadata = json.loads(service_info['extra'])
            for propertyname_service, propertyname_manifest in propertyname_correspond_metadata.iteritems():
                manifest_env_value = manifest_env.get(propertyname_manifest, property_metadata_defaultvalue.get(propertyname_manifest));
                if service_info_metadata[propertyname_service] != manifest_env_value :
                    raise AssertionError('info [%s] in service: %r, not correspond to [%s] in broker manifest configuration: %r' % (propertyname_service, service_info_metadata[propertyname_service], propertyname_manifest, manifest_env_value))

            
    def service_plan_info_should_correspond_to_broker_manifest(self, plan_info): 
        with open(self.manifest_path, 'r') as f:
            doc = yaml.load(f)
            manifest_env = doc["applications"][0]["env"]

            # the correspond property name between in plan_info and in manifest_env (only for the properties whose value is string)
            propertyname_correspond = {'name':'PLAN_NAME', 'description':'PLAN_DESCRIPTION', 'free':'PLAN_FREE'}
            property_defaultvalue = {'PLAN_NAME':'default', 'PLAN_DESCRIPTION':'Default plan', 'PLAN_FREE':True}
            for propertyname_plan, propertyname_manifest in propertyname_correspond.iteritems():
                manifest_env_value = manifest_env.get(propertyname_manifest, property_defaultvalue.get(propertyname_manifest));
                if plan_info[propertyname_plan] != manifest_env_value:
                    raise AssertionError('info [%s] in service plan: %r, not correspond to [%s] in broker manifest configuration: %r' % (propertyname_plan, plan_info[propertyname_plan], propertyname_manifest, manifest_env_value))

            # compare PLAN_METADATA info which is in format json with default value: empty list
            if "PLAN_METADATA" not in manifest_env:
                plan_metadata_empty = {"bullets":None,"costs":None,"displayName":None}
                if cmp(json.loads(plan_info['extra']), plan_metadata_empty) != 0:
                    raise AssertionError('info [PLAN_METADATA] in service: %r, not correspond to default broker manifest configuration: %r' % (json.loads(plan_info['extra']), plan_metadata_empty))
            elif cmp(json.loads(plan_info['extra']), json.loads(manifest_env["PLAN_METADATA"])) != 0:
                raise AssertionError('info [PLAN_METADATA] in service: %r, not correspond to broker manifest configuration: %r' % (json.loads(plan_info['extra']), json.loads(manifest_env["PLAN_METADATA"])))