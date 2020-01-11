#Betesda

## Api

### Build app
`mvn clean package`

### Build docker image
`docker build -t betesda-api .`

### Run docker container
`docker run -d -p 8088:8088 betesda-api` and Navigate to `http://localhost:8080/api/app/...`