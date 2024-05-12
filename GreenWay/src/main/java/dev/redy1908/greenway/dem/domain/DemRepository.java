package dev.redy1908.greenway.dem.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DemRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Double> findValuesForLineString(String lineString) {
        Query query = em.createNativeQuery("SELECT ST_Value(rast, (dp).geom) As val " +
                        "FROM ( " +
                        "  SELECT (ST_DumpPoints(ST_GeomFromText(:lineString, 4326))) as dp " +
                        ") as points, dem " +
                        "WHERE ST_Intersects(rast, (dp).geom)" +
                        "ORDER BY (dp).path[1];")
                .setParameter("lineString", lineString);

        return query.getResultList();
    }
}
