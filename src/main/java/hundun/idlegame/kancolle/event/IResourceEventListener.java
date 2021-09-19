package hundun.idlegame.kancolle.event;

import java.util.Map;

import hundun.idlegame.kancolle.data.SessionData;
import hundun.idlegame.kancolle.resource.ResourceModel;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public interface IResourceEventListener {
    void onResourceChanged(SessionData sessionData, Map<String, Integer> delta,
            Map<String, ResourceModel> resources);
}
