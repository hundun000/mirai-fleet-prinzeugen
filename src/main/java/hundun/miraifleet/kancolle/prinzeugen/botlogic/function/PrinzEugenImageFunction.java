package hundun.miraifleet.kancolle.prinzeugen.botlogic.function;

import java.awt.image.BufferedImage;
import java.util.List;
import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.function.BaseFunction;
import hundun.miraifleet.framework.core.function.FunctionReplyReceiver;
import hundun.miraifleet.image.share.function.ImageStableFunction;
import hundun.miraifleet.image.share.function.SharedPetFunction;
import lombok.Getter;
import net.mamoe.mirai.console.command.AbstractCommand;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.utils.ExternalResource;
import xmmt.dituon.share.ImageSynthesis;
import xmmt.dituon.share.TextData;

public class PrinzEugenImageFunction extends BaseFunction {

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
                "PrinzEugenImageFunction"
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
            super(plugin, botLogic, new UserLevelFunctionComponentConstructPack(characterName, functionName));
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
