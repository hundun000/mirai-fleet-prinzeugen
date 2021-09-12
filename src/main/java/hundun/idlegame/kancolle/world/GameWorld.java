package hundun.idlegame.kancolle.world;
/**
 * @author hundun
 * Created on 2021/09/01
 */


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import hundun.idlegame.kancolle.container.CommandResult;
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
import hundun.idlegame.kancolle.format.DescriptionFormatter;
import hundun.idlegame.kancolle.format.SimpleExceptionFormatter;
import hundun.idlegame.kancolle.resource.ResourceFactory;
import hundun.idlegame.kancolle.resource.ResourceManager;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipManager;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.ship.ShipWorkStatus;
import hundun.idlegame.kancolle.time.TimerManager;
import lombok.Getter;

public class GameWorld {
    
    EventBus eventBus;
    DataBus dataBus;
    @Getter
    SimpleExceptionFormatter exceptionAdvice;
    //WorldConfig worldConfig;
    
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
    
    private WorldConfig worldConfig;
    

    public SessionData getOrCreateSessionData(String sessionId) {
        SessionData sessionData = sessionDataMap.get(sessionId);
        if (sessionData == null) {
            sessionData = SessionData.newSession(sessionId);
            sessionDataMap.put(sessionId, sessionData);
        }
        return sessionData;
    }
    
    public GameWorld(IGameContainer container, WorldConfig worldConfig) {
        this.container = container;
        this.eventBus = new EventBus(container);
        this.dataBus = new DataBus(this);
        this.expeditionManager = new ExpeditionManager(eventBus, dataBus);
        this.timerManager = new TimerManager(eventBus, dataBus);
        this.resourceManager = new ResourceManager(eventBus, dataBus);
        this.shipManager = new ShipManager(eventBus, dataBus);
        this.exportEventManager = new ExportEventManager(eventBus, dataBus);
        
        this.sessionDataMap = new HashMap<>();
        this.exceptionAdvice = new SimpleExceptionFormatter();
        this.worldConfig = worldConfig;
        
        worldConfig.registerAll();
    }
    

    
    public CommandResult<Void> commandShowData(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        return CommandResult.success(DescriptionFormatter.desWorld(sessionData, this));
    }
    
    /**
     * 即把GameSaveData转为SessionData
     * @return 
     * @throws IdleGameException 
     */
    public CommandResult<Void> commandLoadGame(String sessionId, GameSaveData gameSaveData) throws IdleGameException {
        SessionData sessionData = new SessionData();
        sessionData.setId(sessionId);
        sessionData.setCalendar(gameSaveData.getCalendar());
        sessionData.setExpeditions(ExpeditionFactory.INSTANCE.listSaveDataToModel(gameSaveData.getExpeditionSaveDatas()));
        sessionData.setShips(ShipFactory.INSTANCE.listSaveDataToModel(gameSaveData.getShipSaveDatas()));
        sessionData.setResources(ResourceFactory.INSTANCE.listSaveDataToModelMap(gameSaveData.getResourceSaveDatas()));
        sessionDataMap.put(sessionId, sessionData);
        return CommandResult.success("读档成功");
    }
    
    /**
     * 即把SessionData转为GameSaveData
     */
    public CommandResult<GameSaveData> commandSaveGame(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        GameSaveData gameSaveData = new GameSaveData();
        gameSaveData.setId(sessionId);
        gameSaveData.setCalendar(sessionData.getCalendar());
        gameSaveData.setExpeditionSaveDatas(ExpeditionFactory.INSTANCE.listModelToSaveData(sessionData.getExpeditions()));
        gameSaveData.setShipSaveDatas(ShipFactory.INSTANCE.listModelToSaveData(sessionData.getShips()));
        gameSaveData.setResourceSaveDatas(ResourceFactory.INSTANCE.listModelToSaveData(sessionData.getResources().values()));
        return CommandResult.success(gameSaveData, "读档成功");
    }
    
    
    public void commandTick(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        this.timerManager.generateTick(sessionData);
    }
    
    public CommandResult<GameSaveData> commandStartGame(String sessionId) throws IdleGameException {
        sessionDataMap.remove(sessionId);
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        for (String startShipId : worldConfig.getStartShips()) {
            dataBus.addNewShip(sessionData, startShipId);
        }
        
        //commandCreateExpedition(sessionId, "A1", "吹雪");
        return commandSaveGame(sessionId);
    }
    

    
    public CommandResult<Void> commandCreateExpedition(String sessionId, String expeditionId, String shipId) throws IdleGameException {
        eventBus.log(sessionId, LogTag.COMMAND, "CreateExpedition: {}", expeditionId);
        SessionData sessionData = this.getOrCreateSessionData(sessionId);

        ExpeditionPrototype prototype = ExpeditionFactory.INSTANCE.getPrototype(expeditionId);
        ShipModel ship = shipManager.findFreeShip(sessionData, shipId, ShipWorkStatus.IDLE, true);
        if (ship == null) {
            throw BadCreateExpeditionCommandException.shipBusy(Arrays.asList(shipId));
        }
        expeditionManager.createExpedition(sessionData, prototype, Arrays.asList(ship));
        shipManager.moveShipToWork(sessionData, ship);
        return CommandResult.success("远征创建成功");
    }

}
