package hundun.idlegame.kancolle.building.instance;
/**
 * @author hundun
 * Created on 2021/09/23
 */
public enum BuidingId {
    EXPEDITION_BUILDING("远征中心"), 
    GACHA_BUILDING("造船中心"),
    ;
    
    private final String idText;

    private BuidingId(String id) {
        this.idText = id;
    }
    
    public String getIdText() {
        return idText;
    }

}
