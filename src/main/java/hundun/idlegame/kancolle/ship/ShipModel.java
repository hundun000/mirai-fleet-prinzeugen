package hundun.idlegame.kancolle.ship;

import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2021/09/02
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShipModel {
    ShipPrototype prototype;
    int level;
    int exp;
}
