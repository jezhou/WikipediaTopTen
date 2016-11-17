package wikipedia.top.ten.partc;

import org.apache.ignite.cache.eviction.EvictableEntry;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by s1675039 on 17/11/16.
 */
class ViewComparator implements Comparator, Serializable {
    @Override
    public int compare(Object o1, Object o2) {
        EvictableEntry entry1 = (EvictableEntry)o1;
        EvictableEntry entry2 = (EvictableEntry)o2;

        if(entry1 == null && entry2 == null) return 0;
        if(entry1 == null || entry1.getValue() == null ) return -1;
        if(entry2 == null || entry2.getValue() == null ) return 1;
        if(entry1.getValue() == null && entry2.getValue() ==  null) return 0;

        return ((Long) entry1.getValue()).compareTo((Long) entry2.getValue());
    }
}
