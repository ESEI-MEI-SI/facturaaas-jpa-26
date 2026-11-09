package es.uvigo.mei.facturaaas;

import static org.junit.jupiter.api.Assertions.assertTrue;

import es.uvigo.mei.facturaaas.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
class FacturaaasApplicationTests {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void muestraElSqlGeneradoEnDesarrollo(CapturedOutput output) {
		usuarioRepository.count();
		assertTrue(output.getOut().contains("Hibernate:"));
		assertTrue(output.getOut().contains("count("));
	}

}
