package hundun.idlegame.kancolle;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hundun.idlegame.kancolle.container.CommandResult;
import hundun.idlegame.kancolle.exception.BadCreateExpeditionCommandException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.format.SimpleExceptionFormatter;
import hundun.idlegame.kancolle.world.GameWorld;

/**
 * @author hundun
 * Created on 2021/09/13
 */
public class DemoGameContainerTest {

    DemoGameContainer gameContainer = new DemoGameContainer(false);
    GameWorld world;
    String sessionId;
    CommandResult<Void> voidResult;
    
    @Before
    public void before() {
        world = new GameWorld(gameContainer, new TestWorldConfig());
        sessionId = "123";
    }
    
    private void printIdleGameExceptionAdvice(IdleGameException e) {
        System.out.println("Container: " + world.getExceptionAdvice().exceptionToMessage(e));
    }

    
    @Test
    public void testException() throws IdleGameException {

        world.commandStartGame(sessionId);
        
        // Assert check wrong shipId
        try {
            world.commandCreateExpedition(sessionId, 
                    TestWorldConfig.EASY_EXPEDITION_ID, 
                    TestWorldConfig.WRONG_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            PrototypeNotFoundException childException = (PrototypeNotFoundException)e;
            assertEquals(TestWorldConfig.WRONG_SHIP_ID, childException.getProtoypeId());
            printIdleGameExceptionAdvice(e);
        }
        
        // Assert check wrong expeditionId
        try {
            world.commandCreateExpedition(sessionId, 
                    TestWorldConfig.WRONG_EXPEDITION_ID, 
                    TestWorldConfig.WEAK_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            PrototypeNotFoundException childException = (PrototypeNotFoundException)e;
            assertEquals(TestWorldConfig.WRONG_EXPEDITION_ID, childException.getProtoypeId());
            printIdleGameExceptionAdvice(e);
        }
        
        // Assert not match expedition-level-Requirement
        try {
            world.commandCreateExpedition(sessionId,
                    TestWorldConfig.HIGH_LEVEL_REQUIREMENT_EXPEDITION_ID, 
                    TestWorldConfig.WEAK_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            BadCreateExpeditionCommandException childException = (BadCreateExpeditionCommandException)e;
            assertEquals(true, childException.getRequirement() != null);
            printIdleGameExceptionAdvice(e);
        }
        
        voidResult = world.commandCreateExpedition(sessionId,
                TestWorldConfig.EASY_EXPEDITION_ID, 
                TestWorldConfig.WEAK_SHIP_ID);
        assertEquals(true, voidResult.isSuccess());
        
        // Assert cannot create another Expedition with a busy ship
        try {
            world.commandCreateExpedition(sessionId, 
                    TestWorldConfig.EASY_EXPEDITION_ID_2, 
                    TestWorldConfig.WEAK_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            BadCreateExpeditionCommandException childException = (BadCreateExpeditionCommandException)e;
            assertEquals(TestWorldConfig.WEAK_SHIP_ID, childException.getBusyShipIds().get(0));
            printIdleGameExceptionAdvice(e);
        }
        
        // Assert cannot create same Expedition at same time
        try {
            world.commandCreateExpedition(sessionId, 
                    TestWorldConfig.EASY_EXPEDITION_ID, 
                    TestWorldConfig.HIGH_POWER_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            BadCreateExpeditionCommandException childException = (BadCreateExpeditionCommandException)e;
            assertEquals(true, childException.isExpeditionPresent());
            printIdleGameExceptionAdvice(e);
        }

    }
    
    @Test
    public void testExpeditionCompleted() throws IdleGameException {
        

        world.commandStartGame(sessionId);
        
        
        voidResult = world.commandCreateExpedition(sessionId,
                TestWorldConfig.EASY_EXPEDITION_ID, 
                TestWorldConfig.WEAK_SHIP_ID);
        assertEquals(true, voidResult.isSuccess());
        world.commandTick(sessionId);
        world.commandTick(sessionId);
        world.commandTick(sessionId);
        world.commandTick(sessionId);

    }
    
    

}
