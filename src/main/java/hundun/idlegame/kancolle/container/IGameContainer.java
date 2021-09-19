package hundun.idlegame.kancolle.container;

import hundun.idlegame.kancolle.data.config.WorldConfig;

/**
 * @author hundun
 * Created on 2021/09/03
 */
public interface IGameContainer {
    WorldConfig provideWorldConfig();
    void handleLog(String sessionId, String msg);
    void handleExportEvent(String sessionId, String data);
}
