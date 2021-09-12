package hundun.idlegame.kancolle.world;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import hundun.idlegame.kancolle.event.EventBus;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public class TimerManager extends BaseManager {
    
    public static final int TICK_PER_DAY = 6;
    public static final double HOUR_PER_TICK = 24.0 / TICK_PER_DAY;
    

    
    public TimerManager(EventBus eventBus, DataBus dataBus) {
        super(eventBus, dataBus);
    }
    
    public static int tickToHour(int tick) {
        return (int) Math.round(tick * HOUR_PER_TICK);
    }

    
    final Set<Integer> monthsHas31Days = new HashSet<>(Arrays.asList(1, 3, 5, 7, 8, 10, 12));
    
    public void generateTick(SessionData sessionData) {
        sessionData.setTick(sessionData.getTick() + 1);
        if (sessionData.getTick() == TICK_PER_DAY) {
            sessionData.setTick(0);
            sessionData.setDay(sessionData.getDay() + 1);
        }
        //sessionData.setSumTickCount(sessionData.getSumTickCount() + 1);
        boolean nextMonth = (monthsHas31Days.contains(sessionData.getMonth()) && sessionData.getDay() == 31) 
                || (!monthsHas31Days.contains(sessionData.getMonth()) && sessionData.getDay() == 30);
        if (nextMonth) {
            sessionData.setMonth(sessionData.getMonth() + 1);
            sessionData.setDay(1);
        }
        if (sessionData.getMonth() == 13) {
            sessionData.setMonth(1);
            sessionData.setYear(sessionData.getYear() + 1);
        }
        eventBus.sendClockEvent(sessionData);
    }

    public String overviewTime(SessionData sessionData) {
        int hour = tickToHour(sessionData.getTick());
        return String.format("第%s年 %s月%s日%s时", sessionData.getYear(), sessionData.getMonth(), sessionData.getDay(), hour);
    }
}
