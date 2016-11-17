package wikipedia.top.ten.partc;

import org.apache.ignite.cache.eviction.sorted.SortedEvictionPolicy;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.configuration.FactoryBuilder;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by s1675039 on 15/11/16.
 */
public class CacheConfig {
    public static CacheConfiguration<String, Long> wordCache() {
        CacheConfiguration<String, Long> cfg = new CacheConfiguration<>("wikipedia");

        // Index the words and their counts,
        // so we can use them for fast SQL querying.
        cfg.setIndexedTypes(String.class, Long.class);

        // Sliding window of 1 seconds.
        cfg.setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                new CreatedExpiryPolicy(new Duration(SECONDS, 1))));

        return cfg;
    }

    public static CacheConfiguration<String, Long> topTenCache() {
        CacheConfiguration<String, Long> cfg = new CacheConfiguration<>("topTen");

        // Index the words and their counts,
        // so we can use them for fast SQL querying.
        cfg.setIndexedTypes(String.class, Long.class);

        // Evict if too old
        cfg.setEvictionPolicy(new SortedEvictionPolicy<String, Long>((o1, o2) ->
                o1.getValue().compareTo(o2.getValue())));
        return cfg;
    }
}
