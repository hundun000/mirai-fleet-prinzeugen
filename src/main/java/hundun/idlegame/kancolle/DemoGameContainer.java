package hundun.idlegame.kancolle;

import hundun.idlegame.kancolle.container.IGameContainer;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.format.SimpleExceptionFormatter;
import hundun.idlegame.kancolle.world.GameWorld;

/**
 * @author hundun
 * Created on 2021/09/03
 */
public class DemoGameContainer implements IGameContainer {
    
    boolean printGameLog;
    
    public DemoGameContainer(boolean printGameLog) {
        this.printGameLog = printGameLog;
    }

    @Override
    public void handleLog(String sessionId, String msg) {
        if (printGameLog) {
            System.out.println("ContainerGameLog: " + msg);
        }
    }

    @Override
    public void handleExportEvent(String sessionId, String data) {
        System.out.println("ContainerExportEvent: " + data);
    }
}
