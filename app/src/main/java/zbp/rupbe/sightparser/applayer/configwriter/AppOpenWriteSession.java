package zbp.rupbe.sightparser.applayer.configwriter;

import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.descriptors.Service;

/**
 * Created by Tebbe Ubben on 11.07.2017.
 */

public class AppOpenWriteSession extends AppLayerMessage {
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
        return 0x491E;
    }

    @Override
    protected boolean isError() {
        return error != 0x0000 && error != 0xBB11;
    }
}
