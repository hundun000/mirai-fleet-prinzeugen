package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2021/05/24
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KcwikiInitEquip {
    int id;
    String name;
    List<String> slots;
    List<String> slot_names;
}
