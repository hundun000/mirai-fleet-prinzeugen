package hundun.idlegame.kancolle.ship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2021/09/03
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShipSaveData {
    String prototypeId;
    int level;
    int exp;
    ShipWorkStatus workStatus;
    String workInBuildingId;
}
