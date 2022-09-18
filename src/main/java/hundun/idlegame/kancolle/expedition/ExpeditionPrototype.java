package hundun.idlegame.kancolle.expedition;
/**
 * @author hundun
 * Created on 2021/09/01
 */

import hundun.idlegame.kancolle.base.BaseProtoype;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpeditionPrototype extends BaseProtoype {
    Requirement requirement;
    Reward normalReward;
    int tick;
}
