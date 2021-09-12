package hundun.idlegame.kancolle.expedition;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/09/10
 */
@AllArgsConstructor
@Data
public class Reward {
    Map<String, Integer> resources;
    String shipId;
    int exp;
}
