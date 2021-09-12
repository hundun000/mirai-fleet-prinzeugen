package hundun.idlegame.kancolle.expedition;
/**
 * @author hundun
 * Created on 2021/09/01
 */

import hundun.idlegame.kancolle.base.BaseProtoype;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class ExpeditionPrototype extends BaseProtoype {
    Requirement requirement;
    Reward normalReward;
    int tick;
}
