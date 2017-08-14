package zbp.rupbe.sightparser.crypto;

import org.spongycastle.crypto.params.RSAKeyParameters;
import org.spongycastle.crypto.params.RSAPrivateCrtKeyParameters;

/**
 * Created by Tebbe Ubben on 30.06.2017.
 */
public class KeyPair {

    RSAPrivateCrtKeyParameters privateKey;
    RSAKeyParameters publicKey;

    public RSAPrivateCrtKeyParameters getPrivateKey() {
        return privateKey;
    }

    public RSAKeyParameters getPublicKey() {
        return publicKey;
    }
}
