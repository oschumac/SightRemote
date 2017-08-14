package zbp.rupbe.sightparser;

import zbp.rupbe.sightparser.authlayer.ConnectionRequest;
import zbp.rupbe.sightparser.authlayer.SynRequest;
import zbp.rupbe.sightparser.pipeline.DelayCallback;
import zbp.rupbe.sightparser.pipeline.Pipeline;
import zbp.rupbe.sightparser.pipeline.handler.Handler;

import java.io.OutputStream;

/**
 * Created by Tebbe Ubben on 27.06.2017.
 */
public class InsightPump {

    private OutputStream outputStream;
    private DataStorage dataStorage;
    private Pipeline pipeline;
    private DelayCallback delayCallback;

    public InsightPump(DataStorage dataStorage, DelayCallback delayCallback) {
        this.dataStorage = dataStorage;
        this.delayCallback = delayCallback;
        this.pipeline = new Pipeline(this);
    }

    public void parseBytes(byte[] bytes) {
        pipeline.receive(bytes);
    }

    public void establishPairing(OutputStream outputStream) {
        this.outputStream = outputStream;
        pipeline.send(new ConnectionRequest());
    }

    public void connect(OutputStream outputStream) {
        this.outputStream = outputStream;
        pipeline.send(new SynRequest());
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public void addHandlerFirst(Handler handler) {
        pipeline.getHandlers().add(0, handler);
    }

    public void addHandlerLast(Handler handler) {
        pipeline.getHandlers().add(handler);
    }

    public void removeHandler(Handler handler) {
        pipeline.getHandlers().remove(handler);
    }

    public DelayCallback getDelayCallback() {
        return delayCallback;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }
}
