package hundun.idlegame.kancolle.resource;

import hundun.idlegame.kancolle.base.BaseFactory;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;

/**
 * @author hundun
 * Created on 2021/09/10
 */
public class ResourceFactory extends BaseFactory<ResourcePrototype, ResourceModel, ResourceSaveData>{
    
    public ResourceFactory() {
        super(ResourcePrototype.class);
    }
    
    @Override
    public ResourceModel saveDataToModel(ResourceSaveData saveData) throws PrototypeNotFoundException {
        ResourcePrototype prototype = getPrototype(saveData.getId());
        ResourceModel model = new ResourceModel();
        model.setPrototype(prototype);
        model.setAmount(saveData.getAmount());
        return model;
    }

    @Override
    public ResourceSaveData modelToSaveData(ResourceModel model) {
        ResourceSaveData saveData = new ResourceSaveData(model.getPrototype().getId(), model.getAmount());
        return saveData;
    }


}
