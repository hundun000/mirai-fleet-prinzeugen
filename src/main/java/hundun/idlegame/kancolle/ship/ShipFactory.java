package hundun.idlegame.kancolle.ship;

import hundun.idlegame.kancolle.base.BaseFactory;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public class ShipFactory extends BaseFactory<ShipPrototype, ShipModel, ShipSaveData> {
    
    
    public static final ShipFactory INSTANCE = new ShipFactory();

    public ShipFactory() {
        super(ShipPrototype.class);
    }
    
    @Override
    public ShipModel saveDataToModel(ShipSaveData saveData) throws PrototypeNotFoundException {
        ShipPrototype prototype = getPrototype(saveData.getPrototypeId());
        ShipModel model = new ShipModel();
        model.setPrototype(prototype);
        model.setLevel(saveData.getLevel());
        model.setLevel(saveData.getExp());
        return model;
    }
    @Override
    public ShipSaveData modelToSaveData(ShipModel model) {
        ShipSaveData saveData = new ShipSaveData(model.getPrototype().getId(), model.getLevel(), model.getExp());
        return saveData;
    }

}
