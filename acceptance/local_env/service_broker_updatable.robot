*** Settings ***
Documentation   Test whether service broker could be updated
Resource        local_env_resource.robot
Resource 		../services_credentials_keywords.robot
Force Tags		Service broker
Suite Setup		Run Keywords  Prepare test environment   Unregister and undeploy broker
Suite Teardown	Run Keywords  Clean all service broker data

*** Variables ***
&{API_DIRECTORY_prod_credentials}			HOSTNAME=http://prod.company.com 	URI=http://myprod-api.org		ACCESS_KEY=prodAZERT23456664DFDSFSDFDSF
&{updated_API_DIRECTORY_prod_credentials}	HOSTNAME=http://company.com			URI=http://myprod-api.org		ACCESS_KEY=prodAZERT23456664DFDSFSDFDSF

*** Test Cases ***
0) Deploy broker
    [Documentation]     Deploy the broker as an application on the Cloud Foundry.
    Deploy service broker

1) Register broker
	[Documentation]		Register the broker as a private service broker for one space.
	Register service broker

2) Credential info should match initial configuration 
	[Documentation]     Test whether credential info returned by service-key matches initial configuration.
	Credential info in service key of service API_DIRECTORY_test_Service1 plan prod should match configuration &{API_DIRECTORY_prod_credentials}

3) Update service broker configuration
	[Documentation]		Test the updating of service broker configuration
	Run Keyword If      ${USE_REMOTE_CONFIG}    Update service broker configured by remote yaml configuration file
    ...                 ELSE IF                 ${USE_YAML_CONFIG}      Update service broker configured by yaml configuration file
    ...                 ELSE                    Update service broker configured by environment variables

4) Credential info should match updated configuration 
	[Documentation]     Test whether credential info returned by service-key matches updated configuration.
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
	${result}= 	Execute:    cf unset-env ${BROKER_APP_NAME} SERVICES_API_DIRECTORY_PLAN_3_CREDENTIALS_HOSTNAME
	Log	    ${result}
	Should Contain  ${result}   OK
	${result}= 	Execute:    cf restage ${BROKER_APP_NAME}
	Log	    ${result}
	Should Not Contain  ${result}   FAILED