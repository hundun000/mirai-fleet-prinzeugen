package hundun.miraifleet.kancolle.prinzeugen.botlogic;

import hundun.miraifleet.framework.core.function.AbstractAllCompositeCommandProxy;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.CompositeCommand;
import net.mamoe.mirai.console.command.descriptor.CommandArgumentContext;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;

/**
 * 目前只能用唯一的CompositeCommand注册所有SubCommand，未来改为分别注册。
 * https://github.com/mamoe/mirai-console/issues/397
 * @author hundun
 * Created on 2021/08/11
 */
public class AllCompositeCommandProxy extends AbstractAllCompositeCommandProxy<PrinzEugenBotLogic> {


    
    public AllCompositeCommandProxy(
            PrinzEugenBotLogic botLogic, 
            JvmPlugin plugin,
            String characterName
            ) {
        super(botLogic, plugin, characterName);
    }
    
    @SubCommand("查询报时")
    public void listHourlyChatConfig(CommandSender sender) {
        botLogic.reminderFunction.getCommandComponent().listHourlyChatConfig(sender);
    }
    
    @SubCommand("载入任务数据文件")
    public void loadQuestFiles(CommandSender sender) {
        botLogic.kcwikiFunction.getCommandComponent().loadQuestFiles(sender);
    }
    
    @SubCommand("任务详情")
    public void detailQuest(CommandSender sender, String id) {
        botLogic.kcwikiFunction.getCommandComponent().detailQuest(sender, id);
    }
    
    @SubCommand("搜任务")
    public void searchQuest(CommandSender sender, String questKeyword) {
        botLogic.kcwikiFunction.getCommandComponent().searchQuest(sender, questKeyword);
    }
    
    @SubCommand("舰娘详情")
    public void quickSearchShipFromCommand(CommandSender sender, String shipName) {
        botLogic.kcwikiFunction.getCommandComponent().quickSearchShipFromCommand(sender, shipName);
    }
    
    @SubCommand("删除舰娘别名")
    public void deleteShipFuzzyName(CommandSender sender, String fuzzyName) {
        botLogic.kcwikiFunction.getCommandComponent().deleteShipFuzzyName(sender, fuzzyName);
    }
    
    @SubCommand("查询舰娘别名")
    public void listShipFuzzyName(CommandSender sender, String name) {
        botLogic.kcwikiFunction.getCommandComponent().listShipFuzzyName(sender, name);
    }
    
    @SubCommand("添加舰娘别名")
    public void addShipFuzzyName(CommandSender sender, String fuzzyName, String shipName) {
        botLogic.kcwikiFunction.getCommandComponent().addShipFuzzyName(sender, fuzzyName, shipName);
    }
    
    @SubCommand("微博订阅")
    public void listListen(CommandSender sender) {
        botLogic.weiboFunction.getCommandComponent().listListen(sender);
    }
    
    @SubCommand("最新微博")
    public void listTopSummary(CommandSender sender) {
        botLogic.weiboFunction.getCommandComponent().listTopSummary(sender);
    }
    
    @SubCommand("最新微博")
    public void listTopForUid(CommandSender sender, String name) {
        botLogic.weiboFunction.getCommandComponent().listTopForUid(sender, name);
    }

    @SubCommand("立刻私聊")
    public void chat(CommandSender sender, User target, String messageCode) {
        botLogic.driveFunction.getCommandComponent().chat(sender, target, messageCode);
    }
    
    @SubCommand("立刻群聊")
    public void chat(CommandSender sender, Group target, String messageCode) {
        botLogic.driveFunction.getCommandComponent().chat(sender, target, messageCode);
    }
    
    @SubCommand("help")
    public void help(CommandSender sender) {
        botLogic.characterHelpFunction.getCommandComponent().help(sender);
    }
    
    @SubCommand("北方指人")
    public void lianyebing(CommandSender sender, String text) {
        botLogic.prinzEugenImageFunction.getCommandComponent().lianyebing(sender, text);
    }
    
    @SubCommand("94")
    public void jiusi(CommandSender sender, User target, String text) {
        botLogic.prinzEugenImageFunction.getCommandComponent().jiusi(sender, target, text);
    }
    
    @SubCommand("摸")
    public void patpat(CommandSender sender, User target) {
        botLogic.prinzEugenImageFunction.getCommandComponent().patpat(sender, target);
    }

}
