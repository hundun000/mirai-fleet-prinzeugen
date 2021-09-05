package hundun.idlegame.kancolle.container;

import java.util.List;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.event.IExpeditionEventListener;
import hundun.idlegame.kancolle.event.LogTag;
import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.resource.ResourceManager;
import hundun.idlegame.kancolle.ship.ShipFactory;
import hundun.idlegame.kancolle.world.BaseManager;
import hundun.idlegame.kancolle.world.SessionData;

/**
 * @author hundun
 * Created on 2021/09/06
 */
public class ExportEventManager extends BaseManager implements IExpeditionEventListener {

    IGameContainer gameContainer;
    
    public ExportEventManager(EventBus eventBus, IGameContainer gameContainer) {
        super(eventBus);
        this.gameContainer = gameContainer;
    }

    @Override
    public void onExpeditionCompleted(SessionData sessionData, List<ExpeditionModel> completedTasks) {
        for (ExpeditionModel model : completedTasks) {
            StringBuilder builder = new StringBuilder()
                .append(model.getPrototype().getId()).append("远征归来").append("\n")
                .append("获得").append(ResourceManager.des(model.getPrototype().getResourceRewards())).append("\n")
                .append(model.getShipIds().stream().collect(Collectors.joining(",")))
.append(" +").append(model.getPrototype().getRewardExp()).append("经验").append("\n");
                ;
            gameContainer.handleExportEvent(sessionData.getId(), builder.toString());
        }
    }


}
