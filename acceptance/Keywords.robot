*** Settings ***
Library         String
Library         Process
Variables       Configuration.py

*** Variables ***
&{DEFAULT_INSTANCE_PARAMETERS}	

*** Settings ***
Library			Cloudfoundry	${CLIENT_ENDPOINT}    ${CLIENT_SKIP_SSL}	${CLIENT_USER}	${CLIENT_PASSWORD}      ${ORGANIZATION_NAME}    ${SPACE_NAME}	${TESTED_APP_NAME}	${SERVICE_NAME}	${PLAN_NAME}	${SERVICE_INSTANCE_NAME}   ${DEFAULT_INSTANCE_PARAMETERS}