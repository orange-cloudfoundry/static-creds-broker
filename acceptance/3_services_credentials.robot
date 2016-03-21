*** Settings ***
Resource            Keywords.robot
Documentation   Test static-creds-broker services credentials infomation
Library         String
Library         Collections
Force Tags      Service broker
Variables       Configuration.py
Library         Compare      ${USE_YAML_CONFIG}      ${MANIFEST_PATH}

Suite Setup      Run Keywords  Cloud Foundry config and target   Clean all service broker data   Deploy service broker   Register service broker
Suite Teardown   Run Keywords  Clean all service broker data

*** Test Cases ***
Credential info in service key should match manifest
    [Documentation]     Test whether credential info returned by service-key matches manifest definition.
    ${services}=    Get services name and its plans name
    ${services}=    Get Dictionary Items    ${services}
    ${service_instance_name}=   Generate Random String
    ${service_key_name}=    Generate Random String
    :FOR    ${service_name}     ${service_plans}     IN  @{services}
    \       Test credential info in service key for multi plans of a service ${service_name} ${service_plans} ${service_instance_name} ${service_key_name}
    [Teardown]      Clean up services ${service_instance_name} ${service_key_name} ${TEST_APP_NAME}     

Credential info in bound application should match manifest
    [Documentation]     Test whether credential info returned by service-key matches manifest definition.
    ${services}=    Get services name and its plans name
    ${services}=    Get Dictionary Items    ${services}
    ${service_instance_name}=   Generate Random String
    ${service_key_name}=    Generate Random String
    :FOR    ${service_name}     ${service_plans}     IN  @{services}
    \       Test credential info in bound application for multi plans of a service ${service_name} ${service_plans} ${service_instance_name} ${service_key_name} 
    [Teardown]      Clean up services ${service_instance_name} ${service_key_name} ${TEST_APP_NAME}     
    # ...             AND             Clean all service broker data

*** Keywords ***
Test credential info in service key for one service plan ${service_name} ${plan_name} ${service_instance_name} ${service_key_name}
    [Documentation]     Test credential info through service-key for service [${service_name}], plan [${plan_name}].
    Create service instance ${service_name} ${plan_name} ${service_instance_name}
    Create service key ${service_instance_name} ${service_key_name}
    ${service_key_info}=    Get service key ${service_instance_name} ${service_key_name}
    Service key info should match configuration      ${service_key_info}     ${service_name}     ${plan_name}
    Delete service key ${service_instance_name} ${service_key_name}
    Delete service instance ${service_instance_name}

Test credential info in bound application for one service plan ${service_name} ${plan_name} ${TEST_APP_NAME} ${service_instance_name} ${service_key_name}
    [Documentation]     Test credential info through bound application [${TEST_APP_NAME}] for service [${service_name}], plan [${plan_name}].
    Create service instance ${service_name} ${plan_name} ${service_instance_name}   
    Bind service ${TEST_APP_NAME} ${service_instance_name}
    ${app_env_info}=    Get application environment ${TEST_APP_NAME}
    Bound app env credential info should match configuration      ${app_env_info}     ${service_name}     ${plan_name}   ${service_instance_name}
    Unbind service ${TEST_APP_NAME} ${service_instance_name}
    Delete service instance ${service_instance_name}

Test credential info in service key for multi plans of a service ${service_name} ${service_plans} ${service_instance_name} ${service_key_name}
    [Documentation]     Test credential info through service-key for service [${service_name}], iteration over all its plans.
    :FOR    ${plan_name}     IN     @{service_plans}
    \       Test credential info in service key for one service plan ${service_name} ${plan_name} ${service_instance_name} ${service_key_name}

Test credential info in bound application for multi plans of a service ${service_name} ${service_plans} ${service_instance_name} ${service_key_name}
    [Documentation]     Test credential info through bound application [${TEST_APP_NAME}] for service [${service_name}], iteration over all its plans.
    :FOR    ${plan_name}     IN     @{service_plans}
    \       Test credential info in bound application for one service plan ${service_name} ${plan_name} ${TEST_APP_NAME} ${service_instance_name} ${service_key_name}