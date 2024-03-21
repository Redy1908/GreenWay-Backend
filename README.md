# GreenWay-Backend
This repository contains the backend for the multiplatform application (IOS and Android),
GreenWay, an Electric Vehicle (EV) routing service.

## Set-up

> :warning: This set up is only for development/prototyping <u>DO NOT</u> use it in production :warning:

This project uses [KeyCloak](https://www.keycloak.org/) as its Identity and Access Management (IAM) system. 
A pre-configured realm and the following users are already configured:

```
Username: admin
Password: admin
KeyCloak default admin
```

```
Username: GREEN_WAY_ADMIN
Password: 12345
with GREEN_WAY_ADMIN privileges
```

```
Username: deliveryMan1
Password: 12345
with GREEN_WAY_DELIVERY_MAN privileges
```

If you want to edit the KeyCloak configuration or add new users, access the dashboard at - KeyCloak dashboard http://localhost:8090/

## Running the Project

Ensure that [Docker](https://www.docker.com/) is installed and running on your system.

Execute ```docker compose up -d``` in the root directory. 
- The REST API will be available at http://localhost:8080.

## Running only OSRM

Ensure that [Docker](https://www.docker.com/) is installed and running on your system.

Execute ```docker compose up -d``` in ```osrm/```.

- OSRM backend will be available at http://localhost:5000
- OSRM frontend will be available at http://localhost:9966



## Customizing the Setup (Optional)

### 1. OpenStreetMap Routing (OSMR)

By default, OSMR is configured with the map of Southern Italy and the relative elevation data. To set up a different location, follow these steps:

1. Download the needed map data from [Geofabrik](https://www.geofabrik.de/)
2. You will get a file named ```your-location.osm.pbf```
3. Move the file to ```osrm/data```
4. Go to ```osrm/``` edit the ```Dockerfile``` at line ```6``` set ```OSRM_FILE``` value to ```your-location```
5. Download the needed elevation data from [here](https://srtm.csi.cgiar.org/srtmdata/)
6. You will get a file with extension ```.asc``` move that file to ```osrm/data```
7. Open the file e note the content of the first ```6``` lines
8. Go to ```osrm/``` edit the ```Dockerfile``` from line ```7``` to ```13``` with the content from the  ```.asc``` file (step 7)
9. Remove the first ```6``` lines from the  ```.asc``` file, save the changes
10. Run the following command inside ```osrm/```: ```docker build . -t {dockerHubUsername}/{imageName}:{imageTag}```
11. Edit the ```docker-compose.yml``` file in the root directory, on line 64, replace the `image` value with the name of your image (use the value used in the step above)


### 2. Spring Boot

1. Navigate to ```GreenWay/``` and make the necessary changes.
2. Edit the ```pom.xml``` file replace ```redy1908``` with your dockerHub username at line ```106```.
3. Execute ```./mvnw -DskipTests spring-boot:build-image```.
4. Edit the ```docker-compose.yml``` file in the root directory, on line 4, replace the `image`  value with the name of your image (use the value at line 106 in the ```pom.xml```)
5. Optionally, push the image to DockerHub with ```docker push docker.io/{your-dockerHub-username}/green-way-backend:v1```

If you want to make changes to the Spring Boot REST API and run it immediately without creating a custom image, 
do the following:
    
1. Enable the Spring Boot profile ```local```
2. Run ```docker compose up -d``` in ```osrm/```
3. Run ```docker-compose up -d postgis``` in the root folder
4. Run the Spring Boot Application the REST API will be available at http://localhost:8080

---

| Useful commands                                           |
|-----------------------------------------------------------|
| ```./mvnw -DskipTests clean install```                    |
| ```./mvnw -DskipTests spring-boot:build-image```          |
| ```docker push docker.io/redy1908/green-way-backend:v1``` |