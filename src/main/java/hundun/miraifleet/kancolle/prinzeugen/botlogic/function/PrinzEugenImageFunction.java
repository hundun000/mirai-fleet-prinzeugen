package hundun.miraifleet.kancolle.prinzeugen.botlogic.function;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.AsListenerHost;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.framework.core.function.BaseFunction.AbstractCompositeCommandFunctionComponent;
import hundun.miraifleet.image.share.function.ImageStableFunction;
import hundun.miraifleet.image.share.function.SharedPetFunction;
import hundun.miraifleet.image.share.function.ImageStableFunction.CompositeCommandFunctionComponent;
import lombok.Data;
import lombok.Getter;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.ExternalResource;
import xmmt.dituon.share.ImageSynthesis;
import xmmt.dituon.share.TextData;

import org.jetbrains.annotations.NotNull;

public class PrinzEugenImageFunction extends BaseFunction<Void>{

    @Getter
    private final CompositeCommandFunctionComponent commandComponent;

    private SharedPetFunction petFunction;

    public PrinzEugenImageFunction(
            BaseBotLogic baseBotLogic,
            JvmPlugin plugin,
            String characterName
    ) {
        super(
                baseBotLogic,
                plugin,
                characterName,
                "PrinzEugenImageFunction",
                null
        );
        this.commandComponent = new CompositeCommandFunctionComponent();
    }

    public PrinzEugenImageFunction lazyInitSharedFunction(SharedPetFunction petFunction) {
        this.petFunction = petFunction;
        return this;
    }

    @Override
    public AbstractCommand provideCommand() {
        return commandComponent;
    }

    public class CompositeCommandFunctionComponent extends AbstractCompositeCommandFunctionComponent {
        public CompositeCommandFunctionComponent() {
            super(plugin, botLogic, characterName, functionName);
        }
        
        @SubCommand("北方指人")
        public void lianyebing(CommandSender sender, String text) {
            imageGeneral(sender, null, "北方指人", ImageStableFunction.textDataForLianyebing(text));
        }
        
        @SubCommand("摸")
        public void petpet(CommandSender sender, User target) {
            imageGeneral(sender, target, "petpet", null);
        }

        private void imageGeneral(CommandSender sender, User target, String key, List<TextData> additionTextDatas) {
            if (!checkCosPermission(sender)) {
                return;
            }
            BufferedImage fromAvatarImage = (sender != null && sender.getUser() != null) ? ImageSynthesis.getAvatarImage(sender.getUser().getAvatarUrl()) : null;
            BufferedImage toAvatarImage = target != null ? ImageSynthesis.getAvatarImage(target.getAvatarUrl()) : null;
            FunctionReplyReceiver receiver = new FunctionReplyReceiver(sender, plugin.getLogger());
            var resultFile = petFunction.petService(fromAvatarImage, toAvatarImage, key, additionTextDatas);
            if (resultFile != null) {
                ExternalResource externalResource = ExternalResource.create(resultFile).toAutoCloseable();
                Message message = receiver.uploadImageAndCloseOrNotSupportPlaceholder(externalResource);
                receiver.sendMessage(message);
            }
        }





    }

}
