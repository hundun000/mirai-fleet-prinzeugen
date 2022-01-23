package hundun.miraifleet.kancolle.prinzeugen.botlogic;

import hundun.miraifleet.framework.core.botlogic.BaseBotLogic;
import hundun.miraifleet.framework.starter.botlogic.function.RepeatFunction;
import hundun.miraifleet.framework.starter.botlogic.function.reminder.ReminderFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.WeiboFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.PrinzEugenChatFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.idlegame.GameFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.KcwikiFunction;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * @author hundun
 * Created on 2021/08/09
 */
public class PrinzEugenBotLogic extends BaseBotLogic {

    PrinzEugenChatFunction prinzEugenChatFunction;
    KcwikiFunction kcwikiFunction;
    GameFunction gameFunction;
    
    RepeatFunction repeatFunction;
    
    WeiboFunction weiboFunction;
    ReminderFunction reminderFunction;
    
    AllCompositeCommandProxy allCompositeCommandProxy;
    
    public PrinzEugenBotLogic(JvmPlugin plugin) {
        super(plugin, "欧根");
        
        prinzEugenChatFunction = new PrinzEugenChatFunction(this, plugin, characterName);
        functions.add(prinzEugenChatFunction);
        
        kcwikiFunction = new KcwikiFunction(this, plugin, characterName);
        functions.add(kcwikiFunction);
        
        repeatFunction = new RepeatFunction(this, plugin, characterName);
        functions.add(repeatFunction);
        
        weiboFunction = new WeiboFunction(this, plugin, characterName);
        functions.add(weiboFunction);
        
        reminderFunction = new ReminderFunction(this, plugin, characterName);
        functions.add(reminderFunction);
        
//        gameFunction = new GameFunction(this, plugin, characterName);
//        functions.add(gameFunction);
        
        allCompositeCommandProxy = new AllCompositeCommandProxy(this, plugin, characterName);
    }
    
    @Override
    public void onBotLogicEnable() {
        super.onBotLogicEnable();
        
        CommandManager.INSTANCE.registerCommand(allCompositeCommandProxy, false);
    }
    

}
