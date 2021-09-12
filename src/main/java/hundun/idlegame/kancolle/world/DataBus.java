package hundun.idlegame.kancolle.world;

import java.util.List;
import java.util.Map;

import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipPrototype;

/**
 * @author hundun
 * Created on 2021/09/09
 */
public class DataBus {

    GameWorld gameWorld;
    
    public DataBus(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    
    public void addNewShip(SessionData sessionData, String shipId) throws PrototypeNotFoundException {
        
        ShipPrototype prototype = ShipFactory.INSTANCE.getPrototype(shipId);
        gameWorld.getShipManager().addNewShip(sessionData, prototype);
    }


    public void resourceMerge(SessionData sessionData, Map<String, Integer> delta) throws IdleGameException {
        gameWorld.getResourceManager().merge(sessionData, delta);
    }


    public void shipAddExp(SessionData sessionData, List<String> shipIds, int exp) {
        gameWorld.getShipManager().shipAddExp(sessionData, shipIds, exp);
    }


    public void releaseShip(SessionData sessionData, List<String> shipIds) {
        gameWorld.getShipManager().releaseShip(sessionData, shipIds);
    }
    

}
