package hundun.idlegame.kancolle.container;

import java.io.File;
import java.io.IOException;

import hundun.idlegame.kancolle.data.config.UnittestWorldConfig;
import hundun.idlegame.kancolle.data.config.WorldConfig;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.format.ExceptionFormatter;
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
    
    public void printIdleGameExceptionAdvice(IdleGameException e, ExceptionFormatter exceptionFormatter) {
        System.out.println("ContainerShowException: " + exceptionFormatter.exceptionToMessage(e));
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

    @Override
    public WorldConfig provideWorldConfig() {
        try {
            WorldConfig worldConfig = GameWorld.readWorldConfigFile(new File("data/hundun.fleet.prinzeugen/镇守府/WorldConfig.json"));
            UnittestWorldConfig unittestWorldConfig = new UnittestWorldConfig(worldConfig);
            return unittestWorldConfig;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
