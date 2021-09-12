package hundun.idlegame.kancolle.resource;
/**
 * @author hundun
 * Created on 2021/09/10
 */

import java.math.BigDecimal;
import java.util.List;

import hundun.idlegame.kancolle.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
public class ResourceModel extends BaseModel<ResourcePrototype> {
    BigDecimal amount;
    
    
}
