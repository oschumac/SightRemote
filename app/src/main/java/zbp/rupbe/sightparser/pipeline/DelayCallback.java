package zbp.rupbe.sightparser.pipeline;

/**
 * Created by Tebbe Ubben on 30.06.2017.
 */
public interface DelayCallback {

    void runDelayed(int milliSeconds, Runnable runnable);

}
