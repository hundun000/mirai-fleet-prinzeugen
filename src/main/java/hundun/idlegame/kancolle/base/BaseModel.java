package hundun.idlegame.kancolle.base;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/09/11
 */
@Data
public abstract class BaseModel<T extends BaseProtoype> {
    protected T prototype;
    
    public String getId() {
        return prototype.getId();
    }
}
