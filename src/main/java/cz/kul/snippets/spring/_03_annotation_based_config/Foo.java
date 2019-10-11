package cz.kul.snippets.spring._03_annotation_based_config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Foo implements Baz {

}
