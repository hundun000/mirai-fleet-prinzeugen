package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.dto;

import java.util.List;

import lombok.Data;

@Data
public class WhoCallsTheFleetItem {
    int id;
    NameBlock name; 
    List<Integer> default_equipped_on;
    
    @Data
    public static class NameBlock {
        String zh_cn;
    }
}
