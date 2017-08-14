package zbp.rupbe.sightparser.pipeline.handlers;

import android.util.Log;

import org.spongycastle.util.encoders.Hex;

import zbp.rupbe.sightparser.authlayer.AuthLayerMessage;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;
import zbp.rupbe.sightparser.pipeline.handler.InboundHandler;

import java.util.Arrays;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class AuthLayerParser implements InboundHandler<ByteBuf> {
    @Override
    public void receive(Pipeline pipeline, ByteBuf data) {
        Log.d("zbp.rupbe.sightremote", "AUTH RECEIVE: " + Hex.toHexString(data.getBytes()));
        try {
            while (data.size() >= 6 && Arrays.equals(data.getBytes(4), AuthLayerMessage.MAGIC_HEADER)) {
                int length = data.getShortLE(4);
                if (data.size() < length + 8) return;
                AuthLayerMessage.deserialize(data, pipeline);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
