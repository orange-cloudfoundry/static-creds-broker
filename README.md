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
    
# deploy the broker    
$ cf push 

# register the broker system-wise (required cloudcontroller.admin)
$ 

# register as a private service broker
```

The catalog exposed by the broker is controlled by environment variables matching the [service broker catalog endpoint response](http://docs.cloudfoundry.org/services/api.html#catalog-mgmt):
* SERVICES_ID_NAME (mandatory String, no default)
* SERVICES_ID_DESCRIPTION (mandatory String, no default)
* SERVICES_ID_BINDEABLE (default is "true")
* SERVICES_ID_TAGS (array-of-strings, default is ```[]```)
* SERVICES_ID_METADATA_DISPLAYNAME (mandatory String, no default)
* SERVICES_ID_METADATA_IMAGEURL (String, default is "")
* SERVICES_ID_METADATA_SUPPORTURL (String, default is "")
* SERVICES_ID_METADATA_DOCUMENTATIONURL (String, default is "")
* SERVICES_ID_METADATA_PROVIDERDISPLAYNAME (String, default is "")
* SERVICES_ID_METADATA_LONGDESCRIPTION (String, default is "")



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

Mature SDKs are available for Java, ruby, go to ease implementing the [service broker REST API](http://docs.cloudfoundry.org/services/api.html)

Proof-of-concept SDK have been contributed for PHP

Someservice provider teams might find this broker useful because:
* there is not yet SDK in a programming language the service provider team is confortable with
* there is not much to program, it's mainly static configuration needed.

