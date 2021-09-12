package hundun.idlegame.kancolle.helper;
/**
 * @author hundun
 * Created on 2021/09/14
 */

import java.util.function.Function;

public class Functions {
    
    
    public static final Function<Integer, Integer> shipLevelUpNeedExpFuction = lv -> lv > 1 ? 100 * lv : 10;

}
