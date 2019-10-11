package cz.kul.snippets.spring._03_annotation_based_config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomMethodInitialization {

    private Foo foo;

    private Bar bar;

    @Autowired
    public void init(Foo foo, Bar bar) {
        this.foo = foo;
        this.bar = bar;
    }

    public Foo getFoo() {
        return foo;
    }

    public Bar getBar() {
        return bar;
    }
}
