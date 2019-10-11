package cz.kul.snippets.spring._03_annotation_based_config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConstructorInjectionService {

    private Injected injected;

    @Autowired
    public ConstructorInjectionService(Injected injected) {
        this.injected = injected;
    }

    public Injected getInjected() {
        return injected;
    }

}
