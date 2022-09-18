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
    
    protected Map<String, T_PROTOTYPE> prototypes = new HashMap<>();
    @Getter
    private List<String> sortOrders = new ArrayList<>();
    
    Class<T_PROTOTYPE> prototypeClass;
    
    public BaseFactory(Class<T_PROTOTYPE> prototypeClass) {
        this.prototypeClass = prototypeClass;
    }
    
    public void checkPrototypeExist(String id) throws PrototypeNotFoundException {
        T_PROTOTYPE prototype = prototypes.get(id);
        if (prototype == null) {
            throw new PrototypeNotFoundException(id, prototypeClass);
        }
    }
    
    public T_PROTOTYPE getPrototype(String id) throws PrototypeNotFoundException {
        T_PROTOTYPE prototype = prototypes.get(id);
        if (prototype == null) {
            throw new PrototypeNotFoundException(id, prototypeClass);
        }
        return prototype;
    }
    
    public List<T_PROTOTYPE> listIdToPrototype(List<String> ids) throws PrototypeNotFoundException {
        List<T_PROTOTYPE> prototypes = new ArrayList<>(ids.size());
        for (String id : ids) {
            prototypes.add(getPrototype(id));
        }
        return prototypes;
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
        if (prototypes.containsKey(prototype.getId())) {
            throw new RuntimeException("duplicated prototype register: " + prototype.getId());
        }
        
        prototypes.put(prototype.getId(), prototype);
        sortOrders.add(prototype.getId());
    }
    
    public void registerAll(List<T_PROTOTYPE> prototypes) {
        for (T_PROTOTYPE prototype : prototypes) {
            register(prototype);
        }
    }
    
    public void clear() {
        prototypes.clear();
        sortOrders.clear();
    }

}
