package hundun.idlegame.kancolle.ship;

import hundun.idlegame.kancolle.base.BaseProtoype;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author hundun
 * Created on 2021/09/01
 */
@SuperBuilder
@Data
public class ShipPrototype extends BaseProtoype {

    int basePower;
    
}
