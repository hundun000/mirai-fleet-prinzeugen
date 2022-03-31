package hundun.miraifleet.kancolle.prinzeugen;

import org.laolittle.plugin.SkikoMirai;

import hundun.miraifleet.kancolle.prinzeugen.PrinzEugenPlugin;
import net.mamoe.mirai.console.plugin.PluginManager;
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal;
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader;
/**
 * @author hundun
 * Created on 2021/06/03
 */
public class PrinzEugenPluginTest {
    public static void main(String[] args) throws InterruptedException {
        MiraiConsoleTerminalLoader.INSTANCE.startAsDaemon(new MiraiConsoleImplementationTerminal());
        
        PluginManager.INSTANCE.loadPlugin(SkikoMirai.INSTANCE);
        PluginManager.INSTANCE.loadPlugin(PrinzEugenPlugin.INSTANCE);
        PluginManager.INSTANCE.enablePlugin(SkikoMirai.INSTANCE);
        PluginManager.INSTANCE.enablePlugin(PrinzEugenPlugin.INSTANCE);
    }
}
