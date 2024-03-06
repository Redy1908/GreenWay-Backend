# GreenWay-Backend
Backend of the multiplatform App GreenWay a Routhing Service for EV

## Set up

Install [Docker](https://www.docker.com/)

- Download map data of Sud Italy: https://download.geofabrik.de/europe/italy/sud-latest.osm.pbf
- You will get a file named ```sud-latest.osm.pbf``` 
- Move the file to ```osrm/data```
- Run the following command inside ```osrm/```: ```docker build . -t {dockerHubUsername}/{imageName}:{imageTag}``` 
the process will take several minutes
- Go to the root directory end edit the ```docker-compose.yml``` file modify the ```image``` name at line ```25``` 
with the value you have used in the step above ```{dockerHubUsername}/{imageName}:{imageTag}```



