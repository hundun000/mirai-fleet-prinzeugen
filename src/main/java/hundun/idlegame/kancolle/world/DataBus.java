package hundun.idlegame.kancolle.world;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.building.BaseBuilding;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.exception.BadCreateExpeditionCommandException;
import hundun.idlegame.kancolle.exception.BadMoveToBuildingCommandException;
import hundun.idlegame.kancolle.exception.BuildingNotFoundException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.expedition.ExpeditionFactory;
import hundun.idlegame.kancolle.expedition.ExpeditionManager;
import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.format.SimpleExceptionFormatter;
import hundun.idlegame.kancolle.resource.ResourceFactory;
import hundun.idlegame.kancolle.resource.ResourceManager;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipManager;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import hundun.idlegame.kancolle.ship.ShipWorkStatus;
import hundun.idlegame.kancolle.time.TimerManager;
import lombok.Getter;
import lombok.Setter;

/**
 * 所有数据操作都应通过本类。<br> 
 * 接受原始请求（常为id），转为对应的model后，交给对应manager实现。
 * @author hundun
 * Created on 2021/09/09
 */
public class DataBus {

    private ComponentContext context;
    
    public DataBus(ComponentContext context) {
        this.context = context;
    }



    
    public void addNewShip(SessionData sessionData, String shipId) throws PrototypeNotFoundException {
        ShipPrototype prototype = context.getShipFactory().getPrototype(shipId);
        context.getShipManager().addNewShip(sessionData, prototype);
    }


    public void resourceMerge(SessionData sessionData, Map<String, Integer> delta, int rate) throws IdleGameException {
        context.getResourceManager().merge(sessionData, delta, rate);
    }


    public void shipAddExp(SessionData sessionData, List<String> shipIds, int exp) {
        context.getShipManager().shipAddExp(sessionData, shipIds, exp);
    }


    public void shipBackToBuilding(SessionData sessionData, List<String> shipIds) {
        List<ShipModel> ships = sessionData.getShips().stream().filter(shipModel -> shipIds.contains(shipModel.getPrototype().getId())).collect(Collectors.toList());
        context.getShipManager().shipBackToBuilding(sessionData, ships);
    }


    public SimpleExceptionFormatter getExceptionAdvice() {
        return this.getExceptionAdvice();
    }


    public void createExpedition(SessionData sessionData, String expeditionId, String shipId) throws IdleGameException {
        ExpeditionPrototype prototype = context.getExpeditionFactory().getPrototype(expeditionId);
        ShipModel ship = context.getShipManager().findShip(sessionData, shipId);
        if (ship.getWorkStatus() != ShipWorkStatus.IN_BUILDING) {
            throw BadCreateExpeditionCommandException.shipBusy(Arrays.asList(shipId));
        }
        String targetBuildingId = context.getExpeditionBuilding().getId();
        boolean inExpeditionBuilding = targetBuildingId.equals(ship.getWorkInBuildingId());
        if (!inExpeditionBuilding) {
            throw BadCreateExpeditionCommandException.needShipInBuilding(targetBuildingId);
        }
        context.getExpeditionManager().createExpedition(sessionData, prototype, Arrays.asList(ship));
        context.getShipManager().shipGotoExpedition(sessionData, ship);
    }


    public void shipChangeWork(SessionData sessionData, String buildingId, String shipId) throws IdleGameException {
        BaseBuilding building = context.getBuilding(buildingId);
        ShipModel ship = context.getShipManager().findShip(sessionData, shipId);
        if (!ship.canChangeWork(buildingId)) {
            throw BadMoveToBuildingCommandException.shipBusy(shipId);
        }
        context.getShipManager().shipSetWork(sessionData, ship, building);
    }


    public void generateTick(SessionData sessionData) {
        context.getTimerManager().generateTick(sessionData);
    }
    

}
