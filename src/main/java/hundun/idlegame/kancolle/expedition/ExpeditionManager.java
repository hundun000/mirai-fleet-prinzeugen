package hundun.idlegame.kancolle.expedition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.base.BaseManager;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.IClockEventListener;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.exception.BadCreateExpeditionCommandException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.format.DescriptionFormatter;
import hundun.idlegame.kancolle.format.SimpleExceptionFormatter;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.time.TimerManager;
import hundun.idlegame.kancolle.world.ComponentContext;
import hundun.idlegame.kancolle.world.DataBus;
import hundun.idlegame.kancolle.world.SessionData;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public class ExpeditionManager extends BaseManager implements IClockEventListener {

    

    public ExpeditionManager(ComponentContext context) {
        super(context);
    }

    
    
    
    private boolean checkRequirement(Requirement requirement, List<ShipModel> ships) {
        if (requirement != null) {
            boolean checkSumLevel = requirement.getShipLevel() != null ? ships.stream().mapToInt(ship -> ship.getLevel()).sum() >= requirement.getShipLevel() : true;
            if (!checkSumLevel) {
                return false;
            }
            boolean checkSumPower = requirement.getShipPower() != null ? ships.stream().mapToInt(ship -> ship.calculatePower()).sum() >= requirement.getShipPower() : true;
            if (!checkSumPower) {
                return false;
            }
        }
        return true;
    }
    
    
    
    public void createExpedition(SessionData sessionData, ExpeditionPrototype prototype, List<ShipModel> ships) throws IdleGameException {
        boolean isPresent = sessionData.getExpeditions().stream().filter(model -> model.getPrototype().equals(prototype)).findFirst().isPresent();
        if (isPresent) {
            throw BadCreateExpeditionCommandException.expeditionIsPresent();
        }
        boolean requirementMatch = checkRequirement(prototype.getRequirement(), ships);
        if (!requirementMatch) {
            throw BadCreateExpeditionCommandException.requirementNotMatch(prototype.getRequirement());
        }
        List<String> shipIds = context.getShipFactory().listModelToId(ships);
        ExpeditionModel task = new ExpeditionModel();
        task.setPrototype(prototype);
        task.setRemainTick(prototype.tick);
        task.setShipIds(shipIds);
        sessionData.getExpeditions().add(task);
    }

    @Override
    public void tick(SessionData sessionData) {

        List<ExpeditionModel> completedTasks = new ArrayList<>();
        for (ExpeditionModel runningTask : sessionData.getExpeditions()) {
            runningTask.remainTick--;
            eventBus.log(sessionData.getId(), LogTag.EXPEDITION, "task {} remainTick change to {}", runningTask.getPrototype().getId(), runningTask.remainTick);
            if (runningTask.remainTick == 0) {
//                if (runningTask.getRepeat() != INFINITE_REPEAT) {
//                    runningTask.setRepeat(runningTask.getRepeat() - 1);
//                }
                completedTasks.add(runningTask);
            }
        }
        if (!completedTasks.isEmpty()) {
            handleComplete(sessionData, completedTasks);
        }
    }
    
    private void handleComplete(SessionData sessionData, List<ExpeditionModel> completedTasks)   {

        try {
            for (ExpeditionModel task : completedTasks) {
                eventBus.log(sessionData.getId(), LogTag.EXPEDITION, "handle completedTask: {}", task.getPrototype().getId());
                handleReward(sessionData, task.getShipIds(), task.getPrototype().getNormalReward());
                dataBus.shipBackToBuilding(sessionData, task.getShipIds());
            }
            eventBus.sendExpeditionCompletedEvent(sessionData, completedTasks);
            
            sessionData.getExpeditions().removeAll(completedTasks);
            eventBus.log(sessionData.getId(), LogTag.EXPEDITION, "handle all completedTask done");
        } catch (IdleGameException e) {
            eventBus.log(sessionData.getId(), LogTag.ERROR, "ExpeditionModel handleReward error:" + dataBus.getExceptionAdvice().exceptionToMessage(e));
        }


    }
    
    private void handleReward(SessionData sessionData, List<String> shipIds, Reward reward) throws IdleGameException {
        if (reward == null) {
           return; 
        }
        
        if (reward.getResources() != null) {
            dataBus.resourceMerge(sessionData, reward.getResources(), 1);
        }
        if (reward.getExp() != 0) {
            dataBus.shipAddExp(sessionData, shipIds, reward.getExp());
        }
        if (reward.getShipId() != null) {
            dataBus.addNewShip(sessionData, reward.getShipId());
        }
    }

}
