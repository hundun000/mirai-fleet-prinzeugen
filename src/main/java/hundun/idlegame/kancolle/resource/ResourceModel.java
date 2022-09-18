package hundun.idlegame.kancolle.resource;
/**
 * @author hundun
 * Created on 2021/09/10
 */

import java.math.BigDecimal;
import hundun.idlegame.kancolle.base.BaseModel;
import lombok.Data;

@Data
public class ResourceModel extends BaseModel<ResourcePrototype> {
    BigDecimal amount;
    
    
}
