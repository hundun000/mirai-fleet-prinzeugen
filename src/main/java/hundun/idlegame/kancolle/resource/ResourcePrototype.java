package hundun.idlegame.kancolle.resource;

import hundun.idlegame.kancolle.base.BaseProtoype;
import hundun.idlegame.kancolle.base.BaseProtoype.BaseProtoypeBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author hundun
 * Created on 2021/09/10
 */
@SuperBuilder
@Data
public class ResourcePrototype extends BaseProtoype {
    String name;
}
