package zbp.rupbe.sightparser.pipeline.handlers;

import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.params.RSAKeyParameters;

import zbp.rupbe.sightparser.ErrorType;
import zbp.rupbe.sightparser.InsightError;
import zbp.rupbe.sightparser.Message;
import zbp.rupbe.sightparser.Status;
import zbp.rupbe.sightparser.applayer.connection.AppBind;
import zbp.rupbe.sightparser.authlayer.ConnectionResponse;
import zbp.rupbe.sightparser.authlayer.KeyRequest;
import zbp.rupbe.sightparser.authlayer.KeyResponse;
import zbp.rupbe.sightparser.authlayer.PairingStatus;
import zbp.rupbe.sightparser.authlayer.VerifyConfirmRequest;
import zbp.rupbe.sightparser.authlayer.VerifyConfirmResponse;
import zbp.rupbe.sightparser.authlayer.VerifyDisplayRequest;
import zbp.rupbe.sightparser.authlayer.VerifyDisplayResponse;
import zbp.rupbe.sightparser.crypto.Cryptograph;
import zbp.rupbe.sightparser.pipeline.Pipeline;
import zbp.rupbe.sightparser.pipeline.handler.InboundHandler;

/**
 * Created by Tebbe Ubben on 29.06.2017.
 */
public class PairingEstablisher implements InboundHandler<Message> {

    private int retry = 0;

    @Override
    public void receive(final Pipeline pipeline, Message data) {
        if (data instanceof ConnectionResponse) {
            KeyRequest keyRequest = new KeyRequest();
            keyRequest.setRandomBytes(pipeline.getRandomBytes());
            keyRequest.setPreMasterKey(keyToBytes(pipeline.getKeyPair().getPublicKey()));
            pipeline.send(Status.KEY_EXCHANGE);
            pipeline.send(keyRequest);
        } else if (data instanceof KeyResponse) {
            try {
                KeyResponse keyResponse = (KeyResponse) data;
                pipeline.setDerivedKeys(Cryptograph.deriveKeys(Cryptograph.decryptRSA(pipeline.getKeyPair().getPrivateKey(), keyResponse.getPreMasterSecret()), pipeline.getRandomBytes(), keyResponse.getRandomData()));
                VerifyDisplayRequest verifyDisplayRequest = new VerifyDisplayRequest();
                pipeline.send(Status.CONFIRMING);
                pipeline.send(verifyDisplayRequest);
            } catch (InvalidCipherTextException e) {
                pipeline.send(new InsightError(ErrorType.KEY_EXCHANGE_ERROR, null));
            }
        } else if (data instanceof VerifyDisplayResponse) {
            VerifyConfirmRequest verifyConfirmRequest = new VerifyConfirmRequest();
            verifyConfirmRequest.setPairingStatus(PairingStatus.CONFIRMED);
            pipeline.send(verifyConfirmRequest);
        } else if (data instanceof VerifyConfirmResponse) {
            VerifyConfirmResponse verifyConfirmResponse = (VerifyConfirmResponse) data;
            if (verifyConfirmResponse.getPairingStatus().equals(PairingStatus.CONFIRMED)) {
                pipeline.send(Status.BIND);
                pipeline.send(new AppBind());
            } else if (verifyConfirmResponse.getPairingStatus().equals(PairingStatus.REJECTED)) {
                pipeline.send(new InsightError(ErrorType.PAIRING_DENIED, null));
            } else if (verifyConfirmResponse.getPairingStatus().equals(PairingStatus.PENDING)) {
                if (retry >= 15) {
                    pipeline.send(new InsightError(ErrorType.PAIRING_TIMEOUT, null));
                } else {
                    pipeline.getPump().getDelayCallback().runDelayed(2000, new Runnable() {
                        @Override
                        public void run() {
                            VerifyConfirmRequest verifyConfirmRequest = new VerifyConfirmRequest();
                            verifyConfirmRequest.setPairingStatus(PairingStatus.CONFIRMED);
                            pipeline.send(verifyConfirmRequest);
                        }
                    });
                }
            }
        } else if (data instanceof AppBind) {
            pipeline.send(Status.PAIRING_ESTABLISHED);
            pipeline.send(Status.CONNECTED);
        }
    }

    private static byte[] keyToBytes(RSAKeyParameters publicKey) {
        byte[] modulus = publicKey.getModulus().toByteArray();
        byte[] bytes = new byte[256];
        System.arraycopy(modulus, 1, bytes, 0, 256);
        return bytes;
    }
}
