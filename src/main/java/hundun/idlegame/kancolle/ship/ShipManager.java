package hundun.idlegame.kancolle.ship;

import java.util.List;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.event.EventBus;
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
        sessionData.getIdleShips().remove(ship);
        sessionData.getBusyShips().add(ship);
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
    
    public String overviewShips(SessionData sessionData) {
        return "空闲舰娘:" + sessionData.getIdleShips().stream().map(ship -> ship.getPrototype().getId()).collect(Collectors.joining(",")) + "\n"
                + "工作中舰娘:" + sessionData.getBusyShips().stream().map(ship -> ship.getPrototype().getId()).collect(Collectors.joining(","))
                ;
    }

    public ShipModel findFreeShip(SessionData sessionData, String shipId) {
        return sessionData.getIdleShips().stream().filter(item -> item.getPrototype().getId().equals(shipId)).findFirst().orElse(null);
    }

    public void addNewShip(SessionData sessionData, ShipPrototype prototype) {
        ShipModel shipModel = new ShipModel();
        shipModel.setPrototype(prototype);
        shipModel.setLevel(1);
        shipModel.setExpAndCheckLevelUp(0);
        sessionData.getIdleShips().add(shipModel);
        eventBus.sendShipAddNewEvent(sessionData, shipModel);
    }


    public void shipAddExp(SessionData sessionData, List<String> shipIds, int exp) {
        List<ShipModel> ships = sessionData.getBusyShips().stream().filter(shipModel -> shipIds.contains(shipModel.getPrototype().getId())).collect(Collectors.toList());
        ships.forEach(ship -> {
            boolean levelUp =  ship.setExpAndCheckLevelUp(exp);
            if (levelUp) {
                eventBus.sendShipLevelUpEvent(sessionData, ship);
            }
        });
    }


    public void releaseShip(SessionData sessionData, List<String> shipIds) {
        List<ShipModel> ships = sessionData.getBusyShips().stream().filter(shipModel -> shipIds.contains(shipModel.getPrototype().getId())).collect(Collectors.toList());
        sessionData.getBusyShips().removeAll(ships);
        sessionData.getIdleShips().addAll(ships);
    }
}
