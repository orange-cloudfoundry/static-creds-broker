import os

CLIENT_ENDPOINT = os.environ["cf_target_endpoint"].replace("\r","")
CLIENT_SKIP_SSL = os.environ["skip_ssl_verification"].replace("\r","").lower() == "true"
CLIENT_USER = os.environ["cf_username"].replace("\r","")
CLIENT_PASSWORD = os.environ["cf_password"].replace("\r","")
ORGANIZATION_NAME = os.environ["organization_name"].replace("\r","")
SPACE_NAME = os.environ["space_name"].replace("\r","")

USE_YAML_CONFIG = os.environ["use_yaml_config"].replace("\r","").lower() == "true"
CONFIG_PATH = os.environ["config_path"].replace("\r","")
BINARY_PATH = os.environ["binary_path"].replace("\r","")
MANIFEST_PATH = os.environ["manifest_path"].replace("\r","")
BROKER_USERNAME = os.environ["broker_username"].replace("\r","")
BROKER_PASSWORD = os.environ["broker_password"].replace("\r","")

BROKER_APP_NAME = os.environ["broker_app_name"].replace("\r","")
BROKER_APP_URL = os.environ["broker_app_url"].replace("\r","")
BROKER_NAME = os.environ["broker_name"].replace("\r","")
TEST_APP_NAME = os.environ["test_application_name"].replace("\r","")