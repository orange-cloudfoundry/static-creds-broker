*** Settings ***
Resource        local_env_resource.robot
Documentation   Test basic service broker lifecycle
Library         String
Force Tags      Service broker
Suite Setup     Run Keywords  Prepare test environment   Unregister and undeploy broker
Suite Teardown  Run Keywords  Clean all service broker data

*** Test Cases ***
0) Deploy broker
    [Documentation]     Deploy the broker as an application on the Cloud Foundry.
    Deploy service broker

1) Register broker
	[Documentation]		Register the broker as a private service broker for one space.
	Register service broker

2) Test service instance lifecycle
	[Documentation]		Test for each plan of each service defined (Create/Delete a service instance; Create/Delete a service key; Bind/Unbind service instance to the application [${TEST_APP_NAME}]).
    [Template]      Test service instance lifecycle for the service ${service_name} plan ${plan_name}
    "TRIPADVISOR_test_Service1"      "default"
    "API_DIRECTORY_test_Service1"    "dev"
    "API_DIRECTORY_test_Service1"    "preprod"
    "API_DIRECTORY_test_Service1"    "prod"
    "static-creds-service-test1"     "myplan"

3) Unregister borker
	[Documentation]		Remove the registered private broker, which means remove all services and plans in the broker’s catalog from the Cloud Foundry Marketplace.
	Unregister service broker

4) Undeploy broker
    [Documentation]     Delete the deployed broker application from the Cloud Foundry.
    Undeploy service broker

*** Keywords ***
Test service instance lifecycle for the service ${service_name} plan ${plan_name}
    [Documentation]     Test lifecycle of the plan [${PLAN_NAME}] of the service [${SERVICE_NAME}] (Create/Delete a service instance; Create/Delete a service key; Bind/Unbind service instance to the application [${TEST_APP_NAME}]).
    ${service_instance_name}=   Generate Random String
    ${service_key_name}=    Generate Random String
    Create service instance ${service_name} ${plan_name} ${service_instance_name}
    Create service key ${service_instance_name} ${service_key_name}
    Get service key ${service_instance_name} ${service_key_name}
    Delete service key ${service_instance_name} ${service_key_name}
    Bind service ${TEST_APP_NAME} ${service_instance_name}
    Unbind service ${TEST_APP_NAME} ${service_instance_name}
    Delete service instance ${service_instance_name}
    [Teardown]      Delete service instance ${service_instance_name} with service key ${service_key_name} and bound to ${TEST_APP_NAME}

Delete service instance ${service_instance_name} with service key ${service_key_name} and bound to ${TEST_APP_NAME}
    Delete service key ${service_instance_name} ${service_key_name}
    Unbind service ${TEST_APP_NAME} ${service_instance_name}
    Delete service instance ${service_instance_name}
