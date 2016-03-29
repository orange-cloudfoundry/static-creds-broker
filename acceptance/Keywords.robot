*** Settings ***
Library         Process
Variables       Configuration.py

*** Keywords ***
Execute:
    [Documentation]     Execute command which uses cf-cli, return the standard output of the command result.  
    [arguments]     ${cmd}  ${CF_TRACE}=false    ${cwd}=.
    ${result}=		Run Process    ${cmd}   cwd=${cwd}  env:CF_COLOR=false    env:CF_TRACE=${CF_TRACE}		shell=True
    [return]		${result.stdout}

Execute command: ${cmd}
    [Documentation]     Execute command which not uses cf-cli, and verify the command was executed successfully.
    ${result}=      Run Process    ${cmd}    shell=True
    Should Be Equal As Integers     ${result.rc}    0

Cloud Foundry config and target
    [Documentation]     Configure Cloud Foundry local to en_US and no color, and login to Cloud Foundry.
    Execute:    cf config --locale en_US --color false
    ${result}=  Run Keyword If 	${CF_SKIP_SSL}     Execute:    cf login -a ${CF_ENDPOINT} -u ${CF_USER} -p ${CF_PASSWORD} -o ${ORGANIZATION_NAME} -s ${SPACE_NAME} --skip-ssl-validation
    ...     ELSE    Execute:    cf login -a ${CF_ENDPOINT} -u ${CF_USER} -p ${CF_PASSWORD} -o ${ORGANIZATION_NAME} -s ${SPACE_NAME}
    Log		${result}
    Should Contain  ${result}   OK

Clean all service broker data
    [Documentation]     Delete the service broker, and deployed broker application if exist.
    Unregister service broker
    Undeploy service broker

Replace ${old_str} with ${new_str} in the file ${file_path}
    Execute command: sed -i.bak s/${old_str}/${new_str}/ ${file_path}

Edit manifest file
    [Documentation]     create the manifest.yml and adapt it to the running environment
    Execute command: cp ${MANIFEST_TMPL_PATH} ${MANIFEST_PATH}
    Replace "<broker_app_name>" with ${BROKER_APP_NAME} in the file ${MANIFEST_PATH}
    Replace "<broker_hostname>" with ${BROKER_HOSTNAME} in the file ${MANIFEST_PATH}
    Replace "<my-admin-domain.cf.io>" with ${BROKER_DOMAIN} in the file ${MANIFEST_PATH}
    Replace "<LATEST_RELEASE_VERSION>" with ${BROKER_RELEASE_VERSION} in the file ${MANIFEST_PATH}
    Replace "<broker_password>" with ${BROKER_PASSWORD} in the file ${MANIFEST_PATH}

Deploy service broker
    [Documentation]     Deploy the broker as an application on the Cloud Foundry.
    Edit manifest file
    ${result}=	Execute:     cf push    cwd=${BINARY_DIRECTORY}
    Log	    ${result}
    Should Not Contain  ${result}   FAILED
    ${result}=  Execute:     cf apps
    Log     ${result}
    ${broker_app_route}= 	Catenate    SEPARATOR=.     ${BROKER_HOSTNAME}  ${BROKER_DOMAIN}
    Should Match Regexp  ${result}   ${BROKER_APP_NAME}\\s*started.*${broker_app_route}

Undeploy service broker
    [Documentation]     Delete the deployed broker application from the Cloud Foundry.
    ${result}=  Execute:     cf delete ${BROKER_APP_NAME} -f
    Log     ${result}
    Should Contain  ${result}   OK

Register service broker
    [Documentation]     Register the broker as a private service broker for one space.
	${broker_app_url}= 	Catenate    SEPARATOR=     ${PROTOCOL} 	:// 	${BROKER_HOSTNAME}	.  ${BROKER_DOMAIN}
    ${result}=	Execute:    cf create-service-broker ${BROKER_NAME} user ${BROKER_PASSWORD} ${broker_app_url} --space-scoped
    Log     ${result}
    Should Contain  ${result}   OK
    ${result}=  Execute:    cf service-brokers
    Log     ${result}
    Should Match Regexp  ${result}  ${BROKER_NAME}\\s*${broker_app_url}

Unregister service broker
    [Documentation]     Remove the registered private broker, which means remove all services and plans in the broker’s catalog from the Cloud Foundry Marketplace.
    ${result}=  Execute:    cf delete-service-broker ${BROKER_NAME} -f
    Log     ${result}
    Should Contain  ${result}   OK

Create service instance ${service_name} ${plan_name} ${service_instance_name}
    [Documentation]     Create a service instance.
	${result}=	Execute:    cf create-service ${service_name} ${plan_name} ${service_instance_name}
    Log     ${result}
    Should Contain  ${result}   OK

Delete service instance ${service_instance_name}
    [Documentation]     Create a service instance.
    ${result}=  Execute:    cf delete-service ${service_instance_name} -f
    Log     ${result}
    Should Contain  ${result}   OK

Create service key ${service_instance_name} ${service_key_name}
    [Documentation]     Create a service key.
    ${result}=  Execute:    cf create-service-key ${service_instance_name} ${service_key_name}
    Log     ${result}
    Should Contain  ${result}   OK

Delete service key ${service_instance_name} ${service_key_name}
    [Documentation]     Delete the service key.
    ${result}=  Execute:    cf delete-service-key ${service_instance_name} ${service_key_name} -f
    Log     ${result}
    Should Contain  ${result}   OK

Get service key ${service_instance_name} ${service_key_name}
    [Documentation]     Create a service key.
    ${result}=  Execute:    cf service-key ${service_instance_name} ${service_key_name}
    Log     ${result}
    [return]     ${result}

Bind service ${TEST_APP_NAME} ${service_instance_name}
    [Documentation]    Bind application [${TEST_APP_NAME}] to the service instance [${service_instance_name}].
    ${result}=  Execute:    cf bind-service ${TEST_APP_NAME} ${service_instance_name}
    Log     ${result}
    Should Contain  ${result}   OK

Unbind service ${TEST_APP_NAME} ${service_instance_name}
    [Documentation]    Unbind application [${TEST_APP_NAME}] from the service instance [${service_instance_name}].
    ${result}=  Execute:    cf unbind-service ${TEST_APP_NAME} ${service_instance_name} 
    Log     ${result}
    Should Match Regexp  ${result}   (OK|not found)

Get application environment ${app_name}
    [Documentation]    Get application [${app_name}] environment variables
    ${result}=  Execute:    cf env ${app_name}
    Log     ${result}
    Should Contain  ${result}   OK
    [return]     ${result}