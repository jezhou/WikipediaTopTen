package wikipedia.top.ten.partc;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;

import java.io.PrintWriter;
import java.util.List;

/**
 * Created by s1675039 on 15/11/16.
 */
public class QueryNode {
    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start(QueryNode.class.getResource("/example-ignite-no-discovery.xml"))) {
            IgniteCache<String, Long> topCache = ignite.getOrCreateCache(CacheConfig.topTenCache());
            IgniteCache<String, Long> strCache = ignite.getOrCreateCache(CacheConfig.wordCache());

            // Select top 10 words.
            SqlFieldsQuery top10Qry = new SqlFieldsQuery(
                    "select _key, _val from Long order by _val desc limit 10");

            // QueryNode top 10 popular words every 5 seconds.
            List<List<?>> top10 = topCache.query(top10Qry).getAll();
            List<List<?>> current10 = strCache.query(top10Qry).getAll();

            System.out.println("Current value: " + current10);

            while(current10.isEmpty()) {
                current10 = strCache.query(top10Qry).getAll();
            }

            // Since we're starting the QueryNode node first, wait
            // until the stream nodes start
            System.out.println("Waiting for top cache to fill...");
            try (PrintWriter writer = new PrintWriter("log-partC.txt")) {
                while(current10.isEmpty()){
                    current10 = strCache.query(top10Qry).getAll();
                }
                while (!current10.isEmpty()) {
                    long currentTime = System.currentTimeMillis();
                    top10.forEach(item -> {
                        String output = currentTime + ":" + item.get(0) + ":" + item.get(1);
                        System.out.println(output);
                    });
                    top10 = topCache.query(top10Qry).getAll();
                    current10 = strCache.query(top10Qry).getAll();
                }
                System.out.println("Done querying! Writing to local file.");
                top10.forEach(item -> {
                    String output = System.currentTimeMillis() + ":" + item.get(0) + ":" + item.get(1);
                    writer.println(output);
                });
                writer.close();
            }
        }
    }
}