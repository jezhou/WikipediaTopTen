package wordcount;

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
        CacheConfiguration<String, Long> cfg = new CacheConfiguration<>("words");

        // Index the words and their counts,
        // so we can use them for fast SQL querying.
        cfg.setIndexedTypes(String.class, Long.class);

        // Sliding window of 5 seconds.
        cfg.setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                new CreatedExpiryPolicy(new Duration(SECONDS, 5))));

        return cfg;
    }
}
