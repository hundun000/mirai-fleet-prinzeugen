package hundun.idlegame.kancolle.exception;

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
