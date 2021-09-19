package hundun.idlegame.kancolle.exception;
/**
 * @author hundun
 * Created on 2021/09/13
 */

import hundun.idlegame.kancolle.base.BaseProtoype;
import hundun.idlegame.kancolle.building.BuildingModel;
import lombok.Getter;

@SuppressWarnings("serial")
public class BuildingNotFoundException extends IdleGameException {
    @Getter
    String protoypeId;

    
    public BuildingNotFoundException(String protoypeId) {
        super();
        this.protoypeId = protoypeId;
    }
    
    
}
