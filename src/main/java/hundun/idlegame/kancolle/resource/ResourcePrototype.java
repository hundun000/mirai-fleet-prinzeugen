package hundun.idlegame.kancolle.resource;

import hundun.idlegame.kancolle.base.BaseProtoype;
import hundun.idlegame.kancolle.base.BaseProtoype.BaseProtoypeBuilder;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @author hundun
 * Created on 2021/09/10
 */
@NoArgsConstructor
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ResourcePrototype extends BaseProtoype {
    String name;
}
