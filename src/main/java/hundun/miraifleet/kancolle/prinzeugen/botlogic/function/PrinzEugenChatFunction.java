package hundun.miraifleet.kancolle.prinzeugen.botlogic.function;

import java.io.File;
import org.jetbrains.annotations.NotNull;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.AsListenerHost;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.AbstractMessageEvent;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/08/09
 */
@AsListenerHost
public class PrinzEugenChatFunction extends BaseFunction {

    public PrinzEugenChatFunction(
            BaseBotLogic botLogic,
            JvmPlugin plugin, 
            String characterName
            ) {
        super(
                botLogic,
                plugin, 
                characterName, 
                "PrinzEugenChatFunction"
                );
        initExternalResource();
    }
    
    File pupuExternalResource;
    File xiuXiuXiuVoiceExternalResource;
    
    
    private void initExternalResource() {
        try {
            pupuExternalResource = plugin.resolveDataFile(functionName + File.separator + "噗噗.jpg");
            xiuXiuXiuVoiceExternalResource = plugin.resolveDataFile(functionName + File.separator + "咻咻咻.amr");
        } catch (Exception e) {
            plugin.getLogger().error("initExternalResource error: " + e.getMessage());
        }
    }
    
    
    @EventHandler
    public void onMessage(@NotNull AbstractMessageEvent event) throws Exception { 
        if (!checkCosPermission(event)) {
            return;
        }
        chat(new FunctionReplyReceiver(event.getSubject(), plugin.getLogger()), event.getMessage().contentToString());
        
    }
    
    private void chat(FunctionReplyReceiver subject, String message) {
        
        if (message.contains("噗噗")) {
            Image image = subject.uploadImageAndClose(ExternalResource.create(pupuExternalResource));
            if (image != null) {
                subject.sendMessage(
                        new PlainText("")
                        .plus(image)
                        );
            } else {
                subject.sendMessage("你根本不是噗噗");
            }
        } else if (message.contains("咻咻咻") || message.contains("西姆咻")) {
            Audio voice = subject.uploadVoiceAndClose(ExternalResource.create(xiuXiuXiuVoiceExternalResource));
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


    @Override
    public AbstractCommand provideCommand() {
        return null;
    }
    

}
