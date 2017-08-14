package zbp.rupbe.sightparser.pipeline.handlers;

import android.util.Log;

import org.spongycastle.util.encoders.Hex;

import zbp.rupbe.sightparser.authlayer.AuthLayerMessage;
import zbp.rupbe.sightparser.authlayer.CRCAuthLayerMessage;
import zbp.rupbe.sightparser.authlayer.ConnectionRequest;
import zbp.rupbe.sightparser.pipeline.Pipeline;
import zbp.rupbe.sightparser.pipeline.handler.OutboundHandler;

import java.math.BigInteger;

/**
 * Created by Tebbe Ubben on 28.06.2017.
 */
public class AuthLayerTranslator implements OutboundHandler<AuthLayerMessage> {
    @Override
    public void send(Pipeline pipeline, AuthLayerMessage data) {
        pipeline.send(data.serialize(data instanceof CRCAuthLayerMessage ? BigInteger.ZERO : pipeline.getNextNonce(), data instanceof ConnectionRequest ? 0 : pipeline.getCommID(), pipeline.getDerivedKeys() != null ? pipeline.getDerivedKeys().getOutgoingKey() : null));
    }
}
