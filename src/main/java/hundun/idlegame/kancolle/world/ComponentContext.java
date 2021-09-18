package hundun.idlegame.kancolle.world;

import java.util.HashMap;
import java.util.Map;

import hundun.idlegame.kancolle.building.BaseBuilding;
import hundun.idlegame.kancolle.building.ExpeditionBuilding;
import hundun.idlegame.kancolle.container.IGameContainer;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.exception.BuildingNotFoundException;
import hundun.idlegame.kancolle.expedition.ExpeditionFactory;
import hundun.idlegame.kancolle.expedition.ExpeditionManager;
import hundun.idlegame.kancolle.format.DescriptionFormatter;
import hundun.idlegame.kancolle.resource.ResourceFactory;
import hundun.idlegame.kancolle.resource.ResourceManager;
import hundun.idlegame.kancolle.ship.GachaMachine;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipManager;
import hundun.idlegame.kancolle.time.TimerManager;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/09/18
 */
public class ComponentContext {
    @Getter
    EventBus eventBus;
    @Getter
    DataBus dataBus;
    @Getter
    private ExpeditionManager expeditionManager;
    @Getter
    private TimerManager timerManager;
    @Getter
    private ResourceManager resourceManager;
    @Getter
    private ShipManager shipManager;

    @Getter
    private ShipFactory shipFactory;
    
    @Getter
    private ExpeditionFactory expeditionFactory;
    
    @Getter
    private ResourceFactory resourceFactory;
    
    @Getter
    private DescriptionFormatter descriptionFormatter;
    
    @Getter
    private ExpeditionBuilding expeditionBuilding;
    
    @Getter
    private GachaMachine gachaMachine;
    
    private Map<String, BaseBuilding> buildings = new HashMap<>();
    
    public ComponentContext(WorldConfig worldConfig, IGameContainer container) {
        initBeforeDataBus(worldConfig);
        
        

        this.dataBus = new DataBus(this);
        this.eventBus = new EventBus(container, this);
        
        initAfterDataBus();

        
        
    }
    

    /**
     * 需要本过程完成，才能构造this.dataBus
     */
    private void initBeforeDataBus(WorldConfig worldConfig) {
        this.shipFactory = new ShipFactory();
        worldConfig.registerShips(shipFactory);
        this.expeditionFactory = new ExpeditionFactory();
        worldConfig.registerExpeditions(expeditionFactory);
        this.resourceFactory = new ResourceFactory();
        worldConfig.registerResources(resourceFactory);
        
        this.descriptionFormatter = new DescriptionFormatter(resourceFactory);
        this.gachaMachine = new GachaMachine(this);
    }
    
    /**
     * 只能在dataBus构造后调用，因为本过程需要调用this.getDataBus()，
     */
    private void initAfterDataBus() {
        this.expeditionBuilding = new ExpeditionBuilding(this);
        buildings.put(expeditionBuilding.getId(), expeditionBuilding);
        
        this.expeditionManager = new ExpeditionManager(this);
        this.timerManager = new TimerManager(this);
        this.resourceManager = new ResourceManager(this);
        this.shipManager = new ShipManager(this);
    }
    
    public BaseBuilding getBuilding(String id) throws BuildingNotFoundException {
        if (!buildings.containsKey(id)) {
            throw new BuildingNotFoundException(id);
        }
        return buildings.get(id);
    }
}
