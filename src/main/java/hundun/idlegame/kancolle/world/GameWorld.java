package hundun.idlegame.kancolle.world;
/**
 * @author hundun
 * Created on 2021/09/01
 */


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.container.CommandResult;
import hundun.idlegame.kancolle.container.ExportEventManager;
import hundun.idlegame.kancolle.container.GameSaveData;
import hundun.idlegame.kancolle.container.IGameContainer;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.expedition.ExpeditionFactory;
import hundun.idlegame.kancolle.expedition.ExpeditionManager;
import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.expedition.ExpeditionSaveData;
import hundun.idlegame.kancolle.resource.ResourceManager;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipManager;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import hundun.idlegame.kancolle.ship.ShipSaveData;

public class GameWorld {
    
    EventBus eventBus;
    
    ExpeditionManager expeditionManager;
    TimerManager timerManager;
    ResourceManager resourceManager;
    ShipManager shipManager;
    ExportEventManager exportEventManager;
    IGameContainer container;
    
    protected Map<String, SessionData> sessionDataMap; 
    

    public SessionData getOrCreateSessionData(String sessionId) {
        SessionData sessionData = sessionDataMap.get(sessionId);
        if (sessionData == null) {
            sessionData = SessionData.newSession(sessionId);
            sessionDataMap.put(sessionId, sessionData);
        }
        return sessionData;
    }
    
    public GameWorld(IGameContainer container) {
        this.container = container;
        this.eventBus = new EventBus(container);
        this.expeditionManager = new ExpeditionManager(eventBus);
        this.timerManager = new TimerManager(eventBus);
        this.resourceManager = new ResourceManager(eventBus);
        this.shipManager = new ShipManager(eventBus);
        this.exportEventManager = new ExportEventManager(eventBus, container);
        
        this.sessionDataMap = new HashMap<>();
    }
    

    
    public CommandResult<String> commandShowData(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        StringBuilder builder = new StringBuilder();
        builder.append(timerManager.overviewTime(sessionData)).append("\n");
        builder.append(resourceManager.overviewResourceAmount(sessionData)).append("\n");
        builder.append(shipManager.overviewShips(sessionData)).append("\n");
        builder.append(expeditionManager.overviewExpeditions(sessionData)).append("\n");
        return CommandResult.success(builder.toString());
    }
    
    /**
     * 即把GameSaveData转为SessionData
     */
    public void commandLoadGame(String sessionId, GameSaveData gameSaveData) {
        SessionData sessionData = new SessionData();
        sessionData.setId(sessionId);
        sessionData.setDay(gameSaveData.getDay());
        sessionData.setMonth(gameSaveData.getMonth());
        sessionData.setYear(gameSaveData.getYear());
        sessionData.setExpeditions(ExpeditionFactory.listSaveDataToModel(gameSaveData.getExpeditionSaveDatas()));
        sessionData.setIdleShips(ShipFactory.listSaveDataToModel(gameSaveData.getIdleShipSaveDatas()));
        sessionData.setBusyShips(ShipFactory.listSaveDataToModel(gameSaveData.getBusyShipSaveDatas()));
        sessionData.setResourceBoard(gameSaveData.getResourceBoard());
        sessionDataMap.put(sessionId, sessionData);
    }
    
    /**
     * 即把SessionData转为GameSaveData
     */
    public CommandResult<GameSaveData> commandSaveGame(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        GameSaveData gameSaveData = new GameSaveData();
        gameSaveData.setId(sessionId);
        gameSaveData.setTick(sessionData.getTick());
        gameSaveData.setDay(sessionData.getDay());
        gameSaveData.setMonth(sessionData.getMonth());
        gameSaveData.setYear(sessionData.getYear());
        gameSaveData.setExpeditionSaveDatas(ExpeditionFactory.listModelToSaveData(sessionData.getExpeditions()));
        gameSaveData.setIdleShipSaveDatas(ShipFactory.listModelToSaveData(sessionData.getIdleShips()));
        gameSaveData.setBusyShipSaveDatas(ShipFactory.listModelToSaveData(sessionData.getBusyShips()));
        gameSaveData.setResourceBoard(sessionData.getResourceBoard());
        return CommandResult.success(gameSaveData);
    }
    
    
    public void commandTick(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        this.timerManager.generateTick(sessionData);
    }
    
    public CommandResult<GameSaveData> commandStartGame(String sessionId) {
        sessionDataMap.remove(sessionId);
        commandAddNewShip(sessionId, "吹雪");
        commandAddNewShip(sessionId, "欧根");
        //commandCreateExpedition(sessionId, "A1", "吹雪");
        return commandSaveGame(sessionId);
    }
    
    public void commandAddNewShip(String sessionId, String shipId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        ShipPrototype prototype = ShipFactory.getPrototype(shipId);
        shipManager.addNewShip(sessionData, prototype);
    }
    
    public CommandResult<Void> commandCreateExpedition(String sessionId, String expeditionId, String shipId) {
        eventBus.log(sessionId, LogTag.COMMAND, "CreateExpedition: {}", expeditionId);
        SessionData sessionData = this.getOrCreateSessionData(sessionId);

        ExpeditionPrototype prototype = ExpeditionFactory.getPrototype(expeditionId);
        if (prototype == null) {
            return CommandResult.fail("未找到【远征】：id = " + shipId);
        }
        ShipModel ship = shipManager.findFreeShip(sessionData, shipId);
        if (ship == null) {
            return CommandResult.fail("未找到可用的【舰娘】：id = " + shipId);
        }
        boolean success = expeditionManager.createExpedition(sessionData, prototype, Arrays.asList(shipId));
        if (!success) {
            return CommandResult.fail("【远征】已在进行中：id = " + shipId);
        }
        shipManager.moveShipToWork(sessionData, ship);
        return CommandResult.success(null);
    }

}
