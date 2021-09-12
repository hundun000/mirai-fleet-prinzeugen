package hundun.idlegame.kancolle.exception;
/**
 * @author hundun
 * Created on 2021/09/13
 */

import hundun.idlegame.kancolle.base.BaseProtoype;
import lombok.Getter;

@SuppressWarnings("serial")
public class ModelNotFoundException extends IdleGameException {
    @Getter
    String protoypeId;
    @Getter
    Class<? extends BaseProtoype>  protoypeClass;
    
    public ModelNotFoundException(String protoypeId, Class<? extends BaseProtoype> protoypeClass) {
        super();
        this.protoypeId = protoypeId;
        this.protoypeClass = protoypeClass;
    }
    
    
}
