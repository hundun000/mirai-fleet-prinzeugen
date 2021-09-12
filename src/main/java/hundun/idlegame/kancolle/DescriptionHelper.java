package hundun.idlegame.kancolle;

import java.util.Map;
import java.util.Map.Entry;

import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.expedition.Reward;
import hundun.idlegame.kancolle.resource.ResourceFactory;
import hundun.idlegame.kancolle.resource.ResourceModel;
import hundun.idlegame.kancolle.resource.ResourcePrototype;

/**
 * @author hundun
 * Created on 2021/09/10
 */
public class DescriptionHelper {

    public static String desExpeditionCompleted(ExpeditionModel model) {
        StringBuilder builder = new StringBuilder()
                .append(model.getPrototype().getId()).append("远征归来").append("\n")
//                .append("获得").append(ResourceManager.des(model.getPrototype().getResourceRewards())).append("\n")
//                .append(model.getShipIds().stream().collect(Collectors.joining(",")))
//                .append(" +").append(model.getPrototype().getRewardExp()).append("经验").append("\n");
                ;
        return builder.toString();
    }

    public static String desExpeditionReward(Reward reward) throws PrototypeNotFoundException {
        StringBuilder builder = new StringBuilder();
        if (reward.getResources() != null) {
            builder.append(desResourceDelta(reward.getResources())).append(" ");
        }
        return builder.toString();
    }
    
    public static String desResourceDelta(Map<String, Integer> resourceMap) throws PrototypeNotFoundException {
        StringBuilder builder = new StringBuilder()
                .append("资源:");
                
        for (Entry<String, Integer> entry : resourceMap.entrySet()) {
            ResourcePrototype prototype = ResourceFactory.INSTANCE.getPrototype(entry.getKey());
            builder.append(prototype.getName()).append(entry.getValue()).append(" ");
        }


        return builder.toString();
    }
    
    public static String desResource(Map<String, ResourceModel> resources) {
        StringBuilder builder = new StringBuilder()
                .append("资源:");
                
        resources.forEach((resourceId, model) -> {
            builder.append(model.getPrototype().getName()).append(model.getAmount().toString()).append(" ");
        });

        return builder.toString();
    }
}
