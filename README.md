# GreenWay-Backend
This repository contains the backend for the multiplatform application (IOS and Android),
GreenWay, an Electric Vehicle (EV) routing service.

## Set-up

1. This project use [Project OSRM](https://project-osrm.org/) for its routing capabilities. OSRM has been configured
   with map and elevation data for Southern Italy. You will need to configure [Opentopodata](https://www.opentopodata.org/) with the same
   elevation data used in OSRM. Navigate to ```oepntopodata/data``` create a new folder named ```greenWay```
   download this [Archive](https://srtm.csi.cgiar.org/wp-content/uploads/files/srtm_5x5/TIFF/srtm_39_04.zip) and extract the file ```srtm_39_04.tif```,
   rename it as ```greenway.tif``` and move it in ```oepntopodata/data/greenWay```.

   If you wish to use a different configuration, please continue reading.

2. > :warning: This set up is only for development/prototyping <u>DO NOT</u> use it in production :warning:

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
    Username: deliveryman1
    Password: 12345
    with GREEN_WAY_DELIVERY_MAN privileges
    ```
   
    ```
    Username: deliveryman2
    Password: 12345
    with GREEN_WAY_DELIVERY_MAN privileges
    ```

    If you want to edit the KeyCloak configuration or add new users, access the 
    KeyCloak dashboard http://localhost:8090/ using the default admin profile

## Running the Project

Ensure that [Docker](https://www.docker.com/) is installed and running on your system.

Execute ```docker compose up -d``` in the root directory. 
- The REST API will be available at http://localhost:8080.

## Customizing the Setup (Optional)

### 1. Open Source Routing Machine (OSMR)

By default, the Open Source Routing Machine (OSRM) is instantiated in four distinct configurations, each optimized for a 
different routing parameter: distance, duration, elevation, and the standard OSRM configuration. Each instance 
is pre-configured with the map of Southern Italy, the elevation instance is also configured with the corresponding elevation data. 
To configure a different geographical location, please follow the following steps:

1. Download the needed map data from [Geofabrik](https://www.geofabrik.de/)
2. You will get a file named ```your-location.osm.pbf```
3. Move the file to ```osrm/data```
4. Go to ```osrm/``` edit the ```4 Dockerfile``` at line ```6``` set ```OSRM_FILE``` value to ```your-location```
5. Download the needed elevation data from [here](https://srtm.csi.cgiar.org/srtmdata/), select ```Esri ASCII```
6. You will get a file with extension ```.asc``` move that file to ```osrm/data```
7. Open the file end note the content of the first ```6``` lines
8. Go to ```osrm/``` edit the ```Dockerfile-osrm-elevation``` from line ```7``` to ```13``` according to your ```.asc``` file
9. Remove the first ```6``` lines from the  ```.asc``` file, save the changes 
10. Go to ```GreenWay/src/main/resources``` edit the files ```application.yml``` and ```application-local.yml```, lines 27-28-29-30 with 
    your max and min coordinates. (You will have to create a new Spring Boot image or run a local version with the new changes see below)
11. Run the following commands inside ```osrm/```:
    - ```docker build -t {dockerHubUsername}/{imageName}:{imageTag} -f Dockerfile-osrm-distance .```
    - ```docker build -t {dockerHubUsername}/{imageName}:{imageTag} -f Dockerfile-osrm-duration .```
    - ```docker build -t {dockerHubUsername}/{imageName}:{imageTag} -f Dockerfile-osrm-elevation .```
    - ```docker build -t {dockerHubUsername}/{imageName}:{imageTag} -f Dockerfile-osrm-standard .```
12. Edit the ```docker-compose.yml``` file in the root directory, lines 74-82-86-92, replace the `image` value with the name of your image

### 2. Opentopodata

Opentopodata needs to be configured with the same elevation data usend in OSRM

1. Download the same file downloaded in step 5 above, from [here](https://srtm.csi.cgiar.org/srtmdata/), this time select ```Geo TIFF```
2. Go to ```oepntopodata/data``` create a new folder ```yourDatasetFolder/``` move the downloaded ```.tif``` file inside this folder
3. Edit the file ```oepntopodata/config.yml``` set the ```name``` end ```path``` to your dataset
4. Edit the ```docker-compose.yml``` in the root directory: line ```78``` with the number of threads you want to use

### 3. Spring Boot

1. Navigate to ```GreenWay/``` and make the necessary changes.
2. Edit the ```pom.xml``` file replace ```redy1908``` with your dockerHub username at line ```121```.
3. Execute ```./mvnw -DskipTests spring-boot:build-image```.
4. Edit the ```docker-compose.yml``` file in the root directory, on line 4, replace the `image`  value with the name of your image (use the value at line 121 in the ```pom.xml```)
5. Optionally, push the image to DockerHub with ```docker push docker.io/{your-dockerHub-username}/green-way-backend:v1```

If you want to make changes to the Spring Boot REST API and run it immediately without creating a custom image,
do the following:

1. Enable the Spring Boot profile ```local```
2. Start all the services from the ```docker-compose``` in the root directory except ```green-way-backend```
3. Run the Spring Boot Application the REST API will be available at http://localhost:8080

---

| Useful commands                                           |
|-----------------------------------------------------------|
| ```./mvnw -DskipTests clean install```                    |
| ```./mvnw -DskipTests spring-boot:build-image```          |
| ```docker push docker.io/redy1908/green-way-backend:v1``` |