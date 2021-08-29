package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/08/28
 */
public class KcwikiQuestData {
    @JsonProperty("game_id") 
    public int gameId;
    @JsonProperty("wiki_id") 
    public String wikiId;
    public int category;
    public int type;
    public String name;
    public String detail;
    @JsonProperty("reward_fuel") 
    public int rewardFuel;
    @JsonProperty("reward_ammo") 
    public int rewardAmmo;
    @JsonProperty("reward_steel") 
    public int rewardSteel;
    @JsonProperty("reward_bauxite") 
    public int rewardBauxite;
    public List<RequirementsItem> rewardOther;
    public List<Integer> prerequisite;
    public Requirements requirements;
    
    public static class RequirementsItem{
        public String name;
        public Integer amount;
    }

    public static class RequirementsGroup{
        public JsonNode ship;
        public Integer select;
        public JsonNode amount;
        public List<String> lv;
    }

    public static class RequirementsObject {
        public JsonNode id;
        public Integer times;
    }

    
    public static class Requirements{
        public List<RequirementsItem> equipments;
        public List<Integer> resources;
        public List<RequirementsObject> objects;
        public String category;
        public Boolean boss;
        public String result;
        public Integer times;
        public JsonNode map;
        public Integer fleetid;
        public JsonNode disallowed;
        public List<RequirementsGroup> groups;
    }
}


