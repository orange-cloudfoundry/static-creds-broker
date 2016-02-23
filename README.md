# static-creds-broker

A CloudFoundry service broker to ease exposition of static credentials in the CF marketplace, without requiring software development.

# Intro

This broker is aimed at service providers which would expose an existing service with static credentials.

# Usage overview

```
# Download the binary release of this broker
$ curl -O -L https://github.com/Orange-OpenSource/static-creds-broker/releases/download/v1.0/static-creds-broker-1.0.war

# Configure the broker through environment variables, possibly captured in a manifest
$ curl -O -L https://raw.githubusercontent.com/Orange-OpenSource/static-creds-broker/master/manifest.yml 

$ vi manifest.yml
---
applications:
- name: my-broker
  memory: 256M
  instances: 1
  host: mybroker
  domain: my-admin-domain.cf.io
  path: static-creds-broker-1.0.war 

  env:
    SECURITY_PASSWORD: MySecurePwd
    SERVICES_ID_NAME: MyService
    SERVICES_ID_DESCRIPTION: My existing service
    SERVICES_ID_METADATA_LONGDESCRIPTION: A long description for my service
    SERVICES_ID_CREDENTIALS_URI: mysql://USERNAME:PASSWORD@HOSTNAME:PORT/NAME
# deploy the broker    
$ cf push 

# Register the broker system-wise (required cloudcontroller.admin)
# refer to http://docs.cloudfoundry.org/services/managing-service-brokers.html#register-brokre
$ cf create-service-broker mybrokername someuser somethingsecure http://mybroker.example.com/
$ cf enable-service-access mybrokername

# Alternatively, register as a private service broker for one space or one org
# get the CF cli 6.16 or the latest edge binaries from https://github.com/cloudfoundry/cli#downloads
# cf create-service-broker SERVICE_BROKER USERNAME PASSWORD URL [--space-scoped]
$ cf create-service-broker MyService user MySecurePwd http://mybroker.my-admin-domain.cf.io --space-scoped

# Check presence of the service in the marketplace, and proper description of its plan
$ cf m
$ cf m -s MyService

# Create a service instance and service key to check proper binding of the static credentials
$ cf cs MyService myplan static-creds-instance
$ cf create-service-key static-creds-instance static-service-key
$ cf service-key static-creds-instance static-service-key
```

# Config reference

## Catalog

The catalog exposed by the broker is controlled by environment variables matching the [service broker catalog endpoint response](http://docs.cloudfoundry.org/services/api.html#catalog-mgmt). 
* SERVICES_ID_NAME (mandatory String, no default): the technical name of the service which should be unique among the cloudfoundry installation to register with (among orgs and spaces).
* SERVICES_ID_DESCRIPTION (mandatory String, no default)
* SERVICES_ID_BINDEABLE (default is "true"). Useful for service keys.
* SERVICES_ID_TAGS (array-of-strings, default is ```[]```)
* SERVICES_ID_METADATA_DISPLAYNAME (String, default is SERVICES_ID_NAME). The user-facing name of the service.
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
* SERVICES_ID_CREDENTIALS_URI String. Recommended see http://docs.cloudfoundry.org/services/binding-credentials.html
* SERVICES_ID_CREDENTIALS_HOSTNAME String. Optional
* SERVICES_ID_CREDENTIALS: a String holding a Json hash potentially compound the same format as 'cf cups', e.g. ```'{"username":"admin","password":"pa55woRD"}'````

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

Mature SDKs are available for Java through [spring-cloud-cloudfoundry-service-broker](https://github.com/spring-cloud/spring-cloud-cloudfoundry-service-broker) or [cf-java-component](https://github.com/cloudfoundry-community/cf-java-component/tree/master/cf-service-broker) (see associated [intro video](https://www.youtube.com/watch?v=AcpdO_AfEH0#t=11m43s) , ruby, go to ease implementing the [service broker REST API](http://docs.cloudfoundry.org/services/api.html). See [examples implementation](http://docs.cloudfoundry.org/services/examples.html)

Proof-of-concept SDK have been contributed for [PHP](https://github.com/cloudfoundry-community/php-cf-service-broker)

Someservice provider teams might find this broker useful because:
* there is not yet SDK in a programming language the service provider team is confortable with
* there is not much to program, it's mainly static configuration needed.

# Future work/backlog

* Support for multiple services in a single broker

As described, a single set of credentials is returned by the broker. In order to save RAM, multiple ID can be returned

* UI

As a service-provider, in order to be informed of the number of service instances, with which org/space they are bound to, I need a UI to display current service instances/service keys along with the corresponding org/space name

As a service-provider, in order to notify my customers of changes in my services,  I need a UI to display current service instances/service keys along with the corresponding org/space name to feed into the notifications services

* Multi-site support: per site credentials
   * The service broker is deployed on 3 sites with the same environement variabels. Some credentials are returned identically on all 3 sites, some credentials are returned differently depending on sites (overriding default values)
