package hundun.idlegame.kancolle.base;

import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.world.ComponentContext;
import hundun.idlegame.kancolle.world.DataBus;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public abstract class BaseManager {
    protected EventBus eventBus;
    protected DataBus dataBus;
    protected ComponentContext context;
    public BaseManager(ComponentContext context) {
        this.context = context;
        this.eventBus = context.getEventBus();
        this.dataBus = context.getDataBus();
        eventBus.register(this);
    }

    
}
