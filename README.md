# GreenWay-Backend
Backend of the multiplatform App GreenWay a Routhing Service for EV

## OSMR Set up

Install [Docker](https://www.docker.com/)

Download the necessary map data from [Geofabrik](https://www.geofabrik.de/)

Example set for Sud Italy Map:
- Download needed file: https://download.geofabrik.de/europe/italy/sud-latest.osm.pbf

- You will get a file named ```sud-latest.osm.pbf``` 
- Move the file to ```osrm/data```
- Run the following command inside ```osrm/```: ```docker build . -t {dockerHubUsername}/{imageName}:{imageTag}``` the process will take several minutes
- Edit the ```docker-compose.yml``` file, replace the ```image``` variable value with ```{dockerHubUsername}/{imageName}:{imageTag}```
- Run ```docker compose up``` inside ```osrm/```

> OSMR is now redy to be used at http://0.0.0.0:5000

- Given the following starting point ```START```: 14.2681,40.8518
- Given the following arriving point ```FINISH```: 12.4964,41.9028
- We can also add intermediary points
- (Note )

http://0.0.0.0:5000/trip/v1/driving/{START};{FINISH} will generate a trip from start to finish
specifically the response (JSON) will have a ```GEOMETRY``` field like:

```wzixFqvavA{{AqoIq~b@h@cfFb~Eg|HvjOcoIjwH__DxeIo~RvcB}aOpkTynFveR{tDzhXo{Lhw_@uHvnNubBlgP_mErvLumClyA_tBjeIoyItqPgnJxoVmJnyLecEfsXipD~|HanVrnLncAtkNk_@|qGxdErlSucAplBju@s_C_vDuyRj_@{oGos@qtMt|UqeMtqD}|HdcEcsXdKi|LfnJ{oV~uIojPbwBskIdmCqxAllEktLjcBghP~IitNn{L}v_@jsDidXfqFqjRt_OogTx}RscBl|CcbI`nImvHt`IkpOzeFs}EhhQge@voKr}@plCvnGlsBde@```

http://0.0.0.0:5000/route/v1/driving/polyline(GEOMETRY)?overview=false will return all the points of the trip
