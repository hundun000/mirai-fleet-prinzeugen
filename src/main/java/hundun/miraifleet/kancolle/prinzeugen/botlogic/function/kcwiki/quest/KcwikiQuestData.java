package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest;

import java.util.List;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/08/28
 */
@Data
public class KcwikiQuestData {
    public int game_id;
    public String wiki_id;
    public int category;
    public int type;
    public String name;
    public String detail;
    public int reward_fuel;
    public int reward_ammo;
    public int reward_steel;
    public int reward_bauxite;
    public List<RewardOther> reward_other;
    public List<Integer> prerequisite;
    public Requirements requirements;
    
    @Data
    public static class RewardOther{
        public String name;
        public int amount;
    }

    @Data
    public static class Group{
        public String ship;
        public int amount;
    }

    @Data
    public static class Requirements{
        public String category;
        public List<Group> groups;
    }
}


