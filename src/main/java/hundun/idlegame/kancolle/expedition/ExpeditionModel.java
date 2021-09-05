package hundun.idlegame.kancolle.expedition;

import java.util.List;

import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created on 2021/09/01
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExpeditionModel {
    List<String> shipIds;
    ExpeditionPrototype prototype;
    int remainTick;
}
