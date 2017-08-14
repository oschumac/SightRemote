package zbp.rupbe.sightparser.applayer.connection;

import org.spongycastle.util.encoders.Hex;

import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;

/**
 * Created by Tebbe Ubben on 09.07.2017.
 */

public class AppDisconnect extends AppLayerMessage {

    @Override
    public Service getService() {
        return Service.CONNECTION;
    }

    @Override
    public short getCommand() {
        return 0x14F0;
    }

    @Override
    protected byte[] getData() {
        return Hex.decode("0360");
    }

    @Override
    public Class getMessageClass() {
        return getClass();
    }
}
