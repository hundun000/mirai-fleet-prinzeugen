package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.idlegame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import hundun.idlegame.kancolle.container.CommandResult;
import hundun.idlegame.kancolle.container.GameSaveData;
import hundun.idlegame.kancolle.container.IGameContainer;
import hundun.idlegame.kancolle.world.GameWorld;
import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.AsCommand;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.helper.repository.MapDocumentRepository;
import hundun.miraifleet.framework.starter.botlogic.function.reminder.domain.ReminderList;
import lombok.Data;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.MemberCommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.contact.Group;

/**
 * @author hundun
 * Created on 2021/09/03
 */
@AsCommand
public class GameFunction extends BaseFunction<Void> implements IGameContainer {
    
    GameWorld gameWorld;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    GameFunctionSaveDataRepository gameFunctionSaveDataRepository;
    
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
                tickAllGameSessions();
                saveAllGameSessions();
            }
        }, 5, 5, TimeUnit.MINUTES);
        this.gameFunctionSaveDataRepository = new GameFunctionSaveDataRepository(
                plugin, 
                resolveFunctionRepositoryFile("GameFunctionSaveData.json")
                );
        initGame();
    }
    
    //Map<String, GameSenderRelation> gameSessionIdToGameSenderRelation = new HashMap<>();
    
//    @Data
//    public static class GameSenderRelation {
//        CommandSender sender;
//        String gameSessionId;
//    }

    protected void saveAllGameSessions() {
        List<GameFunctionSaveData> gameFunctionSaveDatas = gameFunctionSaveDataRepository.findAll();
        log.info(gameFunctionSaveDatas.size() + " GameSessions will save");
        for (GameFunctionSaveData gameFunctionSaveData : gameFunctionSaveDatas) {
            String gameSessionId = gameFunctionSaveData.getData().getId();
            
            CommandResult<GameSaveData> result = gameWorld.commandSaveGame(gameSessionId);
            if (!result.isSuccess()) {
                log.warning("失败: " + result.getFailReason());
                return;
            }
            
            gameFunctionSaveData.setData(result.getData());
        }
        gameFunctionSaveDataRepository.saveAll(gameFunctionSaveDatas);
    }

    private void initGame() {
        gameWorld = new GameWorld(this);
        
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
    
    @SubCommand("创建存档")
    public void createFunctionSaveData(CommandSender sender) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        if (functionSaveData == null) {
            String gameSessionId = UUID.randomUUID().toString();
            CommandResult<GameSaveData> result = gameWorld.commandStartGame(gameSessionId);
            if (!result.isSuccess()) {
                sender.sendMessage("失败: " + result.getFailReason());
                return;
            }
            
            functionSaveData = new GameFunctionSaveData();
            functionSaveData.setId(functionSessionId);
            functionSaveData.setPlayer(new Player(sender));
            functionSaveData.setData(result.getData());
            gameFunctionSaveDataRepository.save(functionSaveData);
            
            sender.sendMessage("创建成功");
        } else {
            sender.sendMessage("存档已存在");
        }
    }
    
    @SubCommand("保存存档")
    public void save(CommandSender sender) {
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
        if (!result.isSuccess()) {
            sender.sendMessage("失败: " + result.getFailReason());
            return;
        }
        
        functionSaveData.setData(result.getData());
        gameFunctionSaveDataRepository.save(functionSaveData);
        
        sender.sendMessage("存档成功");
    }
    
    @SubCommand("读取存档")
    public void load(CommandSender sender) {
        if (!checkCosPermission(sender)) {
            return;
        }
        String functionSessionId = getSessionId(sender);
        GameFunctionSaveData functionSaveData = gameFunctionSaveDataRepository.findById(functionSessionId);
        if (functionSaveData == null) {
            sender.sendMessage(NO_GAME_DATA_REPLY);
            return;
        }
        gameWorld.commandLoadGame(functionSaveData.getData().getId(), functionSaveData.getData());
        
//        GameSenderRelation gameData = getOrCreateSessionData(sender);
//        gameData.setGameSessionId(functionSaveData.getData().getId());
//        gameData.setSender(sender);
        //gameSessionIdToGameSenderRelation.put(gameData.gameSessionId, gameData);
        
        sender.sendMessage("读档成功");
    }
    
    @SubCommand("删除存档")
    public void delete(CommandSender sender) {
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
    
    @SubCommand("一览")
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
        
        CommandResult<String> result = gameWorld.commandShowData(functionSaveData.getData().getId());
        if (!result.isSuccess()) {
            sender.sendMessage("失败！" + result.getFailReason());
            return;
        }
        
        sender.sendMessage(result.getData());

    }
    
    @SubCommand("跳过tick")
    public void commandTick(CommandSender sender) {
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
    
    @SubCommand("派出远征")
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
        CommandResult<Void> result = gameWorld.commandCreateExpedition(functionSaveData.getData().getId(), expeditionId, shipId);
        if (!result.isSuccess()) {
            sender.sendMessage("失败！" + result.getFailReason());
            return;
        }
        sender.sendMessage("执行成功");
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
            if (functionSaveData.getPlayer().isGroupType()) {
                Group group = Bot.getInstance(functionSaveData.getPlayer().getBotId()).getGroup(functionSaveData.getPlayer().getGroupId());
                group.sendMessage(data);
            } else {
                log.info("【NOT_GROUP_PLAYER】: " + data);
            }
        } else {
            log.warning("gameShowData not found sessionId = " + sessionId);
        }
        
    }
}
