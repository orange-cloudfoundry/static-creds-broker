*** Settings ***
Resource		../keywords.robot
Library         Collections

*** Variables ***
&{plan_default_metadata}    bullets=${None}    costs=${None}  displayName=${None}

*** Keywords ***
Execute: CF_TRACE=true ${cmd}
    [Documentation]     Execute command which uses cf-cli with the envrionment variable CF_TRACE=true.
    ${result}=		Execute:    ${cmd}   CF_TRACE=true
    [return]		${result}

Delete file ${file_path}
    Execute command: rm ${file_path}

Write service ${service_name} catalog info in marketplace to the file ${service_name}.txt
    [Documentation]     Get service catalog info from Cloud Foundry marketplace, write it to ${service_name}.txt file.
    Execute: CF_TRACE=true cf marketplace -s ${service_name} > ${service_name}.txt

Service [${service_name}] basic catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}]
    [Documentation]     Verify that service basic properties in Cloud Foundry marketplace match its definition in the manifest.
    &{cf_service_info}=     Get cloud foundry service basic catalog info
    Log     ${cf_service_info}
    ${cf_property_value}=   Get From Dictionary     ${cf_service_info}  ${property_key}
    Should Be Equal         ${expected_property_value}      ${cf_property_value}      

Service [${service_name}] metadata catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}]
    [Documentation]     Verify that service metadata properties in Cloud Foundry marketplace match its definition in the manifest.
    &{cf_service_info}=     Get cloud foundry service metadata catalog info
    Log     ${cf_service_info}
    ${cf_property_value}=   Get From Dictionary     ${cf_service_info}  ${property_key}
    Should Be Equal         ${expected_property_value}      ${cf_property_value}  

Service [${service_name}] plan [${plan_name}] catalog info [${property_key}] in marketplace should match configured value [${expected_property_value}]
	[Documentation]     Verify that plan properties in Cloud Foundry marketplace match its definition in the manifest.
    &{cf_plans_info}=     Get cloud foundry service plan catalog info   ${plan_name}
    Log     ${cf_plans_info}
    ${cf_property_value}=   Get From Dictionary     ${cf_plans_info}  ${property_key}
    Should Be Equal         ${expected_property_value}      ${cf_property_value}  