# GreenWay-Backend
Backend of the multiplatform App GreenWay a Routhing Service for EV

## Set-up

Install [Docker](https://www.docker.com/)

- Download map data of Sud Italy: https://download.geofabrik.de/europe/italy/sud-latest.osm.pbf
- You will get a file named ```sud-latest.osm.pbf``` 
- Move the file to ```osrm/data```
- Run the following command inside ```osrm/```: ```docker build . -t {dockerHubUsername}/{imageName}:{imageTag}``` 
the process will take several minutes
- Go to the root directory end edit the ```docker-compose.yml``` file modify the ```image``` name at line ```25``` 
with the value you have used in the step above ```{dockerHubUsername}/{imageName}:{imageTag}```
- Run the ```docker compose up``` in the root directory the REST API will be available at http://localhost:8080

## Customize the Set-up

- OSMR
  - Download the needed map data from [Geofabrik](https://www.geofabrik.de/)
  - You will get a file named ```your-location.osm.pbf```
  - Move the file to ```osrm/data```
  - Go to ```osrm/``` edit the ```Dockerfile``` at line ```5``` set ```OSRM_FILE``` value to ```your-location```
  - Run the following command inside ```osrm/```: ```docker build . -t {dockerHubUsername}/{imageName}:{imageTag}```

* Spring Boot
  * Go to ```GreenWay/```
  * Make the needed changes
  * Edit the ```pom.xml``` file replace ```redy1908``` with your dockerHub username at line ```94```
  * Run the following command ```./mvnw -DskipTests spring-boot:build-image ```
  * Optional push the image to dockerHub ```docker push docker.io/{your-dockerHub-username}/green-way-backend:v1```

- Docker-Compose
  - Go to the root folder
  - Edit the ```docker-compose``` file at line ```4``` edit the ```image``` value with the name of your image

## Run the Project

- - Run the ```docker compose up``` in the root directory the REST API will be available at http://localhost:8080