package wikipedia.top.ten;

import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;

public class DataNode {
    public static void main(String[] args) throws IgniteException {
        Ignition.start(DataNode.class.getResource("/example-ignite-no-discovery.xml"));
    }
}
