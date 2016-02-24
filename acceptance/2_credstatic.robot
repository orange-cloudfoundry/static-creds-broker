*** Settings ***
Resource            Keywords.robot
Documentation   Test service info and credential info
Force Tags      Service broker



*** Test Cases ***
0) Service info should correspond to broker manifest
    [Documentation]     Service info should correspond to broker manifest configuration
    ${ENTITY_INFO} =	Get service info
    ${PLAN_INFO} =	Get plan info
    Service entity info should correspond to broker manifest	${ENTITY_INFO}
    Service plan info should correspond to broker manifest	${PLAN_INFO}