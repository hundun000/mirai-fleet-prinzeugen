package hundun.idlegame.kancolle.event;

import java.util.List;
import java.util.Map;

import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.resource.Resource;
import hundun.idlegame.kancolle.resource.ResourceBoard;
import hundun.idlegame.kancolle.world.SessionData;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public interface IResourceEventListener {
    void amountChanged(SessionData sessionData, Map<Resource, Integer> delta, ResourceBoard resourceBoard);
}
