*** Settings ***
Resource		../../service_catalog_keywords.robot
Documentation   Test static-creds-broker service static-creds-service-test catalog infomation

Suite Setup		Run Keywords	Write service static-creds-service-test3 catalog info in marketplace to the file static-creds-service-test3.txt
...				AND				Import Library      CFServiceCatalogParser      static-creds-service-test3.txt
Suite Teardown	Delete file static-creds-service-test3.txt

*** Variables ***
@{static-creds-service-test_tags}=	tag1	tag2	tag3

*** Test Cases ***
Service static-creds-service-test3 basic catalog info in the Cloud Foundry marketplace should match configuration
    [Documentation]     Test whether service basic properties in broker catalog info match configuration.
    [Template]      Service [static-creds-service-test3] basic catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}]
    label			static-creds-service-test3
    description		static-creds service for test
    bindable		${true}
    tags			@{static-creds-service-test_tags}

Service static-creds-service-test3 metadata catalog info in the Cloud Foundry marketplace should match configuration
    [Documentation]     Test whether service metadata properties in broker catalog info match configuration.
    [Template]      Service [static-creds-service-test3] metadata catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}]
    displayName				displayname for my service
    imageUrl				https://upload.wikimedia.org/wikipedia/commons/c/c8/Orange_logo.svg
    supportUrl				https://github.com/tao-xinxiu/static-creds-broker
	documentationUrl		https://github.com/tao-xinxiu/static-creds-broker
    providerDisplayName		provider display name
    longDescription			A long description for my service

Service static-creds-service-test3 plans catalog info in the Cloud Foundry marketplace should match configuration
    [Documentation]     Test whether plans properties in broker catalog info match configuration.
    [Template]       Service [static-creds-service-test3] plan [${plan_name}] catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}]
    myplan		name			myplan
    myplan		description 	The description of the plan
    myplan		free			${true}
    myplan		extra			&{plan_default_metadata}