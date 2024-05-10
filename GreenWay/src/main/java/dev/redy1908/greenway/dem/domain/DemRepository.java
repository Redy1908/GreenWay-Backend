package dev.redy1908.greenway.dem.domain;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DemRepository {

    @PersistenceContext
    private EntityManager em;

    public Map<String, Object> findValuesForLineString(String lineString) {
        Query query = em.createNativeQuery("SELECT (dp).path[1] As pt_num, ST_Value(rast, (dp).geom) As val " +
                        "FROM ( " +
                        "  SELECT (ST_DumpPoints(ST_GeomFromText(:lineString, 4326))) as dp " +
                        ") as points, dem " +
                        "WHERE ST_Intersects(rast, (dp).geom)" +
                        "ORDER BY (dp).path[1];", Tuple.class)
                .setParameter("lineString", lineString);

        List<Tuple> results = query.getResultList();

        Map<String, Object> resultMap = new LinkedHashMap<>();
        for (Tuple tuple : results) {
            resultMap.put(tuple.get("pt_num").toString(), tuple.get("val"));
        }

        return resultMap;

    }
}
