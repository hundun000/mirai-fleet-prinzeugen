package hundun.idlegame.kancolle.building;

import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.world.DataBus;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public abstract class BaseBuilding {
    
    public static final String NONE_ID = "未入驻";
    
    @Getter
    protected String id;
    protected EventBus eventBus;
    protected DataBus dataBus;
    
    public BaseBuilding(String id, EventBus eventBus, DataBus dataBus) {
        this.id = id;
        this.eventBus = eventBus;
        this.dataBus = dataBus;
        eventBus.register(this);
    }
    
}
