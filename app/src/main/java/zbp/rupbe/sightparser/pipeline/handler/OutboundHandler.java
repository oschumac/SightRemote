package zbp.rupbe.sightparser.pipeline.handler;

import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 27.06.2017.
 */
public interface OutboundHandler<T> extends Handler<T> {

    void send(Pipeline pipeline, T data);

}
