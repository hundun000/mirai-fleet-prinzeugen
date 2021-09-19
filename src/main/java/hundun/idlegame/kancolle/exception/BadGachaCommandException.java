package hundun.idlegame.kancolle.exception;

import java.util.List;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import hundun.idlegame.kancolle.expedition.Requirement;
import hundun.idlegame.kancolle.resource.ResourcePrototype;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/09/13
 */
@SuppressWarnings("serial")
public class BadGachaCommandException extends IdleGameException {

    @Getter
    boolean inputAmoutTooSmall;
    
    @Getter
    List<ResourcePrototype> notEnoughPrototypes;

    
    public static BadGachaCommandException resourcesNotEnough(List<ResourcePrototype> notEnoughPrototypes) {
        BadGachaCommandException exception = new BadGachaCommandException();
        exception.notEnoughPrototypes = notEnoughPrototypes;
        return exception;
    }

    public static BadGachaCommandException inputAmoutTooSmall() {
        BadGachaCommandException exception = new BadGachaCommandException();
        exception.inputAmoutTooSmall = true;
        return exception;
    }

}
