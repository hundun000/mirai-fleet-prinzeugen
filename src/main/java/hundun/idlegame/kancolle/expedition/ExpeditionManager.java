package hundun.idlegame.kancolle.expedition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.container.GameSaveData;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.IClockEventListener;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.resource.Resource;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.ship.ShipPrototype;
import hundun.idlegame.kancolle.world.BaseManager;
import hundun.idlegame.kancolle.world.SessionData;
import hundun.idlegame.kancolle.world.TimerManager;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public class ExpeditionManager extends BaseManager implements IClockEventListener {

    
    
    public ExpeditionManager(EventBus eventBus) {
        super(eventBus);
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
    
    public boolean createExpedition(SessionData sessionData, ExpeditionPrototype prototype, List<String> shipIds) {
        boolean isPresent = sessionData.getExpeditions().stream().filter(model -> model.getPrototype().equals(prototype)).findFirst().isPresent();
        if (isPresent) {
            return false;
        }
        ExpeditionModel task = new ExpeditionModel();
        task.setPrototype(prototype);
        task.setRemainTick(prototype.tick);
        task.setShipIds(shipIds);
        sessionData.getExpeditions().add(task);
        return true;
    }

    @Override
    public void tick(SessionData sessionData) {

        List<ExpeditionModel> completedTasks = new ArrayList<>();
        for (ExpeditionModel runningTask : sessionData.getExpeditions()) {
            runningTask.remainTick--;
            eventBus.log(sessionData.getId(), LogTag.EXPEDITION, "task {} remainTick change to {}", runningTask.getPrototype().getId(), runningTask.remainTick);
            if (runningTask.remainTick == 0) {
                completedTasks.add(runningTask);
            }
        }
        
        if (!completedTasks.isEmpty()) {
            sessionData.getExpeditions().removeAll(completedTasks);
            eventBus.sendExpeditionTaskEvent(sessionData, completedTasks);
        }
    }

}
