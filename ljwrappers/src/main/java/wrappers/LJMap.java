package wrappers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LJMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = 1L;

    public LJMap() {
        super();
    }

    public LJMap(Map<K, V> map) {
        super(map);
    }

    public boolean contains(K key) {
        return containsKey(key);
    }

    public LJSet<K> keys() {
        return new LJSet<>(keySet());
    }
}
