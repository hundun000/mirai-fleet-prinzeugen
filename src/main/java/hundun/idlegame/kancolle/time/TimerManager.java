package hundun.idlegame.kancolle.time;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import hundun.idlegame.kancolle.base.BaseManager;
import hundun.idlegame.kancolle.data.SessionData;
import hundun.idlegame.kancolle.event.EventBus;
import hundun.idlegame.kancolle.world.ComponentContext;
import hundun.idlegame.kancolle.world.DataBus;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public class TimerManager extends BaseManager {
    
    public static final int TICK_PER_DAY = 6;
    public static final double HOUR_PER_TICK = 24.0 / TICK_PER_DAY;
    

    
    public TimerManager(ComponentContext context) {
        super(context);
    }

    public static int tickToHour(int tick) {
        return (int) Math.round(tick * HOUR_PER_TICK);
    }

    
    final Set<Integer> monthsHas31Days = new HashSet<>(Arrays.asList(1, 3, 5, 7, 8, 10, 12));
    
    public void generateTick(SessionData sessionData) {
        GameCalendar calendar = sessionData.getCalendar();
        calendar.setTick(calendar.getTick() + 1);
        if (calendar.getTick() == TICK_PER_DAY) {
            calendar.setTick(0);
            calendar.setDay(calendar.getDay() + 1);
        }
        //sessionData.setSumTickCount(sessionData.getSumTickCount() + 1);
        boolean nextMonth = (monthsHas31Days.contains(calendar.getMonth()) && calendar.getDay() == 31) 
                || (!monthsHas31Days.contains(calendar.getMonth()) && calendar.getDay() == 30);
        if (nextMonth) {
            calendar.setMonth(calendar.getMonth() + 1);
            calendar.setDay(1);
        }
        if (calendar.getMonth() == 13) {
            calendar.setMonth(1);
            calendar.setYear(calendar.getYear() + 1);
        }
        eventBus.sendClockEvent(sessionData);
    }

}
