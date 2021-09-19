package hundun.idlegame.kancolle.ship;

import java.util.Collection;
import java.util.List;

import hundun.idlegame.kancolle.base.BaseFactory;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public class ShipFactory extends BaseFactory<ShipPrototype, ShipModel, ShipSaveData> {
    


    public ShipFactory() {
        super(ShipPrototype.class);
    }
    
    @Override
    public ShipModel saveDataToModel(ShipSaveData saveData) throws PrototypeNotFoundException {
        ShipPrototype prototype = getPrototype(saveData.getPrototypeId());
        ShipModel model = new ShipModel();
        model.setPrototype(prototype);
        model.setLevel(saveData.getLevel());
        model.setExpAndCheckLevelUp(saveData.getExp());
        model.setWorkStatus(saveData.getWorkStatus());
        model.setWorkInBuildingId(saveData.getWorkInBuildingId());
        return model;
    }
    @Override
    public ShipSaveData modelToSaveData(ShipModel model) {
        ShipSaveData saveData = new ShipSaveData(model.getPrototype().getId(), model.getLevel(), model.getExp(), model.getWorkStatus(), model.getWorkInBuildingId());
        return saveData;
    }
    
    public Collection<ShipPrototype> getPrototypes() {
        return prototypes.values();
    }

}
