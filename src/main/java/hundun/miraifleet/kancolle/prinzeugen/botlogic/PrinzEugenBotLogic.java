package hundun.miraifleet.kancolle.prinzeugen.botlogic;

import hundun.miraifleet.framework.core.botlogic.BaseJavaBotLogic;
import hundun.miraifleet.framework.starter.botlogic.function.RepeatFunction;
import hundun.miraifleet.framework.starter.botlogic.function.character.CharacterAdminHelperFunction;
import hundun.miraifleet.framework.starter.botlogic.function.character.CharacterHelpFunction;
import hundun.miraifleet.framework.starter.botlogic.function.drive.DriveFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.WeiboFunction;
import hundun.miraifleet.image.share.function.SharedPetFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.PrinzEugenChatFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.PrinzEugenImageFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.KcwikiFunction;
import hundun.miraifleet.reminder.share.function.reminder.ReminderFunction;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;

/**
 * @author hundun
 * Created on 2021/08/09
 */
public class PrinzEugenBotLogic extends BaseJavaBotLogic {
    
    public PrinzEugenBotLogic(JavaPlugin plugin) {
        super(plugin, "欧根");
        
        
    }

    @Override
    protected void onFunctionsEnable() {
        SharedPetFunction sharedPetFunction = new SharedPetFunction(this, plugin, characterName);
        
        registerFunction(new PrinzEugenChatFunction(this, plugin, characterName));
        
        registerFunction(new PrinzEugenImageFunction(this, plugin, characterName)
                .lazyInitSharedFunction(sharedPetFunction));
        
        registerFunction(new KcwikiFunction(this, plugin, characterName));
        
        registerFunction(new RepeatFunction(this, plugin, characterName));
        
        registerFunction(new WeiboFunction(this, plugin, characterName, 
                PrinzEugenDefaultConfigAndData.weiboConfigDefaultDataSupplier()));

        registerFunction(new ReminderFunction(this, plugin, characterName, 
                PrinzEugenDefaultConfigAndData.reminderListDefaultDataSupplier()));

        registerFunction(new DriveFunction(this, plugin, characterName));

        registerFunction(new CharacterHelpFunction(this, plugin, characterName));
        
        registerFunction(new CharacterAdminHelperFunction(this, plugin, characterName));
        
        allCompositeCommandProxy = new AllCompositeCommandProxy(this, plugin, characterName);
        
    }

}
