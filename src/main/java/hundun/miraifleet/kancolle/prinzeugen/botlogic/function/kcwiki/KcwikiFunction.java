package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.AsListenerHost;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.helper.file.CacheableFileHelper;
import hundun.miraifleet.framework.helper.repository.MapDocumentRepository;
import hundun.miraifleet.framework.helper.repository.SingletonDocumentRepository;
import hundun.miraifleet.framework.starter.helper.feign.FeignClientFactory;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.config.ShipFuzzyNameConfig;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.dto.KcwikiShipDetail;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.dto.WhoCallsTheFleetItem;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.model.ShipInfo;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.model.ShipUpgradeLink;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.feign.KcwikiApiFeignClient;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.oldversion.OldKcwikiQuestData;
import lombok.Getter;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/08/10
 */
@AsListenerHost
public class KcwikiFunction extends BaseFunction {
    public static String kancolleGameDataSubFolder =  "GameData";
    public static String questSubFolder =  "quest_old";
    public static String itemFile = "WhoCallsTheFleet-DB/items.nedb";
    
    private final KcwikiService kcwikiService;
    private final KcwikiOldQuestService kcwikiOldQuestService;
    private final WhoCallsTheFleetService whoCallsTheFleetService;
    
    private final SingletonDocumentRepository<ShipFuzzyNameConfig> shipFuzzyNameConfigRepository;
    
    
    
    @Getter
    private final CompositeCommandFunctionComponent commandComponent;
    
    public KcwikiFunction(
        BaseBotLogic botLogic,
        JvmPlugin plugin, 
        String characterName
        ) {
    super(
            botLogic,
            plugin, 
            characterName, 
            "KcwikiFunction"
            );
        this.kcwikiService = new KcwikiService(
                FeignClientFactory.get(KcwikiApiFeignClient.class, "http://api.kcwiki.moe", plugin.getLogger()),
                new CacheableFileHelper(resolveFunctionCacheFileFolder(), plugin.getLogger())
                );
        this.shipFuzzyNameConfigRepository = new SingletonDocumentRepository<>(
                plugin, 
                resolveFunctionConfigFile("ShipFuzzyNameConfig.json"), 
                ShipFuzzyNameConfig.class,
                () -> ShipFuzzyNameConfig.builder()
                        .map(new HashMap<>())
                        .build()
                );
        this.kcwikiOldQuestService = new KcwikiOldQuestService(
                new MapDocumentRepository<>(
                        plugin, 
                        resolveDataRepositoryFile("OldKcwikiQuestData.json"), 
                        OldKcwikiQuestData.class, 
                        (item -> item.getId()), 
                        ((item, id) -> item.setId(id))
                        ),
                plugin.getLogger()
                );
        this.whoCallsTheFleetService = new WhoCallsTheFleetService(
                new MapDocumentRepository<>(
                        plugin, 
                        resolveDataRepositoryFile("WhoCallsTheFleetItemData.json"), 
                        WhoCallsTheFleetItem.class, 
                        (item -> String.valueOf(item.getId())), 
                        ((item, id) -> item.setId(Integer.valueOf(id)))
                        ),
                plugin.getLogger()
                );
        this.commandComponent = new CompositeCommandFunctionComponent();
    }
    
    @Override
    public AbstractCommand provideCommand() {
        return commandComponent;
    }

    public class CompositeCommandFunctionComponent extends AbstractCompositeCommandFunctionComponent {
        public CompositeCommandFunctionComponent() {
            super(plugin, botLogic, new UserLevelFunctionComponentConstructPack(characterName, functionName));
        }
        
        @SubCommand("载入任务数据文件")
        public void loadQuestFiles(CommandSender sender) {
            if (!checkCosPermission(sender)) {
                return;
            }
            File folder = plugin.resolveDataFile(functionName + File.separator + questSubFolder);
            String result = kcwikiOldQuestService.loadQuestFiles(folder);
            sender.sendMessage(result);
        }
        
        @SubCommand("载入装备数据文件")
        public void loadItemFiles(CommandSender sender) {
            if (!checkCosPermission(sender)) {
                return;
            }
            File file = plugin.resolveDataFile(functionName + File.separator + itemFile);
            String result = whoCallsTheFleetService.loadItemFiles(file);
            sender.sendMessage(result);
        }
        
        @SubCommand("任务详情")
        public void detailQuest(CommandSender sender, String id) {
            if (!checkCosPermission(sender)) {
                return;
            }
            
            OldKcwikiQuestData questData = kcwikiOldQuestService.findById(id);
            if (questData != null) {
                sender.sendMessage(questData.getChinese_detail());
            } else {
                sender.sendMessage("未找到该任务");
            }
            
        }
        
        @SubCommand("搜任务")
        public void searchQuest(CommandSender sender, String questKeyword) {
            if (!checkCosPermission(sender)) {
                return;
            }
            var functionReplyReceiver = new FunctionReplyReceiver(sender, plugin.getLogger());
            String result = kcwikiOldQuestService.searchQuest(questKeyword);
            functionReplyReceiver.sendMessage(result);
        }


        @SubCommand("舰娘详情")
        public void quickSearchShipFromCommand(CommandSender sender, String shipName) {
            if (!checkCosPermission(sender)) {
                return;
            }
            searchShip(new FunctionReplyReceiver(sender, plugin.getLogger()), shipName);
        }
        
