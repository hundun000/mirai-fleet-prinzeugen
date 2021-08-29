package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;

/**
 * @author hundun
 * Created on 2021/08/30
 */
@AllArgsConstructor
public class TranslatorDictionary {
    
    
//    @JsonProperty("Plugin Description") 
//    public String pluginDescription;
//    @JsonProperty("Quest Information") 
//    public String questInformation;
//    @JsonProperty("Composition") 
//    public String composition;
//    @JsonProperty("Sortie") 
//    public String sortie;
//    @JsonProperty("Exercise") 
//    public String exercise;
//    @JsonProperty("Expedition") 
//    public String expedition;
//    @JsonProperty("Supply/Docking") 
//    public String supplyDocking;
//    @JsonProperty("Arsenal") 
//    public String arsenal;
//    @JsonProperty("Modernization") 
//    public String modernization;
//    @JsonProperty("Marriage") 
//    public String marriage;
//    @JsonProperty("One-time") 
//    public String oneTime;
//    @JsonProperty("Daily") 
//    public String daily;
//    @JsonProperty("Weekly") 
//    public String weekly;
//    @JsonProperty("Monthly") 
//    public String monthly;
//    @JsonProperty("Quarterly") 
//    public String quarterly;
//    @JsonProperty("-3rd/-7th/-0th") 
//    public String _3rd7th0th;
//    @JsonProperty("-2nd/-8th") 
//    public String _2nd8th;
//    @JsonProperty("Quest Type") 
//    public String questType;
//    @JsonProperty("Quest Name") 
//    public String questName;
//    @JsonProperty("Quests") 
//    public String quests;
//    @JsonProperty("Select Quest") 
//    public String selectQuest;
//    @JsonProperty("Reward") 
//    public String reward;
//    @JsonProperty("Requires") 
//    public String requires;
//    @JsonProperty("Unlocks") 
//    public String unlocks;
//    @JsonProperty("Note") 
//    public String note;
//    @JsonProperty("Requirement") 
//    public String requirement;
//    @JsonProperty("Fuel") 
//    public String fuel;
//    @JsonProperty("Ammo") 
//    public String ammo;
//    @JsonProperty("Steel") 
//    public String steel;
//    @JsonProperty("Bauxite") 
//    public String bauxite;
//    @JsonProperty("Operable") 
//    public String operable;
//    @JsonProperty("Locked") 
//    public String locked;
//    @JsonProperty("Completed") 
//    public String completed;
//    public String 艦;
//    public String 他の艦;
//    public String 駆逐;
//    public String 軽巡;
//    public String 重巡;
//    public String 航巡;
//    public String 水母;
//    public String 空母;
//    public String 装母;
//    public String 軽母;
//    public String 戦艦;
//    public String 低速戦艦;
//    public String 航戦;
//    public String 高速艦;
//    public String 潜水艦;
//    public String 潜水空母;
//    public String 潜水母艦;
//    public String 海防艦;
//    public String 敵補給艦;
//    public String 敵潜水艦;
//    public String 敵空母;
//    public String 敵軽母;
//    public String 電探;
//    @JsonProperty("Undocumented quest, please wait for updates") 
//    public String undocumentedQuestPleaseWaitForUpdates;
//    public String multiple_choices;
//    @JsonProperty("The info for this quest is incorrect, report it") 
//    public String theInfoForThisQuestIsIncorrectReportIt;
    
    
    /**
     * 原json里除了Req子树以外的成员，作为自由json放这里面
     */
    public ObjectNode other;
    public Req req;
    
    
    public static class Option{
        public boolean pluralize;
        public boolean ordinalize;
        public boolean frequency;
    }

    public static class Result{
        public String クリア;
        @JsonProperty("S") 
        public String s;
        @JsonProperty("A") 
        public String a;
        @JsonProperty("B") 
        public String b;
        @JsonProperty("C") 
        public String c;
    }

    public static class Group{
        public String main;
        public String lv;
        public String lvmore;
        public String amount;
        public String amountonly;
        public String amountmore;
        public String select;
        public String note;
        public String flagship;
        public String place;
        @JsonProperty("class") 
        public String clazz;
    }

    public static class Groups{
        public String delim;
    }

    public static class Fleet{
        public String main;
        public String fleetid;
        public String disallowed;
    }

    public static class Sortie{
        public String main;
        public String map;
        public String boss;
        @JsonProperty("!boss") 
        public String notBoss;
        public String result;
        public String times;
        public String groups;
        public String fleet;
        public String disallowed;
    }

    public static class Sink{
        public String main;
        public String amount;
        public String ship;
    }

    public static class Expedition{
        public String main;
        public String object;
        public String any;
        public String id;
        public String resources;
        public String delim;
        public String groups;
        public String disallowed;
    }

    public static class Simple{
        public String equipment;
        public String ship;
        public String scrapequipment;
        public String scrapequipment_batch;
        @JsonProperty("scrapequipment_!batch") 
        public String scrapequipment_notBatch;
        public String scrapship;
        public String modernization;
        public String improvement;
        public String resupply;
        public String repair;
        public String battle;
        public String resource;
        public String resource_delim;
    }

    public static class Excercise{
        public String main;
        public String victory;
        public String daily;
        public String groups;
    }

    public static class Slot{
        @JsonProperty("0") 
        public String _0;
        @JsonProperty("1") 
        public String _1;
        @JsonProperty("2") 
        public String _2;
        @JsonProperty("3") 
        public String _3;
        @JsonProperty("4") 
        public String _4;
    }

    public static class Modelconversion{
        public String main;
        public String noextra;
        public String equip;
        public String noequip;
        public String secretarydefault;
        public String scraps;
        public String scrap;
        public String consumptions;
        public String consumption;
        public String equipmentdelim;
        public String scrapdelim;
        public String fullyskilled;
        public String maxmodified;
        public String useskilledcrew;
        public Slot slot;
    }

    public static class Scrapequipment{
        public String main;
        public String scrap;
        public String scrapdelim;
    }

    public static class Equipexchange{
        public String main;
        public String equipments;
        public String equipment;
        public String scraps;
        public String scrap;
        public String consumptions;
        public String consumption;
        public String delim;
    }

    public static class Modernization{
        public String main;
        public String consumptions;
        public String consumption;
        public String resources;
        public String delim;
    }


    public static class LogicOperator{
        public String separator;
        public String word;
    }

    public static class Req{
        public Option option;
        public Result result;
        public Group group;
        public Groups groups;
        public Fleet fleet;
        public Sortie sortie;
        public Sink sink;
        @JsonProperty("a-gou") 
        public String aGou;
        public Expedition expedition;
        public Simple simple;
        public Excercise excercise;
        public Modelconversion modelconversion;
        public Scrapequipment scrapequipment;
        public Equipexchange equipexchange;
        public Modernization modernization;
        public LogicOperator and;
        public LogicOperator then;
        public LogicOperator or;
    }
}
