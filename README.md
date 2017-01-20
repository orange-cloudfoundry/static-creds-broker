# static-creds-broker [![Build Status](https://travis-ci.org/Orange-OpenSource/static-creds-broker.svg?branch=master)](https://travis-ci.org/Orange-OpenSource/static-creds-broker)

A CloudFoundry service broker to ease exposition of static credentials in the CF marketplace, without requiring software development.

# Intro

This generic service broker is used by service providers to expose one or more services systematically returning static credentials, along with static service catalog. A static-creds-broker instance is usually configured to return distinct catalog and binding credentials per service and per service plan.

Deploying a static-creds-broker instance takes few minutes: configure the static data to be returned, and ``cf push`` the pre-built static-creds-brokers binaries.

When deploying a static-creds-broker instance on cloudfoundry, configure it either through 
- environment variables (see [manifest.tmpl.yml](manifest.tmpl.yml) for an example CF CLI manifest), 
- a yaml configuration file deployed along with the broker, see [yaml deployment procedure](use_yaml_config.md)
- a yaml configuration file fetched from a remote git repository over the network
(see [manifest.tmpl.remote-config.yml](manifest.tmpl.remote-config.yml) for an example CF CLI manifest using this configuration mode).

Notice that the default behavior is to fetch configuration properties from a remote git repo.  
To disable the default behavior, please set following env variable : SPRING_PROFILES_ACTIVE: native

