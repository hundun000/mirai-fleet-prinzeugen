package hundun.idlegame.kancolle.exception;

import java.util.List;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import hundun.idlegame.kancolle.expedition.Requirement;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/09/13
 */
@SuppressWarnings("serial")
public class BadCreateExpeditionCommandException extends IdleGameException {
    boolean expeditionIsPresent;
    @Getter
    Requirement requirement;
    @Getter
    List<String> busyShipIds;
    
    public BadCreateExpeditionCommandException() {
    }
    
    public static BadCreateExpeditionCommandException shipBusy(List<String> busyShipIds) {
        BadCreateExpeditionCommandException exception = new BadCreateExpeditionCommandException();
        exception.busyShipIds = busyShipIds;
        return exception;
    }
    
    public static BadCreateExpeditionCommandException requirementNotMatch(Requirement requirement) {
        BadCreateExpeditionCommandException exception = new BadCreateExpeditionCommandException();
        exception.requirement = requirement;
        return exception;
    }

    public static BadCreateExpeditionCommandException expeditionIsPresent() {
        BadCreateExpeditionCommandException exception = new BadCreateExpeditionCommandException();
        exception.expeditionIsPresent = true;
        return exception;
    }
}
