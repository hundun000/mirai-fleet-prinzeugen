package hundun.idlegame.kancolle.ship;

import hundun.idlegame.kancolle.base.BaseProtoype;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author hundun
 * Created on 2021/09/01
 */
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShipPrototype extends BaseProtoype {

    int basePower;
    int gachaRarity;
    int[] standardGachaResources;
}
