package hundun.idlegame.kancolle.world;
/**
 * @author hundun
 * Created on 2021/09/01
 */


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import hundun.idlegame.kancolle.container.ExportEventManager;
import hundun.idlegame.kancolle.container.GameSaveData;
import hundun.idlegame.kancolle.container.IGameContainer;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.exception.BadCreateExpeditionCommandException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.expedition.ExpeditionFactory;
import hundun.idlegame.kancolle.expedition.ExpeditionManager;
import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.resource.ResourceFactory;
import hundun.idlegame.kancolle.resource.ResourceManager;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipManager;
import hundun.idlegame.kancolle.ship.ShipModel;
import lombok.Getter;

public class GameWorld {
    
    EventBus eventBus;
    DataBus dataBus;
    WorldConfig worldConfig;
    
    @Getter
    private ExpeditionManager expeditionManager;
    @Getter
    private TimerManager timerManager;
    @Getter
    private ResourceManager resourceManager;
    @Getter
    private ShipManager shipManager;
    @Getter
    private ExportEventManager exportEventManager;
    
    private IGameContainer container;
    
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
        this.dataBus = new DataBus(this);
        this.expeditionManager = new ExpeditionManager(eventBus, dataBus);
        this.timerManager = new TimerManager(eventBus, dataBus);
        this.resourceManager = new ResourceManager(eventBus, dataBus);
        this.shipManager = new ShipManager(eventBus, dataBus);
        this.exportEventManager = new ExportEventManager(eventBus, dataBus);
        
        
        
        this.sessionDataMap = new HashMap<>();
        this.worldConfig = new WorldConfig();
    }
    

    
    public String commandShowData(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        StringBuilder builder = new StringBuilder();
        builder.append(timerManager.overviewTime(sessionData)).append("\n");
        builder.append(resourceManager.overviewResourceAmount(sessionData)).append("\n");
        builder.append(shipManager.overviewShips(sessionData)).append("\n");
        builder.append(expeditionManager.overviewExpeditions(sessionData)).append("\n");
        return builder.toString();
    }
    
    /**
     * 即把GameSaveData转为SessionData
     * @throws IdleGameException 
     */
    public void commandLoadGame(String sessionId, GameSaveData gameSaveData) throws IdleGameException {
        SessionData sessionData = new SessionData();
        sessionData.setId(sessionId);
        sessionData.setDay(gameSaveData.getDay());
        sessionData.setMonth(gameSaveData.getMonth());
        sessionData.setYear(gameSaveData.getYear());
        sessionData.setExpeditions(ExpeditionFactory.INSTANCE.listSaveDataToModel(gameSaveData.getExpeditionSaveDatas()));
        sessionData.setIdleShips(ShipFactory.INSTANCE.listSaveDataToModel(gameSaveData.getIdleShipSaveDatas()));
        sessionData.setBusyShips(ShipFactory.INSTANCE.listSaveDataToModel(gameSaveData.getBusyShipSaveDatas()));
        sessionData.setResources(ResourceFactory.INSTANCE.listSaveDataToModelMap(gameSaveData.getResourceSaveDatas()));
        sessionDataMap.put(sessionId, sessionData);
    }
    
    /**
     * 即把SessionData转为GameSaveData
     */
    public GameSaveData commandSaveGame(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        GameSaveData gameSaveData = new GameSaveData();
        gameSaveData.setId(sessionId);
        gameSaveData.setTick(sessionData.getTick());
        gameSaveData.setDay(sessionData.getDay());
        gameSaveData.setMonth(sessionData.getMonth());
        gameSaveData.setYear(sessionData.getYear());
        gameSaveData.setExpeditionSaveDatas(ExpeditionFactory.INSTANCE.listModelToSaveData(sessionData.getExpeditions()));
        gameSaveData.setIdleShipSaveDatas(ShipFactory.INSTANCE.listModelToSaveData(sessionData.getIdleShips()));
        gameSaveData.setBusyShipSaveDatas(ShipFactory.INSTANCE.listModelToSaveData(sessionData.getBusyShips()));
        gameSaveData.setResourceSaveDatas(ResourceFactory.INSTANCE.listModelToSaveData(sessionData.getResources().values()));
        return gameSaveData;
    }
    
    
    public void commandTick(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        this.timerManager.generateTick(sessionData);
    }
    
    public GameSaveData commandStartGame(String sessionId) throws IdleGameException {
        sessionDataMap.remove(sessionId);
        commandAddNewShip(sessionId, "吹雪");
        commandAddNewShip(sessionId, "欧根");
        //commandCreateExpedition(sessionId, "A1", "吹雪");
        return commandSaveGame(sessionId);
    }
    
    public void commandAddNewShip(String sessionId, String shipId) throws PrototypeNotFoundException {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        dataBus.addNewShip(sessionData, shipId);
    }
    
    public void commandCreateExpedition(String sessionId, String expeditionId, String shipId) throws IdleGameException {
        eventBus.log(sessionId, LogTag.COMMAND, "CreateExpedition: {}", expeditionId);
        SessionData sessionData = this.getOrCreateSessionData(sessionId);

        ExpeditionPrototype prototype = ExpeditionFactory.INSTANCE.getPrototype(expeditionId);
        ShipModel ship = shipManager.findFreeShip(sessionData, shipId);
        if (ship == null) {
            throw BadCreateExpeditionCommandException.shipBusy(Arrays.asList(shipId));
        }
        expeditionManager.createExpedition(sessionData, prototype, Arrays.asList(ship));
        shipManager.moveShipToWork(sessionData, ship);
    }

}
