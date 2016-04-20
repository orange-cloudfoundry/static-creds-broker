*** Settings ***
Resource		../../service_catalog_keywords.robot
Documentation   Test static-creds-broker service TRIPADVISOR_test_Service catalog infomation

Suite Setup		Run Keywords	Write service TRIPADVISOR_test_Service3 catalog info in marketplace to the file TRIPADVISOR_test_Service3.txt
...				AND				Import Library      CFServiceCatalogParser      TRIPADVISOR_test_Service3.txt
Suite Teardown	Delete file TRIPADVISOR_test_Service3.txt

*** Test Cases ***
Service TRIPADVISOR_test_Service3 basic catalog info in the Cloud Foundry marketplace should match configuration
    [Documentation]     Test whether service basic properties in broker catalog info match configuration.
    [Template]      Service [TRIPADVISOR_test_Service3] basic catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}]
    label			TRIPADVISOR_test_Service3
    description		My existing service
    bindable		${true}
    tags			@{EMPTY}

Service TRIPADVISOR_test_Service3 metadata catalog info in the Cloud Foundry marketplace should match configuration
    [Documentation]     Test whether service metadata properties in broker catalog info match configuration.
    [Template]      Service [TRIPADVISOR_test_Service3] metadata catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}]
    displayName				TRIPADVISOR_test_Service3
    imageUrl				${EMPTY}
    supportUrl				${EMPTY}
	documentationUrl		${EMPTY}
    providerDisplayName		${EMPTY}
    longDescription			${EMPTY}

Service TRIPADVISOR_test_Service3 plans catalog info in the Cloud Foundry marketplace should match configuration
    [Documentation]     Test whether plans properties in broker catalog info match configuration.
    [Template]       Service [TRIPADVISOR_test_Service3] plan [${plan_name}] catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}]
    default		name			default
    default		description 	plan default
    default		free			${true}
    default		extra			&{plan_default_metadata}