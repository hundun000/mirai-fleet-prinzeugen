package hundun.idlegame.kancolle.ship;

import hundun.idlegame.kancolle.base.BaseModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public class ShipModel extends BaseModel<ShipPrototype>{
    @Getter
    @Setter
    int level;
    
    @Getter
    int exp;
    
    
    public boolean setExpAndCheckLevelUp(int add) {
        exp += add;
        int needExp = 100 * level;
        boolean levelUp = false;
        while (exp >= needExp) {
            exp -= needExp;
            level++;
            levelUp = true;
        }
        return levelUp;
    }
}
