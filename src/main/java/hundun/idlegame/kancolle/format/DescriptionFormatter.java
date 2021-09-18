package hundun.idlegame.kancolle.format;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import hundun.idlegame.kancolle.expedition.ExpeditionModel;
import hundun.idlegame.kancolle.expedition.Reward;
import hundun.idlegame.kancolle.resource.ResourceFactory;
import hundun.idlegame.kancolle.resource.ResourceModel;
import hundun.idlegame.kancolle.resource.ResourcePrototype;
import hundun.idlegame.kancolle.ship.ShipModel;
import hundun.idlegame.kancolle.ship.ShipWorkStatus;
import hundun.idlegame.kancolle.time.GameCalendar;
import hundun.idlegame.kancolle.time.TimerManager;
import hundun.idlegame.kancolle.world.GameWorld;
import hundun.idlegame.kancolle.world.SessionData;

/**
 * @author hundun
 * Created on 2021/09/10
 */
public class DescriptionFormatter {

    
    public static String desExpedition(ExpeditionModel model) {
        int remainHour = TimerManager.tickToHour(model.getRemainTick());
        StringBuilder builder = new StringBuilder()
            .append(model.getPrototype().getId()).append(" ")
            .append("剩余时间:").append(remainHour).append("时 ")
            .append("成员: ").append(model.getShipIds().stream().collect(Collectors.joining(","))).append(" ")
            ;
        return builder.toString();
    }
    
    public static String desExpeditionCompleted(ExpeditionModel model) throws PrototypeNotFoundException {
        StringBuilder builder = new StringBuilder();
        builder.append(model.getPrototype().getId()).append("远征归来").append("\n");
        builder.append("成员--").append(model.getShipIds().stream().collect(Collectors.joining(","))).append("\n");
        if (model.getPrototype().getNormalReward() != null) {
            builder.append("奖励--").append(desReward(model.getPrototype().getNormalReward())).append("\n");
        }
        return builder.toString();
    }

    private static String desReward(Reward reward) throws PrototypeNotFoundException {
        StringBuilder builder = new StringBuilder();
        if (reward.getResources() != null) {
            builder.append("资源:").append(desResourceInteger(reward.getResources())).append(" ");
        }
        if (reward.getShipId() != null) {
            builder.append("舰娘:").append(reward.getShipId()).append(" ");
        }
        if (reward.getExp() > 0) {
            builder.append("经验:").append(reward.getExp()).append(" ");
        }
        if (builder.length() == 0) {
            builder.append("无");
        }
        return builder.toString();
    }

    
    public static String desResourceInteger(Map<String, Integer> resourceMap) throws PrototypeNotFoundException {
        StringBuilder builder = new StringBuilder();
        List<String> sortOrders = ResourceFactory.INSTANCE.getSortOrders();   
        for (String id : sortOrders) {
            Integer amount = resourceMap.get(id);
            if (amount != null) {
                ResourcePrototype prototype = ResourceFactory.INSTANCE.getPrototype(id);
                builder.append(prototype.getName()).append(amount.toString()).append(",");
            }
        }

        if (builder.length() == 0) {
            builder.append("无");
        }
        
        return builder.toString();
    }
    
    public static String desShipModel(ShipModel shipModel) {
        return shipModel.getId() + "(lv" + shipModel.getLevel() + ")";
    }
    
    public static String desCalendar(GameCalendar calendar) {
        int hour = TimerManager.tickToHour(calendar.getTick());
        String desTime = String.format("第%s年 %s月%s日%s时\n", calendar.getYear(), calendar.getMonth(), calendar.getDay(), hour);
        return desTime;
    }
    
    public static String desResourceModel(Map<String, ResourceModel> resources) {
        
        StringBuilder builder = new StringBuilder();
        List<String> sortOrders = ResourceFactory.INSTANCE.getSortOrders();   
        for (String id : sortOrders) {
            ResourceModel model = resources.get(id);
            if (model != null) {
                builder.append(model.getPrototype().getName()).append(model.getAmount().toString()).append(",");
            }
        }

        if (builder.length() == 0) {
            builder.append("无");
        }
        
        return builder.toString();
    }

    public static String desWorld(SessionData sessionData, GameWorld world) {
        StringBuilder builder = new StringBuilder();
        
        
        builder.append(desCalendar(sessionData.getCalendar()));
        
        String desResources = "资源:\n";
        desResources += DescriptionFormatter.desResourceModel(sessionData.getResources()) + "\n";
        builder.append(desResources);
        
        String desShips;
        builder.append("舰娘:\n");
        builder.append("空闲--");
        desShips = sessionData.getShips().stream().filter(ship -> ship.getWorkStatus() == ShipWorkStatus.IN_BUILDING).map(ship -> desShipModel(ship)).collect(Collectors.joining(",")) + "\n";
        if (desShips.length() == 0) {
            desShips = "无";
        }
        builder.append(desShips);
        
        builder.append("工作中--");
        desShips = sessionData.getShips().stream().filter(ship -> ship.getWorkStatus() == ShipWorkStatus.IN_EXPETITION).map(ship -> desShipModel(ship)).collect(Collectors.joining(",")) + "\n";
        if (desShips.length() == 0) {
            desShips = "无";
        }
        builder.append(desShips);
        
        builder.append("进行中远征:\n");
        String desExpeditions = sessionData.getExpeditions().stream().map(model -> DescriptionFormatter.desExpedition(model)).collect(Collectors.joining("\n"));
        if (desExpeditions.length() == 0) {
            desExpeditions = "无";
        }
        builder.append(desExpeditions);
        
        
        return builder.toString();
    }
}
