package zbp.rupbe.sightparser.applayer.configwriter;

import org.spongycastle.util.encoders.Hex;

import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

public class AppCloseWriteSession extends AppLayerMessage {
    @Override
    public Class getMessageClass() {
        return getClass();
    }

    @Override
    public Service getService() {
        return Service.CONFIGWRITER;
    }

    @Override
    public short getCommand() {
        return (short) 0xB51E;
    }

    @Override
    protected byte[] getData() {
        return Hex.decode("50C3");
    }
}
