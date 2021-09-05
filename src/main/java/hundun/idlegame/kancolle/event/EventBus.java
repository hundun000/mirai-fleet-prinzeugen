package hundun.idlegame.kancolle.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.container.IGameContainer;
import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.resource.Resource;
import hundun.idlegame.kancolle.resource.ResourceBoard;
import hundun.idlegame.kancolle.world.SessionData;
import lombok.extern.slf4j.Slf4j;



/**
 * @author hundun
 * Created on 2021/09/01
 */
public class EventBus {
    List<IResourceEventListener> resourceEventListeners = new ArrayList<>();
    List<IExpeditionEventListener> expeditionTaskEventListeners = new ArrayList<>();
    List<IClockEventListener> clockEventListeners = new ArrayList<>();

    IGameContainer container;
    
    public EventBus(IGameContainer container) {
        this.container = container;
    }


    public void log(String sessionId, LogTag tag, String msg) {
        msg = "[" + tag + "]" + msg;
        container.handleLog(sessionId, msg);
    }
    
    public void log(String sessionId, LogTag tag, String msg, Object... args) {
        int index = 0;
        while (msg.contains("{}") && index < args.length) {
            msg = msg.replaceFirst("\\{\\}", args[index].toString());
            index++;
        }
        log(sessionId, tag, msg);
    }

    public void register(Object object) {
        if (object instanceof IResourceEventListener && !resourceEventListeners.contains(object)) {
            resourceEventListeners.add((IResourceEventListener) object);
        }
        if (object instanceof IExpeditionEventListener && !expeditionTaskEventListeners.contains(object)) {
            expeditionTaskEventListeners.add((IExpeditionEventListener) object);
        }
        if (object instanceof IClockEventListener && !clockEventListeners.contains(object)) {
            clockEventListeners.add((IClockEventListener) object);
        }
    }
    
    public void sendExpeditionTaskEvent(SessionData sessionData, List<ExpeditionModel> completedTasks) {
        String info = completedTasks.stream().map(task -> task.getPrototype().getId()).collect(Collectors.joining("\n"));
        log(sessionData.getId(), LogTag.EVENT, "ExpeditionTask.completed: " + info);
        for (IExpeditionEventListener listener : expeditionTaskEventListeners) {
            listener.onExpeditionCompleted(sessionData, completedTasks);
        }
    }

    public void sendClockEvent(SessionData sessionData) {
        log(sessionData.getId(), LogTag.EVENT, "ClockEvent.tick");
        for (IClockEventListener listener : clockEventListeners) {
            listener.tick(sessionData);
        }
    }

    public void sendResourceChangeEvent(SessionData sessionData, Map<Resource, Integer> delta,
            ResourceBoard resourceBoard) {
        log(sessionData.getId(), LogTag.EVENT, "ResourceEvent.amountChanged: +" + delta + " -> " + resourceBoard);
        for (IResourceEventListener listener : resourceEventListeners) {
            listener.amountChanged(sessionData, delta, resourceBoard);
        }
    }

}
