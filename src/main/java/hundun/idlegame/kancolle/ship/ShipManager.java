package hundun.idlegame.kancolle.ship;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.IExpeditionEventListener;
import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.world.BaseManager;
import hundun.idlegame.kancolle.world.SessionData;
import kotlinx.coroutines.scheduling.Task;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public class ShipManager extends BaseManager implements IExpeditionEventListener{

    public ShipManager(EventBus eventBus) {
        super(eventBus);
    }
    
    
    public void moveShipToWork(SessionData sessionData, ShipModel ship) {
        sessionData.getIdleShips().remove(ship);
        sessionData.getBusyShips().add(ship);
    }

    @Override
    public void onExpeditionCompleted(SessionData sessionData, List<ExpeditionModel> completedTasks) {
        completedTasks.forEach(task -> {
            List<ShipModel> ships = sessionData.getBusyShips().stream().filter(shipModel -> task.getShipIds().contains(shipModel.getPrototype().getId())).collect(Collectors.toList());
            ships.forEach(ship -> ship.setExp(ship.getExp() + task.getPrototype().getRewardExp()));
            sessionData.getBusyShips().removeAll(ships);
            sessionData.getIdleShips().addAll(ships);
        });
    }
    
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
        shipModel.setExp(0);
        sessionData.getIdleShips().add(shipModel);
    }
}
