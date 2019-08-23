#!/bin/bash
set -euo pipefail
mvn clean compile assembly:single
AWS_PROFILE=cc-service ~/src/s3patch/s3patch -v cp target/brics-lambda-1.0-SNAPSHOT-jar-with-dependencies.jar s3://code402-us-east-1/brics.jar
AWS_PROFILE=cc-service aws lambda update-function-code --function-name brics --s3-bucket code402-us-east-1 --s3-key brics.jar
