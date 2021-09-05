package hundun.idlegame.kancolle.container;
/**
 * @author hundun
 * Created on 2021/09/03
 */
public interface IGameContainer {
    void handleLog(String sessionId, String msg);
    void handleExportEvent(String sessionId, String data);
}
