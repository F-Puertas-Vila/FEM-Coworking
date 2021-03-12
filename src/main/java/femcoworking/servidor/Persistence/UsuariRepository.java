package femcoworking.servidor.Persistence;

import femcoworking.servidor.Models.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Fernando Puertas
 */
public interface UsuariRepository extends JpaRepository<Usuari, String>{
    List<Usuari> findUsuariByEmailAndDeshabilitatIsFalse(String email);
    Usuari findByIdUsuari(String idUsuari);
}
