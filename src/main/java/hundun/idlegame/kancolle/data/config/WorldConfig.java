package hundun.idlegame.kancolle.data.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.resource.ResourcePrototype;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/09/22
 */
@Data
public class WorldConfig {
    
    public final static String RESOURCE_MODERNIZATION_POINT_ID = "MODERNIZATION_POINT";
    
    public final static String RESOURCE_FUEL_ID = "FUEL";
    public final static String RESOURCE_AMMO_ID = "AMMO";
    public final static String RESOURCE_STEEL_ID = "STEEL";
    public final static String RESOURCE_BAUXITE_ID = "BAUXITE";
    
    protected List<String> startShips = new ArrayList<>();
    protected Map<String, Integer> startResources = new HashMap<>();
    
    protected List<ShipPrototype> shipPrototypes = new ArrayList<>();
    protected List<ResourcePrototype> resourcePrototypes = new ArrayList<>();
    protected List<ExpeditionPrototype> expeditionPrototypes = new ArrayList<>();
}
