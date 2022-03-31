package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.idlegame;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import hundun.idlegame.kancolle.container.CommandResult;
import hundun.idlegame.kancolle.container.GameSaveData;
import hundun.idlegame.kancolle.container.IGameContainer;
import hundun.idlegame.kancolle.data.config.HardCodeWorldConfig;
import hundun.idlegame.kancolle.data.config.UnittestWorldConfig;
import hundun.idlegame.kancolle.data.config.WorldConfig;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.format.ExceptionFormatter;
import hundun.idlegame.kancolle.world.GameWorld;
import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.BaseFunction;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.contact.Group;

/**
 * @author hundun
 * Created on 2021/09/03
 */
public class GameFunction extends BaseFunction<Void> implements IGameContainer {
    
    GameWorld gameWorld;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    GameFunctionSaveDataRepository gameFunctionSaveDataRepository;
    ExceptionFormatter gameExceptionFormatter = new ExceptionFormatter();
    
    public GameFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName
            ) {
        super(
                botLogic,
                plugin, 
                "", 
                "镇守府", 
                null//(() -> new GameSenderRelation())
                );
        this.scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    tickAllGameSessions();
                    saveAllGameSessions();
                } catch (Exception e) {
                    log.error("Tick scheduler error:", e);
                }
            }
        }, 5, 5, TimeUnit.MINUTES);
        this.gameFunctionSaveDataRepository = new GameFunctionSaveDataRepository(
                plugin, 
                resolveFunctionRepositoryFile("GameFunctionSaveData.json")
                );
        initFunction();
    }
    
    //Map<String, GameSenderRelation> gameSessionIdToGameSenderRelation = new HashMap<>();
    
//    @Data
//    public static class GameSenderRelation {
//        CommandSender sender;
//        String gameSessionId;
//    }

    private void printIdleGameExceptionAdvice(Player player, IdleGameException e) {
        String msg = gameExceptionFormatter.exceptionToMessage(e);
        sendMessageToPlayer(player, msg);
    }
    
    protected void loadAllGameSessions() {
        List<GameFunctionSaveData> gameFunctionSaveDatas = gameFunctionSaveDataRepository.findAll();
        log.info(gameFunctionSaveDatas.size() + " GameSessions will load");
        for (GameFunctionSaveData gameFunctionSaveData : gameFunctionSaveDatas) {
            String gameSessionId = gameFunctionSaveData.getData().getId();
            
            CommandResult<Void> result;
            try {
                result = gameWorld.commandLoadGame(gameSessionId, gameFunctionSaveData.getData());
            } catch (IdleGameException e) {
                printIdleGameExceptionAdvice(gameFunctionSaveData.getPlayer(), e);
                return;
            }
        }
    }
    
    protected void saveAllGameSessions() {
        List<GameFunctionSaveData> gameFunctionSaveDatas = gameFunctionSaveDataRepository.findAll();
        log.info(gameFunctionSaveDatas.size() + " GameSessions will save");
        for (GameFunctionSaveData gameFunctionSaveData : gameFunctionSaveDatas) {
            String gameSessionId = gameFunctionSaveData.getData().getId();
            
            CommandResult<GameSaveData> result;
//            try {
                result = gameWorld.commandSaveGame(gameSessionId);
//            } catch (IdleGameException e) {
//                log.warning("IdleGameException: " + SimpleExceptionAdvice.INSTANCE.exceptionToMessage(e));
//                return;
//            }
            
            gameFunctionSaveData.setData(result.getPayload());
        }
        gameFunctionSaveDataRepository.saveAll(gameFunctionSaveDatas);
    }

    private void initFunction() {
        gameWorld = new GameWorld(this);
        loadAllGameSessions();
    }
    
    private static final String NO_GAME_DATA_REPLY = "请先创建存档以开始游戏";
    
    
    private void tickAllGameSessions() {
        List<GameFunctionSaveData> datas = gameFunctionSaveDataRepository.findAll();
        log.info(datas.size() + " GameSessions will tick");
        for (GameFunctionSaveData data : datas) {
            String gameSessionId = data.getData().getId();
            gameWorld.commandTick(gameSessionId);
        }
    }
    
    //@SubCommand("创建存档")
    public void commandCreateGameData(CommandSender sender) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        if (functionSaveData == null) {
            functionSaveData = new GameFunctionSaveData();
            functionSaveData.setId(functionSessionId);
            functionSaveData.setPlayer(new Player(sender));
            
            
            String gameSessionId = functionSessionId;

            CommandResult<GameSaveData> result;
            try {
                result = gameWorld.commandStartGame(gameSessionId);
            } catch (IdleGameException e) {
                printIdleGameExceptionAdvice(functionSaveData.getPlayer(), e);
                return;
            }
            
            functionSaveData.setData(result.getPayload());
            gameFunctionSaveDataRepository.save(functionSaveData);
            
            sender.sendMessage(result.getAdviceMessage());
        } else {
            sender.sendMessage("存档已存在");
        }
    }
    
    //@SubCommand("保存存档")
    public void commandSaveGameData(CommandSender sender) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        if (functionSaveData == null) {
            sender.sendMessage(NO_GAME_DATA_REPLY);
            return;
        }
        
        CommandResult<GameSaveData> result = gameWorld.commandSaveGame(functionSaveData.getData().getId());

        
        functionSaveData.setData(result.getPayload());
        gameFunctionSaveDataRepository.save(functionSaveData);
        
        sender.sendMessage(result.getAdviceMessage());
    }
    
    //@SubCommand("读取存档")
    public void commandLoadGameData(CommandSender sender) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        if (functionSaveData == null) {
            sender.sendMessage(NO_GAME_DATA_REPLY);
            return;
        }

        CommandResult<Void> result;
        try {
            result = gameWorld.commandLoadGame(functionSaveData.getData().getId(), functionSaveData.getData());
        } catch (IdleGameException e) {
            printIdleGameExceptionAdvice(functionSaveData.getPlayer(), e);
            return;
        }
