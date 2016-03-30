# Use application.yml to configure static-creds-broker
how to deploy the broker by using ```application.yml``` instead of environment variables to configure static-creds-broker.

## Deploy
### Download the latest binary release of this broker and unzip the zip release of this broker. (Same step as using environment variables)
The zip contains binary release and manifest template file. 
```
# Create a clean directory for the deployment.
$ mkdir broker_deployment
$ cd broker_deployment/
$ LATEST_RELEASE_URL=$(curl -s https://api.github.com/repos/Orange-OpenSource/static-creds-broker/releases/latest | grep browser_download_url | head -n 1 | cut -d '"' -f 4)
$ LATEST_RELEASE_VERSION=${LATEST_RELEASE_URL#*/download/v}
$ LATEST_RELEASE_VERSION=${LATEST_RELEASE_VERSION%/static-creds-broker.zip}
$ echo "Latest version: $LATEST_RELEASE_VERSION"
$ echo "Downloading $LATEST_RELEASE_URL"
$ curl -O -L $LATEST_RELEASE_URL
$ unzip static-creds-broker.zip
```

### Unzip the .jar release of the broker and create the configuration file application.yml.
We need to unzip .jar binary release to create the configuration file application.yml. A sample application configuration file (application.tmpl.yml) is provided.
```
$ unzip static-creds-broker-$LATEST_RELEASE_VERSION.jar -d static-creds-broker-exploded/
$ cd static-creds-broker-exploded/
$ cp application.tmpl.yml application.yml
$ vi application.yml
enable: true
security:
    user:
        password: MySecurePwd
services:  
    TRIPADVISOR:
        NAME: MyService
        DESCRIPTION: My existing service 
        METADATA:
            LONGDESCRIPTION: A long description for my service
        CREDENTIALS:
            URI: mysql://USERNAME:PASSWORD@HOSTNAME:PORT/NAME
            ACCESS_KEY: AZERT23456664DFDSFSDFDSF
```

### Configure the manifest file used by CF CLI and deploy the broker.
A sample manifest file (manifest.tmpl.yaml-config.yml) is provided, create a manifest.yml file by adapting it to your environment (in particular set the domain)
```
$ cp manifest.tmpl.yaml-config.yml manifest.yml
$ vi manifest.yml
---
applications:
- name: my-broker
  memory: 256M
  instances: 1
  host: mybroker
  domain: my-admin-domain.cf.io
  path: static-creds-broker-1.3.war 
  env:
    SECURITY_USER_PASSWORD: MySecurePwd
# deploy the broker    
$ cf push 
# The rest part will be same as using envrionment variables
```

Note: broker security properties could be configured either in application.yml or in environment variables. If they are configured in both, the value configured in environment variables will be taken, according to the precedence of Spring property source.

## Config syntax
Note: <service_id> and <plan_id> should not contain "."
The properties meaning and default value could be consulted in [README](https://github.com/Orange-OpenSource/static-creds-broker#config-reference)
```
enable: <whether_use_yaml_config>
security:
    user:
        name: <broker_username>
        password: <broker_password>
services:  
    <service_id>:
        NAME: <service_name>
        DESCRIPTION: <service_description> 
        TAGS: <service_tags_separated_by_comma>
        METADATA:
            DISPLAYNAME: <service_displayname>
            IMAGEURL: <service_imageurl>
            SUPPORTURL: <service_supporturl>
            DOCUMENTATIONURL: <service_documentationurl>
            PROVIDERDISPLAYNAME: <service_provider_displayname>
            LONGDESCRIPTION: <service_long_description>
        CREDENTIALS:
            <service_credential_key>: <service_credential_value>
        PLAN:
            <plan_id>:
                NAME: <plan_name>
                DESCRIPTION: <plan_description>
                METADATA: <plan_metadata_string_holding_json_object>
                FREE: <whether_plan_is_free>
                CREDENTIALS:
                    <plan_credential_key>: <plan_credential_value>
    <another_service_id>:
        NAME: <another_service_name>
        DESCRIPTION: <another_service_description>
        CREDENTIALS: <credentials_in_format_string_holding_json_hash>
```