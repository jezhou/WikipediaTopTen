package wordcount;

import org.apache.ignite.*;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.util.List;

/**
 * Created by s1675039 on 15/11/16.
 */
public class QueryWords {
    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("examples/config/example-ignite-no-discovery.xml")) {
            IgniteCache<String, Long> stmCache = ignite.getOrCreateCache(CacheConfig.wordCache());

            // Select top 10 words.
            SqlFieldsQuery top10Qry = new SqlFieldsQuery(
                    "select _key, _val from Long order by _val desc limit 10");

            // Query top 10 popular words every 5 seconds.
            while (true) {
                // Execute queries.
                List<List<?>> top10 = stmCache.query(top10Qry).getAll();

                // Print top 10 words.
                System.out.println(top10);

                Thread.sleep(5000);
            }
        }
    }
}