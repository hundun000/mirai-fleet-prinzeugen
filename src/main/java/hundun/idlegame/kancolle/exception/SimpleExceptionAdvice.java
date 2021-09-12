package hundun.idlegame.kancolle.exception;

import hundun.idlegame.kancolle.base.BaseProtoype;
import hundun.idlegame.kancolle.ship.ShipPrototype;

/**
 * 把gameException转为message的一种实现。可用自定义的Advice替代。
 * @author hundun
 * Created on 2021/09/13
 */
public class SimpleExceptionAdvice {
    
    public static final SimpleExceptionAdvice INSTANCE = new SimpleExceptionAdvice();
    
    public String exceptionToMessage(IdleGameException exception) {
        if (exception instanceof PrototypeNotFoundException) {
            PrototypeNotFoundException childException = (PrototypeNotFoundException)exception;
            return String.format("没有找到id为%s的%s", childException.getProtoypeId(), prototypeClassToName(childException.getProtoypeClass()));
        }
        return exception.getMessage();
    }
    
    
    
    
    private String prototypeClassToName(Class<? extends BaseProtoype> clazz) {
        if (clazz.equals(ShipPrototype.class)) {
            return "船";
        }
        
        return "[" + clazz.getSimpleName() + ":???]";
    }
}
