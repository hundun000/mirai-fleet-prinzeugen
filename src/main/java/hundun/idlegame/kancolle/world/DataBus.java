package hundun.idlegame.kancolle.world;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.building.BuildingModel;
import hundun.idlegame.kancolle.building.instance.GachaBuilding.GachaResult;
import hundun.idlegame.kancolle.data.SessionData;
import hundun.idlegame.kancolle.data.config.WorldConfig;
import hundun.idlegame.kancolle.exception.BadCreateExpeditionCommandException;
import hundun.idlegame.kancolle.exception.BadGachaCommandException;
import hundun.idlegame.kancolle.exception.BadMoveToBuildingCommandException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import hundun.idlegame.kancolle.ship.ShipWorkStatus;
import lombok.Data;


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
    
    @Data
    public static class CreateShipResult {
        ShipPrototype prototype;
        int maxGachaRarity;
        int gainModenizationPoint;
    }


    public CreateShipResult createShip(SessionData sessionData, int[] inputResources) throws IdleGameException {
        Map<String, Integer> cost = Map.of(
                WorldConfig.RESOURCE_FUEL_ID, inputResources[0], 
                WorldConfig.RESOURCE_AMMO_ID, inputResources[1], 
                WorldConfig.RESOURCE_STEEL_ID, inputResources[2], 
                WorldConfig.RESOURCE_BAUXITE_ID, inputResources[3] 
                );
        context.getResourceManager().consume(sessionData, cost);
        GachaResult gachaResult = context.getGachaBuilding().gachaShip(sessionData, inputResources);
        ShipPrototype prototype = gachaResult.getPayload();
        if (prototype == null) {
            throw BadGachaCommandException.inputAmoutTooSmall();
        }
        boolean isNewShip = !context.getShipManager().existShip(sessionData, prototype.getId());
        int gainModenizationPoint = -1;
        if (isNewShip) {
            context.getShipManager().addNewShip(sessionData, prototype);
        } else {
            gainModenizationPoint = prototype.getGachaRarity();
            context.getResourceManager().merge(sessionData, WorldConfig.RESOURCE_MODERNIZATION_POINT_ID, gainModenizationPoint);
        }
        CreateShipResult result = new CreateShipResult();
        result.setPrototype(prototype);
        result.setGainModenizationPoint(gainModenizationPoint);
        result.setMaxGachaRarity(gachaResult.getMaxGachaRarity());
        return result;
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
        BuildingModel building = context.getBuilding(buildingId);
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
