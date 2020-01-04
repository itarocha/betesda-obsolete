#Betesda

## Api

### Build app
`mvn clean package`

### Build docker image
`docker build -t betesda-api .`

### Run docker container
`docker run -d -p 8088:8088 betesda-api` and Navigate to `http://localhost:8080/api/app/...`

## Client

### install dependencies
npm install

### serve with hot reload at localhost:8080
npm run dev

### build for production with minification
npm run build

### build for production and view the bundle analyzer report
npm run build --report

