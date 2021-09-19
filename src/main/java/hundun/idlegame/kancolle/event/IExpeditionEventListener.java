package hundun.idlegame.kancolle.event;

import java.util.List;

import hundun.idlegame.kancolle.data.SessionData;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.expedition.ExpeditionModel;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public interface IExpeditionEventListener {
    void onExpeditionCompleted(SessionData sessionData, List<ExpeditionModel> completedTasks) throws IdleGameException;
}
