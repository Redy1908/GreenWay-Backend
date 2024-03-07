# GreenWay-Backend
This repository contains the backend for the multiplatform application,
GreenWay, an Electric Vehicle (EV) routing service.

## Set-up

Ensure that [Docker](https://www.docker.com/) is installed and running on your system.

This project uses [KeyCloak](https://www.keycloak.org/) as its Identity and Access Management (IAM) system. 
A pre-configured realm and two users are provided. 
Follow the steps below to import this configuration into the KeyCloak container:

- Navigate to the root folder and execute ```docker compose up keycloak -d```.
- Execute ```docker ps``` and note the ```CONTAINEIR-ID``` associated with the image ```quay.io/keycloak/keycloak:23.0.7``` save it
- Navigate to the ```KeyCloak/``` directory.
- Execute  ```docker cp GreenWay-realm.json <CONTAINEIR-ID>:/tmp/```.
- Execute  ```docker exec -it <CONTAINEIR-ID> /opt/keycloak/bin/kc.sh import --file tmp/GreenWay-realm.json```. 

If the setup is successful, 
accessing http://localhost:8090/realms/GreenWay/protocol/openid-connect/certs should yield a response. 
If not, restart the container with ```docker restart <CONTAINEIR-ID>```

Upon successful setup, KeyCloak will have the following two users configured:

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

To export new users or modified KeyCloak configurations, use the following command:
```dockerfile 
docker exec -it <CONTAINEIR-ID> /opt/keycloak/bin/kc.sh export --dir /tmp --users realm_file && docker cp <CONTAINEIR-ID>:/tmp/GreenWay-realm.json .
```

This will create the file ```GreenWay-realm.json``` in the current directory.

## Running the Project

Execute ```docker compose up -d``` iin the root directory. The REST API will be available at http://localhost:8080.

## Customizing the Setup (Optional)

## 1. OpenStreetMap Routing (OSMR)

By default, OSMR is configured with the map of Southern Italy. To set up a different location, follow these steps:

1. Download the needed map data from [Geofabrik](https://www.geofabrik.de/)
2. You will get a file named ```your-location.osm.pbf```
3. Move the file to ```osrm/data```
4. Go to ```osrm/``` edit the ```Dockerfile``` at line ```5``` set ```OSRM_FILE``` value to ```your-location```
5. Run the following command inside ```osrm/```: ```docker build . -t {dockerHubUsername}/{imageName}:{imageTag}```


## 2. Spring Boot
To make changes to the Spring Boot REST API and run it immediately without creating a custom image, follow these steps:

1. Navigate to ```GreenWay/``` and make the necessary changes.
2. Edit the ```pom.xml``` file replace ```redy1908``` with your dockerHub username at line ```106```.
3. Execute ```./mvnw -DskipTests spring-boot:build-image ```.
4. Optionally, push the image to DockerHub with ```docker push docker.io/{your-dockerHub-username}/green-way-backend:v1```.

If you want to make changes to the Spring Boot REST API and run it immediately without creating a custom image, 
do the following:
    
1. Enable the Spring Boot profile ```local```
2. Run ```docker compose up -d``` in ```osrm/```
3. Run ```docker-compose up -d postgis``` in the root folder
4. Run the Spring Boot Application the REST API will be available at http://localhost:8080

## 3. Docker-Compose 
If you have performed any of the previous steps, you must edit the ```docker-compose``` file in the root folder:
   1. On line 4, replace the `image`  value with the name of your image (use the value at line 106 in the ```pom.xml```) if you have performed the previous ```Spring Boot``` step.
   2. On line 25, replace the `image` value with the name of your image (use the value used in the last step of OSMR) if you have performed the previous ```OSMR``` step.