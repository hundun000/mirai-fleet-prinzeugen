package hundun.idlegame.kancolle.expedition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.IClockEventListener;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.exception.BadCreateExpeditionCommandException;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.SimpleExceptionAdvice;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.world.BaseManager;
import hundun.idlegame.kancolle.world.DataBus;
import hundun.idlegame.kancolle.world.SessionData;
import hundun.idlegame.kancolle.world.TimerManager;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public class ExpeditionManager extends BaseManager implements IClockEventListener {

    

    public ExpeditionManager(EventBus eventBus, DataBus dataBus) {
        super(eventBus, dataBus);
    }
    
    public String overviewExpeditions(SessionData sessionData) {
        String info = "远征列表:\n" + sessionData.getExpeditions().stream().map(model -> describeExpedition(model)).collect(Collectors.joining("\n"));
        return info;
    }
    
    public String describeExpedition(ExpeditionModel model) {
        int remainHour = TimerManager.tickToHour(model.remainTick);
        StringBuilder builder = new StringBuilder()
            .append(model.getPrototype().getId()).append(" ")
            .append("剩余时间: ").append(remainHour).append("时 ")
            .append("成员: ").append(model.getShipIds().stream().collect(Collectors.joining(","))).append(" ")
            ;
        return builder.toString();
    }
    
    private boolean checkRequirement(Requirement requirement, List<ShipModel> ships) {
        if (requirement != null) {
            boolean checkSumLevel = requirement.getSumLevel() != null ? ships.stream().mapToInt(ship -> ship.getLevel()).sum() > requirement.getSumLevel() : true;
            if (!checkSumLevel) {
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
        if (checkRequirement(prototype.getRequirement(), ships)) {
            throw BadCreateExpeditionCommandException.requirementNotMatch(prototype.getRequirement());
        }
        List<String> shipIds = ShipFactory.INSTANCE.listModelToId(ships);
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
            
            for (ExpeditionModel task : completedTasks) {
                try {
                    handleReward(sessionData, task.getShipIds(), task.getPrototype().getNormalReward());
                    boolean isFirstTime = !sessionData.getCompletedExpeditions().contains(task);
                    if (isFirstTime) {
                        handleReward(sessionData, task.getShipIds(), task.getPrototype().getFirstTimeReward());
                    }
                } catch (IdleGameException e) {
                    eventBus.log(sessionData.getId(), LogTag.ERROR, "ExpeditionModel handleReward error:" + SimpleExceptionAdvice.INSTANCE.exceptionToMessage(e));
                }
                dataBus.releaseShip(sessionData, task.getShipIds());
            }
            eventBus.sendExpeditionCompletedEvent(sessionData, completedTasks);
            
            sessionData.getExpeditions().removeAll(completedTasks);
//            for (ExpeditionModel task : completedTasks) {
//                if (task.getRepeat() == INFINITE_REPEAT || task.getRepeat() > 0) {
//                    createExpedition(sessionData, task.getPrototype(), task.getShipIds(), task.getRepeat());
//                }
//            }
        }
    }
    
    private void handleReward(SessionData sessionData, List<String> shipIds, Reward reward) throws IdleGameException {
        if (reward.getResources() != null) {
            dataBus.resourceMerge(sessionData, reward.getResources());
        }
        if (reward.getExp() != 0) {
            dataBus.shipAddExp(sessionData, shipIds, reward.getExp());
        }
        if (reward.getShipId() != null) {
            dataBus.addNewShip(sessionData, reward.getShipId());
        }
    }

}
