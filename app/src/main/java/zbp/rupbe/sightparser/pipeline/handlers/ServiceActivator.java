package zbp.rupbe.sightparser.pipeline.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zbp.rupbe.sightparser.applayer.connection.AppActivateService;
import zbp.rupbe.sightparser.applayer.AppLayerMessage;
import zbp.rupbe.sightparser.applayer.connection.AppServiceChallenge;
import zbp.rupbe.sightparser.applayer.descriptors.Service;
import zbp.rupbe.sightparser.crypto.Cryptograph;
import zbp.rupbe.sightparser.pipeline.Pipeline;
import zbp.rupbe.sightparser.pipeline.handler.InboundHandler;

/**
 * Created by Tebbe Ubben on 10.07.2017.
 */

public class ServiceActivator implements InboundHandler<AppLayerMessage> {

    private static Map<Service, List<AppLayerMessage>> awaitingServiceActivation = new HashMap<>();
    private static Service lastRequested;

    @Override
    public void receive(Pipeline pipeline, AppLayerMessage data) {
        if (data instanceof AppServiceChallenge) {
            byte[] password = Cryptograph.getServicePasswordHash(lastRequested.getServicePassword(), ((AppServiceChallenge) data).getRandomData());
            AppActivateService activateService = new AppActivateService();
            activateService.setService(lastRequested.getServiceID());
            activateService.setServicePassword(password);
            activateService.setVersion(lastRequested.getServiceVersion());
            pipeline.send(activateService);
        } else if (data instanceof AppActivateService) {
            pipeline.getEnabledServices().add(lastRequested);
            for (AppLayerMessage appLayerMessage : awaitingServiceActivation.get(lastRequested)) pipeline.send(appLayerMessage);
            awaitingServiceActivation.remove(lastRequested);
            lastRequested = null;
            if (awaitingServiceActivation.size() != 0)
                for (Service service : awaitingServiceActivation.keySet()) {
                    activateService(service, pipeline);
                    break;
                }
        }
    }

    public synchronized static void activateService(Pipeline pipeline, AppLayerMessage appLayerMessage) {
        Service service = appLayerMessage.getService();
        if (lastRequested != null) {
            List<AppLayerMessage> appLayerMessages = awaitingServiceActivation.get(service);
            if (appLayerMessages == null) appLayerMessages = new ArrayList<>();
            appLayerMessages.add(appLayerMessage);
            awaitingServiceActivation.put(service, appLayerMessages);
            return;
        }
        List<AppLayerMessage> appLayerMessages = new ArrayList<>();
        appLayerMessages.add(appLayerMessage);
        awaitingServiceActivation.put(service, appLayerMessages);
        activateService(service, pipeline);
    }

    private static void activateService(Service service, Pipeline pipeline) {
        lastRequested = service;
        if (service.getServicePassword() != null) {
            AppServiceChallenge serviceChallenge = new AppServiceChallenge();
            serviceChallenge.setService(service.getServiceID());
            serviceChallenge.setVersion(service.getServiceVersion());
            pipeline.send(serviceChallenge);
        } else {
            AppActivateService activateService = new AppActivateService();
            activateService.setService(service.getServiceID());
            activateService.setVersion(service.getServiceVersion());
            activateService.setServicePassword(new byte[16]);
            pipeline.send(activateService);
        }
    }
}
