package hundun.idlegame.kancolle.ship;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import hundun.idlegame.kancolle.DemoGameContainer;
import hundun.idlegame.kancolle.TestWorldConfig;
import hundun.idlegame.kancolle.container.CommandResult;
import hundun.idlegame.kancolle.ship.GachaMachine.GachaResult;
import hundun.idlegame.kancolle.world.GameWorld;

/**
 * @author hundun
 * Created on 2021/09/18
 */
public class GachaMachineTest {

    DemoGameContainer gameContainer = new DemoGameContainer(true);
    GameWorld world;
    String sessionId;
    CommandResult<Void> voidResult;
    
    @Before
    public void before() {
        System.out.println("====== new @test start ======");
        world = new GameWorld(gameContainer, new TestWorldConfig());
        sessionId = "123";
    }
    
    @Test
    public void test() {
        GachaMachine gachaMachine = world.getContext().getGachaMachine();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int[] cost = new int[]{30 + random.nextInt(100), 30 + random.nextInt(100), 30 + random.nextInt(100), 20 + random.nextInt(100)};
            GachaResult result = gachaMachine.gachaShip(cost, 3);
            System.out.println("消耗资源: " + Arrays.toString(cost) + ", 抽卡结果: " + result.getPayload().getId());
        }
        
    }

}
