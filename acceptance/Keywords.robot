*** Settings ***
Library         Process
Variables       Configuration.py

*** Keywords ***
Execute:      
    [arguments]     ${cmd}      ${CF_TRACE}=false    #${timeout}=30s    ${on_timeout}=continue
    ${result}=		Run Process    ${cmd}    env:CF_COLOR=false    env:CF_TRACE=${CF_TRACE}		shell=True
    [return]		${result.stdout}

Cloud Foundry config and target
    [Documentation]     Configure Cloud Foundry local to en_US and no color, and login to Cloud Foundry.
    Execute:    cf config --locale en_US --color false
    ${result}=  Run Keyword If 	${CLIENT_SKIP_SSL}     Execute:    cf login -a ${CLIENT_ENDPOINT} -u ${CLIENT_USER} -p ${CLIENT_PASSWORD} -o ${ORGANIZATION_NAME} -s ${SPACE_NAME} --skip-ssl-validation
    ...     ELSE    Execute:    cf login -a ${CLIENT_ENDPOINT} -u ${CLIENT_USER} -p ${CLIENT_PASSWORD} -o ${ORGANIZATION_NAME} -s ${SPACE_NAME}
    Log		${result}
    Should Contain  ${result}   OK

Clean all service broker data
    [Documentation]     Delete the service broker, and deployed broker application if exist.
    ${result}=  Execute:    cf delete-service-broker ${BROKER_NAME} -f
    Log     ${result}
    Should Contain  ${result}   OK
    ${result}=  Execute:    cf d ${BROKER_APP_NAME} -f
    Log     ${result}
    Should Contain  ${result}   OK

Deploy service broker
    [Documentation]     Deploy the broker as an application on the Cloud Foundry.
    ${result}=	Execute:     cf p ${BROKER_APP_NAME} -f ${MANIFEST_PATH} -p ${BINARY_PATH} 
    Log	    ${result}
    Should Not Contain  ${result}   FAILED
    ${result}=  Execute:     cf a
    Log     ${result}
    ${BROKER_APP_URL_NOHTTP}=    Remove String Using Regexp     ${BROKER_APP_URL}   (http://|https://)
    Should Match Regexp  ${result}   ${BROKER_APP_NAME}\\s*started.*${BROKER_APP_URL_NOHTTP}

Undeploy service broker
    [Documentation]     Delete the deployed broker application from the Cloud Foundry.
    ${result}=  Execute:     cf d ${BROKER_APP_NAME} -f
    Log     ${result}
    Should Contain  ${result}   OK

Register service broker
    [Documentation]     Register the broker as a private service broker for one space.
	${result}=	Execute:    cf create-service-broker ${BROKER_NAME} ${BROKER_USERNAME} ${BROKER_PASSWORD} ${BROKER_APP_URL} --space-scoped
    Log     ${result}
    Should Contain  ${result}   OK
    ${result}=  Execute:    cf service-brokers
    Log     ${result}
    Should Match Regexp  ${result}  ${BROKER_NAME}\\s*${BROKER_APP_URL}

Unregister service broker
    [Documentation]     Remove the registered private broker, which means remove all services and plans in the broker’s catalog from the Cloud Foundry Marketplace.
    ${result}=  Execute:    cf delete-service-broker ${BROKER_NAME} -f
    Log     ${result}
    Should Contain  ${result}   OK

Create service instance ${SERVICE_NAME} ${PLAN_NAME} ${SERVICE_INSTANCE_NAME}
    [Documentation]     Create a service instance.
	${result}=	Execute:    cf cs ${SERVICE_NAME} ${PLAN_NAME} ${SERVICE_INSTANCE_NAME}
    Log     ${result}
    Should Contain  ${result}   OK

Delete service instance ${SERVICE_INSTANCE_NAME}
    [Documentation]     Create a service instance.
    ${result}=  Execute:    cf ds ${SERVICE_INSTANCE_NAME} -f
    Log     ${result}
    Should Contain  ${result}   OK

Create service key ${SERVICE_INSTANCE_NAME} ${SERVICE_KEY_NAME}
    [Documentation]     Create a service key.
    ${result}=  Execute:    cf csk ${SERVICE_INSTANCE_NAME} ${SERVICE_KEY_NAME}
    Log     ${result}
    Should Contain  ${result}   OK

Delete service key ${SERVICE_INSTANCE_NAME} ${SERVICE_KEY_NAME}
    [Documentation]     Delete the service key.
    ${result}=  Execute:    cf dsk ${SERVICE_INSTANCE_NAME} ${SERVICE_KEY_NAME} -f
    Log     ${result}
    Should Contain  ${result}   OK

Bind service ${TEST_APP_NAME} ${SERVICE_INSTANCE_NAME}
    [Documentation]    Bind application [${TEST_APP_NAME}] to the service instance [${SERVICE_INSTANCE_NAME}].
    ${result}=  Execute:    cf bs ${TEST_APP_NAME} ${SERVICE_INSTANCE_NAME}
    Log     ${result}
    Should Contain  ${result}   OK

Unbind service ${TEST_APP_NAME} ${SERVICE_INSTANCE_NAME}
    [Documentation]    Unbind application [${TEST_APP_NAME}] from the service instance [${SERVICE_INSTANCE_NAME}].
    ${result}=  Execute:    cf us ${TEST_APP_NAME} ${SERVICE_INSTANCE_NAME} 
    Log     ${result}
    Should Contain  ${result}   OK