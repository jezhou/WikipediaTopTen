package wikipedia.top.ten.parta;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.util.List;

/**
 * Created by s1675039 on 15/11/16.
 */
public class QueryPartA {
    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("examples/config/example-ignite-no-discovery.xml")) {
            IgniteCache<String, Long> stmCache = ignite.getOrCreateCache(CacheConfig.wordCache());

            // Select top 10 words.
            SqlFieldsQuery top10Qry = new SqlFieldsQuery(
                    "select _key, _val from Long order by _val desc limit 10");

            // QueryPartA top 10 popular words every 5 seconds.
            List<List<?>> top10 = stmCache.query(top10Qry).getAll();

            // Since we're starting the QueryPartA node first, wait
            // until the stream nodes start
            while(true) {
                long currentTime = System.currentTimeMillis();
                top10.forEach(item -> {
                    System.out.println(currentTime + ":" + item.get(0) + ":" + item.get(1));
                });
                top10 = stmCache.query(top10Qry).getAll();
                Thread.sleep(10000);
            }
        }
    }
}