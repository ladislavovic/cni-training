package cz.kul.snippets.spring._03_annotation_based_config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InjectedCollection {

    private List<Baz> bazInstances;

    @Autowired
    public InjectedCollection(List<Baz> bazInstances) {
        super();
        this.bazInstances = bazInstances;
    }

    public List<Baz> getBazInstances() {
        return bazInstances;
    }

}
