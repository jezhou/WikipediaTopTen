package wikipedia.top.ten.partc;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.Ignition;
import org.apache.ignite.stream.StreamVisitor;
import wikipedia.top.ten.partc.CacheConfig;

import javax.cache.Cache;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by s1675039 on 15/11/16.
 */
public class StreamNode {

    public static void main(String[] args) throws Exception {

        if(args.length != 1) {
            System.err.println("Incorrect # of arguments");
            System.exit(1);
        }
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start(StreamNode.class.getResource("/example-ignite-no-discovery.xml"))) {
            IgniteCache<String, Long> stmCache = ignite.getOrCreateCache(CacheConfig.wordCache());
            IgniteCache<String, Long> topCache = ignite.getOrCreateCache(CacheConfig.topTenCache());

            // Create a streamer to stream words into the cache.
            try (IgniteDataStreamer<String, Long> stmr = ignite.dataStreamer(stmCache.getName())) {
                // Allow data updates.
                stmr.allowOverwrite(true);

                // Configure data transformation to count instances of the same word.
                stmr.receiver(StreamVisitor.from((cache, e) -> {
                    String article = e.getKey();
                    Long views = e.getValue();

                    stmCache.put(article, views);

                    if (topCache.containsKey(article)) {
                        views += topCache.get(article);
                    }

                    topCache.put(article, views);
                }));

                // StreamNode words from "alice-in-wonderland" book.
                Path path = Paths.get(args[0]);
                System.out.println("Reading from " + path);

                // Read words from a text file.
                try (java.util.stream.Stream<String> lines = Files.lines(path)) {
                    lines.forEach(line -> {
                        String[] words = java.util.stream.Stream
                                .of(line.split(" "))
                                .toArray(size -> new String[size]);
                        stmr.addData(words[1], new Long(Long.parseLong(words[2].trim())));
                    });
                }
                System.out.println("Finished streaming, clearing the top cache");
                stmCache.removeAll();

            }
        }
    }
}