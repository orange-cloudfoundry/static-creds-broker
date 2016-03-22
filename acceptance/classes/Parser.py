import abc
import json

class Parser(object):
    """abstract class Parser"""
    ROBOT_LIBRARY_SCOPE = "GLOBAL"
    __metaclass__ = abc.ABCMeta

    def __init__(self):
        self.services_plans_id_and_name = self.get_services_plans_id_and_name()

    @abc.abstractmethod
    def get_services_plans_id_and_name(self): 
        """return dict of service id and name with its plans id and name: {service_id:(service_name, {plan_id:plan_name})}"""
        pass
        
    @abc.abstractmethod
    def get_configured_credential_info(self, service_name, plan_name): 
        """return a dict of credential info of the specific plan of specific service"""
        pass

    @abc.abstractmethod
    def get_configured_service_info(self, config_service_id):
        """return a dict of service catalog info"""
        pass

    @abc.abstractmethod
    def get_configured_plans_info(self, config_service_id):
        """return a list of dict of plans info of specific service"""
        pass

    # return dict of service name and its plans name: {service_name:[plans_name]}
    def get_services_name_and_its_plans_name(self):
        services_plans_name = {}
        for service_tuple in self.services_plans_id_and_name.values(): # [(service_name, {plan_id:plan_name})]
            services_plans_name[service_tuple[0]] = service_tuple[1].values()
        return services_plans_name

    # return [services_name]
    def get_services_name(self):
        return dict(self.services_plans_id_and_name.values()).keys()

    # return {service_id:service_name}
    def get_services_id_and_name(self):
        services_id_and_name = {}
        for service_id, service_tuple in self.services_plans_id_and_name.iteritems():
            services_id_and_name[service_id] = service_tuple[0]
        return services_id_and_name

    # return (service_id, plan_id)
    def get_service_and_plan_id_from_name(self, target_service_name, target_plan_name):
        for service_id, service_tuple in self.services_plans_id_and_name.iteritems():
            if service_tuple[0] == target_service_name:
                for plan_id, plan_name in service_tuple[1].iteritems():
                    if plan_name == target_plan_name:
                        return (service_id, plan_id)

    def get_configured_service_info_pattern(self, service_properties_definition, config_service_propertyname, meta_properties_definition, config_meta_propertyname):
        """
        :param config_service_propertyname is the property name used in the configuration file 
        corresponding the cloud foundry service property: ['label', 'description', 'bindable', 'tags']
        :param config_meta_propertyname is the metadata property name used in the configuration file 
        corresponding the cloud foundry service property: ['displayName','imageUrl','supportUrl','documentationUrl','providerDisplayName','longDescription']
        """
        configured_catalog_info = {}
        propertyname_cf = ['label', 'description', 'bindable', 'tags']
        propertyname_correspond = zip(propertyname_cf, config_service_propertyname)
        property_defaultvalue = {'bindable':True, 'tags':''}
        for propertyname_service, propertyname_config in propertyname_correspond:
            config_value = service_properties_definition.get(propertyname_config, property_defaultvalue.get(propertyname_service));
            if propertyname_service == 'bindable':
                configured_catalog_info[propertyname_service] = bool(config_value)
            elif propertyname_service == 'tags':
                configured_catalog_info[propertyname_service] = filter(None, config_value.split(','))
            else:
                configured_catalog_info[propertyname_service] = config_value
        metadata_propertyname_cf = ['displayName', 'imageUrl', 'supportUrl', 'documentationUrl', 'providerDisplayName', 'longDescription']
        metadata_propertyname_correspond = zip(metadata_propertyname_cf, config_meta_propertyname)
        metadata_property_defaultvalue = {'displayName':service_properties_definition.get(config_service_propertyname[0]),
            'imageUrl':'', 'supportUrl':'', 'documentationUrl':'', 'providerDisplayName':'', 'longDescription':''}
        metadata_info = {}
        for propertyname_metadata, propertyname_config in metadata_propertyname_correspond:
            config_value = meta_properties_definition.get(propertyname_config, metadata_property_defaultvalue.get(propertyname_metadata));
            metadata_info[propertyname_metadata] = config_value
        configured_catalog_info['extra'] = metadata_info
        return configured_catalog_info

    def get_configured_plans_info_pattern(self, config_plans_id_and_name, plans_properties_definition, config_plans_propertyname):
        """
        :param plans_properties_definition is a map of plans id and its properties format {plan_id:plan_properties_map}
        :param config_plans_propertyname is a map of the property name list used in the configuration file 
        corresponding the cloud foundry plan property: ['description','free','extra']
        format {plan_id:correspond_plan_propertyname_list}
        """
        assert plans_properties_definition
        config_plans_info = []
        for plan_id, plan_name in config_plans_id_and_name.items():
            plan_properties = plans_properties_definition.get(plan_id)
            config_plan_propertyname = config_plans_propertyname.get(plan_id)
            propertyname_cf = ['description','free','extra']
            config_plan_info = {}
            config_plan_info['name'] = plan_name
            propertyname_correspond = zip(propertyname_cf, config_plan_propertyname)
            property_defaultvalue = {'description':'plan ' + plan_name, 'free':True,
                'extra': {"bullets":None,"costs":None,"displayName":None}}
            for propertyname_plan, propertyname_config in propertyname_correspond:
                config_value = plan_properties.get(propertyname_config, property_defaultvalue.get(propertyname_plan));
                config_plan_info[propertyname_plan] = config_value
                if propertyname_plan == 'free':
                    config_plan_info[propertyname_plan] = bool(config_value)
                elif propertyname_plan == 'extra' and type(config_value) is str:
                    config_plan_info[propertyname_plan] = json.loads(config_value)
            config_plans_info.append(config_plan_info)
        return config_plans_info