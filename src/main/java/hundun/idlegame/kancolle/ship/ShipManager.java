package hundun.idlegame.kancolle.ship;

import java.util.List;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.ModelNotFoundException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.world.BaseManager;
import hundun.idlegame.kancolle.world.DataBus;
import hundun.idlegame.kancolle.world.SessionData;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public class ShipManager extends BaseManager{

    public ShipManager(EventBus eventBus, DataBus dataBus) {
        super(eventBus, dataBus);
    }
    
    
    public void moveShipToWork(SessionData sessionData, ShipModel ship) {
        ship.workStatus = ShipWorkStatus.IN_EXPETITION;
    }

//    @Override
//    public void onExpeditionCompleted(SessionData sessionData, List<ExpeditionModel> completedTasks) {
//        completedTasks.forEach(task -> {
//            List<ShipModel> ships = sessionData.getBusyShips().stream().filter(shipModel -> task.getShipIds().contains(shipModel.getPrototype().getId())).collect(Collectors.toList());
//            ships.forEach(ship -> ship.setExp(ship.getExp() + task.getPrototype().getRewardExp()));
//            sessionData.getBusyShips().removeAll(ships);
//            sessionData.getIdleShips().addAll(ships);
//        });
//    }
    
    
    
    

    public ShipModel findFreeShip(SessionData sessionData, String shipId, ShipWorkStatus workStatusFilter, boolean exceptionIfModelNotFound) throws IdleGameException {
        ShipFactory.INSTANCE.getPrototype(shipId);
        boolean workStatusFilterMatched = false;
        ShipModel target = null;
        for (ShipModel ship : sessionData.getShips()) {
            if (ship.getPrototype().getId().equals(shipId)) {
                workStatusFilterMatched = workStatusFilter != null && ship.getWorkStatus() == workStatusFilter;
                target = ship;
                break;
            }
        }
        
        if (exceptionIfModelNotFound && target == null) {
            throw new ModelNotFoundException(shipId, ShipPrototype.class);
        }
        if (workStatusFilterMatched) {
            return target;
        } else {
            return null;
        }
        
    }

    public void addNewShip(SessionData sessionData, ShipPrototype prototype) {
        ShipModel shipModel = new ShipModel();
        shipModel.setPrototype(prototype);
        shipModel.setLevel(1);
        shipModel.setExpAndCheckLevelUp(0);
        shipModel.setWorkStatus(ShipWorkStatus.IDLE);
        sessionData.getShips().add(shipModel);
        eventBus.sendShipAddNewEvent(sessionData, shipModel);
    }


    public void shipAddExp(SessionData sessionData, List<String> shipIds, int exp) {
        List<ShipModel> ships = sessionData.getShips().stream().filter(shipModel -> shipIds.contains(shipModel.getPrototype().getId())).collect(Collectors.toList());
        ships.forEach(ship -> {
            boolean levelUp =  ship.setExpAndCheckLevelUp(exp);
            if (levelUp) {
                eventBus.sendShipLevelUpEvent(sessionData, ship);
            }
        });
    }


    public void releaseShip(SessionData sessionData, List<String> shipIds) {
        List<ShipModel> ships = sessionData.getShips().stream().filter(shipModel -> shipIds.contains(shipModel.getPrototype().getId())).collect(Collectors.toList());
        ships.forEach(ship -> ship.setWorkStatus(ShipWorkStatus.IDLE));
    }
}
