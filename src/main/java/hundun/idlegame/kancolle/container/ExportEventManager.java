package hundun.idlegame.kancolle.container;

import java.util.List;
import hundun.idlegame.kancolle.DescriptionHelper;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.IExpeditionEventListener;
import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.world.BaseManager;
import hundun.idlegame.kancolle.world.DataBus;
import hundun.idlegame.kancolle.world.SessionData;

/**
 * @author hundun
 * Created on 2021/09/06
 */
public class ExportEventManager extends BaseManager implements IExpeditionEventListener {

    
    public ExportEventManager(EventBus eventBus, DataBus dataBus) {
        super(eventBus, dataBus);
    }

    @Override
    public void onExpeditionCompleted(SessionData sessionData, List<ExpeditionModel> completedTasks) {
        for (ExpeditionModel model : completedTasks) {
            String desExpeditionCompleted = DescriptionHelper.desExpeditionCompleted(model);
            eventBus.sendExportEvent(sessionData.getId(), desExpeditionCompleted);
        }
    }


}
