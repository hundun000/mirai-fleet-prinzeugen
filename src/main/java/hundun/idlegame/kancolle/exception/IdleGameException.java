package hundun.idlegame.kancolle.exception;

import hundun.idlegame.kancolle.format.SimpleExceptionFormatter;

/**
 * @author hundun
 * Created on 2021/09/13
 */
@SuppressWarnings("serial")
public class IdleGameException extends Exception {

    public IdleGameException() {
        super();
    }
    
    public IdleGameException(String arg0) {
        super(arg0);
    }
    
    @Override
    public String getMessage() {
        return SimpleExceptionFormatter.INSTANCE.exceptionToMessage(this);
    }
}
