package zbp.rupbe.sightparser.pipeline;

import android.util.Log;

import org.spongycastle.util.encoders.Base64;
import org.spongycastle.util.encoders.Hex;
import zbp.rupbe.sightparser.DataStorage;
import zbp.rupbe.sightparser.InsightPump;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.crypto.Cryptograph;
import zbp.rupbe.sightparser.crypto.DerivedKeys;
import zbp.rupbe.sightparser.crypto.KeyPair;
import zbp.rupbe.sightparser.pipeline.handler.Handler;
import zbp.rupbe.sightparser.pipeline.handler.InboundHandler;
import zbp.rupbe.sightparser.pipeline.handler.OutboundHandler;
import zbp.rupbe.sightparser.pipeline.handlers.*;

import java.lang.reflect.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tebbe Ubben on 27.06.2017.
 */
public class Pipeline {

    private ByteBuf byteBuf = new ByteBuf(4096);
    private List<Handler> handlers = new ArrayList<>();
    private InsightPump pump;
    private DataStorage dataStorage;

    private BigInteger lastNonceSent;
    private BigInteger lastNonceReceived = BigInteger.valueOf(0);
    private KeyPair keyPair = null;
    private DerivedKeys derivedKeys;
    private byte[] randomBytes;
    private List<Service> enabledServices = new ArrayList<>();
    private int commID;

    private static final BigInteger connMultiplier = BigInteger.valueOf(65536);

    public Pipeline(InsightPump pump) {
        this.pump = pump;
        dataStorage = pump.getDataStorage();
        if (dataStorage.contains("INCOMINGKEY") && dataStorage.contains("OUTGOINGKEY")) {
            derivedKeys = new DerivedKeys();
            derivedKeys.setIncomingKey(Base64.decode(dataStorage.get("INCOMINGKEY")));
            derivedKeys.setOutgoingKey(Base64.decode(dataStorage.get("OUTGOINGKEY")));
        }
        if (dataStorage.contains("COMMID")) {
            commID = Integer.parseInt(dataStorage.get("COMMID"));
        } else {
            commID = 1;
        }
        if (dataStorage.contains("CONNECTIONS")) {
            int connections = Integer.parseInt(dataStorage.get("CONNECTIONS"));
            lastNonceSent = BigInteger.valueOf(connections).multiply(connMultiplier);
            dataStorage.set("CONNECTIONS", ++connections + "");
        } else {
            lastNonceSent = BigInteger.valueOf(0);
            dataStorage.set("CONNECTIONS", 1 + "");
        }
        setupPipeline();
    }

    private void setupPipeline() {
        handlers.add(new ServiceActivator());
        handlers.add(new PacketAggregator());
        handlers.add(new AuthLayerParser());
        handlers.add(new AuthLayerTranslator());
        handlers.add(new AppLayerParser());
        handlers.add(new AppLayerTranslator());
        handlers.add(new PairingEstablisher());
        handlers.add(new ConnectionEstablisher());
    }

    public synchronized void send(Object object) {
        Log.d("zbp.rupbe.sightremote", "PIPELINE SEND: " + object.getClass().getName());
        process(OutboundHandler.class, "send", object);
    }

    public synchronized void receive(Object object) {
        if (object instanceof byte[]) {
            byteBuf.putBytes((byte[]) object);
            receive(byteBuf);
            return;
        }
        Log.d("zbp.rupbe.sightremote", "PIPELINE RECEIVE: " + object.getClass().getName());
        process(InboundHandler.class, "receive", object);
    }

    private void process(Class<? extends Handler> handlerClass, String methodName, Object object) {
        for (Handler handler : handlers) {
            if (!handlerClass.isAssignableFrom(handler.getClass())) continue;
            ParameterizedType parameterizedType = (ParameterizedType) handler.getClass().getGenericInterfaces()[0];
            Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];
            if (!clazz.isAssignableFrom(object.getClass())) continue;
            try {
                Method method = handler.getClass().getMethod(methodName, Pipeline.class, clazz);
                method.setAccessible(true);
                method.invoke(handler, this, object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    public InsightPump getPump() {
        return pump;
    }

    public int getCommID() {
        return commID;
    }

    public void setCommID(int commID) {
        dataStorage.set("COMMID", "" + commID);
        this.commID = commID;
    }

    public BigInteger getLastNonceReceived() {
        return lastNonceReceived;
    }

    public void setLastNonceReceived(BigInteger lastNonceReceived) {
        this.lastNonceReceived = lastNonceReceived;
    }

    public BigInteger getNextNonce() {
        lastNonceSent = lastNonceSent.add(BigInteger.ONE);
        return lastNonceSent;
    }

    public KeyPair getKeyPair() {
        if (keyPair == null) keyPair = Cryptograph.generateRSAKey();
        return keyPair;
    }

    public byte[] getRandomBytes() {
        if (randomBytes == null) {
            randomBytes = new byte[28];
            new SecureRandom().nextBytes(randomBytes);
        }
        return randomBytes;
    }

    public DerivedKeys getDerivedKeys() {
        return derivedKeys;
    }

    public void setDerivedKeys(DerivedKeys derivedKeys) {
        dataStorage.set("INCOMINGKEY", new String(Base64.encode(derivedKeys.getIncomingKey())));
        dataStorage.set("OUTGOINGKEY", new String(Base64.encode(derivedKeys.getOutgoingKey())));
        this.derivedKeys = derivedKeys;
    }

    public List<Handler> getHandlers() {
        return handlers;
    }

    public List<Service> getEnabledServices() {
        return enabledServices;
    }
}
