package gitlet;

import java.util.HashMap;

/**
 * Duplicate Hashmap. A specific hashmap that does not raise compiler warnings.
 *
 * @author Ryan Johnson
 */
public class HashMapMod extends HashMap {
    /**
     * HashmapMod constructor.
     *
     * @author Ryan Johnson
     */
    public HashMapMod() {
        _map = new HashMap<String, String>();
    }

    /**
     * Put method, KEY, VAL.
     */
    public void put(String key, String val) {
        _map.put(key, val);
    }

    /**
     * Replace method, KEY, VAL.
     */
    public void replace(String key, String val) {
        _map.replace(key, val);
    }

    /**
     * Remove method, KEY.
     */
    public void remove(String key) {
        _map.remove(key);
    }

    /**
     * Contains method, KEY, returns boolean.
     */
    public boolean containsKey(String key) {
        if (_map.containsKey(key)) {
            return true;
        }
        return false;
    }

    /**
     * Get method, KEY, returns String.
     */
    public String get(String key) {
        return _map.get(key);
    }

    /**
     * Hashmap _map.
     */
    private HashMap<String, String> _map;
}
