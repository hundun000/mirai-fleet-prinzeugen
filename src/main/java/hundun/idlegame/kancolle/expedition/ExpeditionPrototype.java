package hundun.idlegame.kancolle.expedition;
/**
 * @author hundun
 * Created on 2021/09/01
 */

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ser.std.RawSerializer;

import hundun.idlegame.kancolle.resource.Resource;
import lombok.Data;

@Data
public class ExpeditionPrototype {
    
    
    String id;
    Map<Resource, Integer> resourceRewards;
    int rewardExp;
    int tick;
}
