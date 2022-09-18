package hundun.miraifleet.kancolle.prinzeugen.botlogic;

import hundun.miraifleet.framework.core.function.AbstractAllCompositeCommandProxy;
import hundun.miraifleet.framework.starter.botlogic.function.CharacterHelpFunction;
import hundun.miraifleet.framework.starter.botlogic.function.drive.DriveFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.WeiboFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.PrinzEugenImageFunction;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.KcwikiFunction;
import net.mamoe.mirai.console.command.CommandSender;
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
    
    @SubCommand("载入任务数据文件")
    public void loadQuestFiles(CommandSender sender) {
        botLogic.getFunction(KcwikiFunction.class).getCommandComponent().loadQuestFiles(sender);
    }
    
    @SubCommand("载入装备数据文件")
    public void loadItemFiles(CommandSender sender) {
        botLogic.getFunction(KcwikiFunction.class).getCommandComponent().loadItemFiles(sender);
    }
    
    @SubCommand("任务详情")
    public void detailQuest(CommandSender sender, String id) {
        botLogic.getFunction(KcwikiFunction.class).getCommandComponent().detailQuest(sender, id);
    }
    
    @SubCommand("搜任务")
    public void searchQuest(CommandSender sender, String questKeyword) {
        botLogic.getFunction(KcwikiFunction.class).getCommandComponent().searchQuest(sender, questKeyword);
    }
    
    @SubCommand("舰娘详情")
    public void quickSearchShipFromCommand(CommandSender sender, String shipName) {
        botLogic.getFunction(KcwikiFunction.class).getCommandComponent().quickSearchShipFromCommand(sender, shipName);
    }
    
    @SubCommand("删除舰娘别名")
    public void deleteShipFuzzyName(CommandSender sender, String fuzzyName) {
        botLogic.getFunction(KcwikiFunction.class).getCommandComponent().deleteShipFuzzyName(sender, fuzzyName);
    }
    
    @SubCommand("查询舰娘别名")
    public void listShipFuzzyName(CommandSender sender, String name) {
        botLogic.getFunction(KcwikiFunction.class).getCommandComponent().listShipFuzzyName(sender, name);
    }
    
    @SubCommand("添加舰娘别名")
    public void addShipFuzzyName(CommandSender sender, 
            @Name("fuzzyName") String fuzzyName, 
            @Name("shipName") String shipName) {
        botLogic.getFunction(KcwikiFunction.class).getCommandComponent().addShipFuzzyName(sender, fuzzyName, shipName);
    }
    
    @SubCommand("微博订阅")
    public void listListen(CommandSender sender) {
        botLogic.getFunction(WeiboFunction.class).getCommandComponent().listListen(sender);
    }
    
    @SubCommand("最新微博")
    public void listTopSummary(CommandSender sender) {
        botLogic.getFunction(WeiboFunction.class).getCommandComponent().listTopSummary(sender);
    }
    
    @SubCommand("最新微博")
    public void listTopForName(CommandSender sender, 
            @Name("name") String name) {
        botLogic.getFunction(WeiboFunction.class).getCommandComponent().listTopForName(sender, name);
    }

    @SubCommand("立刻私聊")
    public void chat(CommandSender sender, User target, String messageCode) {
        botLogic.getFunction(DriveFunction.class).getCommandComponent().chat(sender, target, messageCode);
    }
    
    @SubCommand("立刻群聊")
    public void chat(CommandSender sender, Group target, String messageCode) {
        botLogic.getFunction(DriveFunction.class).getCommandComponent().chat(sender, target, messageCode);
    }
    
    @SubCommand("help")
    public void help(CommandSender sender) {
        botLogic.getFunction(CharacterHelpFunction.class).getCommandComponent().help(sender);
    }
    
    @SubCommand("北方指人")
    public void lianyebing(CommandSender sender, String text) {
        botLogic.getFunction(PrinzEugenImageFunction.class).getCommandComponent().lianyebing(sender, text);
    }
    
    @SubCommand("摸")
    public void patpat(CommandSender sender, User target) {
        botLogic.getFunction(PrinzEugenImageFunction.class).getCommandComponent().petpet(sender, target);
    }

}
