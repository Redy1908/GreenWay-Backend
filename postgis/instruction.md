In un container con postgis eseguire in ordine:
- docker cp /DEM <container-id>:/DEM
- apt-get update && apt-get install -y postgis
- psql -U postgis -d postgis -c "CREATE EXTENSION postgis_raster CASCADE;"
- raster2pgsql -s 4326 -I -C -M -F -t 100x100 /DEM/sud-italy.tif dem | psql -U <db-user> -d <db-name>
- Aprire un terminale ed eseguire il comando: docker exec -it <postgis-container-id> bash -c "pg_dump -U postgis -d postgis > db_dump.sql" && docker cp <postgis-container-id>:db_dump.sql .
- Aggiungere IF NOT EXISTS
- Muovere il file in postgis/DB
- Build postgist dokcerfile