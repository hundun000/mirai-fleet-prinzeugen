package hundun.idlegame.kancolle;

import hundun.idlegame.kancolle.container.IGameContainer;
import hundun.idlegame.kancolle.world.GameException;
import hundun.idlegame.kancolle.world.GameWorld;

/**
 * @author hundun
 * Created on 2021/09/03
 */
public class DemoGameContainer implements IGameContainer {
    public static void main(String[] args) throws GameException {
        DemoGameContainer gameContainer = new DemoGameContainer();
        GameWorld world = new GameWorld(gameContainer);
        String sessionId = "123";
        world.commandStartGame(sessionId);
        world.commandShowData(sessionId);

        world.commandCreateExpedition(sessionId, "A1", "吹雪");
        world.commandTick(sessionId);
        world.commandShowData(sessionId);
        
        world.commandTick(sessionId);
        world.commandShowData(sessionId);
        world.commandTick(sessionId);
        world.commandShowData(sessionId);
    }

    @Override
    public void handleLog(String sessionId, String msg) {
        System.out.println(msg);
    }

    @Override
    public void handleExportEvent(String sessionId, String data) {
        System.out.println(data);
    }
}
