# brics-lambda

An AWS lambda deployment to validate and test brics.dk automata.

`aws cloudformation --region us-east-1 create-stack --stack-name brics --template-body file://stack.yaml --capabilities CAPABILITY_IAM` creates the stack.

`./go` builds the code, uploads to S3 and refreshes the Lambda.

After building the stack, go to the API Gateway and enable CORS for the stage.

## Usage

```
$ curl https://us-east-1.brics.code402.com/test` -d '{"needle": "[0-9]+", "haystack": "abc123 def456"}'
{"hits":["123","456"]}
```
