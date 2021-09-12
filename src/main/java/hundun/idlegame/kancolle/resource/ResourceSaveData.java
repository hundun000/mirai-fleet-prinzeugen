package hundun.idlegame.kancolle.resource;
/**
 * @author hundun
 * Created on 2021/09/10
 */

import java.math.BigDecimal;


import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceSaveData {
    String id;
    BigDecimal amount;
}
