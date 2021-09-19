package hundun.idlegame.kancolle.building;

import java.util.List;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.base.BaseModel;
import hundun.idlegame.kancolle.base.BaseProtoype;
import hundun.idlegame.kancolle.building.instance.BuidingId;
import hundun.idlegame.kancolle.data.SessionData;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.world.ComponentContext;
import hundun.idlegame.kancolle.world.DataBus;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public abstract class BuildingModel {
    
    public static final String NONE_ID = "未入驻";

    @Getter
    @Setter
    protected int level;
    
    @Getter
    @Setter
    protected BuidingId buildingId;
    
    
    
    protected EventBus eventBus;
    protected DataBus dataBus;
    protected ComponentContext context;

    public BuildingModel(BuidingId id, ComponentContext context) {
        this.buildingId = id;
        this.context = context;
        this.eventBus = context.getEventBus();
        this.dataBus = context.getDataBus();
        eventBus.register(this);
    }
    
    protected List<ShipModel> getWorkers(SessionData sessionData) {
        return sessionData.getShips().stream().filter(ship -> this.getId().equals(ship.getWorkInBuildingId())).collect(Collectors.toList());
    }
    
    public String getId() {
        return buildingId.getIdText();
    }
    
}
