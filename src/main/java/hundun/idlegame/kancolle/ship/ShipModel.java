package hundun.idlegame.kancolle.ship;

import hundun.idlegame.kancolle.base.BaseModel;
import hundun.idlegame.kancolle.building.BaseBuilding;
import hundun.idlegame.kancolle.helper.Functions;
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
    
    public boolean setExpAndCheckLevelUp(int add) {
        exp += add;
        int needExp = Functions.shipLevelUpNeedExpFuction.apply(level);
        boolean levelUpHappen = false;
        while (exp >= needExp) {
            exp -= needExp;
            level++;
            levelUpHappen = true;
            needExp = Functions.shipLevelUpNeedExpFuction.apply(level);
        }
        return levelUpHappen;
    }


    public int calculatePower() {
        return prototype.getBasePower()
                + (int) Math.round(prototype.getBasePower() * 0.5 * (level / 100.0))
                + 5 * (level - 1);
    }


    public boolean canChangeWork(String buildingId) {
        boolean inOtherBuilding = (!workInBuildingId.equals(BaseBuilding.NONE_ID) && workStatus == ShipWorkStatus.IN_BUILDING);
        boolean noWork = workInBuildingId.equals(BaseBuilding.NONE_ID);
        return noWork || inOtherBuilding;
    }
}
