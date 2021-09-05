package hundun.idlegame.kancolle.world;

import hundun.idlegame.kancolle.container.GameSaveData;
import hundun.idlegame.kancolle.event.EventBus;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public abstract class BaseManager {
    protected EventBus eventBus;
    
    public BaseManager(EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }
    
}
