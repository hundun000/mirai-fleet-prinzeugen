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
        this.commandComponent = new CompositeCommandFunctionComponent(plugin, characterName, functionName);
    }

    public void lazyInitSharedFunction(SharedPetFunction petFunction) {
        this.petFunction = petFunction;
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
        public void lianyebing(CommandSender sender, String text) {
            imageGeneral(sender, null, "北方指人", textDataForLianyebing(text));
        }
        
        private List<TextData> textDataForLianyebing(String text) {
            int fullWidth = 392;
            int board = 20;
            int textWidth = fullWidth - board * 2;
            int fontSizeByHeight = 40;
            int fontSizeByWidth = (int) (textWidth * 1.0 / text.length());
            int fontSize = Math.min(fontSizeByWidth, fontSizeByHeight);
            int x = (fullWidth / 2) - (text.length() * fontSize / 2);
            return Arrays.asList(new TextData(
                    text,
                    Arrays.asList(x, 300),
                    null,
                    null,
                    fontSize
            ));
        }
        
        @SubCommand("94")
        public void jiusi(CommandSender sender, User target, String text) {
            imageGeneral(sender, target, "94", textDataForJiusi("请问你们看到" + text + "？"));
        }
        
        private List<TextData> textDataForJiusi(String text) {
            int fullWidth = 1080;
            int board = 50;
            int textWidth = fullWidth - board * 2;
            int fontSizeByHeight = 150;
            int fontSizeByWidth = (int) (textWidth * 1.0 / text.length());
            int fontSize = Math.min(fontSizeByWidth, fontSizeByHeight);
            int x = (fullWidth / 2) - (text.length() * fontSize / 2);
            return Arrays.asList(new TextData(
                    text,
                    Arrays.asList(x, 50),
                    null,
                    null,
                    fontSize
            ));
        }

        @SubCommand("摸")
        public void patpat(CommandSender sender, User target) {
            imageGeneral(sender, target, "patpat", null);
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
