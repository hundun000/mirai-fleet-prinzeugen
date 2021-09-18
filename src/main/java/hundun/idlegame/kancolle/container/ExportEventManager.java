package hundun.idlegame.kancolle.container;

import java.util.List;
import java.util.Map;

import hundun.idlegame.kancolle.base.BaseManager;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.IExpeditionEventListener;
import hundun.idlegame.kancolle.event.IResourceEventListener;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.format.DescriptionFormatter;
import hundun.idlegame.kancolle.resource.ResourceModel;
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
    public void onExpeditionCompleted(SessionData sessionData, List<ExpeditionModel> completedTasks) throws IdleGameException {
        for (ExpeditionModel model : completedTasks) {
            String desExpeditionCompleted = DescriptionFormatter.desExpeditionCompleted(model);
            eventBus.sendExportEvent(sessionData.getId(), desExpeditionCompleted);
        }
    }




}
