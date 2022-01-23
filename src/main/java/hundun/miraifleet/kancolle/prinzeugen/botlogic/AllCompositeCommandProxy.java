package hundun.miraifleet.kancolle.prinzeugen.botlogic;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.descriptor.CommandArgumentContext;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;

/**
 * 目前只能用唯一的CompositeCommand注册所有SubCommand，未来改为分别注册。
 * https://github.com/mamoe/mirai-console/issues/397
 * @author hundun
 * Created on 2021/08/11
 */
public class AllCompositeCommandProxy extends CompositeCommand {

    
    PrinzEugenBotLogic botLogic;
    
    
    public AllCompositeCommandProxy(
            PrinzEugenBotLogic botLogic, 
            JvmPlugin plugin,
            String characterName
            ) {
        super(plugin, characterName, new String[]{}, "我是" + characterName, plugin.getParentPermission(), CommandArgumentContext.EMPTY);
        this.botLogic = botLogic;
    }
    
    @SubCommand("查询报时")
    public void listHourlyChatConfig(CommandSender sender) {
        botLogic.reminderFunction.listHourlyChatConfig(sender);
    }
    
    @SubCommand("任务详情")
    public void detailQuest(CommandSender sender, String id) {
        botLogic.kcwikiFunction.detailQuest(sender, id);
    }
    
    @SubCommand("搜任务")
    public void searchQuest(CommandSender sender, String questKeyword) {
        botLogic.kcwikiFunction.searchQuest(sender, questKeyword);
    }
    
    @SubCommand("舰娘详情")
    public void quickSearchShipFromCommand(CommandSender sender, String shipName) {
        botLogic.kcwikiFunction.quickSearchShipFromCommand(sender, shipName);
    }
    
    @SubCommand("删除舰娘别名")
    public void deleteShipFuzzyName(CommandSender sender, String fuzzyName) {
        botLogic.kcwikiFunction.deleteShipFuzzyName(sender, fuzzyName);
    }
    
    @SubCommand("查询舰娘别名")
    public void listShipFuzzyName(CommandSender sender, String name) {
        botLogic.kcwikiFunction.listShipFuzzyName(sender, name);
    }
    
    @SubCommand("添加舰娘别名")
    public void addShipFuzzyName(CommandSender sender, String shipName, String fuzzyName) {
        botLogic.kcwikiFunction.addShipFuzzyName(sender, shipName, fuzzyName);
    }
    
    @SubCommand("微博订阅")
    public void listListen(CommandSender sender) {
        botLogic.weiboFunction.listListen(sender);
    }
    
    @SubCommand("最新微博")
    public void listTopSummary(CommandSender sender) {
        botLogic.weiboFunction.listTopSummary(sender);
    }
    
    @SubCommand("最新微博")
    public void listTopForUid(CommandSender sender, String name) {
        botLogic.weiboFunction.listTopForUid(sender, name);
    }

    @SubCommand("驾驶")
    public void chat(CommandSender sender, String messageCode) {
        botLogic.driveFunction.chat(sender, messageCode);
    }

}
