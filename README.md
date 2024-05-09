<a name="readme-top"></a>

<div>
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
     <a href="#architecture">Architecture</a>
      <ul>
        <li><a href="#system">System</a></li>
        <li><a href="#database">Database</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#setup">Setup</a></li>
        <li><a href="#run">Run</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#example">Example</a></li>
    <li><a href="#keycloak">Keycloak</a></li>
    <li>
     <a href="#license">License</a>
    </li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

This is the backend of GreenWay. GreenWay optimizes the routes for a fleet of electric vehicles, taking into account their load capacity, range, courier work shifts, and terrain elevation. By considering terrain elevation, GreenWay generates more efficient routes for electric vehicles, taking advantage of their ability to recharge batteries during downhill travel. GreenWay solves a variant of the [Travelling Salesman Problem](https://en.wikipedia.org/wiki/Travelling_salesman_problem), the [Vehicle Routing Problem](https://en.wikipedia.org/wiki/Vehicle_routing_problem). Deliveries are organized in such a way that a vehicle can deliver its load and return to the depot within the courier’s end-of-shift time, without the need for recharging breaks see the [example](https://github.com/Redy1908/GreenWay-Backend?tab=readme-ov-file#example).

### Built With

[![SpringBoot][SpringBoot]][SpringBoot-url][![Project OSRM][ProjectOSRM]][ProjectOSRM-url][![Open Topo Data][OpenTopoData]][OpenTopoData-url][![Keycloack][Keycloack]][Keycloack-url][![Java][Java]][Java-url][![Docker][Docker]][Docker-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Design -->
## Architecture

### System

<img src="images/System.png" alt="database">

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Database

<img src="images/GreenWayDB.png" alt="database">

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

### Prerequisites

Make sure to install the following dependencies

* [Docker](https://docs.docker.com/desktop/)

### Setup

You need to download the map and the elevation data for the location you want to target.

Download the map data from [Geofabrik](https://www.geofabrik.de/), 
you will get a file named `your-location.osm.pbf` rename the file to `green-way.osm.pbf`
and place the file in `osrm/data-standard/` and `osrm/data-elevation/` (duplicate the file)

Download the elevation data from [srtm.csi.org](https://srtm.csi.cgiar.org/srtmdata/),
download both the `Esri ASCII` and `Geo TIFF` format.
You will get 2 files:
   - For the `your-location.asc` file 
     - Rename the `your-location.asc` file to `green-way.asc` and move it to `osrm/data-elevation/`
     - Open the file `green-way.asc` and note the first two lines, edit the `docker-compose.yml` file in the root directory lines [113][Github-url-17] and [114][Github-url-18]
     - Remove the first `6` lines from the `green-way.asc` file and save it
     - Edit the`docker-compose.yml` file in the root directory lines [109][Github-url-9], [110][Github-url-10], [111][Github-url-11], [112][Github-url-12] with the limit of your elevation data, you can find them in the download page on [srtm.csi.org](https://srtm.csi.cgiar.org/srtmdata/), 
     - The map data end elevation data will probably cover different locations;
       you want to limit the queries to the intersection of this data, 
       to do this edit the `application.yml` file in `GreenWay/src/resources/` lines [27][Github-url-13], [28][Github-url-14], [29][Github-url-15], [30][Github-url-16] 
   - For the `your-location.tif` file
     - move the `your-location.tif` file to `opentopodata/data/`
     - Note that Opentopodata will provide the elevation data for up to `5000` locations then will throw `ERROR 400` you can [change this limit][Github-url-8]

### Run

Within the root folder execute:

```cmd
   docker compose up -d
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE EXAMPLES -->
## Usage

**TODO**

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Example

Let’s consider a fleet of three vehicles, all waiting at the depot (withe dot on the map) and 11 deliveries
(yellow dots on the map):

<img src="images/map.png" alt="map">

Suppose that inside our fleet of three vehicles we have one vehicle capable of doing all the deliveries in one take
the algorithm output will be:

<img src="images/map1.png" alt="map1">

Now let's limit the number of deliveries for the three vehicles to 4:

<img src="images/map3.png" alt="map2">

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Keycloak

> :warning: This set up is only for development/prototyping <u>DO NOT</u> use it in production :warning:

GreenWay uses [KeyCloak](https://www.keycloak.org/) as its Identity and Access Management (IAM) system.
A pre-configured realm and the following users are already configured:

```
Username: admin
Password: admin
KeyCloak default admin
```
```
Username: GREEN_WAY_ADMIN
Password: 12345
with GREEN_WAY_ADMIN ROLE
```
```
Username: deliverymanX where X can be 1-2-3-4-5
Password: 12345
with GREEN_WAY_DELIVERY_MAN ROLE
```

If you want to edit the Keycloak configuration or add new users, access the
KeyCloak [dashboard](http://localhost:8090/) using the default admin profile.

## License

This project is licensed under the Creative Commons Attribution-ShareAlike 4.0 International (CC BY-SA 4.0) License - see the [LICENSE file](https://github.com/Redy1908/GreenWay-Backend/blob/main/LICENSE) for details.

<p align="right">(<a href="#readme-top">back to top</a>)</p>
</div>


[SpringBoot]: https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Spring&logoColor=white
[SpringBoot-url]: https://spring.io/projects/spring-boot
[ProjectOSRM]: https://img.shields.io/badge/Project_OSRM-black?style=for-the-badge&logo=openstreetmap&logoColor=6DB33F
[ProjectOSRM-url]: https://project-osrm.org/
[Docker]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[Docker-url]: https://www.docker.com/
[Java]:https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]:https://www.oracle.com/it/java/technologies/downloads/
[Keycloack]:https://img.shields.io/badge/Keycloak-purple?style=for-the-badge&logo=keycloak&logoColor=FFFFFF
[Keycloack-url]: https://www.keycloak.org/
[OpenTopoData]: https://img.shields.io/badge/Open_Topo_Data-blue?style=for-the-badge
[OpenTopoData-url]: https://www.opentopodata.org/

[Github-url-1]: https://github.com/Redy1908/GreenWay-Backend/blob/a669dbe472d8ff0ce111a6f76280de9bd6a24f0e/osrm/Dockerfile-osrm-elevation#L6
[Github-url-2]: https://github.com/Redy1908/GreenWay-Backend/blob/a669dbe472d8ff0ce111a6f76280de9bd6a24f0e/GreenWay/src/main/resources/application.yml#L27
[Github-url-3]: https://github.com/Redy1908/GreenWay-Backend/blob/a669dbe472d8ff0ce111a6f76280de9bd6a24f0e/GreenWay/src/main/resources/application.yml#L28
[Github-url-4]: https://github.com/Redy1908/GreenWay-Backend/blob/a669dbe472d8ff0ce111a6f76280de9bd6a24f0e/GreenWay/src/main/resources/application.yml#L29
[Github-url-5]: https://github.com/Redy1908/GreenWay-Backend/blob/a669dbe472d8ff0ce111a6f76280de9bd6a24f0e/GreenWay/src/main/resources/application.yml#L30
[Github-url-6]: https://github.com/Redy1908/GreenWay-Backend/blob/a669dbe472d8ff0ce111a6f76280de9bd6a24f0e/docker-compose.yml#L65
[Github-url-7]: https://github.com/Redy1908/GreenWay-Backend/blob/a669dbe472d8ff0ce111a6f76280de9bd6a24f0e/GreenWay/pom.xml#L132C1-L132C26
[Github-url-8]: https://github.com/Redy1908/GreenWay-Backend/blob/a47ae71ee16a85db0d704b584f9cbfb288b84ad7/opentopodata/config.yaml#L2
[Github-url-9]: https://github.com/Redy1908/GreenWay-Backend/blob/9112e40020df8c6a8be55ab98fa40bc4517404df/docker-compose.yml#L109
[Github-url-10]: https://github.com/Redy1908/GreenWay-Backend/blob/9112e40020df8c6a8be55ab98fa40bc4517404df/docker-compose.yml#L110
[Github-url-11]: https://github.com/Redy1908/GreenWay-Backend/blob/9112e40020df8c6a8be55ab98fa40bc4517404df/docker-compose.yml#L111
[Github-url-12]: https://github.com/Redy1908/GreenWay-Backend/blob/9112e40020df8c6a8be55ab98fa40bc4517404df/docker-compose.yml#L112
[Github-url-13]: https://github.com/Redy1908/GreenWay-Backend/blob/9112e40020df8c6a8be55ab98fa40bc4517404df/GreenWay/src/main/resources/application.yml#L27
[Github-url-14]: https://github.com/Redy1908/GreenWay-Backend/blob/9112e40020df8c6a8be55ab98fa40bc4517404df/GreenWay/src/main/resources/application.yml#L28
[Github-url-15]: https://github.com/Redy1908/GreenWay-Backend/blob/9112e40020df8c6a8be55ab98fa40bc4517404df/GreenWay/src/main/resources/application.yml#L29
[Github-url-16]: https://github.com/Redy1908/GreenWay-Backend/blob/9112e40020df8c6a8be55ab98fa40bc4517404df/GreenWay/src/main/resources/application.yml#L30
[Github-url-17]: https://github.com/Redy1908/GreenWay-Backend/blob/9112e40020df8c6a8be55ab98fa40bc4517404df/docker-compose.yml#L113
[Github-url-18]: https://github.com/Redy1908/GreenWay-Backend/blob/9112e40020df8c6a8be55ab98fa40bc4517404df/docker-compose.yml#L114
