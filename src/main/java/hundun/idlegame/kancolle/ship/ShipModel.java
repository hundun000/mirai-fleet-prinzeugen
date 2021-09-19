package hundun.idlegame.kancolle.ship;

import java.util.function.Function;

import hundun.idlegame.kancolle.base.BaseModel;
import hundun.idlegame.kancolle.building.BuildingModel;
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
    
    @Getter
    @Setter
    ShipWorkStatus workStatus;
    
    @Getter
    @Setter
    String workInBuildingId;
    
    static final Function<Integer, Integer> shipLevelUpNeedExpFuction = lv -> lv > 1 ? 100 * lv : 10;
    
    public boolean setExpAndCheckLevelUp(int add) {
        exp += add;
        int needExp = shipLevelUpNeedExpFuction.apply(level);
        boolean levelUpHappen = false;
        while (exp >= needExp) {
            exp -= needExp;
            level++;
            levelUpHappen = true;
            needExp = shipLevelUpNeedExpFuction.apply(level);
        }
        return levelUpHappen;
    }


    public int calculatePower() {
        return prototype.getBasePower()
                + (int) Math.round(prototype.getBasePower() * 0.5 * (level / 100.0))
                + 5 * (level - 1);
    }


    public boolean canChangeWork(String buildingId) {
        boolean inOtherBuilding = (!workInBuildingId.equals(BuildingModel.NONE_ID) && workStatus == ShipWorkStatus.IN_BUILDING);
        boolean noWork = workInBuildingId.equals(BuildingModel.NONE_ID);
        return noWork || inOtherBuilding;
    }
}
