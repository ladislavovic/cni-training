package com.cross_ni.training.springintroduction.examples03_autowiring.example1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {
	
	private MyDao1 dao1;
	
	private MyDao2 dao2;

	@Autowired
	public MyService(MyDao1 dao1, MyDao2 dao2) {
		this.dao1 = dao1;
		this.dao2 = dao2;
	}

	public MyDao1 getDao1() {
		return dao1;
	}

	public MyDao2 getDao2() {
		return dao2;
	}
	
}
