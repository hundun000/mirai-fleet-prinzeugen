package hundun.miraifleet.kancolle.prinzeugen.botlogic.function;

import java.io.File;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.AsCommand;
import hundun.miraifleet.framework.core.function.AsListenerHost;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.starter.botlogic.function.RepeatFunction.SessionData;

import net.mamoe.mirai.console.command.CommandSender;

import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/08/09
 */
@AsCommand
@AsListenerHost
public class PrinzEugenChatFunction extends BaseFunction<Void> {

    public PrinzEugenChatFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "PrinzEugenChatFunction", 
                null
                );
        initExternalResource();
    }
    
    ExternalResource pupuExternalResource;
    ExternalResource xiuXiuXiuVoiceExternalResource;
    
    
    private void initExternalResource() {
        try {
            pupuExternalResource = ExternalResource.create(plugin.resolveDataFile(functionName + File.separator + "噗噗.jpg"));
            xiuXiuXiuVoiceExternalResource = ExternalResource.create(plugin.resolveDataFile(functionName + File.separator + "咻咻咻.amr"));
        } catch (Exception e) {
            plugin.getLogger().error("open cannotRelaxImage error: " + e.getMessage());
        }
    }
    
    
    @SubCommand("测试闲聊")
    public void chatFromCommand(CommandSender sender, String testText) {
        if (!checkCosPermission(sender)) {
            return;
        }
        chat(new FunctionReplyReceiver(sender, plugin.getLogger()), testText);
    }
    
    
    @EventHandler
    public void onMessage(@NotNull GroupMessageEvent event) throws Exception { 
        if (!checkCosPermission(event)) {
            return;
        }
        chat(new FunctionReplyReceiver(event.getGroup(), plugin.getLogger()), event.getMessage().contentToString());
        
    }
    
    private void chat(FunctionReplyReceiver subject, String message) {
        
        if (message.contains("噗噗")) {
            Image image = subject.uploadImage(pupuExternalResource);
            if (image != null) {
                subject.sendMessage(
                        new PlainText("")
                        .plus(image)
                        );
            } else {
                subject.sendMessage("你根本不是噗噗");
            }
        } else if (message.contains("咻咻咻") || message.contains("西姆咻")) {
            Voice voice = subject.uploadVoice(xiuXiuXiuVoiceExternalResource);
            if (voice != null) {
                subject.sendMessage(
                        new PlainText("")
                        .plus(voice)
                        );
            } else {
                subject.sendMessage("西姆咻咻咻");
            }
        }
    }
    

}
