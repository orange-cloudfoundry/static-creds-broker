# static-creds-broker

A CloudFoundry service broker to ease exposition of static credentials in the CF marketplace, without requiring software development.

# Intro

This broker is aimed at service providers which would expose an existing service with static credentials.

# Usage

```
# Download the binary release of this broker
$ curl -O https://github.com/Orange-OpenSource/static-creds-broker/.../release/.../latest/static-cred.war

# Configure the broker through environment variables, possibly captured in a manifest
$ curl -O https://github.com/Orange-OpenSource/static-creds-broker/[..]/manifest.yml 

$ vi manifest.yml
---
applications:
- name: my-broker
  memory: 256M
  instances: 1
  host: mybroker
  domain: my-admin-domain.cf.io
  path: static-cred.war 

  env:
    SECURITY_PASSWORD: MySecurePwd
    SERVICES_ID_NAME: MyService
    SERVICES_ID_DESCRIPTION: My existing service
    SERVICES_ID_METADATA_LONGDESCRIPTION: A long description for my service
    CREDENTIALS_URI: mysql://USERNAME:PASSWORD@HOSTNAME:PORT/NAME
# deploy the broker    
$ cf push 

# register the broker system-wise (required cloudcontroller.admin)
# refer to http://docs.cloudfoundry.org/services/managing-service-brokers.html#register-brokre
$ cf create-service-broker mybrokername someuser somethingsecure http://mybroker.example.com/
$ cf enable-service-access mybrokername


# register as a private service broker for one space or one org
[pending doc]
```

## Catalog

The catalog exposed by the broker is controlled by environment variables matching the [service broker catalog endpoint response](http://docs.cloudfoundry.org/services/api.html#catalog-mgmt). 
* SERVICES_ID_NAME (mandatory String, no default)
* SERVICES_ID_DESCRIPTION (mandatory String, no default)
* SERVICES_ID_BINDEABLE (default is "true"). Useful for service keys.
* SERVICES_ID_TAGS (array-of-strings, default is ```[]```)
* SERVICES_ID_METADATA_DISPLAYNAME (String, default is SERVICES_ID_NAME)
* SERVICES_ID_METADATA_IMAGEURL (String, default is "")
* SERVICES_ID_METADATA_SUPPORTURL (String, default is "")
* SERVICES_ID_METADATA_DOCUMENTATIONURL (String, default is "")
* SERVICES_ID_METADATA_PROVIDERDISPLAYNAME (String, default is "")
* SERVICES_ID_METADATA_LONGDESCRIPTION (String, default is "")

A single plan is supported. Use the following environment variables to configure it, or let the default values apply:
* PLAN_NAME (String, default is "default")
* PLAN_DESCRIPTION (String, default is "Default plan")
* PLAN_METADATA (String holding a JSON object, default is "{}")
* PLAN_FREE (String, default is "true")

A number of catalog variables are not configureable, the broker always return the following default value:
* requires: ```[]``` (empty array)
* plan_updateable: false
* dashboard_client: ````{}```` (empty)

## Bound credentials

The returned credentials are identical for all bound service instances and configured by the following environment variables, with at least one define.
* CREDENTIALS_URI String. Recommended see http://docs.cloudfoundry.org/services/binding-credentials.html
* CREDENTIALS_HOSTNAME String. Optional
* CREDENTIALS_MYOWNKEY String or JSON hash. Allow

This is mapped to [spring-cloud-cloudfoundry-service-broker](https://github.com/spring-cloud/spring-cloud-cloudfoundry-service-broker/blob/master/src%2Fmain%2Fjava%2Forg%2Fspringframework%2Fcloud%2Fservicebroker%2Fmodel%2FCreateServiceInstanceBindingResponse.java#L35) 

## Authentication

The service broker authenticates calls coming from Cloud Foundry through basic auth (see [more context](http://docs.cloudfoundry.org/services/api.html#authentication)) controlled by the following two environment variables
* SECURITY_USER: String default is "user"
* SECURITY_PASSWORD: String (mandatory, no default)


# FAQ

## Why not using CUPS ?

[User provided service instances](https://docs.cloudfoundry.org/devguide/services/user-provided.html) is a syntaxic support in CF targetted at application teams to expose static credentials as a service instance.

Limitations of this approach:
 * Service providers are not involved/notified when a CUPS is created 
    * making it harder to track who is referencing their service, e.g. to deprecate the service
    * making it harder to charge/bill for service usage
    * making it harder to notify users of changes (e.g. through notification service)
 * Discovery of the service requires out-of-band communication between application teams and service provider team, while the CF marketplace plays this role
    * This includes pointers to documentation, support ...

## Why not implementing a service broker using the community SDKs ?

Mature SDKs are available for Java through [spring-cloud-cloudfoundry-service-broker](https://github.com/spring-cloud/spring-cloud-cloudfoundry-service-broker) or [cf-java-component](https://github.com/cloudfoundry-community/cf-java-component/tree/master/cf-service-broker) (see associated [intro video](https://www.youtube.com/watch?v=AcpdO_AfEH0#t=11m43s) , ruby, go to ease implementing the [service broker REST API](http://docs.cloudfoundry.org/services/api.html)

Proof-of-concept SDK have been contributed for [PHP](https://github.com/cloudfoundry-community/php-cf-service-broker)

Someservice provider teams might find this broker useful because:
* there is not yet SDK in a programming language the service provider team is confortable with
* there is not much to program, it's mainly static configuration needed.

# Design/Implementation

Possible implementation/inspirations:
- [spring-cloud-cloudfoundry-service-broker](https://github.com/spring-cloud/spring-cloud-cloudfoundry-service-broker)
- [cf-java-component](https://github.com/cloudfoundry-community/cf-java-component/tree/master/cf-service-broker) and SPEL expressions to lookup environment variables, inspiration in cf-service-broker-smtp:
   - https://github.com/Orange-OpenSource/elpaaso-brokers/blob/master/cf-service-broker-smtp%2Fsrc%2Fmain%2Fjava%2Fcom%2Forange%2Fclara%2Fcloud%2Fcfbrokers%2Fsmtp%2FMain.java#L28
   - https://github.com/Orange-OpenSource/elpaaso-brokers/blob/master/cf-service-broker-smtp%2Fsrc%2Fmain%2Fjava%2Fcom%2Forange%2Fclara%2Fcloud%2Fcfbrokers%2Fsmtp%2FSmtpServiceBroker.java#L48-L91
