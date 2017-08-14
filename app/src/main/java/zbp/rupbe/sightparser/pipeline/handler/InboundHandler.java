package zbp.rupbe.sightparser.pipeline.handler;

import zbp.rupbe.sightparser.pipeline.Pipeline;

/**
 * Created by Tebbe Ubben on 27.06.2017.
 */
public interface InboundHandler<T> extends Handler<T> {

    void receive(Pipeline pipeline, T data);

}
