*** Settings ***
Library         String
Library         Process
Variables       Configuration.py

*** Keywords ***
Execute:
    [Documentation]     Execute command which uses cf-cli, return the standard output of the command result.  
    [arguments]     ${cmd}  ${CF_TRACE}=false    ${current_working_directory}=.
    ${result}=		Run Process    ${cmd}   cwd=${current_working_directory}  env:CF_COLOR=false    env:CF_TRACE=${CF_TRACE}	stdout=${DEPLOY_PATH}/stdout.txt 	shell=True
    [return]		${result.stdout}

Execute command:
	[arguments]     ${cmd}  ${current_working_directory}=.
    [Documentation]     Execute command which not uses cf-cli, and verify the command was executed successfully.
    ${result}=      Run Process    ${cmd}    cwd=${current_working_directory}     stdout=${DEPLOY_PATH}/stdout.txt 	shell=True
    Log     ${result.stdout}
    Log     ${result.stderr}
    Should Be Equal As Integers     ${result.rc}    0

Prepare test environment
    [Documentation]     Prepare a testing env, create a temporary directory, configure Cloud Foundry local to en_US and no color, and login to Cloud Foundry.
    ${DEPLOY_DIR_NAME}=    Generate Random String
    ${DEPLOY_PATH}=		Get directory path ${BINARY_DIRECTORY} ${DEPLOY_DIR_NAME}
    Run Process    mkdir ${DEPLOY_DIR_NAME} 	cwd=${BINARY_DIRECTORY} 	shell=True
    Set Global Variable  ${DEPLOY_PATH}
    Execute:    cf config --locale en_US --color false
    ${result}=  Run Keyword If 	${CF_SKIP_SSL}     Execute:    cf login -a ${CF_ENDPOINT} -u ${CF_USER} -p ${CF_PASSWORD} -o ${ORGANIZATION_NAME} -s ${SPACE_NAME} --skip-ssl-validation
    ...     ELSE    Execute:    cf login -a ${CF_ENDPOINT} -u ${CF_USER} -p ${CF_PASSWORD} -o ${ORGANIZATION_NAME} -s ${SPACE_NAME}
    Log		${result}
    Should Contain  ${result}   OK

Unregister and undeploy broker
    Unregister service broker
    Undeploy service broker

Clean all service broker data
    [Documentation]     Delete the service broker, deployed broker application and deploy directory if exist.
    Unregister and undeploy broker
    Run Process    rm -rf ${DEPLOY_PATH} 	shell=True

Replace ${old_str} with ${new_str} in the file ${file_path}
    Execute command: 	sed -i s#${old_str}#${new_str}# ${file_path}

Create manifest file ${MANIFEST_PATH} based on manifest.tmpl.yml
    [Documentation]     create the manifest.yml based on template ${MANIFEST_TMPL_PATH} and adapt it to the running environment
    Execute command: 	cp ${MANIFEST_TMPL_PATH} ${MANIFEST_PATH}
    Replace "<broker_app_name>" with ${BROKER_APP_NAME} in the file ${MANIFEST_PATH}
    Replace "<broker_hostname>" with ${BROKER_HOSTNAME} in the file ${MANIFEST_PATH}
    Replace "<my-admin-domain.cf.io>" with ${BROKER_DOMAIN} in the file ${MANIFEST_PATH}
    Replace "<LATEST_RELEASE_VERSION>" with ${BROKER_RELEASE_VERSION} in the file ${MANIFEST_PATH}
    Replace "<broker_password>" with ${BROKER_PASSWORD} in the file ${MANIFEST_PATH}

Create manifest file ${MANIFEST_PATH} based on manifest.tmpl.yaml-config.yml with <binary_jar_exploded_path> ${BINARY_JAR_EXPLODED_PATH}
    [Documentation]     create the manifest.yml based on template ${MANIFEST_TMPL_YAML_CONFIG_PATH} and adapt it to the running environment
    Execute command: 	cp ${MANIFEST_TMPL_YAML_CONFIG_PATH} ${MANIFEST_PATH}
    Replace "<broker_app_name>" with ${BROKER_APP_NAME} in the file ${MANIFEST_PATH}
    Replace "<broker_hostname>" with ${BROKER_HOSTNAME} in the file ${MANIFEST_PATH}
    Replace "<my-admin-domain.cf.io>" with ${BROKER_DOMAIN} in the file ${MANIFEST_PATH}
    Replace "<binary_jar_exploded_path>" with "${BINARY_JAR_EXPLODED_PATH}" in the file ${MANIFEST_PATH}
    Replace "<broker_password>" with ${BROKER_PASSWORD} in the file ${MANIFEST_PATH}

