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
public class BadMoveToBuildingCommandException extends IdleGameException {

    @Getter
    String busyShipId;
    
    public BadMoveToBuildingCommandException() {
    }
    
    public static BadMoveToBuildingCommandException shipBusy(String busyShipId) {
        BadMoveToBuildingCommandException exception = new BadMoveToBuildingCommandException();
        exception.busyShipId = busyShipId;
        return exception;
    }

}
