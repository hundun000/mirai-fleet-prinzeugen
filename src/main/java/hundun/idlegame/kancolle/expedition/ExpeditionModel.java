package hundun.idlegame.kancolle.expedition;

import java.util.List;

import hundun.idlegame.kancolle.base.BaseModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Created on 2021/09/01
 */
@Data
public class ExpeditionModel extends BaseModel<ExpeditionPrototype> {
    List<String> shipIds;
    int remainTick;
}
