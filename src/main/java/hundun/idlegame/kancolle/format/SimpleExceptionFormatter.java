package hundun.idlegame.kancolle.format;

import hundun.idlegame.kancolle.base.BaseProtoype;
import hundun.idlegame.kancolle.exception.BadCreateExpeditionCommandException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.ModelNotFoundException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.expedition.ExpeditionPrototype;
import hundun.idlegame.kancolle.ship.ShipPrototype;

/**
 * 把gameException转为message的一种实现。可用自定义的Advice替代。
 * @author hundun
 * Created on 2021/09/13
 */
public class SimpleExceptionFormatter {
    
    public static final SimpleExceptionFormatter INSTANCE = new SimpleExceptionFormatter();
    
    public String exceptionToMessage(IdleGameException exception) {
        if (exception instanceof PrototypeNotFoundException) {
            PrototypeNotFoundException childException = (PrototypeNotFoundException)exception;
            return String.format("不存在id为“%s”的%s", childException.getProtoypeId(), prototypeClassToName(childException.getProtoypeClass()));
        }
        if (exception instanceof ModelNotFoundException) {
            ModelNotFoundException childException = (ModelNotFoundException)exception;
            return String.format("你未拥有id为“%s”的%s", childException.getProtoypeId(), prototypeClassToName(childException.getProtoypeClass()));
        }
        if (exception instanceof BadCreateExpeditionCommandException) {
            BadCreateExpeditionCommandException childException = (BadCreateExpeditionCommandException)exception;
            if (childException.isExpeditionPresent()) {
                return String.format("该远征已在进行中");
            }
            if (childException.getBusyShipIds() != null) {
                return String.format("包含已在工作中的舰娘");
            }
            if (childException.getRequirement() != null) {
                return String.format("远征需求不满足:" + childException.getRequirement());
            }
            if (childException.getTargetBuildingId() != null) {
                return String.format("只有入驻“%s”的船可以进行远征", childException.getTargetBuildingId());
            }
        }
        return "[" + this.getClass().getSimpleName() + ":" + exception.getClass().getSimpleName() + "]";
    }
    
    
    
    
    private String prototypeClassToName(Class<? extends BaseProtoype> clazz) {
        if (clazz.equals(ShipPrototype.class)) {
            return "船";
        }
        if (clazz.equals(ExpeditionPrototype.class)) {
            return "远征";
        }
        return "[" + this.getClass().getSimpleName() + ":" + clazz.getSimpleName() + "]";
    }
}
