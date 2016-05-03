*** Settings ***
Resource        ../keywords.robot

*** Variables ***
${USE_YAML_CONFIG} 		${false}
${USE_REMOTE_CONFIG} 	${false}
${BROKER_APP_NAME} 		${BROKER_APP_NAME_LOCAL_ENV}
${BROKER_HOSTNAME} 		${BROKER_HOSTNAME_LOCAL_ENV}
${BROKER_NAME} 			${BROKER_NAME_LOCAL_ENV}