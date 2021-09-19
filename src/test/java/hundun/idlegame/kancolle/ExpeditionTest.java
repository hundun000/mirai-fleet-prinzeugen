package hundun.idlegame.kancolle;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import hundun.idlegame.kancolle.building.instance.BuidingId;
import hundun.idlegame.kancolle.building.instance.ExpeditionBuilding;
import hundun.idlegame.kancolle.container.CommandResult;
import hundun.idlegame.kancolle.container.DemoGameContainer;
import hundun.idlegame.kancolle.data.config.UnittestWorldConfig;
import hundun.idlegame.kancolle.exception.BadCreateExpeditionCommandException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.format.ExceptionFormatter;
import hundun.idlegame.kancolle.world.GameWorld;

/**
 * @author hundun
 * Created on 2021/09/13
 */
public class ExpeditionTest {

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
    public void testExpeditionTargetBuildingException() throws IdleGameException {
        world.commandStartGame(sessionId);
        
        try {
            world.commandCreateExpedition(sessionId, 
                    UnittestWorldConfig.EASY_EXPEDITION_ID, 
                    UnittestWorldConfig.WEAK_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            BadCreateExpeditionCommandException childException = (BadCreateExpeditionCommandException)e;
            assertEquals(BuidingId.EXPEDITION_BUILDING.getIdText(), childException.getTargetBuildingId());
            gameContainer.printIdleGameExceptionAdvice(e, world.getContext().getExceptionFormatter());
        }
    }
    
    @Test
    public void testException() throws IdleGameException {

        world.commandStartGame(sessionId);
        
        world.commandShipMoveToBuilding(sessionId, BuidingId.EXPEDITION_BUILDING.getIdText(), UnittestWorldConfig.WEAK_SHIP_ID);
        world.commandShipMoveToBuilding(sessionId, BuidingId.EXPEDITION_BUILDING.getIdText(), UnittestWorldConfig.HIGH_POWER_SHIP_ID);
        
        // Assert check wrong shipId
        try {
            world.commandCreateExpedition(sessionId, 
                    UnittestWorldConfig.EASY_EXPEDITION_ID, 
                    UnittestWorldConfig.WRONG_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            PrototypeNotFoundException childException = (PrototypeNotFoundException)e;
            assertEquals(UnittestWorldConfig.WRONG_SHIP_ID, childException.getProtoypeId());
            gameContainer.printIdleGameExceptionAdvice(e, world.getContext().getExceptionFormatter());
        }
        
        // Assert check wrong expeditionId
        try {
            world.commandCreateExpedition(sessionId, 
                    UnittestWorldConfig.WRONG_EXPEDITION_ID, 
                    UnittestWorldConfig.WEAK_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            PrototypeNotFoundException childException = (PrototypeNotFoundException)e;
            assertEquals(UnittestWorldConfig.WRONG_EXPEDITION_ID, childException.getProtoypeId());
            gameContainer.printIdleGameExceptionAdvice(e, world.getContext().getExceptionFormatter());
        }
        
        // Assert not match expedition-level-Requirement
        try {
            world.commandCreateExpedition(sessionId,
                    UnittestWorldConfig.HIGH_LEVEL_REQUIREMENT_EXPEDITION_ID, 
                    UnittestWorldConfig.WEAK_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            BadCreateExpeditionCommandException childException = (BadCreateExpeditionCommandException)e;
            assertEquals(true, childException.getRequirement() != null);
            gameContainer.printIdleGameExceptionAdvice(e, world.getContext().getExceptionFormatter());
        }
        
     // Assert not match expedition-power-Requirement
        try {
            world.commandCreateExpedition(sessionId,
                    UnittestWorldConfig.HIGH_POWER_REQUIREMENT_EXPEDITION_ID, 
                    UnittestWorldConfig.WEAK_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            BadCreateExpeditionCommandException childException = (BadCreateExpeditionCommandException)e;
            assertEquals(true, childException.getRequirement() != null);
            gameContainer.printIdleGameExceptionAdvice(e, world.getContext().getExceptionFormatter());
        }
        
        voidResult = world.commandCreateExpedition(sessionId,
                UnittestWorldConfig.EASY_EXPEDITION_ID, 
                UnittestWorldConfig.WEAK_SHIP_ID);
        assertEquals(true, voidResult.isSuccess());
        
        // Assert cannot create another Expedition with a busy ship
        try {
            world.commandCreateExpedition(sessionId, 
                    UnittestWorldConfig.EASY_EXPEDITION_ID_2, 
                    UnittestWorldConfig.WEAK_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            BadCreateExpeditionCommandException childException = (BadCreateExpeditionCommandException)e;
            assertEquals(UnittestWorldConfig.WEAK_SHIP_ID, childException.getBusyShipIds().get(0));
            gameContainer.printIdleGameExceptionAdvice(e, world.getContext().getExceptionFormatter());
        }
        
        // Assert cannot create same Expedition at same time
        try {
            world.commandCreateExpedition(sessionId, 
                    UnittestWorldConfig.EASY_EXPEDITION_ID, 
                    UnittestWorldConfig.HIGH_POWER_SHIP_ID);
            Assert.fail();
        } catch (IdleGameException e) {
            BadCreateExpeditionCommandException childException = (BadCreateExpeditionCommandException)e;
            assertEquals(true, childException.isExpeditionPresent());
            gameContainer.printIdleGameExceptionAdvice(e, world.getContext().getExceptionFormatter());
        }

    }
    
    @Test
    public void testExpeditionCompleted() throws IdleGameException {
        

        world.commandStartGame(sessionId);
        
        world.commandShipMoveToBuilding(sessionId, BuidingId.EXPEDITION_BUILDING.getIdText(), UnittestWorldConfig.WEAK_SHIP_ID);
        
        voidResult = world.commandCreateExpedition(sessionId,
                UnittestWorldConfig.EASY_EXPEDITION_ID, 
                UnittestWorldConfig.WEAK_SHIP_ID);
        assertEquals(true, voidResult.isSuccess());
        world.commandTick(sessionId);
        world.commandTick(sessionId);
        world.commandTick(sessionId);
        world.commandTick(sessionId);

    }
    
    

}
