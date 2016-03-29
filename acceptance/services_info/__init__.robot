*** Settings ***
Resource		../keywords.robot
Force Tags		Service broker
Suite Setup		Run Keywords  Cloud Foundry config and target   Clean all service broker data   Deploy service broker   Register service broker
Suite Teardown	Run Keywords  Clean all service broker data