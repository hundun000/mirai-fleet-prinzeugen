package hundun.idlegame.kancolle;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import hundun.idlegame.kancolle.building.instance.BuidingId;
import hundun.idlegame.kancolle.building.instance.GachaBuilding;
import hundun.idlegame.kancolle.building.instance.GachaBuilding.GachaResult;
import hundun.idlegame.kancolle.container.CommandResult;
import hundun.idlegame.kancolle.container.DemoGameContainer;
import hundun.idlegame.kancolle.data.config.UnittestWorldConfig;
import hundun.idlegame.kancolle.exception.BadGachaCommandException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.world.GameWorld;

/**
 * @author hundun
 * Created on 2021/09/18
 */
public class GachaShipTest {

    DemoGameContainer gameContainer = new DemoGameContainer(true);
    GameWorld world;
    String sessionId;
    CommandResult<Void> voidResult;
     
    @Before
    public void before() {
        System.out.println("====== new @test start ======");
        world = new GameWorld(gameContainer);
        sessionId = "123";
    }
    
    @Test
    public void test() throws IdleGameException {
        world.commandStartGame(sessionId);

        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int[] cost = new int[]{30 + random.nextInt(100), 30 + random.nextInt(100), 30 + random.nextInt(100), 30 + random.nextInt(10)};
            voidResult = world.commandGachaShip(sessionId, cost);
            System.out.println(voidResult.getAdviceMessage());
        }
        
        world.commandShipMoveToBuilding(sessionId, BuidingId.GACHA_BUILDING.getIdText(), UnittestWorldConfig.WEAK_SHIP_ID);
        world.commandShipMoveToBuilding(sessionId, BuidingId.GACHA_BUILDING.getIdText(), UnittestWorldConfig.HIGH_POWER_SHIP_ID);
        
        for (int i = 0; i < 3; i++) {
            int[] cost = new int[]{30 + random.nextInt(100), 30 + random.nextInt(100), 30 + random.nextInt(100), 30 + random.nextInt(10)};
            voidResult = world.commandGachaShip(sessionId, cost);
            System.out.println(voidResult.getAdviceMessage());
        }
        
        
    }
    
    @Test
    public void testFail() throws IdleGameException {
        
        world.commandStartGame(sessionId);
        
        try {
            int[] cost = new int[]{1, 1, 1, 1};
            world.commandGachaShip(sessionId, cost);
            Assert.fail();
        } catch (IdleGameException e) {
            BadGachaCommandException childException = (BadGachaCommandException)e;
            assertEquals(true, childException.isInputAmoutTooSmall());
            gameContainer.printIdleGameExceptionAdvice(e, world.getContext().getExceptionFormatter());
        }
        
        try {
            int[] cost = new int[]{100000, 100000, 100000, 10};
            world.commandGachaShip(sessionId, cost);
            Assert.fail();
        } catch (IdleGameException e) {
            BadGachaCommandException childException = (BadGachaCommandException)e;
            assertEquals(true, childException.getNotEnoughPrototypes().size() > 0);
            gameContainer.printIdleGameExceptionAdvice(e, world.getContext().getExceptionFormatter());
        }
        
    }

}
