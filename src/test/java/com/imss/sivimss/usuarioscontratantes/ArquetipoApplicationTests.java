package com.imss.sivimss.usuarioscontratantes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
 

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ArquetipoApplicationTests {

	@Test
	void contextLoads() {
		String result="test";
		UsuariosContratantesApplication.main(new String[]{});
		assertNotNull(result);
	}

}
