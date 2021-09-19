package hundun.idlegame.kancolle.event;

import hundun.idlegame.kancolle.data.SessionData;

/**
 * @author hundun
 * Created on 2021/09/01
 */
public interface IClockEventListener {
    void tick(SessionData sessionData);
}
