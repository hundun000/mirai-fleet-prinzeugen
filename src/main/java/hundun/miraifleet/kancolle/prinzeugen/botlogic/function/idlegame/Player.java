package hundun.miraifleet.kancolle.prinzeugen.botlogic.function.idlegame;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.MemberCommandSender;

/**
 * @author hundun
 * Created on 2021/09/06
 */
@NoArgsConstructor
@Data
public class Player {
    
    Long botId;
    Long groupId;
    boolean usingConsole;
    
    public Player(CommandSender sender) {
        if (sender instanceof MemberCommandSender) {
            this.groupId = ((MemberCommandSender)sender).getGroup().getId();
            this.botId = ((MemberCommandSender)sender).getBot().getId();
            this.usingConsole = false;
        } else {
            this.groupId = null;
            this.botId = null;
            this.usingConsole = true;
        }
    }
}
