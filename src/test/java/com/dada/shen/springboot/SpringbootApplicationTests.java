package com.dada.shen.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {

	@Test
	public void contextLoads() {
//		System.out.println("ok");
//		new SpringbootApplication().main(new String[]{""});
		long start = System.currentTimeMillis();
		for(int i = 0; i < 100000; i++){

		}
		System.out.println(System.currentTimeMillis() - start);
	}

}
