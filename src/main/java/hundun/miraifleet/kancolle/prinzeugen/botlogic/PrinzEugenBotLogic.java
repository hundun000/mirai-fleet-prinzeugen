package hundun.miraifleet.kancolle.prinzeugen.botlogic;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.core.botlogic.BaseJavaBotLogic;
import hundun.miraifleet.framework.starter.botlogic.function.CharacterHelpFunction;
import hundun.miraifleet.framework.starter.botlogic.function.RepeatFunction;
import hundun.miraifleet.framework.starter.botlogic.function.drive.DriveFunction;
import hundun.miraifleet.framework.starter.botlogic.function.reminder.ReminderFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.WeiboFunction;
import hundun.miraifleet.image.share.function.SharedPetFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.PrinzEugenChatFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.PrinzEugenImageFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.idlegame.GameFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.KcwikiFunction;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * @author hundun
 * Created on 2021/08/09
 */
public class PrinzEugenBotLogic extends BaseJavaBotLogic {
    
    public PrinzEugenBotLogic(JavaPlugin plugin) {
        super(plugin, "欧根");
        
        SharedPetFunction sharedPetFunction = new SharedPetFunction(this, plugin, characterName);
        
        registerFunction(new PrinzEugenChatFunction(this, plugin, characterName));
        
        registerFunction(new PrinzEugenImageFunction(this, plugin, characterName)
                .lazyInitSharedFunction(sharedPetFunction));
        
        registerFunction(new KcwikiFunction(this, plugin, characterName));
        
        registerFunction(new RepeatFunction(this, plugin, characterName));
        
        registerFunction(new WeiboFunction(this, plugin, characterName, 
                PrinzEugenDefaultConfigAndData.weiboConfigDefaultDataSupplier()));

        registerFunction(new ReminderFunction(this, plugin, characterName, 
                null, 
                PrinzEugenDefaultConfigAndData.hourlyChatConfigDefaultDataSupplier()));

        registerFunction(new DriveFunction(this, plugin, characterName));

        registerFunction(new CharacterHelpFunction(this, plugin, characterName));
        
        
        allCompositeCommandProxy = new AllCompositeCommandProxy(this, plugin, characterName);
    }

}
