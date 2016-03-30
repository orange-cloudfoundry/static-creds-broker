*** Settings ***
Resource		service_catalog_keywords.robot
Documentation   Test static-creds-broker service API_DIRECTORY_test_Service catalog infomation

Suite Setup		Run Keywords	Write service API_DIRECTORY_test_Service catalog info in marketplace to the file API_DIRECTORY_test_Service.txt
...				AND				Import Library      CFServiceCatalogParser      API_DIRECTORY_test_Service.txt
Suite Teardown	Delete file API_DIRECTORY_test_Service.txt

*** Test Cases ***
Service API_DIRECTORY_test_Service basic catalog info in the Cloud Foundry marketplace should match configuration
    [Documentation]     Test whether service basic properties in broker catalog info match configuration.
    [Template]      Service [API_DIRECTORY_test_Service] basic catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}] 
    label			API_DIRECTORY_test_Service
    description		My existing service
    bindable		${true}
    tags			@{EMPTY}

Service API_DIRECTORY_test_Service metadata catalog info in the Cloud Foundry marketplace should match configuration
    [Documentation]     Test whether service metadata properties in broker catalog info match configuration.
    [Template]      Service [API_DIRECTORY_test_Service] metadata catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}] 
    displayName				API_DIRECTORY_test_Service
    imageUrl				${EMPTY}
    supportUrl				${EMPTY}
	documentationUrl		${EMPTY}
    providerDisplayName		${EMPTY}
    longDescription			A long description for my service

Service API_DIRECTORY_test_Service plans catalog info in the Cloud Foundry marketplace should match configuration
    [Documentation]     Test whether plans properties in broker catalog info match configuration.
    [Template]       Service [API_DIRECTORY_test_Service] plan [${plan_name}] catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}]
	dev			name			dev
	dev			description		plan dev
	dev			free			${true}
	dev			extra			&{plan_default_metadata}
	preprod		name			preprod
	preprod		description		plan preprod
	preprod		free			${true}
	preprod		extra			&{plan_default_metadata}
	prod		name			prod
	prod		description		plan prod
	prod		free			${true}
	prod		extra			&{plan_default_metadata}
