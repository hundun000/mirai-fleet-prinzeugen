package hundun.idlegame.kancolle.base;

import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.world.DataBus;

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
