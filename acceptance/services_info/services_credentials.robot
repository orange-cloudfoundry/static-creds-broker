*** Settings ***
Resource		../keywords.robot
Documentation   Test static-creds-broker services credentials infomation
Library         String
Library			Collections
Library         CFServiceCredentialsParser

*** Variables ***
&{TRIPADVISOR_credentials}				URI=http://....
&{API_DIRECTORY_dev_credentials}		HOSTNAME=http://company.com 		URI=http://mydev-api.org		ACCESS_KEY=devAZERT23456664DFDSFSDFDSF
&{API_DIRECTORY_preprod_credentials}	HOSTNAME=http://company.com 		URI=http://mypreprod-api.org	ACCESS_KEY=preprodAZERT23456664DFDSFSDFDSF
&{API_DIRECTORY_prod_credentials}		HOSTNAME=http://prod.company.com 	URI=http://myprod-api.org		ACCESS_KEY=prodAZERT23456664DFDSFSDFDSF
&{ID_credentials}						uri=mysql://USERNAME:PASSWORD@HOSTNAME:PORT/NAME 	HOSTNAME=us-cdbr-east-03.cleardb.com 	username=admin 	password=pa55woRD

*** Test Cases ***
Credential info in service key of each service plan should match configuration
	[Documentation]     Test whether credential info returned by service-key matches configuration.
	[Template]		Credential info in service key of service ${service_name} plan ${plan_name} should match configuration ${expected_credentials_dict}
	TRIPADVISOR_test_Service		default		&{TRIPADVISOR_credentials}		
	API_DIRECTORY_test_Service		dev 		&{API_DIRECTORY_dev_credentials}
	static-creds-service-test		myplan 		&{ID_credentials}

Credential info in bound application should match configuration for each service plan 
    [Documentation]     Test whether credential info configured in bound application envrionment variables matches configuration.
    [Template]		Credential info in bound application for service ${service_name} plan ${plan_name} should match configuration ${expected_credentials_dict}
	TRIPADVISOR_test_Service		default		&{TRIPADVISOR_credentials}		
	API_DIRECTORY_test_Service		dev 		&{API_DIRECTORY_dev_credentials}
	static-creds-service-test		myplan 		&{ID_credentials}

*** Keywords ***
Credential info in service key of service ${service_name} plan ${plan_name} should match configuration ${expected_credentials_dict}
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

Credential info in bound application for service ${service_name} plan ${plan_name} should match configuration ${expected_credentials_dict} 
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
