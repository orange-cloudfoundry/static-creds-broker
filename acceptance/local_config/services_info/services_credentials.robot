*** Settings ***
Documentation   Test static-creds-broker services credentials infomation
Resource        ../../services_credentials_keywords.robot

*** Variables ***
&{TRIPADVISOR_credentials}				URI=http://....
&{API_DIRECTORY_dev_credentials}		HOSTNAME=http://company.com 		URI=http://mydev-api.org		ACCESS_KEY=devAZERT23456664DFDSFSDFDSF
&{API_DIRECTORY_preprod_credentials}	HOSTNAME=http://company.com 		URI=http://mypreprod-api.org	ACCESS_KEY=preprodAZERT23456664DFDSFSDFDSF
&{API_DIRECTORY_prod_credentials}		HOSTNAME=http://prod.company.com 	URI=http://myprod-api.org		ACCESS_KEY=prodAZERT23456664DFDSFSDFDSF
&{ID_credentials}						uri=mysql://USERNAME:PASSWORD@HOSTNAME:PORT/NAME 	HOSTNAME=us-cdbr-east-03.cleardb.com 	username=admin 	password=pa55woRD

*** Test Cases ***
Credential info in service key of each service plan should match configuration
	[Documentation]     Test whether credential info returned by service-key matches configuration.
	[Template]		Credential info in service key of service ${service_name} plan ${plan_name} should match configuration ${expected_credentials_dict}
	TRIPADVISOR_test_Service2		default		&{TRIPADVISOR_credentials}		
	API_DIRECTORY_test_Service2		dev 		&{API_DIRECTORY_dev_credentials}
    API_DIRECTORY_test_Service2 	preprod     &{API_DIRECTORY_preprod_credentials}
    API_DIRECTORY_test_Service2 	prod        &{API_DIRECTORY_prod_credentials}
	static-creds-service-test2 		myplan 		&{ID_credentials}

Credential info in bound application should match configuration for each service plan 
    [Documentation]     Test whether credential info configured in bound application envrionment variables matches configuration.
    [Template]		Credential info in bound application for service ${service_name} plan ${plan_name} should match configuration ${expected_credentials_dict}
	TRIPADVISOR_test_Service2		default		&{TRIPADVISOR_credentials}		
	API_DIRECTORY_test_Service2		dev         &{API_DIRECTORY_dev_credentials}
    API_DIRECTORY_test_Service2 	preprod     &{API_DIRECTORY_preprod_credentials}
    API_DIRECTORY_test_Service2 	prod        &{API_DIRECTORY_prod_credentials}
	static-creds-service-test2 		myplan 		&{ID_credentials}
