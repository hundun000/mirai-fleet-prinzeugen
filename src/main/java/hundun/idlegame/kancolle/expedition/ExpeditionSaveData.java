package hundun.idlegame.kancolle.expedition;

import java.util.List;

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
public class ExpeditionSaveData {
    String id;
    List<String> shipIds;
    int remainTick;
}
