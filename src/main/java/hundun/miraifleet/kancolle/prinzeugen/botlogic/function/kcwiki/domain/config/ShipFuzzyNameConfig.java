package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.kcwiki.domain.config;
/**
 * @author hundun
 * Created on 2021/08/25
 */

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipFuzzyNameConfig {
    Map<String, String> map;
}
