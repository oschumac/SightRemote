package zbp.rupbe.sightparser;

/**
 * Created by Tebbe Ubben on 27.06.2017.
 */
public interface DataStorage {

    String get(String key);

    void set(String key, String value);

    boolean contains(String key);

}
