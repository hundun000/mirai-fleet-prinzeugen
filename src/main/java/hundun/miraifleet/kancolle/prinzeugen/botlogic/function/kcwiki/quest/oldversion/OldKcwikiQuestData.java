package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.oldversion;

import java.util.List;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/08/28
 */
@Data
public class OldKcwikiQuestData {
    public String id;
    public int category;
    public int type;
    public String title;
    public String detail;
    public List<Integer> reward;
    public List<Reward2> reward2;
    //public List<Object> prerequisite;
    public String wiki_id;
    public String chinese_title;
    public String chinese_detail;
    
    @Data
    public static class Reward2{
        public String category;
        public int id;
        public int amount;
    }
}
