package hundun.idlegame.kancolle.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author hundun
 * Created on 2021/09/11
 */
@NoArgsConstructor
@SuperBuilder
@Data
public abstract class BaseProtoype {
    protected String id;
}