        @SubCommand("查询舰娘别名")
        public void listShipFuzzyName(CommandSender sender, String name) {
            if (!checkCosPermission(sender)) {
                return;
            }
            StringBuilder builder = new StringBuilder();
            ShipFuzzyNameConfig config = shipFuzzyNameConfigRepository.findSingleton();
            config.getMap().entrySet().forEach(entry -> {
                if (entry.getKey().equals(name) || entry.getValue().equals(name)) {
                    builder.append(entry.getKey()).append("-->").append(entry.getValue()).append("\n");
                }
            });
            if (builder.length() > 0) {
                sender.sendMessage(builder.toString());
            } else {
                sender.sendMessage("“" +name + "”没有相关别名");
            }
        }
        
        @SubCommand("删除舰娘别名")
        public void deleteShipFuzzyName(CommandSender sender, String fuzzyName) {
            if (!checkCosPermission(sender)) {
                return;
            }
            ShipFuzzyNameConfig config = shipFuzzyNameConfigRepository.findSingleton();
            config.getMap().entrySet().removeIf(entry -> entry.getKey().equals(fuzzyName));
            sender.sendMessage("已删除");
        }
        
       
        @SubCommand("添加舰娘别名")
        public void addShipFuzzyName(CommandSender sender, 
                @Name("fuzzyName") String fuzzyName, 
                @Name("shipName") String shipName) {
            if (!checkCosPermission(sender)) {
                return;
            }
            ShipFuzzyNameConfig config = shipFuzzyNameConfigRepository.findSingleton();
            
            boolean isValidShipName = false;
            if (config.getMap().values().contains(shipName)) {
                isValidShipName = true;
            } else {
                KcwikiShipDetail shipDetail = kcwikiService.getShipDetail(shipName);
                if (shipDetail != null && shipDetail.getChinese_name() != null) {
                    isValidShipName = true;
                }
            }
            
            if (isValidShipName) {
                config.getMap().put(fuzzyName, shipName);
                shipFuzzyNameConfigRepository.saveSingleton(config);
                sender.sendMessage("已添加");
            } else {
                sender.sendMessage("“" +shipName + "”不是标准的舰娘名，无法添加别名");
            }
        }
    }
    

    
    
    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception { 
        if (!checkCosPermission(event)) {
            return;
        }
        String messageString = event.getMessage().contentToString();
        if (messageString.endsWith(".") && messageString.length() > 1) {
            String shipName = messageString.substring(0, messageString.length() - 1);
            searchShip(new FunctionReplyReceiver(event.getGroup(), plugin.getLogger()), shipName);
        }
    }
    
    private void searchShip(FunctionReplyReceiver subject, String shipName) {
        ShipFuzzyNameConfig config = shipFuzzyNameConfigRepository.findSingleton();

        shipName = config.getMap().getOrDefault(shipName, shipName);
        log.info("shipName = " + shipName);
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        ShipUpgradeLink upgradeLink = kcwikiService.getShipUpgradeLine(shipName);
        if (upgradeLink != null && !upgradeLink.getUpgradeLinkIds().isEmpty()) {
            //chainBuilder.add(new PlainText("\n\n"));
            
            StringBuilder urls = new StringBuilder();
            {
                ShipInfo detail = upgradeLink.getShipDetails().get(upgradeLink.getUpgradeLinkIds().get(0));
                String name = detail.getChineseName();
                String urlEncodedArg;
                try {
                    urlEncodedArg = URLEncoder.encode(
                            name,
                            java.nio.charset.StandardCharsets.UTF_8.toString()
                          );
                } catch (UnsupportedEncodingException e) {
                    plugin.getLogger().warning("Urlencolde fail: " + name);
                    urlEncodedArg = name;
                }
                String wikiUrl = "https://zh.kcwiki.cn/wiki/" + urlEncodedArg;
                urls.append(name).append(" ").append(wikiUrl).append("\n");
            }
            chainBuilder.add(new PlainText(urls.toString()));
            

            StringBuilder nameLink = new StringBuilder();
            for (int i = 0; i < upgradeLink.getUpgradeLinkIds().size(); i++) {
                int id = upgradeLink.getUpgradeLinkIds().get(i);
                ShipInfo detail = upgradeLink.getShipDetails().get(id);
                whoCallsTheFleetService.tryFillInitItem(detail);
                nameLink.append(detail.toSimpleText()).append("\n");
                boolean hasNext = detail.getAfterLv() > 0 && i != upgradeLink.getUpgradeLinkIds().size() - 1;
                if (hasNext) {
                    nameLink.append("-").append(detail.getAfterLv()).append("级->");
                }
            }
            nameLink.append("\n");
            chainBuilder.add(new PlainText(nameLink.toString()));
            
            int firstId = upgradeLink.getUpgradeLinkIds().get(0);
            ShipInfo firstDetail = upgradeLink.getShipDetails().get(firstId);
            String fileId = String.valueOf(firstDetail.getId());
            File rawDataFolder = plugin.resolveDataFile(functionName + File.separator + kancolleGameDataSubFolder);
            File imageFile = kcwikiService.fromCacheOrDownloadOrFromLocal(fileId, rawDataFolder);
            if (imageFile != null) {
                ExternalResource externalResource = ExternalResource.create(imageFile).toAutoCloseable();
                Image image = subject.uploadImageAndClose(externalResource);
                if (image != null) {
                    chainBuilder.add(image);
                } else {
                    chainBuilder.add(NOT_SUPPORT_RESOURCE_PLACEHOLDER);
                }
            } else {
                plugin.getLogger().info("shipDetail no imageFile");
            }
            
        } else {
            plugin.getLogger().info("no shipDetail");
        }
        
        if (!chainBuilder.isEmpty()) {
            subject.sendMessage(chainBuilder.build());
        } 
    }



    
    
    
    

}
