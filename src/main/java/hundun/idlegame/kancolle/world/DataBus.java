package hundun.idlegame.kancolle.world;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.building.BaseBuilding;
import hundun.idlegame.kancolle.building.BuildingLocator;
import hundun.idlegame.kancolle.container.ExportEventManager;
import hundun.idlegame.kancolle.exception.BadCreateExpeditionCommandException;
import hundun.idlegame.kancolle.exception.BadMoveToBuildingCommandException;
import hundun.idlegame.kancolle.exception.BuildingNotFoundException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.expedition.ExpeditionFactory;
import hundun.idlegame.kancolle.expedition.ExpeditionManager;
import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.format.SimpleExceptionFormatter;
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
    @Setter
    private ExpeditionManager expeditionManager;
    @Setter
    private TimerManager timerManager;
    @Setter
    private ResourceManager resourceManager;
    @Setter
    private ShipManager shipManager;
    @Setter
    private ExportEventManager exportEventManager;
    @Setter
    private BuildingLocator buildingLocator;


    
    public DataBus() {

    }

    
    public void addNewShip(SessionData sessionData, String shipId) throws PrototypeNotFoundException {
        ShipPrototype prototype = ShipFactory.INSTANCE.getPrototype(shipId);
        shipManager.addNewShip(sessionData, prototype);
    }


    public void resourceMerge(SessionData sessionData, Map<String, Integer> delta, int rate) throws IdleGameException {
        resourceManager.merge(sessionData, delta, rate);
    }


    public void shipAddExp(SessionData sessionData, List<String> shipIds, int exp) {
        shipManager.shipAddExp(sessionData, shipIds, exp);
    }


    public void shipBackToBuilding(SessionData sessionData, List<String> shipIds) {
        List<ShipModel> ships = sessionData.getShips().stream().filter(shipModel -> shipIds.contains(shipModel.getPrototype().getId())).collect(Collectors.toList());
        shipManager.shipBackToBuilding(sessionData, ships);
    }


    public SimpleExceptionFormatter getExceptionAdvice() {
        return this.getExceptionAdvice();
    }


    public void createExpedition(SessionData sessionData, String expeditionId, String shipId) throws IdleGameException {
        ExpeditionPrototype prototype = ExpeditionFactory.INSTANCE.getPrototype(expeditionId);
        ShipModel ship = shipManager.findShip(sessionData, shipId);
        if (ship.getWorkStatus() != ShipWorkStatus.IN_BUILDING) {
            throw BadCreateExpeditionCommandException.shipBusy(Arrays.asList(shipId));
        }
        String targetBuildingId = buildingLocator.getExpeditionBuilding().getId();
        boolean inExpeditionBuilding = targetBuildingId.equals(ship.getWorkInBuildingId());
        if (!inExpeditionBuilding) {
            throw BadCreateExpeditionCommandException.needShipInBuilding(targetBuildingId);
        }
        expeditionManager.createExpedition(sessionData, prototype, Arrays.asList(ship));
        shipManager.shipGotoExpedition(sessionData, ship);
    }


    public void shipChangeWork(SessionData sessionData, String buildingId, String shipId) throws IdleGameException {
        BaseBuilding building = buildingLocator.getBuilding(buildingId);
        ShipModel ship = shipManager.findShip(sessionData, shipId);
        if (!ship.canChangeWork(buildingId)) {
            throw BadMoveToBuildingCommandException.shipBusy(shipId);
        }
        shipManager.shipSetWork(sessionData, ship, building);
    }


    public void generateTick(SessionData sessionData) {
        timerManager.generateTick(sessionData);
    }
    

}
