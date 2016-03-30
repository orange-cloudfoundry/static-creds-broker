import os

CF_ENDPOINT = os.environ.get("cf_target_endpoint").replace("\r","")
CF_SKIP_SSL = os.environ.get("skip_ssl_verification").replace("\r","").lower() == "true"
CF_USER = os.environ.get("cf_username").replace("\r","")
CF_PASSWORD = os.environ.get("cf_password").replace("\r","")
ORGANIZATION_NAME = os.environ.get("organization_name").replace("\r","")
SPACE_NAME = os.environ.get("space_name").replace("\r","")

BINARY_DIRECTORY = os.environ.get("binary_directory").replace("\r","")
if not BINARY_DIRECTORY.endswith('/'):
	BINARY_DIRECTORY = BINARY_DIRECTORY + '/'
MANIFEST_TMPL_PATH = BINARY_DIRECTORY + "manifest.tmpl.yml"
MANIFEST_PATH = BINARY_DIRECTORY + "manifest.yml"
# CONFIG_PATH = BINARY_DIRECTORY + "application.yml"
# informations about your broker deployment which requires to be defined in the manifest
BROKER_APP_NAME = os.environ.get("broker_app_name").replace("\r","")
BROKER_HOSTNAME = os.environ.get("broker_hostname").replace("\r","")
BROKER_DOMAIN = os.environ.get("broker_domain").replace("\r","")
BROKER_RELEASE_VERSION = os.environ.get("broker_release_version").replace("\r","")
BROKER_PASSWORD = os.environ.get("broker_password").replace("\r","")

# USE_YAML_CONFIG = os.environ.get("use_yaml_config", "false").replace("\r","").lower() == "true"

BROKER_NAME = os.environ.get("broker_name").replace("\r","")
PROTOCOL = os.environ.get("protocol", "http").replace("\r","")
TEST_APP_NAME = os.environ.get("test_application_name").replace("\r","")