Create yaml configuration file ${YAML_CONFIG_PATH} based on template ${YAML_CONFIG_TMPL_PATH}
    [Documentation]     create the application.yml file based on template application.tmpl.yml
    Execute command: 	cp ${YAML_CONFIG_TMPL_PATH} ${YAML_CONFIG_PATH}
    Replace "<broker_password>" with ${BROKER_PASSWORD} in the file ${YAML_CONFIG_PATH}

Create manifest file ${MANIFEST_PATH} based on manifest.tmpl.remote-config.yml
    [Documentation]     create the manifest.yml based on template ${MANIFEST_TMPL_REMOTE_CONFIG_PATH} and adapt it to the running environment
    Execute command:    cp ${MANIFEST_TMPL_REMOTE_CONFIG_PATH} ${MANIFEST_PATH}
    Replace "<broker_app_name>" with ${BROKER_APP_NAME} in the file ${MANIFEST_PATH}
    Replace "<broker_hostname>" with ${BROKER_HOSTNAME} in the file ${MANIFEST_PATH}
    Replace "<my-admin-domain.cf.io>" with ${BROKER_DOMAIN} in the file ${MANIFEST_PATH}
    Replace "<LATEST_RELEASE_VERSION>" with ${BROKER_RELEASE_VERSION} in the file ${MANIFEST_PATH}
    Replace "<broker_password>" with ${BROKER_PASSWORD} in the file ${MANIFEST_PATH}
    Run keyword If  ${USE_PROXY}    
    ...             Run keywords
    ...             Replace "\\\\#JAVA_OPTS:" with "JAVA_OPTS:" in the file ${MANIFEST_PATH}
    ...             Replace "http_proxyhost" with ${HTTP_PROXYHOST} in the file ${MANIFEST_PATH}
    ...             Replace "http_proxyport" with ${HTTP_PROXYPORT} in the file ${MANIFEST_PATH}
    ...             Replace "https_proxyhost" with ${HTTPS_PROXYHOST} in the file ${MANIFEST_PATH}
    ...             Replace "https_proxyport" with ${HTTPS_PROXYPORT} in the file ${MANIFEST_PATH}

Prepare deployment of service broker
    Run Keyword If      ${USE_REMOTE_CONFIG}    Prepare deployment of service broker configured by remote yaml configuration file
    ...                 ELSE IF                 ${USE_YAML_CONFIG}      Prepare deployment of service broker configured by yaml configuration file
    ...                 ELSE                    Prepare deployment of service broker configured by environment variables

Prepare deployment of service broker configured by environment variables
	${MANIFEST_PATH}= 	Get file path ${DEPLOY_PATH} manifest.yml
    Create manifest file ${MANIFEST_PATH} based on manifest.tmpl.yml

Prepare deployment of service broker configured by yaml configuration file
    ${BINARY_JAR_EXPLODED_PATH}=	Get directory path ${DEPLOY_PATH} static-creds-broker-exploded
    Execute command: 	unzip ${BINARY_JAR_PATH} -d ${BINARY_JAR_EXPLODED_PATH}
    ${YAML_CONFIG_PATH}=		Get file path ${BINARY_JAR_EXPLODED_PATH} application.yml
    Create yaml configuration file ${YAML_CONFIG_PATH} based on template ${YAML_CONFIG_TMPL_PATH}
    ${MANIFEST_PATH}= 	Get file path ${DEPLOY_PATH} manifest.yml
    Create manifest file ${MANIFEST_PATH} based on manifest.tmpl.yaml-config.yml with <binary_jar_exploded_path> ${BINARY_JAR_EXPLODED_PATH}

Prepare deployment of service broker configured by remote yaml configuration file
    ${MANIFEST_PATH}=   Get file path ${DEPLOY_PATH} manifest.yml
    Create manifest file ${MANIFEST_PATH} based on manifest.tmpl.remote-config.yml

Deploy service broker 
    [Documentation]     Deploy the broker as an application on the Cloud Foundry.
    Execute command: 	cp ${BINARY_JAR_PATH} ${DEPLOY_PATH}
    Prepare deployment of service broker
    ${result}=	Execute:     cf push    current_working_directory=${DEPLOY_PATH}
    Log	    ${result}
    Run Keyword If      'FAILED' in $result      Execute:   cf logs ${BROKER_APP_NAME} --recent
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

Delete service instance ${service_instance_name:\S+}
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

Get file path ${dir_path} ${file_name}
    ${file_path}=     Catenate    SEPARATOR=     ${dir_path}     ${file_name}
    [return]    ${file_path}

Get directory path ${parent_dir_path} ${dir_name}
    ${dir_path}=	Catenate    SEPARATOR=     ${parent_dir_path}     ${dir_name}  /
    [return]    ${dir_path}   