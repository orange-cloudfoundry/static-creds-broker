import abc

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

    