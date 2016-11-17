package wordcount;

import org.apache.ignite.*;
import org.apache.ignite.stream.*;

import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by s1675039 on 15/11/16.
 */
public class StreamWords {

    public static class StreamingExampleCacheEntryProcessor extends StreamTransformer<String, Long> {
        @Override
        public Object process(MutableEntry<String, Long> e, Object... arg) throws EntryProcessorException {
            System.out.println(e.getValue());
            Long val = e.getValue();
            e.setValue(val == null ? 1L : val + 1);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        // Mark this cluster member as client.
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start("examples/config/example-ignite-no-discovery.xml")) {
            IgniteCache<String, Long> stmCache = ignite.getOrCreateCache(CacheConfig.wordCache());

            // Create a streamer to stream words into the cache.
            try (IgniteDataStreamer<String, Long> stmr = ignite.dataStreamer(stmCache.getName())) {
                // Allow data updates.
                stmr.allowOverwrite(true);

                // Configure data transformation to count instances of the same word.
                stmr.receiver(new StreamingExampleCacheEntryProcessor());

                // StreamNode words from "alice-in-wonderland" book.
                while (true) {
                    Path path = Paths.get("/afs/inf.ed.ac.uk/user/s16/s1675039/apache-ignite-fabric-1.7.0-bin/examples/src/main/java/org/apache/ignite/examples/streaming/wordcount/alice-in-wonderland.txt");
                    System.out.println(path);

                    // Read words from a text file.
                    try (Stream<String> lines = Files.lines(path)) {
                        lines.forEach(line -> {
                            Stream<String> words = Stream.of(line.split(" "));

                            // StreamNode words into Ignite streamer.
                            words.forEach(word -> {
                                if (!word.trim().isEmpty())
                                    stmr.addData(word, 1L);
                            });
                        });
                    }
                }
            }
        }
    }
}