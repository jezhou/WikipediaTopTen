package wikipedia.top.ten.partc;

import org.apache.ignite.Ignition;

public class DataNode {
    public static void main(String[] args) throws Exception {
        Ignition.start(DataNode.class.getResource("/example-ignite-no-discovery.xml"));
    }
}
