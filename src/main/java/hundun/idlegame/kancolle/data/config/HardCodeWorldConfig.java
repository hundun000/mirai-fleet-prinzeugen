package hundun.idlegame.kancolle.data.config;

import java.util.Map;

import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.expedition.Requirement;
import hundun.idlegame.kancolle.expedition.Reward;
import hundun.idlegame.kancolle.resource.ResourcePrototype;
import hundun.idlegame.kancolle.ship.ShipPrototype;

/**
 * @author hundun
 * Created on 2021/09/11
 */
@Deprecated
public class HardCodeWorldConfig extends WorldConfig {
    
    
    
    
    
    
    
    public HardCodeWorldConfig() {
        registerShips();
        registerResources();
        registerExpeditions();
        
        startShips.add("吹雪");
        startShips.add("欧根");
        startResources.put(RESOURCE_FUEL_ID, 1000);
        startResources.put(RESOURCE_AMMO_ID, 1000);
        startResources.put(RESOURCE_STEEL_ID, 1000);
        startResources.put(RESOURCE_BAUXITE_ID, 300);
    }
    

    
    protected void registerResources() {
        ResourcePrototype prototype;
        
        prototype = ResourcePrototype.builder().id(RESOURCE_FUEL_ID).name("油").build();
        resourcePrototypes.add(prototype);
        
        prototype = ResourcePrototype.builder().id(RESOURCE_AMMO_ID).name("弹").build();
        resourcePrototypes.add(prototype);
        
        prototype = ResourcePrototype.builder().id(RESOURCE_STEEL_ID).name("钢").build();
        resourcePrototypes.add(prototype);
        
        prototype = ResourcePrototype.builder().id(RESOURCE_BAUXITE_ID).name("铝").build();
        resourcePrototypes.add(prototype);
        
        prototype = ResourcePrototype.builder().id(RESOURCE_MODERNIZATION_POINT_ID).name("近代化改修点数").build();
        resourcePrototypes.add(prototype);
    }

    protected void registerShips() {
        ShipPrototype prototype;
        
        prototype = ShipPrototype.builder()
                .id("吹雪")
                .basePower(10)
                .gachaRarity(1)
                .standardGachaResources(new int[]{30, 30, 30, 30})
                .build();
        shipPrototypes.add(prototype);
        
        prototype = ShipPrototype.builder()
                .id("睦月")
                .basePower(10)
                .gachaRarity(1)
                .standardGachaResources(new int[]{30, 30, 30, 30})
                .build();
        shipPrototypes.add(prototype);
        
        prototype = ShipPrototype.builder()
                .id("如月")
                .basePower(10)
                .gachaRarity(1)
                .standardGachaResources(new int[]{30, 30, 30, 30})
                .build();
        shipPrototypes.add(prototype);
        
        prototype = ShipPrototype.builder()
                .id("欧根")
                .basePower(30)
                .gachaRarity(10)
                .standardGachaResources(new int[]{300, 300, 300, 300})
                .build();
        shipPrototypes.add(prototype);
    }
    
    protected void registerExpeditions() {
        ExpeditionPrototype prototype;
        
        prototype = ExpeditionPrototype.builder()
                .id("A0")
                .tick(4)
                .requirement(new Requirement(1, 1))
                .normalReward(new Reward(null, null, 30))
                .build();
        expeditionPrototypes.add(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id("A1")
                .tick(4)
                .requirement(new Requirement(30, 1))
                .normalReward(new Reward(Map.of(RESOURCE_FUEL_ID, 200), null, 10))
                .build();
        expeditionPrototypes.add(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id("A2")
                .tick(4)
                .requirement(new Requirement(1, 3))
                .normalReward(new Reward(Map.of(RESOURCE_AMMO_ID, 200), null, 10))
                .build();
        expeditionPrototypes.add(prototype);
    }
}
