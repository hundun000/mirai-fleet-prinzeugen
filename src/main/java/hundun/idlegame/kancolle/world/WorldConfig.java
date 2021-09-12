package hundun.idlegame.kancolle.world;

import java.util.Map;

import hundun.idlegame.kancolle.expedition.ExpeditionFactory;
import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.expedition.Requirement;
import hundun.idlegame.kancolle.expedition.Reward;
import hundun.idlegame.kancolle.resource.ResourceFactory;
import hundun.idlegame.kancolle.resource.ResourcePrototype;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipPrototype;

/**
 * @author hundun
 * Created on 2021/09/11
 */
public class WorldConfig {
    
    
    
    
    public WorldConfig() {
        registerResources();
        registerShips();
        registerExpeditions();
    }
    
    private void registerResources() {
        ResourcePrototype prototype;
        
        prototype = ResourcePrototype.builder().id("FUEL").name("油").build();
        ResourceFactory.INSTANCE.register(prototype);
        
        prototype = ResourcePrototype.builder().id("AMMO").name("弹").build();
        ResourceFactory.INSTANCE.register(prototype);
        
        prototype = ResourcePrototype.builder().id("STEEL").name("钢").build();
        ResourceFactory.INSTANCE.register(prototype);
        
        prototype = ResourcePrototype.builder().id("BAUXITE").name("铝").build();
        ResourceFactory.INSTANCE.register(prototype);
    }

    private void registerShips() {
        ShipPrototype prototype;
        
        prototype = ShipPrototype.builder().id("吹雪").build();
        ShipFactory.INSTANCE.register(prototype);
        
        prototype = ShipPrototype.builder().id("睦月").build();
        ShipFactory.INSTANCE.register(prototype);
        
        prototype = ShipPrototype.builder().id("如月").build();
        ShipFactory.INSTANCE.register(prototype);
        
        prototype = ShipPrototype.builder().id("欧根").build();
        ShipFactory.INSTANCE.register(prototype);
    }
    
    private void registerExpeditions() {
        ExpeditionPrototype prototype;
        
        prototype = ExpeditionPrototype.builder()
                .id("A0")
                .tick(4)
                .requirement(new Requirement(1))
                .normalReward(new Reward(null, null, 30))
                .build();
        ExpeditionFactory.INSTANCE.register(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id("A1")
                .tick(4)
                .requirement(new Requirement(3))
                .normalReward(new Reward(Map.of("FUEL", 200), null, 10))
                .firstTimeReward(new Reward(null, "睦月", 0))
                .build();
        ExpeditionFactory.INSTANCE.register(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id("A2")
                .tick(4)
                .requirement(new Requirement(5))
                .normalReward(new Reward(Map.of("AMMO", 200), null, 10))
                .firstTimeReward(new Reward(null, "如月", 0))
                .build();
        ExpeditionFactory.INSTANCE.register(prototype);
    }
}
