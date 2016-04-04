*** Settings ***
Resource		../keywords.robot
Force Tags		Service broker
Suite Setup		Run Keywords  Prepare test environment   Unregister and undeploy broker   Deploy service broker   Register service broker
Suite Teardown	Run Keywords  Clean all service broker data