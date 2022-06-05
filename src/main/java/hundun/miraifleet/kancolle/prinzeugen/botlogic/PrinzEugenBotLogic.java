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

    PrinzEugenChatFunction prinzEugenChatFunction;
    PrinzEugenImageFunction prinzEugenImageFunction;
    KcwikiFunction kcwikiFunction;
    //GameFunction gameFunction;
    
    RepeatFunction repeatFunction;
    WeiboFunction weiboFunction;
    ReminderFunction reminderFunction;
    DriveFunction driveFunction;
    CharacterHelpFunction characterHelpFunction;
    
    public PrinzEugenBotLogic(JavaPlugin plugin) {
        super(plugin, "欧根");
        
        SharedPetFunction sharedPetFunction = new SharedPetFunction(this, plugin, characterName);
        
        prinzEugenChatFunction = new PrinzEugenChatFunction(this, plugin, characterName);
        functions.add(prinzEugenChatFunction);
        
        prinzEugenImageFunction = new PrinzEugenImageFunction(this, plugin, characterName);
        prinzEugenImageFunction.lazyInitSharedFunction(sharedPetFunction);
        functions.add(prinzEugenImageFunction);
        
        kcwikiFunction = new KcwikiFunction(this, plugin, characterName);
        functions.add(kcwikiFunction);
        
        repeatFunction = new RepeatFunction(this, plugin, characterName);
        functions.add(repeatFunction);
        
        weiboFunction = new WeiboFunction(this, plugin, characterName, 
                PrinzEugenDefaultConfigAndData.weiboConfigDefaultDataSupplier());
        functions.add(weiboFunction);
        
        reminderFunction = new ReminderFunction(this, plugin, characterName, 
                null, 
                PrinzEugenDefaultConfigAndData.hourlyChatConfigDefaultDataSupplier());
        functions.add(reminderFunction);
        
//        gameFunction = new GameFunction(this, plugin, characterName);
//        functions.add(gameFunction);
        driveFunction = new DriveFunction(this, plugin, characterName);
        functions.add(driveFunction);
        
        characterHelpFunction = new CharacterHelpFunction(this, plugin, characterName);
        functions.add(characterHelpFunction);
        
        allCompositeCommandProxy = new AllCompositeCommandProxy(this, plugin, characterName);
    }

}
