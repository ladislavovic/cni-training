package cz.kul.snippets.spring.common;

public class BeanWithDependency {

	private Bean1 bean1;

	public BeanWithDependency(Bean1 bean1) {
		this.bean1 = bean1;
	}

	public Bean1 getBean1() {
		return bean1;
	}

	public void setBean1(Bean1 bean1) {
		this.bean1 = bean1;
	}
	
}
