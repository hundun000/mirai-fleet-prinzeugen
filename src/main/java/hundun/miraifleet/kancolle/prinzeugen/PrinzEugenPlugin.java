package hundun.miraifleet.kancolle.prinzeugen;

import org.jetbrains.annotations.NotNull;

import hundun.miraifleet.kancolle.prinzeugen.botlogic.PrinzEugenBotLogic;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

/**
 * @author hundun
 * Created on 2021/08/09
 */
public class PrinzEugenPlugin extends JavaPlugin {
public static final PrinzEugenPlugin INSTANCE = new PrinzEugenPlugin(); 
    
    PrinzEugenBotLogic botLogic;
    
    public PrinzEugenPlugin() {
        super(new JvmPluginDescriptionBuilder(
                "hundun.fleet.prinzeugen",
                "0.1.0"
            )
            .build());
    }
    
    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        
    }
    
    @Override
    public void onEnable() {
        botLogic = new PrinzEugenBotLogic(this);
        botLogic.onBotLogicEnable();
    }
    
    @Override
    public void onDisable() {
        botLogic.onDisable();
        // 由GC回收即可
        botLogic = null;
    }
}
