package zbp.rupbe.sightparser.pipeline.handlers;

import zbp.rupbe.sightparser.Message;
import zbp.rupbe.sightparser.Status;
import zbp.rupbe.sightparser.applayer.connection.AppConnect;
import zbp.rupbe.sightparser.authlayer.SynAckResponse;
import zbp.rupbe.sightparser.pipeline.Pipeline;
import zbp.rupbe.sightparser.pipeline.handler.InboundHandler;

/**
 * Created by Tebbe Ubben on 30.06.2017.
 */
public class ConnectionEstablisher implements InboundHandler<Message> {

    @Override
    public void receive(Pipeline pipeline, Message data) {
        if (data instanceof SynAckResponse) {
            pipeline.send(new AppConnect());
        } else if (data instanceof AppConnect) {
            pipeline.send(Status.CONNECTED);
        }
    }
}
