package hundun.idlegame.kancolle.world;

import hundun.idlegame.kancolle.event.EventBus;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public abstract class BaseManager {
    protected EventBus eventBus;
    protected DataBus dataBus;
    
    public BaseManager(EventBus eventBus, DataBus dataBus) {
        this.eventBus = eventBus;
        this.dataBus = dataBus;
        eventBus.register(this);
    }
    
}