//        GameSenderRelation gameData = getOrCreateSessionData(sender);
//        gameData.setGameSessionId(functionSaveData.getData().getId());
//        gameData.setSender(sender);
        //gameSessionIdToGameSenderRelation.put(gameData.gameSessionId, gameData);
        
        sender.sendMessage(result.getAdviceMessage());
    }
    
    //@SubCommand("删除存档")
    public void commandDeleteGameData(CommandSender sender) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        if (functionSaveData == null) {
            sender.sendMessage(NO_GAME_DATA_REPLY);
            return;
        }
        gameFunctionSaveDataRepository.delete(functionSaveData);
        sender.sendMessage("删档成功");
    }
    
    //@SubCommand("一览")
    public void commandShowData(CommandSender sender) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        //GameSenderRelation relation = getOrCreateSessionData(sender);
        if (functionSaveData == null) {
            sender.sendMessage(NO_GAME_DATA_REPLY);
            return;
        }
        
        CommandResult<Void> result = gameWorld.commandShowData(functionSaveData.getData().getId());

        
        sender.sendMessage(result.getAdviceMessage());

    }
    
    //@SubCommand("debug跳过tick")
    public void debugCommandTick(CommandSender sender) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        if (functionSaveData == null) {
            sender.sendMessage(NO_GAME_DATA_REPLY);
            return;
        }
        gameWorld.commandTick(functionSaveData.getData().getId());
        sender.sendMessage("执行成功");
    }
    
    //@SubCommand("入驻建筑")
    public void commandShipMoveToBuilding(CommandSender sender, String buildingId, String shipId) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        if (functionSaveData == null) {
            sender.sendMessage(NO_GAME_DATA_REPLY);
            return;
        }
        CommandResult<Void> result;
        try {
            result = gameWorld.commandShipMoveToBuilding(functionSaveData.getData().getId(), buildingId, shipId);
        } catch (IdleGameException e) {
            printIdleGameExceptionAdvice(functionSaveData.getPlayer(), e);
            return;
        }
        sender.sendMessage(result.getAdviceMessage());
    }
    
    //@SubCommand("派出远征")
    public void commandCreateExpedition(CommandSender sender, String expeditionId, String shipId) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        if (functionSaveData == null) {
            sender.sendMessage(NO_GAME_DATA_REPLY);
            return;
        }
        CommandResult<Void> result;
        try {
            result = gameWorld.commandCreateExpedition(functionSaveData.getData().getId(), expeditionId, shipId);
        } catch (IdleGameException e) {
            printIdleGameExceptionAdvice(functionSaveData.getPlayer(), e);
            return;
        }

        sender.sendMessage(result.getAdviceMessage());
    }
    
    //@SubCommand("造船")
    public void commandCreateExpedition(CommandSender sender, int fuel, int ammo, int steel, int bauxite) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        if (functionSaveData == null) {
            sender.sendMessage(NO_GAME_DATA_REPLY);
            return;
        }
        CommandResult<Void> result;
        try {
            int[] cost = new int[] {fuel, ammo, steel, bauxite};
            result = gameWorld.commandGachaShip(functionSaveData.getData().getId(), cost);
        } catch (IdleGameException e) {
            printIdleGameExceptionAdvice(functionSaveData.getPlayer(), e);
            return;
        }

        sender.sendMessage(result.getAdviceMessage());
    }

    @Override
    public void handleLog(String sessionId, String msg) {
        log.info("[GameLog] sessionId: " + sessionId + " " + msg);
    }

    @Override
    public void handleExportEvent(String sessionId, String data) {
        //GameSenderRelation gameData = gameSessionIdToGameSenderRelation.get(sessionId);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findOneByGameSessionId(sessionId);
        if (functionSaveData != null) {
            sendMessageToPlayer(functionSaveData.getPlayer(), data);
        } else {
            log.warning("gameShowData not found sessionId = " + sessionId);
        }
        
    }
    
    private void sendMessageToPlayer(Player player, String message) {
        if (player.isUsingConsole()) {
            log.info("【NOT_GROUP_PLAYER】: " + message);
        } else {
            Group group = Bot.getInstance(player.getBotId()).getGroup(player.getGroupId());
            group.sendMessage(message);
        }
    }

    @Override
    public WorldConfig provideWorldConfig() {
        try {
            WorldConfig worldConfig = GameWorld.readWorldConfigFile(resolveFunctionDataFile("WorldConfig.json"));
            UnittestWorldConfig unittestWorldConfig = new UnittestWorldConfig(worldConfig);
            return unittestWorldConfig;
        } catch (IOException e) {
            log.error(e);
            return new WorldConfig();
        }
    }

    @Override
    public AbstractCommand provideCommand() {
        // TODO Auto-generated method stub
        return null;
    }
}
