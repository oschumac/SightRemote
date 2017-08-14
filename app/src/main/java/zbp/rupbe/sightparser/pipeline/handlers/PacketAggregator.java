package zbp.rupbe.sightparser.pipeline.handlers;

import android.util.Log;

import org.spongycastle.util.encoders.Hex;

import zbp.rupbe.sightparser.ErrorType;
import zbp.rupbe.sightparser.InsightError;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;
import zbp.rupbe.sightparser.pipeline.handler.OutboundHandler;

import java.io.IOException;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class PacketAggregator implements OutboundHandler<ByteBuf> {

    @Override
    public void send(Pipeline pipeline, ByteBuf data) {
        Log.d("zbp.rupbe.sightremote", "AUTH SEND: " + Hex.toHexString(data.getBytes()));
        while (data.size() > 0) {
            try {
                pipeline.getPump().getOutputStream().write(data.readBytes(data.size() >= 110 ? 110 : data.size()));
                pipeline.getPump().getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
                pipeline.send(new InsightError(ErrorType.BROKEN_PIPE, null));
            }
        }
    }
}
