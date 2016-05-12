*** Settings ***
Documentation   Test whether service broker could be updated
Resource        local_env_resource.robot
Resource 		../services_credentials_keywords.robot
Force Tags		Service broker
Suite Setup		Run Keywords  Prepare test environment   Unregister and undeploy broker 	Deploy service broker 	Register service broker
Suite Teardown	Run Keywords  Clean all service broker data

*** Variables ***
&{API_DIRECTORY_prod_credentials}			HOSTNAME=http://prod.company.com 	URI=http://myprod-api.org		ACCESS_KEY=prodAZERT23456664DFDSFSDFDSF
&{updated_API_DIRECTORY_prod_credentials}	HOSTNAME=http://company.com			URI=http://myprod-api.org		ACCESS_KEY=prodAZERT23456664DFDSFSDFDSF

*** Test Cases ***
Credential info should always match broker's current configuration 
	[Documentation]     Credential info returned by service-key should match initial configuration, and after service broker configuration updated, credential info should match updated configuration.
	Credential info in service key of service API_DIRECTORY_test_Service1 plan prod should match configuration &{API_DIRECTORY_prod_credentials}
	Run Keyword If      ${USE_REMOTE_CONFIG}    Update service broker configured by remote yaml configuration file
    ...                 ELSE IF                 ${USE_YAML_CONFIG}      Update service broker configured by yaml configuration file
    ...                 ELSE                    Update service broker configured by environment variables
	Credential info in service key of service API_DIRECTORY_test_Service1 plan prod should match configuration &{updated_API_DIRECTORY_prod_credentials}

*** Keywords ***
Update service broker configured by remote yaml configuration file
	[Documentation] 	Change service broker remote configuration file, then restage the broker.
	${result}= 	Execute:    cf set-env ${BROKER_APP_NAME} SPRING_CLOUD_CONFIG_SERVER_GIT_SEARCH_PATHS "acceptance/changed-remote-config"
	Log	    ${result}
	Should Contain  ${result}   OK
	${result}= 	Execute:    cf restage ${BROKER_APP_NAME}
	Log	    ${result}
	Should Not Contain  ${result}   FAILED

Update service broker configured by yaml configuration file
	[Documentation] 	Change service broker local configuration file application.yml, then re-push the broker.
	${BINARY_JAR_EXPLODED_PATH}=	Get directory path ${DEPLOY_PATH} static-creds-broker-exploded
	${YAML_CONFIG_PATH}=		Get file path ${BINARY_JAR_EXPLODED_PATH} application.yml
	Replace "HOSTNAME: http://prod.company.com" with "" in the file ${YAML_CONFIG_PATH}
	${result}=	Execute:     cf push    current_working_directory=${DEPLOY_PATH}
    Log	    ${result}
    Should Not Contain  ${result}   FAILED

Update service broker configured by environment variables
	[Documentation] 	Change service broker environment variables, then restage the broker.
	${result}= 	Execute:    cf unset-env ${BROKER_APP_NAME} SERVICES[API_DIRECTORY]_PLANS[PROD]_CREDENTIALS[HOSTNAME]
	Log	    ${result}
	Should Contain  ${result}   OK
	${result}= 	Execute:    cf restage ${BROKER_APP_NAME}
	Log	    ${result}
	Should Not Contain  ${result}   FAILED