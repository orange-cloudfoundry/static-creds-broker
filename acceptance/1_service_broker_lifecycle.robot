*** Settings ***
Resource            Keywords.robot
Documentation   Test basic service broker lifecycle
Library         String
Library         Collections
Force Tags      Service broker
Library         Compare      ${USE_YAML_CONFIG}      ${MANIFEST_PATH}       ${CONFIG_PATH}

Suite Setup      Run Keywords  Cloud Foundry config and target   Clean all service broker data
Suite Teardown   Run Keywords  Clean all service broker data

*** Test Cases ***
0) Deploy broker
    [Documentation]     Deploy the broker as an application on the Cloud Foundry.
    Deploy service broker

1) Register broker
	[Documentation]		Register the broker as a private service broker for one space.
	Register service broker

2) Test service instance lifecycle
	[Documentation]		Test for each plan of each service defined (Create/Delete a service instance; Create/Delete a service key; Bind/Unbind service instance to the application [${TEST_APP_NAME}]).
    ${services}=    Get services name and its plans name 
    ${services}=    Get Dictionary Items    ${services}
    :FOR    ${service_name}     ${service_plans}     IN  @{services}
    \       ${service_instance_name}=   Generate Random String
    \       ${service_key_name}=    Generate Random String
    \       Test service instance lifecycle for multi plans of a service ${service_name} ${service_plans} ${service_instance_name} ${service_key_name}
    [Teardown]      Clean up services ${service_instance_name} ${service_key_name} ${TEST_APP_NAME}

3) Unregister borker
	[Documentation]		Remove the registered private broker, which means remove all services and plans in the broker’s catalog from the Cloud Foundry Marketplace.
	Unregister service broker

4) Undeploy broker
    [Documentation]     Delete the deployed broker application from the Cloud Foundry.
    Undeploy service broker

*** Keywords ***
Test service instance lifecycle for one service plan ${service_name} ${plan_name} ${service_instance_name} ${service_key_name}
    [Documentation]     Test lifecycle of the plan [${PLAN_NAME}] of the service [${SERVICE_NAME}] (Create/Delete a service instance; Create/Delete a service key; Bind/Unbind service instance to the application [${TEST_APP_NAME}]).
    Create service instance ${service_name} ${plan_name} ${service_instance_name}
    Create service key ${service_instance_name} ${service_key_name}
    Get service key ${service_instance_name} ${service_key_name}
    Delete service key ${service_instance_name} ${service_key_name}
    Bind service ${TEST_APP_NAME} ${service_instance_name}
    Unbind service ${TEST_APP_NAME} ${service_instance_name}
    Delete service instance ${service_instance_name}

Test service instance lifecycle for multi plans of a service ${service_name} ${plan_list} ${service_instance_name} ${service_key_name}
    [Documentation]     Iterate to test each plan of the service [${SERVICE_NAME}]
    :FOR    ${plan_name}     IN     @{plan_list}
    \       Test service instance lifecycle for one service plan ${service_name} ${plan_name} ${service_instance_name} ${service_key_name}