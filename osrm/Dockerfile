FROM osrm/osrm-backend

COPY ./data /data

ENV OSRM_FILE=italy-latest

RUN if [ ! -f /data/${OSRM_FILE}.osrm]; then \
        osrm-extract -p /opt/car.lua /data/${OSRM_FILE}.osm.pbf && \
        osrm-partition /data/${OSRM_FILE}.osrm && \
        osrm-customize /data/${OSRM_FILE}.osrm; \
    fi

EXPOSE 5000

CMD osrm-routed --algorithm mld /data/${OSRM_FILE}.osrm
