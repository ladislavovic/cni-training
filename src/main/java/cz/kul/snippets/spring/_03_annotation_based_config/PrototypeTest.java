package cz.kul.snippets.spring._03_annotation_based_config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("pt")
@Scope("prototype")
public class PrototypeTest {
    
    private String msg;

    public PrototypeTest(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
