package hundun.idlegame.kancolle.ship;

import java.util.List;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.base.BaseManager;
import hundun.idlegame.kancolle.building.BuildingModel;
import hundun.idlegame.kancolle.data.SessionData;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.ModelNotFoundException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.world.ComponentContext;
import hundun.idlegame.kancolle.world.DataBus;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public class ShipManager extends BaseManager {

    public ShipManager(ComponentContext context) {
        super(context);
    }



    public void shipGotoExpedition(SessionData sessionData, ShipModel ship) {
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
    

    public boolean existShip(SessionData sessionData, String shipId) throws IdleGameException {
        context.getShipFactory().checkPrototypeExist(shipId);
        ShipModel target = null;
        for (ShipModel ship : sessionData.getShips()) {
            if (ship.getPrototype().getId().equals(shipId)) {
                target = ship;
                break;
            }
        }
        
        return target != null;
    }
    

    public ShipModel findShip(SessionData sessionData, String shipId) throws IdleGameException {
        context.getShipFactory().checkPrototypeExist(shipId);
        ShipModel target = null;
        for (ShipModel ship : sessionData.getShips()) {
            if (ship.getPrototype().getId().equals(shipId)) {
                target = ship;
                break;
            }
        }
        
        if (target == null) {
            throw new ModelNotFoundException(shipId, ShipPrototype.class);
        }
        return target;
    }

    public void addNewShip(SessionData sessionData, ShipPrototype prototype) {
        ShipModel shipModel = new ShipModel();
        shipModel.setPrototype(prototype);
        shipModel.setLevel(1);
        shipModel.setExpAndCheckLevelUp(0);
        shipModel.setWorkStatus(ShipWorkStatus.IN_BUILDING);
        shipModel.setWorkInBuildingId(BuildingModel.NONE_ID);
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


    public void shipBackToBuilding(SessionData sessionData, List<ShipModel> ships) {
        ships.forEach(ship -> {
            ship.setWorkStatus(ShipWorkStatus.IN_BUILDING);
        });
    }


    public void shipSetWork(SessionData sessionData, ShipModel ship, BuildingModel building) {
        ship.workInBuildingId = building.getId();
    }

}
