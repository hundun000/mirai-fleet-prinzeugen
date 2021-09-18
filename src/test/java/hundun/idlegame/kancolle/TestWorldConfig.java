package hundun.idlegame.kancolle;

import java.util.List;
import java.util.Map;

import hundun.idlegame.kancolle.expedition.ExpeditionFactory;
import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.expedition.Requirement;
import hundun.idlegame.kancolle.expedition.Reward;
import hundun.idlegame.kancolle.resource.ResourceFactory;
import hundun.idlegame.kancolle.resource.ResourcePrototype;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import hundun.idlegame.kancolle.world.WorldConfig;

/**
 * @author hundun
 * Created on 2021/09/14
 */
public class TestWorldConfig extends WorldConfig {
    
    
    public final static String WRONG_EXPEDITION_ID = "WRONG_EXPEDITION_ID";
    public final static String EASY_EXPEDITION_ID = "EASY_EXPEDITION_ID";
    public final static String EASY_EXPEDITION_ID_2 = "EASY_EXPEDITION_ID_2";
    public final static String HIGH_LEVEL_REQUIREMENT_EXPEDITION_ID = "HIGH_LEVEL_REQUIREMENT_EXPEDITION_ID";
    public final static String HIGH_POWER_REQUIREMENT_EXPEDITION_ID = "HIGH_POWER_REQUIREMENT_EXPEDITION_ID";

    public final static String WRONG_SHIP_ID = "WRONG_SHIP_ID";
    public final static String WEAK_SHIP_ID = "WEAK_SHIP_ID";
    public final static String HIGH_POWER_SHIP_ID = "HIGH_POWER_SHIP_ID";
    
    
    public TestWorldConfig() {
        super();
        
        startShips.add(WEAK_SHIP_ID);
        startShips.add(HIGH_POWER_SHIP_ID);
    }
    
    @Override
    protected void registerShips(ShipFactory shipFactory) {
        super.registerShips(shipFactory);
        
        ShipPrototype prototype;
        
        prototype = ShipPrototype.builder()
                .id(WEAK_SHIP_ID)
                .basePower(1)
                .build();
        shipFactory.register(prototype);
        
        prototype = ShipPrototype.builder()
                .id(HIGH_POWER_SHIP_ID)
                .basePower(20000)
                .build();
        shipFactory.register(prototype);
    }
    
    @Override
    protected void registerExpeditions(ExpeditionFactory expeditionFactory) {
        super.registerExpeditions(expeditionFactory);
        
        ExpeditionPrototype prototype;
        
        prototype = ExpeditionPrototype.builder()
                .id(EASY_EXPEDITION_ID)
                .tick(2)
                .requirement(null)
                .normalReward(new Reward(Map.of(RESOURCE_FUEL_ID, 200), null, 30))
                .build();
        expeditionFactory.register(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id(EASY_EXPEDITION_ID_2)
                .tick(2)
                .requirement(null)
                .build();
        expeditionFactory.register(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id(HIGH_LEVEL_REQUIREMENT_EXPEDITION_ID)
                .tick(2)
                .requirement(new Requirement(1, 10000))
                .build();
        expeditionFactory.register(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id(HIGH_POWER_REQUIREMENT_EXPEDITION_ID)
                .tick(2)
                .requirement(new Requirement(10000, 0))
                .build();
        expeditionFactory.register(prototype);
    }
}
