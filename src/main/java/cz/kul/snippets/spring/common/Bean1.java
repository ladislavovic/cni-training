package cz.kul.snippets.spring.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

// Create another special bean which implements this interface
// Keep Bean1 as simple as possible.
//
//public class Bean1 implements BeanFactoryPostProcessor {


public class Bean1 {
	
	private String val;

	public Bean1() { }

	public Bean1(String val) {
		this.val = val;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return "Bean1 [val=" + val + "]";
	}

}
