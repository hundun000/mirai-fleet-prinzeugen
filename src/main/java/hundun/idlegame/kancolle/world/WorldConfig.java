package hundun.idlegame.kancolle.world;

import java.util.ArrayList;
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
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/09/11
 */
public class WorldConfig {
    
    @Getter
    protected List<String> startShips = new ArrayList<>();
    
    public final static String RESOURCE_FUEL_ID = "FUEL";
    public final static String RESOURCE_AMMO_ID = "AMMO";
    public final static String RESOURCE_STEEL_ID = "STEEL";
    public final static String RESOURCE_BAUXITE_ID = "BAUXITE";
    public final static String RESOURCE_MODERNIZATION_POINT_ID = "MODERNIZATION_POINT";
    
    
    public WorldConfig() {
        startShips.add("吹雪");
        startShips.add("欧根");
    }
    

    
    protected void registerResources(ResourceFactory resourceFactory) {
        ResourcePrototype prototype;
        
        prototype = ResourcePrototype.builder().id(RESOURCE_FUEL_ID).name("油").build();
        resourceFactory.register(prototype);
        
        prototype = ResourcePrototype.builder().id(RESOURCE_AMMO_ID).name("弹").build();
        resourceFactory.register(prototype);
        
        prototype = ResourcePrototype.builder().id(RESOURCE_STEEL_ID).name("钢").build();
        resourceFactory.register(prototype);
        
        prototype = ResourcePrototype.builder().id(RESOURCE_BAUXITE_ID).name("铝").build();
        resourceFactory.register(prototype);
        
        prototype = ResourcePrototype.builder().id(RESOURCE_MODERNIZATION_POINT_ID).name("近代化改修点数").build();
        resourceFactory.register(prototype);
    }

    protected void registerShips(ShipFactory shipFactory) {
        ShipPrototype prototype;
        
        prototype = ShipPrototype.builder()
                .id("吹雪")
                .basePower(10)
                .gachaRarity(1)
                .standardGachaResources(new int[]{30, 30, 30, 30})
                .build();
        shipFactory.register(prototype);
        
        prototype = ShipPrototype.builder()
                .id("睦月")
                .basePower(10)
                .gachaRarity(1)
                .standardGachaResources(new int[]{30, 30, 30, 30})
                .build();
        shipFactory.register(prototype);
        
        prototype = ShipPrototype.builder()
                .id("如月")
                .basePower(10)
                .gachaRarity(1)
                .standardGachaResources(new int[]{30, 30, 30, 30})
                .build();
        shipFactory.register(prototype);
        
        prototype = ShipPrototype.builder()
                .id("欧根")
                .basePower(30)
                .gachaRarity(10)
                .standardGachaResources(new int[]{300, 300, 300, 300})
                .build();
        shipFactory.register(prototype);
    }
    
    protected void registerExpeditions(ExpeditionFactory expeditionFactory) {
        ExpeditionPrototype prototype;
        
        prototype = ExpeditionPrototype.builder()
                .id("A0")
                .tick(4)
                .requirement(new Requirement(1, 1))
                .normalReward(new Reward(null, null, 30))
                .build();
        expeditionFactory.register(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id("A1")
                .tick(4)
                .requirement(new Requirement(30, 1))
                .normalReward(new Reward(Map.of(RESOURCE_FUEL_ID, 200), null, 10))
                .build();
        expeditionFactory.register(prototype);
        
        prototype = ExpeditionPrototype.builder()
                .id("A2")
                .tick(4)
                .requirement(new Requirement(1, 3))
                .normalReward(new Reward(Map.of(RESOURCE_AMMO_ID, 200), null, 10))
                .build();
        expeditionFactory.register(prototype);
    }
}
