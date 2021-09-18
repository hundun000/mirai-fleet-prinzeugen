package hundun.idlegame.kancolle.event;

import java.util.Map;

import hundun.idlegame.kancolle.resource.ResourceModel;
import hundun.idlegame.kancolle.world.SessionData;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public interface IResourceEventListener {
    void onResourceChanged(SessionData sessionData, Map<String, Integer> delta,
            Map<String, ResourceModel> resources);
}
