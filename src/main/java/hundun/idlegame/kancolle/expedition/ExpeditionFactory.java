package hundun.idlegame.kancolle.expedition;

import java.util.List;
import hundun.idlegame.kancolle.base.BaseFactory;
import hundun.idlegame.kancolle.exception.IdleGameException;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public class ExpeditionFactory extends BaseFactory<ExpeditionPrototype, ExpeditionModel, ExpeditionSaveData> {
    
    public ExpeditionFactory() {
        super(ExpeditionPrototype.class);
    }

    @Override
    public ExpeditionModel saveDataToModel(ExpeditionSaveData saveData) throws IdleGameException {
        ExpeditionPrototype prototype = getPrototype(saveData.getId());
        ExpeditionModel model = new ExpeditionModel();
        model.setPrototype(prototype);
        model.setShipIds(saveData.getShipIds());
        model.setRemainTick(saveData.getRemainTick());
        return model;
    }
    
    @Override
    public ExpeditionSaveData modelToSaveData(ExpeditionModel model) {
        List<String> shipIds = model.getShipIds();
        String expeditionId = model.getPrototype().getId();
        ExpeditionSaveData saveData = new ExpeditionSaveData(expeditionId, shipIds, model.getRemainTick());
        return saveData;
    }


}
