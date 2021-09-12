package hundun.idlegame.kancolle.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import hundun.idlegame.kancolle.exception.IdleGameException;
import hundun.idlegame.kancolle.exception.PrototypeNotFoundException;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/09/02
 */
public abstract class BaseFactory<T_PROTOTYPE extends BaseProtoype, T_MODEL extends BaseModel<?>, T_SAVE> {
    
    private Map<String, T_PROTOTYPE> prototypes = new HashMap<>();
    @Getter
    private List<String> sortOrders = new ArrayList<>();
    
    Class<T_PROTOTYPE> prototypeClass;
    
    public BaseFactory(Class<T_PROTOTYPE> prototypeClass) {
        this.prototypeClass = prototypeClass;
    }
    
    public T_PROTOTYPE getPrototype(String id) throws PrototypeNotFoundException {
        T_PROTOTYPE prototype = prototypes.get(id);
        if (prototype == null) {
            throw new PrototypeNotFoundException(id, prototypeClass);
        }
        return prototype;
    }

    public List<T_MODEL> listSaveDataToModel(List<T_SAVE> saveDatas) throws IdleGameException {
        List<T_MODEL> models = new ArrayList<>(saveDatas.size());
        for (T_SAVE saveData : saveDatas) {
            models.add(saveDataToModel(saveData));
        }
        return models;
    }
    
    public Map<String, T_MODEL> listSaveDataToModelMap(List<T_SAVE> saveDatas) throws IdleGameException {
        Map<String, T_MODEL> models = new HashMap<>(saveDatas.size());
        for (T_SAVE saveData : saveDatas) {
            T_MODEL model = saveDataToModel(saveData);
            models.put(model.getPrototype().getId(), model);
        }
        return models;
    }
    
    public List<String> listModelToId(Collection<T_MODEL> models) {
        return models.stream().map(model -> model.getPrototype().getId()).collect(Collectors.toList());
    }

    public List<T_SAVE> listModelToSaveData(Collection<T_MODEL> models) {
        List<T_SAVE> saveDatas = new ArrayList<>();
        for (String id : sortOrders) {
            Optional<T_MODEL> targetModelOptional = models.stream()
                    .filter(model -> model.getPrototype().getId().equals(id))
                    .findFirst();
            if (targetModelOptional.isPresent()) {
                saveDatas.add(modelToSaveData(targetModelOptional.get()));
            }
        }
        return saveDatas;
    }
    
    public abstract T_MODEL saveDataToModel(T_SAVE saveData) throws IdleGameException;
    
    public abstract T_SAVE modelToSaveData(T_MODEL model);

    public void register(T_PROTOTYPE prototype) {
        prototypes.put(prototype.getId(), prototype);
        sortOrders.add(prototype.getId());
    }

}