Also note that the 2.X static-creds-broker releases are only compatible with **Diego backends** and do not support DEA backends (more details in [issue 27](https://github.com/orange-cloudfoundry/static-creds-broker/issues/27). To use static-creds-brokers with DEA backends, please use the latest 1.X branch release such as https://github.com/orange-cloudfoundry/static-creds-broker/tree/v1.3 and the associated 1.X configuration syntax.

# Usage overview

```sh
# Download the latest binary release of this broker
LATEST_RELEASE_URL=$(curl -s https://api.github.com/repos/orange-cloudfoundry/static-creds-broker/releases/latest | grep browser_download_url | head -n 1 | cut -d '"' -f 4)
echo "Downloading $LATEST_RELEASE_URL"
curl -O -L $LATEST_RELEASE_URL

# Unzip the zip release of this broker. The zip contains binary release and manifest template file.
unzip static-creds-broker.zip

# Configure the broker through environment variables, possibly captured in a CF CLI manifest file
# Example manifest files (manifest.tmpl.yml, manifest.tmpl.yaml-config.yml, manifest.tmpl.remote-config.yml)
# are provided, create a manifest.yml file by adapting it to your environment (in particular set the domain)
# Remember that credentials can be set using env variables (see manifest.tmpl.yml), using local configuration properties
#(see manifest.tmpl.yaml-config.yml) or by referencing external configuration properties backed in a remote git repository
#(see manifest.tmpl.remote-config.yml)
# Note: Be careful that services and plans name should be unique in the scope of your Cloud Foundry platform.

cat <<- EOF > manifest.yml
---
applications:
- name: my-broker
  memory: 256M
  instances: 1
  host: mybroker
  domain: my-admin-domain.cf.io
  path: static-creds-broker-2.0.jar 

  env:
    SECURITY_USER_PASSWORD: MySecurePwd
    SERVICES[ID]_NAME: MyService
    SERVICES[ID]_DESCRIPTION: My existing service
    SERVICES[ID]_METADATA_LONG_DESCRIPTION: A long description for my service
    SERVICES[ID]_CREDENTIALS[URI]: mysql://USERNAME:PASSWORD@HOSTNAME:PORT/NAME
EOF

# deploy the broker    
cf push 

# Register the broker system-wise (requires cloudcontroller.admin i.e. admin access to the CloudFoundry instance)
# refer to http://docs.cloudfoundry.org/services/managing-service-brokers.html#register-broker
cf create-service-broker mybrokername someuser somethingsecure http://mybroker.example.com/
# Then make individual services visibles in desired orgs or in all orgs,
# see  http://docs.cloudfoundry.org/services/access-control.html#enable-access
cf enable-service-access MyService

# Alternatively, register as a private service broker for one space or one org
# get the CF cli 6.16 or the latest edge binaries from https://github.com/cloudfoundry/cli#downloads
# cf create-service-broker SERVICE_BROKER USERNAME PASSWORD URL [--space-scoped]
cf create-service-broker mybrokername user MySecurePwd http://mybroker.my-admin-domain.cf.io --space-scoped

# Check presence of the service in the marketplace, and proper description of its plan
cf m
cf m -s MyService

# Create a service instance and service key to check proper binding of the static credentials
cf cs MyService myplan static-creds-instance
cf create-service-key static-creds-instance static-service-key
cf service-key static-creds-instance static-service-key
```

# Config reference

This section lists the configuration key/values that this broker supports (passed through env vars or yml config files).

Conventional notation: 
- {SERVICE_ID} should be replaced in keys by your own service id which is a string used to identify service. 
- {PLAN_ID} should be replaced in key by your own plan id which is a string used to identify different plans defined in a service.

Please notice that from 2.X version, there has been a complete refactoring leading to non backward compatible API changes.
Thus, to benefit from new 2.X version, you will need to review your exiting configuration to ensure it is compliant
with new config reference.

| 1.x version   | 2.X version |
| ------------- | ------------- |
| SERVICES_{SERVICE_ID}_NAME  |SERVICES[{SERVICE_ID}]_NAME  |
| SERVICES_{SERVICE_ID}\_PLAN\_{PLAN_ID}_NAME  | SERVICES[{SERVICE_ID}]\_PLANS\[{PLAN_ID}]_NAME  |
| SERVICES_{SERVICE_ID}_CREDENTIALS_URI  |SERVICES[{SERVICE_ID}]\_CREDENTIALS\[URI] |
| ..._METADATA_DISPLAYNAME | _METADATA_DISPLAY_NAME |
| ..._METADATA_IMAGEURL | _METADATA_IMAGE_URL |
| ..._METADATA_SUPPORTURL | _METADATA_SUPPORT_URL |
| ..._METADATA_DOCUMENTATIONURL | _METADATA_DOCUMENTATION_URL |
| ..._METADATA_PROVIDERDISPLAYNAME | _METADATA_PROVIDER_DISPLAY_NAME |
| ..._METADATA_LONGDESCRIPTION | METADATA_LONG_DESCRIPTION |
| ..._CREDENTIALS = {json} |  _CREDENTIALS_JSON = {json} |


## Catalog

The catalog exposed by the broker is controlled by environment variables matching the [service broker catalog endpoint response](http://docs.cloudfoundry.org/services/api.html#catalog-mgmt). 
* SERVICES[{SERVICE_ID}]_NAME (mandatory String, no default): the technical name of the service which should be unique
among the cloudfoundry installation to register with (among orgs and spaces).
* SERVICES[{SERVICE_ID}]_DESCRIPTION (mandatory String, no default)
* SERVICES[{SERVICE_ID}]_BINDEABLE (default is "true"). Useful for service keys.
* SERVICES[{SERVICE_ID}]_TAGS (String holding an array-of-strings, multiple tags are separated by comma,
as ```tag1,tag2,tag3```, default is ```[]```)
* SERVICES[{SERVICE_ID}]_METADATA_DISPLAY_NAME (String, default is SERVICES_ID_NAME). The user-facing name of the service.
* SERVICES[{SERVICE_ID}]_METADATA_IMAGE_URL (String, default is "")
* SERVICES[{SERVICE_ID}]_METADATA_SUPPORT_URL (String, default is "")
* SERVICES[{SERVICE_ID}]_METADATA_DOCUMENTATION_URL (String, default is "")
* SERVICES[{SERVICE_ID}]_METADATA_PROVIDER_DISPLAY_NAME (String, default is "")
* SERVICES[{SERVICE_ID}]_METADATA_LONG_DESCRIPTION (String, default is "")

Multiple plans are supported. Use the following environment variables to configure it, or let the default values apply:
* SERVICES[{SERVICE_ID}]\_PLANS\[{PLAN_ID}]_NAME (String, default is "default")
* SERVICES[{SERVICE_ID}]\_PLANS\[{PLAN_ID}]_DESCRIPTION (String, default is "Default plan")
* SERVICES[{SERVICE_ID}]\_PLANS\[{PLAN_ID}]_METADATA (String holding a JSON object, default is "{}")
* SERVICES[{SERVICE_ID}]\_PLANS\[{PLAN_ID}]_FREE (String, default is "true")

A number of catalog variables are not configureable, the broker always return the following default value:
* requires: ```[]``` (empty array)
* plan_updateable: false
* dashboard_client: ````{}```` (empty)

## Bound credentials

The returned credentials are identical for all bound service instances of a specific plan~~, with at least one define~~.

The credentials could be defined for a service, it will be applied for all plans of the service.
It is configured by the following environment variables:
* SERVICES[{SERVICE_ID}]\_CREDENTIALS\[URI] String. Recommended
see http://docs.cloudfoundry.org/services/binding-credentials.html
* SERVICES[{SERVICE_ID}]\_CREDENTIALS\[HOSTNAME] String. Optional
* SERVICES[{SERVICE_ID}]\_CREDENTIALS\[{MYOWNKEY}] String. It is for flat custom keys. Note, it's case sensitive.
For example. you could specify ```SERVICES[{SERVICE_ID}]_CREDENTIALS_ACCESS_KEY: azert```, the returned credentials will
 contain a key named "ACCESS_KEY" ```{..., "ACCESS_KEY":"azert", ...}```
* SERVICES[{SERVICE_ID}]\_CREDENTIALS\_JSON: a String holding a Json hash potentially compound the same format as
 'cf cups', e.g. ```'{"username":"admin","password":"pa55woRD"}'```

The credentials could also be defined for a particular plan, if it contains conflict credential key between the service
credentials and plan credentials, the values of the plan credentials will be taken.
It is configured by the following environment variables:
* SERVICES[{SERVICE_ID}]\_PLANS\[{PLAN_ID}]_CREDENTIALS\_[URI] String.
* SERVICES[{SERVICE_ID}]\_PLANS\[{PLAN_ID}]_CREDENTIALS\_[HOSTNAME] String. Optional
* SERVICES[{SERVICE_ID}]\_PLANS\[{PLAN_ID}]\_CREDENTIALS\_[{MYOWNKEY}] String.
It is for flat custom keys, as SERVICES[{SERVICE_ID}]\_CREDENTIALS\_[{MYOWNKEY}]
* SERVICES[{SERVICE_ID}]\_PLANS\[{PLAN_ID}]_CREDENTIALS_JSON: a String holding a Json hash potentially compound
the same format as 'cf cups', e.g. ```'{"username":"admin","password":"pa55woRD"}'```

This is mapped to [spring-cloud-cloudfoundry-service-broker](https://github.com/spring-cloud/spring-cloud-cloudfoundry-service-broker/blob/master/src%2Fmain%2Fjava%2Forg%2Fspringframework%2Fcloud%2Fservicebroker%2Fmodel%2FCreateServiceInstanceBindingResponse.java#L35) 

## Authentication

The service broker authenticates calls coming from Cloud Foundry through basic auth
(see [more context](http://docs.cloudfoundry.org/services/api.html#authentication)) controlled by the following two environment variables
* SECURITY_USER_NAME: String default is "user"
* SECURITY_USER_PASSWORD: String (mandatory, no default)

# Troubleshoot Deployment Problems
If the deployment of the broker fails, you could find the error message using cf cli:
```
$ cf logs <app_name> --recent | findstr Caused
# or more complete error information with:
$ cf logs <app_name> --recent | findstr ERR
```

# Acceptance Test
Acceptance tests source code is available in the "acceptance" folder. To run it locally, you should:
- Install robotframework with ```pip install robotframework```. More details on [robotframework installation](https://code.google.com/archive/p/robotframework/wikis/Installation.wiki)
- Deploy a test application in your Cloud Foundry environment. (ex. a [static website](https://github.com/cloudfoundry/staticfile-buildpack))
- Retrieve the content of the "acceptance" folder from the sources to your {ACCEPTANCE_TEST_DIRECTORY}.
- Set the environment variables required:
  - copy ```{ACCEPTANCE_TEST_DIRECTORY}/acceptance.tmpl.env``` to ```{ACCEPTANCE_TEST_DIRECTORY}/acceptance.env``` 
  - fill the information in ```{ACCEPTANCE_TEST_DIRECTORY}/acceptance.env``` 
  - source it ```source {ACCEPTANCE_TEST_DIRECTORY}/acceptance.env```
- Run the acceptance test with the command: 
```
robot --pythonpath {ACCEPTANCE_TEST_DIRECTORY}/classes/ {ACCEPTANCE_TEST_DIRECTORY}
```

# FAQ

## Why not using CUPS ?

[User provided service instances](https://docs.cloudfoundry.org/devguide/services/user-provided.html) is a syntaxic
support in CF targetted at application teams to expose static credentials as a service instance.

Limitations of the UPS approach:
 * Service providers are not involved/notified when a CUPS is created 
    * making it harder to track who is referencing their service, e.g. to deprecate the service
    * making it harder to charge/bill for service usage
    * making it harder to notify users of changes (e.g. through notification service)
 * Discovery of the service requires out-of-band communication between application teams and service provider team,
 while the CF marketplace plays this role
    * This includes pointers to documentation, support ...
  * does not allow for automation, such as opening security groups to access IPs provided in credentials
  (see https://github.com/Orange-OpenSource/sec-group-brokerchain )

## Why not implementing a service broker using the community SDKs ?

Mature SDKs are available for Java through [spring-cloud-cloudfoundry-service-broker](https://github.com/spring-cloud/spring-cloud-cloudfoundry-service-broker) or [cf-java-component](https://github.com/cloudfoundry-community/cf-java-component/tree/master/cf-service-broker)
(see associated [intro video](https://www.youtube.com/watch?v=AcpdO_AfEH0#t=11m43s) , ruby, go to ease implementing the [service broker REST API](http://docs.cloudfoundry.org/services/api.html).
See [examples implementation](http://docs.cloudfoundry.org/services/examples.html)

Proof-of-concept SDK have been contributed for [PHP](https://github.com/cloudfoundry-community/php-cf-service-broker)

Some service provider teams (or app teams needing to expose services) might find the static-creds-broker useful because:
* they lack a SDK in a programming language the service provider team is confortable with
* they don't need to program, it's mainly static configuration needed.
* they don't have to maintain a broker implementation as the service broker API get enriched or gets possible breaking changes, the static-cred-broker is maintained

# Future work/backlog

See github issues for the list of future features planned, with associated milestones reflecting relative priority (see convenient https://huboard.com/orange-cloudfoundry/static-creds-broker for contributors to reorder priorities). Comments and refinements are welcomed. 

Please suggest more ideas by submitting new issues.

