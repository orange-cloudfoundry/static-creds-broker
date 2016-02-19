import os

ORGANIZATION_NAME = os.environ["organization_name"].replace("\r","")
SPACE_NAME = os.environ["space_name"].replace("\r","")
SERVICE_NAME = os.environ["service_name"].replace("\r","")
PLAN_NAME = os.environ["plan_name"].replace("\r","")
TESTED_APP_NAME = os.environ["application_name"].replace("\r","")
SERVICE_INSTANCE_NAME = os.environ["service_instance_name"].replace("\r","")

CLIENT_ENDPOINT = os.environ["cf_target_endpoint"].replace("\r","")
CLIENT_SKIP_SSL = os.environ["skip_ssl_verification"].replace("\r","").lower() == "true"
CLIENT_USER = os.environ["cf_username"].replace("\r","")
CLIENT_PASSWORD = os.environ["cf_password"].replace("\r","")

