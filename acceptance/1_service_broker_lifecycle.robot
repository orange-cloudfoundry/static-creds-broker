*** Settings ***
Resource            Keywords.robot
Documentation   Test basic service broker lifecycle
Library         String
Library         Collections
Force Tags      Service broker
Library         Staticreds      ${MANIFEST_PATH}

*** Test Cases ***
0) Prepare
    [Documentation]     Prepare the test execution environment, which means login to Cloud Foundry, and delete the service broker, and deployed broker application if exist.
    Cloud Foundry config and target
    Clean all service broker data

1) Deploy broker
    [Documentation]     Deploy the broker as an application on the Cloud Foundry.
    Deploy service broker

2) Register broker
	[Documentation]		Register the broker as a private service broker for one space.
	Register service broker

3) Test service instance lifecycle
	[Documentation]		Test for each plan of each service defined (Create/Delete a service instance; Create/Delete a service key; Bind/Unbind service instance to the application [${TEST_APP_NAME}]).
    ${services}=    Get services name and its plans name 
    ${services}=    Get Dictionary Items    ${services}
    :FOR    ${service_name}     ${service_plans}     IN  @{services}
    \       Test service instance lifecycle for multi plans of a service ${service_name} ${service_plans}

4) Unregister borker
	[Documentation]		Remove the registered private broker, which means remove all services and plans in the broker’s catalog from the Cloud Foundry Marketplace.
	Unregister service broker

5) Undeploy broker
    [Documentation]     Delete the deployed broker application from the Cloud Foundry.
    Undeploy service broker

*** Keywords ***
Test service instance lifecycle for one service plan ${SERVICE_NAME} ${PLAN_NAME}
    [Documentation]     Test lifecycle of the plan [${PLAN_NAME}] of the service [${SERVICE_NAME}] (Create/Delete a service instance; Create/Delete a service key; Bind/Unbind service instance to the application [${TEST_APP_NAME}]).
    ${SERVICE_INSTANCE_NAME}=   Generate Random String
    ${SERVICE_KEY_NAME}=    Generate Random String
    Create service instance ${SERVICE_NAME} ${PLAN_NAME} ${SERVICE_INSTANCE_NAME}
    Create service key ${SERVICE_INSTANCE_NAME} ${SERVICE_KEY_NAME}
    Delete service key ${SERVICE_INSTANCE_NAME} ${SERVICE_KEY_NAME}
    Bind service ${TEST_APP_NAME} ${SERVICE_INSTANCE_NAME}
    Unbind service ${TEST_APP_NAME} ${SERVICE_INSTANCE_NAME}
    Delete service instance ${SERVICE_INSTANCE_NAME}

Test service instance lifecycle for multi plans of a service ${SERVICE_NAME} ${PLAN_LIST} 
    [Documentation]     Iterate to test each plan of the service [${SERVICE_NAME}]
    :FOR    ${PLAN_NAME}     IN     @{PLAN_LIST}
    \       Test service instance lifecycle for one service plan ${SERVICE_NAME} ${PLAN_NAME}