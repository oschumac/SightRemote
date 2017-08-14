package zbp.rupbe.sightparser.pipeline.handlers;

import android.util.Log;

import org.spongycastle.util.encoders.Hex;
import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.authlayer.DataMessage;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;
import zbp.rupbe.sightparser.pipeline.handler.InboundHandler;

/**
 * Created by Tebbe Ubben on 01.07.2017.
 */
public class AppLayerParser implements InboundHandler<DataMessage> {

    @Override
    public void receive(Pipeline pipeline, DataMessage data) {
        try {
            Log.d("zbp.rupbe.sightremote", "APP RECEIVE: " + Hex.toHexString(data.getData()));
            ByteBuf dataBuf = new ByteBuf(data.getData().length);
            dataBuf.putBytes(data.getData());
            AppLayerMessage.deserialize(pipeline, dataBuf);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}