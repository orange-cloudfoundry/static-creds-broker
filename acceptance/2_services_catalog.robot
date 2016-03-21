*** Settings ***
Resource            Keywords.robot
Documentation   Test static-creds-broker services catalog infomation
Library         String
Library         Collections
Force Tags      Service broker
Library         Compare      ${USE_YAML_CONFIG}      ${MANIFEST_PATH}       ${CONFIG_PATH}

Suite Setup      Run Keywords  Cloud Foundry config and target   Clean all service broker data   Deploy service broker   Register service broker
Suite Teardown   Run Keywords  Clean all service broker data

*** Test Cases ***
Catalog info should match manifest
    [Documentation]     Test whether broker catalog info (services and plans properties) match manifest definition.
    Services defined in manifest should appear in marketplace
    Each service catalog info in marketplace should match manifest

*** Keywords ***
Services defined in manifest should appear in marketplace
    [Documentation]     Verify that every services name defined in the manifest shows in Cloud Foundry marketplace.
    ${services_name}=    Get services name
    ${result}=  Execute:     cf m
    Log     ${result}
    :FOR    ${service_name}    IN  @{services_name}
    \       Should Contain     ${result}   ${service_name}

Each service catalog info in marketplace should match manifest
    [Documentation]     Verify that every services properties in Cloud Foundry marketplace match its definition in the manifest. 
    ${services}=    Get services id and name
    ${service_items}=    Get Dictionary Items    ${services}
    :FOR    ${service_id}     ${service_name}     IN  @{service_items}
    \       Execute:    cf m -s ${service_name} > tmp.txt    CF_TRACE=true
    \       Service catalog info should match configuration    tmp.txt   ${service_id}
    \       Execute:    rm tmp.txt   