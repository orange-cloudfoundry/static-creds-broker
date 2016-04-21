*** Settings ***
Resource        keywords.robot
Library         String
Library         Collections
Library         CFServiceCredentialsParser

*** Keywords ***
Try credential info in service key of specific service plan should match configuration
    [Arguments]     ${service_name}     ${plan_name}    ${expected_credentials_dict}
    [Documentation]     Test credential info through service-key for service [${service_name}], plan [${plan_name}].
    ${service_instance_name}=   Generate Random String
    ${service_key_name}=    	Generate Random String
    Create service instance ${service_name} ${plan_name} ${service_instance_name}
    Create service key ${service_instance_name} ${service_key_name}
    ${service_key_info_str}=    Get service key ${service_instance_name} ${service_key_name}
    &{cf_service_key_credentials_dict}=		Parse service key info      ${service_key_info_str}
    Log 	    ${cf_service_key_credentials_dict}
    Dictionaries Should Be Equal 	${cf_service_key_credentials_dict} 	${expected_credentials_dict}
    [Teardown]      Delete service instance ${service_instance_name} with service key ${service_key_name}

Try credential info in bound application for specific service plan should match configuration
    [Arguments]     ${service_name}     ${plan_name}    ${expected_credentials_dict}
    [Documentation]     Test credential info through bound application [${TEST_APP_NAME}] environment variables for service [${service_name}], plan [${plan_name}].
    ${service_instance_name}=   Generate Random String
    Create service instance ${service_name} ${plan_name} ${service_instance_name}
    Bind service ${TEST_APP_NAME} ${service_instance_name}
    ${app_env_info}=    Get application environment ${TEST_APP_NAME}
    &{cf_app_env_credentials_dict}= 	Parse app env credential info	${app_env_info}	${service_name}	${service_instance_name}
    Log 	    ${cf_app_env_credentials_dict}
    Dictionaries Should Be Equal 	${cf_app_env_credentials_dict} 	${expected_credentials_dict}
    [Teardown]      Delete service instance ${service_instance_name} bound to ${TEST_APP_NAME}

Delete service instance ${service_instance_name} with service key ${service_key_name}
    Delete service key ${service_instance_name} ${service_key_name}
    Delete service instance ${service_instance_name}

Delete service instance ${service_instance_name} bound to ${TEST_APP_NAME}
    Unbind service ${TEST_APP_NAME} ${service_instance_name}
    Delete service instance ${service_instance_name}

Credential info in service key of service ${service_name} plan ${plan_name} should match configuration ${expected_credentials_dict}
    Wait Until Keyword Succeeds     3x  30s    Try credential info in service key of specific service plan should match configuration   ${service_name}     ${plan_name}    ${expected_credentials_dict}

Credential info in bound application for service ${service_name} plan ${plan_name} should match configuration ${expected_credentials_dict}
    Wait Until Keyword Succeeds     3x  30s    Try credential info in bound application for specific service plan should match configuration    ${service_name}     ${plan_name}    ${expected_credentials_dict}