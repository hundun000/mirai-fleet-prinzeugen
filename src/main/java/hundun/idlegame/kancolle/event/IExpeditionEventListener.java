package hundun.idlegame.kancolle.event;

import java.util.List;

import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.world.SessionData;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public interface IExpeditionEventListener {
    void onExpeditionCompleted(SessionData sessionData, List<ExpeditionModel> completedTasks);
}
