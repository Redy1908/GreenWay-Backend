# GreenWay-Backend
Backend of the multiplatform App GreenWay a Routhing Service for EV

## Customize the Set-up (Optional)

- OSMR (Optional)
  - By default, OSMR will be set with the southern italy map following the next steps you can set up a different location
  - Download the needed map data from [Geofabrik](https://www.geofabrik.de/)
  - You will get a file named ```your-location.osm.pbf```
  - Move the file to ```osrm/data```
  - Go to ```osrm/``` edit the ```Dockerfile``` at line ```5``` set ```OSRM_FILE``` value to ```your-location```
  - Run the following command inside ```osrm/```: ```docker build . -t {dockerHubUsername}/{imageName}:{imageTag}```

* Spring Boot (Optional)
  * Go to ```GreenWay/```
  * Make the needed changes
  * Edit the ```pom.xml``` file replace ```redy1908``` with your dockerHub username at line ```94```
  * Run the following command ```./mvnw -DskipTests spring-boot:build-image ```
  * Optional push the image to dockerHub ```docker push docker.io/{your-dockerHub-username}/green-way-backend:v1```

- Docker-Compose (Mandatory if you have done any of the previous steps)
  - Go to the root folder
  - Edit the ```docker-compose``` file at line ```4``` edit the ```image``` value with the name of your image (see line 94 in the pom.xml) only if you have done the previous step ```Spring Boot``` 
  - Edit the ```docker-compose``` file at line ```25``` edit the ```image``` value with the name of your image (see the last step of OSMR) only if you have done the previous step ```OSMR```

## Run the Project

- Make sure to have [Docker](https://www.docker.com/) installed and running
- Run the ```docker compose up -d``` in the root directory the REST API will be available at http://localhost:8080

If you want to make changes to the Spring Boot REST API end immediately run it without creating a custom image see (Customize the Set-up) do the following:
  * Enable the Spring Boot profile ```local```
  * Run ```docker compose up -d``` in ```osrm/```
  * Run ```docker-compose up -d postgis``` in the root folder
  * Run the Spring Boot Application the REST API will be available at http://localhost:8080