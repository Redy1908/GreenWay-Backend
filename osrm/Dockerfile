FROM osrm/osrm-backend

COPY ./data /data
COPY ./profiles/greenWay.lua /opt

ENV OSRM_FILE=sud-latest
ENV OSRM_RASTER_SOURCE="../data/srtm_39_04.asc"
ENV LON_MIN=10
ENV LON_MAX=15
ENV LAT_MIN=40
ENV LAT_MAX=45
ENV NROWS=6000
ENV NCOLS=6000

RUN osrm-extract -p /opt/greenWay.lua /data/${OSRM_FILE}.osm.pbf && \
        osrm-partition /data/${OSRM_FILE}.osrm && \
        osrm-customize /data/${OSRM_FILE}.osrm;

EXPOSE 5000

CMD osrm-routed --algorithm mld /data/${OSRM_FILE}.osrm
