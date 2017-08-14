package zbp.rupbe.sightparser.crypto;

/**
 * Created by Tebbe Ubben on 30.06.2017.
 */
public class DerivedKeys {

    byte[] incomingKey;
    byte[] outgoingKey;

    public byte[] getIncomingKey() {
        return incomingKey;
    }

    public byte[] getOutgoingKey() {
        return outgoingKey;
    }

    public void setIncomingKey(byte[] incomingKey) {
        this.incomingKey = incomingKey;
    }

    public void setOutgoingKey(byte[] outgoingKey) {
        this.outgoingKey = outgoingKey;
    }
}
