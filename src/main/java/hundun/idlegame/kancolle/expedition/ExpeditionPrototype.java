package hundun.idlegame.kancolle.expedition;
/**
 * @author hundun
 * Created on 2021/09/01
 */

import hundun.idlegame.kancolle.base.BaseProtoype;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public class ExpeditionPrototype extends BaseProtoype {
    Requirement requirement;
    Reward normalReward;
    Reward firstTimeReward;
    int tick;
}
