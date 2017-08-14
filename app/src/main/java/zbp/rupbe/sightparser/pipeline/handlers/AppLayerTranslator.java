package zbp.rupbe.sightparser.pipeline.handlers;

import android.util.Log;

import org.spongycastle.util.encoders.Hex;

import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.authlayer.DataMessage;
import zbp.rupbe.sightparser.pipeline.Pipeline;
import zbp.rupbe.sightparser.pipeline.handler.OutboundHandler;

/**
 * Created by Tebbe Ubben on 01.07.2017.
 */
public class AppLayerTranslator implements OutboundHandler<AppLayerMessage> {
    @Override
    public void send(Pipeline pipeline, AppLayerMessage data) {
        if (data.getService() != Service.CONNECTION && !pipeline.getEnabledServices().contains(data.getService())) {
            ServiceActivator.activateService(pipeline, data);
            return;
        }
        byte[] dataBytes = data.serialize();
        Log.d("zbp.rupbe.sightremote", "APP SEND: " + Hex.toHexString(dataBytes));
        DataMessage message = new DataMessage();
        message.setData(dataBytes);
        pipeline.send(message);
    }
}
