package hundun.idlegame.kancolle.container;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hundun
 * Created on 2021/09/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandResult<T> {
    boolean success;
    String adviceMessage;
    T payload;
    
//    public static CommandResult<Void> fail(String failReason) {
//        return new CommandResult<>(false, failReason, (Void)null);
//    }
    
    public static <T> CommandResult<T> success(T data, String adviceMessage) {
        return new CommandResult<>(true, adviceMessage, data);
    }

    public static CommandResult<Void> success(String adviceMessage) {
        return success((Void) null, adviceMessage);
    }
}
