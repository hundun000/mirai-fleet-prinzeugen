package hundun.idlegame.kancolle.world;
/**
 * @author hundun
 * Created on 2021/09/01
 */


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import hundun.idlegame.kancolle.container.CommandResult;
import hundun.idlegame.kancolle.container.GameSaveData;
import hundun.idlegame.kancolle.container.IGameContainer;
import hundun.idlegame.kancolle.data.SessionData;
import hundun.idlegame.kancolle.data.config.WorldConfig;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.world.DataBus.CreateShipResult;
import lombok.Getter;

public class GameWorld {
    
    private static final ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                ;
    }

    //WorldConfig worldConfig;
    @Getter
    private ComponentContext context;
    
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
    
    public GameWorld(IGameContainer container) {
        this.container = container;
        this.sessionDataMap = new HashMap<>();
        this.worldConfig = container.provideWorldConfig();
        
        this.context = new ComponentContext(worldConfig, container);

    }
    

    public static WorldConfig readWorldConfigFile(File file) throws IOException {

        return objectMapper.readValue(file, WorldConfig.class);

    }


    public CommandResult<Void> commandShowData(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        return CommandResult.success(context.getDescriptionFormatter().desWorld(sessionData, this));
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
        sessionData.setExpeditions(context.getExpeditionFactory().listSaveDataToModel(gameSaveData.getExpeditionSaveDatas()));
        sessionData.setShips(context.getShipFactory().listSaveDataToModel(gameSaveData.getShipSaveDatas()));
        sessionData.setResources(context.getResourceFactory().listSaveDataToModelMap(gameSaveData.getResourceSaveDatas()));
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
        gameSaveData.setExpeditionSaveDatas(context.getExpeditionFactory().listModelToSaveData(sessionData.getExpeditions()));
        gameSaveData.setShipSaveDatas(context.getShipFactory().listModelToSaveData(sessionData.getShips()));
        gameSaveData.setResourceSaveDatas(context.getResourceFactory().listModelToSaveData(sessionData.getResources().values()));
        return CommandResult.success(gameSaveData, "读档成功");
    }
    
    
    public void commandTick(String sessionId) {
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        context.getDataBus().generateTick(sessionData);
    }
    
    public CommandResult<GameSaveData> commandStartGame(String sessionId) throws IdleGameException {
        sessionDataMap.remove(sessionId);
        SessionData sessionData = this.getOrCreateSessionData(sessionId);
        // handle start-data
        for (String startShipId : worldConfig.getStartShips()) {
            context.getDataBus().addNewShip(sessionData, startShipId);
        }
        context.getDataBus().resourceMerge(sessionData, worldConfig.getStartResources(), 1);
       
        //commandCreateExpedition(sessionId, "A1", "吹雪");
        return commandSaveGame(sessionId);
    }
    

    public CommandResult<Void> commandGachaShip(String sessionId, int[] inputResources) throws IdleGameException {
        context.getEventBus().log(sessionId, LogTag.COMMAND, "GachaShip: {}", Arrays.toString(inputResources));
        SessionData sessionData = this.getOrCreateSessionData(sessionId);

        CreateShipResult result = context.getDataBus().createShip(sessionData, inputResources);
        String text = context.getDescriptionFormatter().desCreateShipResult(result);
        
        return CommandResult.success(text);
    }
    
    public CommandResult<Void> commandCreateExpedition(String sessionId, String expeditionId, String shipId) throws IdleGameException {
        context.getEventBus().log(sessionId, LogTag.COMMAND, "CreateExpedition: {}", expeditionId);
        SessionData sessionData = this.getOrCreateSessionData(sessionId);

        context.getDataBus().createExpedition(sessionData, expeditionId, shipId);
        
        return CommandResult.success("远征创建成功");
    }
    
    public CommandResult<Void> commandShipMoveToBuilding(String sessionId, String buildingId, String shipId) throws IdleGameException {
        context.getEventBus().log(sessionId, LogTag.COMMAND, "ShipMoveToBuilding: {} -> {}", shipId, buildingId);
        SessionData sessionData = this.getOrCreateSessionData(sessionId);

        context.getDataBus().shipChangeWork(sessionData, buildingId, shipId);
        
        return CommandResult.success("工作状态修改成功");
    }

}
