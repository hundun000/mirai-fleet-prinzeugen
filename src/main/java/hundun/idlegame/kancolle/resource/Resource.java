package hundun.idlegame.kancolle.resource;
/**
 * @author hundun
 * Created on 2021/09/01
 */
public enum Resource {
    FUEL("油"),
    AMMO("弹"),
    STEEL("钢"),
    BAUXITE("铝"),
    ;
    
    String chinese;
    Resource(String chinese) {
        this.chinese = chinese;
    }
    public String getChinese() {
        return chinese;
    }
}
