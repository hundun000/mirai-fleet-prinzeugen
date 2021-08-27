package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.AsCommand;
import hundun.miraifleet.framework.core.function.AsListenerHost;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.core.helper.feign.FeignClientFactory;
import hundun.miraifleet.framework.core.helper.repository.PluginConfigRepository;
import hundun.miraifleet.framework.core.helper.repository.PluginDataRepository;
import hundun.miraifleet.framework.starter.botlogic.function.reminder.config.HourlyChatConfig;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.PrinzEugenBotLogic;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.config.ShipFuzzyNameConfig;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.dto.KcwikiShipDetail;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.model.ShipInfo;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.model.ShipUpgradeLink;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.feign.KcwikiApiFeignClient;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/08/10
 */
@AsCommand
@AsListenerHost
public class KcwikiFunction extends BaseFunction<Void> {

    final KcwikiService kcwikiService;
    
    private final PluginConfigRepository<ShipFuzzyNameConfig> shipFuzzyNameConfigRepository;
    
        public KcwikiFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "KcwikiFunction", 
                null
                );
            this.kcwikiService = new KcwikiService(FeignClientFactory.get(KcwikiApiFeignClient.class, "http://api.kcwiki.moe", plugin.getLogger()));
            this.shipFuzzyNameConfigRepository = new PluginConfigRepository<>(plugin, resolveFunctionConfigFile("ShipFuzzyNameConfig.json"), ShipFuzzyNameConfig.class);

            // check config
            ShipFuzzyNameConfig config = shipFuzzyNameConfigRepository.findSingleton();
            if (config == null) {
                config = new ShipFuzzyNameConfig();
                config.setMap(new HashMap<>());
            }
            shipFuzzyNameConfigRepository.saveSingleton(config);
        }
    
    
    
    @SubCommand("舰娘百科")
    public void quickSerachFromCommand(CommandSender sender, String shipName) {
        if (!checkCosPermission(sender)) {
            return;
        }
        quickSerach(new FunctionReplyReceiver(sender, plugin.getLogger()), shipName);
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
    }
    
   
    @SubCommand("添加舰娘别名")
    public void addShipFuzzyName(CommandSender sender, String shipName, String fuzzyName) {
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
    
    
    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception { 
        if (!checkCosPermission(event)) {
            return;
        }
        String messageString = event.getMessage().contentToString();
        if (messageString.endsWith(".") && messageString.length() > 1) {
            String shipName = messageString.substring(0, messageString.length() - 1);
            quickSerach(new FunctionReplyReceiver(event.getGroup(), plugin.getLogger()), shipName);
        }
    }
    
    private void quickSerach(FunctionReplyReceiver subject, String shipName) {
        ShipFuzzyNameConfig config = shipFuzzyNameConfigRepository.findSingleton();

        shipName = config.getMap().getOrDefault(shipName, shipName);
        log.info("shipName = " + shipName);
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        ShipUpgradeLink upgradeLink = kcwikiService.getShipUpgradeLine(shipName);
        if (upgradeLink != null && !upgradeLink.getUpgradeLinkIds().isEmpty()) {
            chainBuilder.add(new PlainText("\n\n"));
            
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
            File cacheFolder = resolveFunctionCacheFileFolder();
            File rawDataFolder = plugin.resolveDataFile(functionName + File.separator + KcwikiService.kancolleGameDataSubFolder);
            File imageFile = kcwikiService.fromCacheOrDownloadOrFromLocal(fileId, cacheFolder, rawDataFolder);
            if (imageFile != null) {
                ExternalResource externalResource = ExternalResource.create(imageFile);
                Image image = subject.uploadImageAndClose(externalResource);
                if (image != null) {
                    chainBuilder.add(image);
                } else {
                    chainBuilder.add("[该终端不支持图片]");
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
