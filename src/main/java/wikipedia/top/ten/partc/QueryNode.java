package wikipedia.top.ten.partc;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import wikipedia.top.ten.partc.CacheConfig;

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

            // Select top 10 words.
            SqlFieldsQuery top10Qry = new SqlFieldsQuery(
                    "select _key, _val from Long order by _val desc limit 10");

            // QueryNode top 10 popular words every 5 seconds.
            List<List<?>> top10 = topCache.query(top10Qry).getAll();

            // Since we're starting the QueryNode node first, wait
            // until the stream nodes start
            System.out.println("Waiting for top cache to fill...");
            try (PrintWriter writer = new PrintWriter("log-partC.txt")) {
                while (top10.isEmpty()) {
                    top10 = topCache.query(top10Qry).getAll();
                }
                while (!top10.isEmpty()) {
                    long currentTime = System.currentTimeMillis();
                    top10.forEach(item -> {
                        String output = currentTime + ":" + item.get(0) + ":" + item.get(1);
                        System.out.println(output);
                        writer.println(output);
                    });
                    top10 = topCache.query(top10Qry).getAll();
                }
                System.out.println("Done querying!");
                writer.close();
            }
        }
    }
}