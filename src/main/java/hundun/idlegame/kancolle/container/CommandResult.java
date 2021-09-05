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
    String failReason;
    T data;
    
    public static CommandResult<Void> fail(String failReason) {
        return new CommandResult<>(false, failReason, (Void)null);
    }
    
    public static <T> CommandResult<T> success(T data) {
        return new CommandResult<>(true, null, data);
    }
}
