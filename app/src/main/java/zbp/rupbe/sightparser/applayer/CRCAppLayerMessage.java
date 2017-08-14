package zbp.rupbe.sightparser.applayer;

import zbp.rupbe.sightparser.ErrorType;
import zbp.rupbe.sightparser.InsightError;
import zbp.rupbe.sightparser.crypto.CRC;
import zbp.rupbe.sightparser.pipeline.ByteBuf;
import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public abstract class CRCAppLayerMessage extends AppLayerMessage {

    @Override
    protected void parse(Pipeline pipeline, ByteBuf data) {
        if (inCRC()) {
            byte[] dataArray = data.readBytes(data.size() - 2);
            short crc = data.readShortLE();
            short calcCRC = (short) CRC.calculateCRC(dataArray);
            if (crc != calcCRC) {
                pipeline.send(new InsightError(ErrorType.APP_CRC_ERROR, crc + "," + calcCRC));
                return;
            }
            ByteBuf byteBuf = new ByteBuf(dataArray.length);
            byteBuf.putBytes(dataArray);
            parseData(pipeline, byteBuf);
        } else parseData(pipeline, data);
    }

    @Override
    protected byte[] getData() {
        byte[] content = getContent();
        if (outCRC()) {
            ByteBuf byteBuf = new ByteBuf(content.length + 2);
            byteBuf.putBytes(content);
            byteBuf.putShortLE((short) CRC.calculateCRC(content));
            return byteBuf.getBytes();
        } else return content;
    }

    protected boolean inCRC() {
        return false;
    }

    protected boolean outCRC() {
        return false;
    }

    protected void parseData(Pipeline pipeline, ByteBuf data) {

    }

    protected byte[] getContent() {
        return new byte[0];
    }
}
