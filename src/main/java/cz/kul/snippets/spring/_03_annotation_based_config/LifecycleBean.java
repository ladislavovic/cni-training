package cz.kul.snippets.spring._03_annotation_based_config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

@Component
public class LifecycleBean {

    private StringBuilder sb = new StringBuilder();

    @PostConstruct
    private void init() {
        sb.append("_postConstruct_");
    }

    @PreDestroy
    private void destroy() {
        sb.append("_preDestroy_");
    }

    @Override
    public String toString() {
        return sb.toString();
    }

}
