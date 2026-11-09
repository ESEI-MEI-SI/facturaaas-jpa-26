package es.uvigo.mei.facturaaas.repository;

import es.uvigo.mei.facturaaas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
