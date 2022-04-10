package hundun.miraifleet.kancolle.prinzeugen.botlogic.function;

import java.io.File;
import java.util.TimerTask;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.AsListenerHost;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.image.share.function.ImageCoreKt;
import lombok.Data;
import lombok.Getter;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;

public class PrinzEugenImageFunction extends BaseFunction<Void>{

    @Getter
    private final CompositeCommandFunctionComponent commandComponent;
    
    ImageCoreKt imageCoreKt = new ImageCoreKt();
    ExternalResource lianyebingExternalResource;
    
    public PrinzEugenImageFunction(
            BaseBotLogic baseBotLogic,
            JavaPlugin plugin,
            String characterName
            ) {
        super(
            baseBotLogic,
            plugin,
            characterName,
            "PrinzEugenImageFunction",
            null
            );
        this.commandComponent = new CompositeCommandFunctionComponent(plugin, characterName, functionName);
        initExternalResource();
    }

    private void initExternalResource() {
        try {
            lianyebingExternalResource = ExternalResource.create(resolveFunctionDataFile("北方指人.jpg"));
        } catch (Exception e) {
            plugin.getLogger().error("initExternalResource error: " + e.getMessage());
        }
    }


    @Override
    public AbstractCommand provideCommand() {
        return commandComponent;
    }

    public class CompositeCommandFunctionComponent extends AbstractCompositeCommandFunctionComponent {
        public CompositeCommandFunctionComponent(JvmPlugin plugin, String characterName, String functionName) {
            super(plugin, characterName, functionName, functionName);
        }
        
        @SubCommand("北方指人")
        public void lianyebing(CommandSender sender, String bottomText) {
            if (!checkCosPermission(sender)) {
                return;
            }
            ExternalResource externalResource = imageCoreKt.anyImageAddBottomText(bottomText, lianyebingExternalResource);
            FunctionReplyReceiver receiver = new FunctionReplyReceiver(sender, log);
            Message image = receiver.uploadImageOrNotSupportPlaceholder(externalResource);
            receiver.sendMessage(image);
        }
        
    }

}
