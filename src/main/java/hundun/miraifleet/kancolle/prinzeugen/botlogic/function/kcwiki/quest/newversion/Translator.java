package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.KcwikiQuestData.Requirements;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.KcwikiQuestData.RequirementsGroup;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.KcwikiQuestData.RequirementsObject;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.TranslatorDictionary.Equipexchange;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.TranslatorDictionary.Expedition;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.TranslatorDictionary.Fleet;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.TranslatorDictionary.Group;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.TranslatorDictionary.Groups;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.TranslatorDictionary.Option;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.TranslatorDictionary.Simple;
import hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.quest.newversion.TranslatorDictionary.Sortie;

/**
 * @author hundun
 * Created on 2021/08/30
 */
public class Translator {
    
    private static final String VAR_START = "{{";
    private static final String VAR_END = "}}";
    private static final int MAX_SHIP_AMOUNT = 7;
    
    TranslatorDictionary dictionary;
    
    ObjectMapper writeObjectMapper;
    
    public Translator(File dictionaryJson) {
        writeObjectMapper = JsonMapper.builder()
                //.configure(JsonWriteFeature.QUOTE_FIELD_NAMES, false)
                .build();
        
        try {
            dictionary = QuestFileParser.parseTranslatorDictionary(dictionaryJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) throws IOException {
        Translator translator = new Translator(new File("data/zh-CN.json"));
        KcwikiQuestData questData;
        
        System.out.println("test single fleet:");
        questData = QuestFileParser.parseKcwikiQuestData(new File("data/hundun.fleet.prinzeugen/KcwikiFunction/quest_new/101.json"));
        System.out.println(translator.tranFleetTypeRequirement(questData.requirements));
        
        System.out.println("test single Sortie:");
        questData = QuestFileParser.parseKcwikiQuestData(new File("data/hundun.fleet.prinzeugen/KcwikiFunction/quest_new/830.json"));
        System.out.println(translator.tranSortieTypeRequirement(questData.requirements));
        
        System.out.println("test single Expedition:");
        questData = QuestFileParser.parseKcwikiQuestData(new File("data/hundun.fleet.prinzeugen/KcwikiFunction/quest_new/409.json"));
        System.out.println(translator.tranExpeditionTypeRequirement(questData.requirements));

        System.out.println("test single Expedition:");
        questData = QuestFileParser.parseKcwikiQuestData(new File("data/hundun.fleet.prinzeugen/KcwikiFunction/quest_new/649.json"));
        System.out.println(translator.tranEquipexchangeTypeRequirement(questData.requirements));
    }
    
    


    private String fillVarTable(String template, Map<String, String> varTable) {
        
        while (template.contains(VAR_START)) {
            int startIndex = template.indexOf(VAR_START);
            int endIndex = template.indexOf(VAR_END);
            String beforePart = template.substring(0, startIndex);
            String varId = template.substring(startIndex + VAR_START.length(), endIndex);
            String afterPart = template.substring(endIndex + VAR_END.length(), template.length());
            String varValue = varTable.getOrDefault(varId, "");
            template = beforePart + varValue + afterPart;
        }
        

        return template;
    }
    
    private String fillOneVar(String template, Object varValue) {
        if (template.contains(VAR_START)) {
            int startIndex = template.indexOf(VAR_START);
            int endIndex = template.indexOf(VAR_END);
            String beforePart = template.substring(0, startIndex);
            String afterPart = template.substring(endIndex + VAR_END.length(), template.length());
            template = beforePart + varValue.toString() + afterPart;
        }
        
        return template;
    }
    
    /**
     * reqstr.js#parseResources()
     */
    private String parseResources(List<Integer> resources) {
        Simple simple = dictionary.req.simple;
        String[] nameKeys = new String[] {"fuel", "ammo", "steel", "bauxite"};

        String resourcesText = IntStream
                .range(0, nameKeys.length)
                .mapToObj(index -> {
                    Integer resource = resources.get(index);
                    if (resource != null) {
                        Map<String, String> varTable1 = new HashMap<>();
                        String name = dictionary.other.get(nameKeys[index]).asText();
                        varTable1.put("name", name);
                        varTable1.put("amount", resource.toString());
                        return fillVarTable(simple.resource, varTable1);
                    } else {
                        return null;
                    }
                })
                .collect(Collectors.joining(simple.resource_delim));
        return resourcesText;
    }
    
    private String parseSingleOrArray(JsonNode node) {
        
        if (node.isArray()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < node.size(); i++) {
                JsonNode item = node.get(i);
                builder.append(item.asText());
                if (i < node.size() - 1) {
                    builder.append("/");
                }
            }
            return builder.toString();
        } else {
            return node.asText();
        }
        
    }
    
    
    /**
     * reqstr.js#parseShip()
     * @param amount 
     */
    private String parseShip(JsonNode node, JsonNode amount) {
        String shipsText;
        if (node.isArray()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < node.size(); i++) {
                String shipName = node.get(i).asText();
                shipName = otherGetOrDefaultSelf(shipName);
                builder.append(shipName);
                if (i < node.size() - 1) {
                    builder.append("/");
                }
            }
            shipsText = builder.toString();
        } else {
            String shipName = node.asText();
            shipsText = otherGetOrDefaultSelf(shipName);
        }
        int amountForPluralize = amount.isArray() ? amount.get(amount.size() - 1).asInt() : amount.asInt();
        return languageSpecialPluralize(shipsText, amountForPluralize);
    }
    
  
    /**
     * reqstr.js#parseShipClass()
     */
    private String parseShipClass(JsonNode node) {
        
        if (node.isArray()) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < node.size(); i++) {
                String shipName = node.get(i).asText();
                shipName = otherGetOrDefaultSelf(shipName);
                builder.append(shipName);
                if (i < node.size() - 1) {
                    builder.append("/");
                }
            }
            return builder.toString();
        } else {
            String shipName = node.asText();
            return otherGetOrDefaultSelf(shipName);
        }
        
    }
    
    /**
     * reqstr.js#parseGroups()
     */
    private String parseGroups(List<RequirementsGroup> requirementsGroupList) throws JsonProcessingException {
        Group group = dictionary.req.group;
        Groups groups = dictionary.req.groups;
        
        List<String> groupResultTexts = new ArrayList<>(requirementsGroupList.size());
        for (RequirementsGroup requirementsGroup : requirementsGroupList) {
            Map<String, String> varTable = new HashMap<>();
            
            if (requirementsGroup.ship != null) {
                String text = parseShip(requirementsGroup.ship, requirementsGroup.amount);
                varTable.put("ship", text);
            }
            
            if (requirementsGroup.select != null) {
                String text = fillOneVar(group.select, requirementsGroup.select);
                varTable.put("select", text);
            }
            
            if (requirementsGroup.amount != null) {
                String text;
                if (requirementsGroup.amount.isArray()) {
                    int amountFloor = requirementsGroup.amount.path(0).asInt();
                    int amountCeiling = requirementsGroup.amount.path(1).asInt();
                    if (amountFloor == amountCeiling) {
                        text = fillOneVar(group.amountonly, amountFloor);
                    } else if (amountCeiling >= MAX_SHIP_AMOUNT) {
                        text = fillOneVar(group.amountmore, amountFloor);
                    } else  {
                        text = fillOneVar(group.amount, amountFloor + "~" + amountCeiling);
                    }
                } else {
                    text = fillOneVar(group.amount, requirementsGroup.amount);
                }
                varTable.put("amount", text);
            }
            
            
            String groupResult = fillVarTable(group.main, varTable);
            groupResultTexts.add(groupResult);
        }
        
        return groupResultTexts.stream().collect(Collectors.joining(groups.delim));
    }
    
    public void appendRequirement(TranslatedQuestData result, Requirements requirements) throws JsonProcessingException {
        String text = null;
        switch (requirements.category) {
            case "sortie":
                text = tranSortieTypeRequirement(requirements);
                break;
            case "fleet":
                text = tranFleetTypeRequirement(requirements);
                break;
            case "expedition":
                text = tranExpeditionTypeRequirement(requirements);
                break;
            case "equipexchange":
                text = tranEquipexchangeTypeRequirement(requirements);
                break;
            default:
                break;
        }
        text = dictionary.other.get(requirements.category).asText() + ":" + text;
        result.setRequirementsResult(text);
    }
    
    private String languageSpecialPluralize(String item, int amount) {
        Option option = dictionary.req.option;
        if (!option.pluralize) {
            return item;
        }
        // TODO
        return item;
    }
    
     /**
     * reqstr.js#parseFrequency()
     */
    private String languageSpecialFrequency(int times) {
        Option option = dictionary.req.option;
        if (!option.frequency) {
            return String.valueOf(times);
        }
        switch (times) {
            case 1:
              return "once";
            case 2:
              return "twice";
            default:
              return times + " times";
        }
    }
    
    /**
     * reqstr.js#expedition()
     */
    private String tranExpeditionTypeRequirement(Requirements requirements) throws JsonProcessingException {
        Expedition expedition = dictionary.req.expedition;
        Map<String, String> varTable = new HashMap<>();


        if (requirements.objects != null) {
            String objects = requirements.objects.stream().map(object -> {
                Map<String, String> varTable1 = new HashMap<>();
                String name;
                if (object.id != null) {
                    String idText = parseSingleOrArray(object.id);
                    name = fillOneVar(expedition.id, idText);
                } else {
                    name = expedition.any;
                }
                varTable1.put("name", name);
                String times = languageSpecialFrequency(object.times);
                varTable1.put("times", times);
                return fillVarTable(expedition.object, varTable1);
            })
            .collect(Collectors.joining(expedition.delim));
            varTable.put("objects", objects);
        }
        
        if (requirements.resources != null) {
            varTable.put("resources", parseResources(requirements.resources));
        }
        
        if (requirements.groups != null) {
            varTable.put("groups", parseGroups(requirements.groups));
        }
        
        if (requirements.disallowed != null) {
            varTable.put("disallowed", fillOneVar(expedition.disallowed, parseShip(requirements.disallowed, JsonNodeFactory.instance.numberNode(2))));
        }
        
        return fillVarTable(expedition.main, varTable);
    }
    
    
    private String tranFleetTypeRequirement(Requirements requirements) throws JsonProcessingException {
        Fleet fleet = dictionary.req.fleet;
        Map<String, String> varTable = new HashMap<>();
        if (requirements.groups != null) {
            String text = parseGroups(requirements.groups);
            varTable.put("groups", text);
        }
        if (requirements.fleetid != null) {
            String text = fillOneVar(fleet.fleetid, requirements.fleetid);
            varTable.put("fleetid", text);
        }
        if (requirements.fleetid != null) {
            String text = fillOneVar(fleet.fleetid, requirements.fleetid);
            varTable.put("fleetid", text);
        }
        
        return fillVarTable(fleet.main, varTable);
    }
    
    /**
     * reqstr.js#__()
     */
    public String otherGetOrDefaultSelf(String key) {
        if (dictionary.other.has(key)) {
            return dictionary.other.get(key).asText();
        } else {
            return key;
        }
    }
    
    /**
     * reqstr.js#equipexchange()
     */
    private String tranEquipexchangeTypeRequirement(Requirements requirements) {
        Equipexchange equipexchange = dictionary.req.equipexchange;
        Map<String, String> varTable = new HashMap<>();
        
        if (requirements.equipments != null) {
            String equipments = requirements.equipments.stream().map(object -> {
                Map<String, String> varTable1 = new HashMap<>();
                String name = otherGetOrDefaultSelf(object.name);
                varTable1.put("name", name);
                String amount = object.amount.toString();
                varTable1.put("amount", amount);
                return fillVarTable(equipexchange.equipment, varTable1);
            })
            .collect(Collectors.joining(equipexchange.delim));
            varTable.put("equipments", equipments);
        }
        
        return fillVarTable(equipexchange.main, varTable);
    }
    
    private String tranSortieTypeRequirement(Requirements requirements) throws JsonProcessingException {

        Sortie sortie = dictionary.req.sortie;
        
        Map<String, String> varTable = new HashMap<>();
        if (requirements.map != null) {
            String mapText = parseSingleOrArray(requirements.map);
            String text = fillOneVar(sortie.map, mapText);
            varTable.put("map", text);
        }
        if (requirements.boss != null) {
            String template;
            if (requirements.boss) {
                template = sortie.boss;
            } else {
                template = sortie.notBoss;
            }
            String text = fillOneVar(template, requirements.boss);
            varTable.put("boss", text);
        }
        if (requirements.result != null) {
            String text = fillOneVar(sortie.result, requirements.result);
            varTable.put("result", text);
        }
        if (requirements.times != null) {
            String text = fillOneVar(sortie.times, requirements.times);
            varTable.put("times", text);
        }
        if (requirements.groups != null) {
            String text = parseGroups(requirements.groups);
            varTable.put("groups", text);
        }
        return fillVarTable(sortie.main, varTable);
        
    }

}
