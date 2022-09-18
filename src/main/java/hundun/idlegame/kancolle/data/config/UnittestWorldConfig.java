package hundun.idlegame.kancolle.data.config;

import java.util.Map;

import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.expedition.Requirement;
import hundun.idlegame.kancolle.expedition.Reward;
import hundun.idlegame.kancolle.ship.ShipPrototype;

/**
 * @author hundun
 * Created on 2021/09/14
 */
public class UnittestWorldConfig extends WorldConfig {
    
    
    public final static String WRONG_EXPEDITION_ID = "WRONG_EXPEDITION_ID";
    public final static String EASY_EXPEDITION_ID = "EASY_EXPEDITION_ID";
    public final static String EASY_EXPEDITION_ID_2 = "EASY_EXPEDITION_ID_2";
    public final static String HIGH_LEVEL_REQUIREMENT_EXPEDITION_ID = "HIGH_LEVEL_REQUIREMENT_EXPEDITION_ID";
    public final static String HIGH_POWER_REQUIREMENT_EXPEDITION_ID = "HIGH_POWER_REQUIREMENT_EXPEDITION_ID";

    public final static String WRONG_SHIP_ID = "WRONG_SHIP_ID";
    public final static String WEAK_SHIP_ID = "WEAK_SHIP_ID";
    public final static String HIGH_POWER_SHIP_ID = "HIGH_POWER_SHIP_ID";
    
    
    public UnittestWorldConfig(WorldConfig worldConfig) {
        super();
        
        this.startShips = worldConfig.getStartShips();
        this.startResources = worldConfig.getStartResources();
        
        this.shipPrototypes = worldConfig.getShipPrototypes();
        this.resourcePrototypes = worldConfig.getResourcePrototypes();
        this.expeditionPrototypes = worldConfig.getExpeditionPrototypes();
    
        moreConfig();
    }
    
    private void moreConfig() {
        startShips.add(WEAK_SHIP_ID);
        startShips.add(HIGH_POWER_SHIP_ID);
        startResources.put(RESOURCE_FUEL_ID, 10000);
        startResources.put(RESOURCE_AMMO_ID, 10000);
        startResources.put(RESOURCE_STEEL_ID, 10000);
        startResources.put(RESOURCE_BAUXITE_ID, 10000);
        moreShips();
        moreExpeditions();
    }
    
    
    protected void moreShips() {
        
        ShipPrototype prototype;
        
        prototype = ShipPrototype.builder()
                .id(WEAK_SHIP_ID)
                .basePower(1)
                .build();
        shipPrototypes.add(prototype);
        
        prototype = ShipPrototype.builder()
                .id(HIGH_POWER_SHIP_ID)
                .basePower(20000)
                .build();
        shipPrototypes.add(prototype);
    }
    
    protected void moreExpeditions() {
        
        ExpeditionPrototype prototype;
        
        prototype = ExpeditionPrototype.builder()
                .id(EASY_EXPEDITION_ID)
                .tick(2)
                .requirement(null)
                .normalReward(new Reward(Map.of(RESOURCE_FUEL_ID, 200), null, 30))
                .build();
        expeditionPrototypes.add(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id(EASY_EXPEDITION_ID_2)
                .tick(2)
                .requirement(null)
                .build();
        expeditionPrototypes.add(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id(HIGH_LEVEL_REQUIREMENT_EXPEDITION_ID)
                .tick(2)
                .requirement(new Requirement(1, 10000))
                .build();
        expeditionPrototypes.add(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id(HIGH_POWER_REQUIREMENT_EXPEDITION_ID)
                .tick(2)
                .requirement(new Requirement(10000, 0))
                .build();
        expeditionPrototypes.add(prototype);
    }
}
