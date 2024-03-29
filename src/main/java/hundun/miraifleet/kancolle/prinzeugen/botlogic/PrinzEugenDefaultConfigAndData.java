package hundun.miraifleet.kancolle.prinzeugen.botlogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import hundun.miraifleet.framework.helper.repository.SingletonDocumentRepository;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.WeiboFunction;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.config.WeiboConfig;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.config.WeiboPushFilterFlag;
import hundun.miraifleet.framework.starter.botlogic.function.weibo.config.WeiboViewFormat;
import hundun.miraifleet.reminder.share.function.reminder.data.ReminderItem;
import hundun.miraifleet.reminder.share.function.reminder.data.ReminderList;

/**
 * @author hundun
 * Created on 2021/12/20
 */
public class PrinzEugenDefaultConfigAndData {
    
    public static <V> Map<String, V> toSingletonMap(V v) {
        return Map.of(SingletonDocumentRepository.THE_SINGLETON_KEY, v);
    }
    
    public static Supplier<WeiboConfig> weiboConfigDefaultDataSupplier() {
        return () -> {
            WeiboConfig weiboConfig = new WeiboConfig(
                    WeiboFunction.DEFAULT_NONEWBLOGMESSAGETEMPLATE,
                    WeiboFunction.DEFAULT_NEWBLOGMESSAGETEMPLATE,
                    WeiboFunction.DEFAULT_SUMMARYBLOGMESSAGETEMPLATE,
                    Map.of(
                            "3837704366", WeiboViewFormat.ALL_IMAGE, 
                            "3297631372", WeiboViewFormat.ALL_IMAGE
                            ), 
                    Map.of(
                            "3297631372", Arrays.asList(WeiboPushFilterFlag.RETWEET))
                    );
            return weiboConfig;
        };
    }
    
    public static Supplier<ReminderList> reminderListDefaultDataSupplier() {
        return () -> {
            ReminderList reminderList = new ReminderList();
            List<ReminderItem> items = new ArrayList<>();
            items.add(ReminderItem.Factory.createHourly(0, "呜哇！？正好0点！今天是，由本欧根·亲王来担当时报的工作呢。好的，知道啦！交给我吧！", "177-0000.amr", null));
            items.add(ReminderItem.Factory.createHourly(1, "凌晨一点到啦！这种感觉可以，吗？啊，是吗！太好啦！", "177-0100.amr", null));
            items.add(ReminderItem.Factory.createHourly(9, "早上九点到了。嗯？啊，是长门！喂！长~门！……嗯？在哪儿遇见过？那当然是！……等……奇怪？那个……是在哪儿来着……？", "177-0900.amr", null));
            items.add(ReminderItem.Factory.createHourly(10, "十点到了。战舰？ 当然！要是和俾斯麦姐姐一起的话，轻轻松松就能击沉！交给我吧！", "177-1000.amr", null));
            items.add(ReminderItem.Factory.createHourly(11, "到上午11了，马上就要到中午了呢。今天，在外面吃午饭也不错呢。", "177-1100.amr", null));
            items.add(ReminderItem.Factory.createHourly(12, "啊啊，都已经到中午了！现在是正午！午饭，在外面吃奶酪和面包可以吗？心情不错所以来点啤酒可以吗？啊哈，果然不行么…", "177-1200.amr", null));
            items.add(ReminderItem.Factory.createHourly(13, "到十三点了。午后的作战也要开始了呢，努力上吧！", "177-1300.amr", null));
            items.add(ReminderItem.Factory.createHourly(14, "十四点了。诶？什么什么，酒匂？啊啊我知道，是那个可爱的轻巡呢。", "177-1400.amr", null));
            items.add(ReminderItem.Factory.createHourly(15, "十五点到了。啊啊，这个吗？，是，这个是地狱犬作战时候穿的舰装。怎么样？合适吗~？", "177-1500.amr", null));
            items.add(ReminderItem.Factory.createHourly(16, "到十六点了。哇，吓了我一跳啊，俾斯麦姐姐大人！请让我与你一同出击！是，务必！", "177-1600.amr", null));
            items.add(ReminderItem.Factory.createHourly(17, "十七点。到傍晚了呢。差不多，今天也要日落了呢。……啊，天空真美……。啊，当然俾斯麦姐姐是最美的。", "177-1700.amr", null));
            items.add(ReminderItem.Factory.createHourly(18, "十八点到了。要准备晚饭了呢。今天，吃凉菜可以吗？诶，不要？那热菜可以吗？", "177-1800.amr", null));
            items.add(ReminderItem.Factory.createHourly(19, "十九点到了。那么，晚饭就吃猪脚火锅吧。汤料的味道也全都浸了进去很不错对吧？最后再把饭加进去做成杂煮也很美味！", "177-1900.amr", null));
            items.add(ReminderItem.Factory.createHourly(20, "二十点到了。日本的重巡也，很充实呢。唔姆唔姆，嗯~原来如此……唔姆唔姆…", "177-2000.amr", null));
            items.add(ReminderItem.Factory.createHourly(21, "二十一点。诶，擅长什么…是吗？是啊，之前有过有过把舰炮压低，狠狠的揍了一顿战车群这回事儿。这个就很擅长！是！", "177-2100.amr", null));
            items.add(ReminderItem.Factory.createHourly(22, "完全入夜了呢，二十二点到了。Admiral，今天一天的，作战真是辛苦您了！", "177-2200.amr", null));
            items.add(ReminderItem.Factory.createHourly(23, "二十三点到了。嗯~差不多我也该休息了……Gute Nacht……诶，不行……？", "177-2300.amr", null));
            
            reminderList.setItems(items);
            return reminderList;
        };
    }
    
    
}
