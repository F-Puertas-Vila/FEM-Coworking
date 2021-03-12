package femcoworking.servidor;

import femcoworking.servidor.Models.Rol;
import femcoworking.servidor.Models.Usuari;
import femcoworking.servidor.Persistence.UsuariRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UsuariRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UsuariRepository usuariRepository;
    private String email = "email@test.com";
    private final String idUsuari = "unIdDeUsuari";
    private final String unaContrasenya = "unaContrasenya";

    @Test
    public void quanBuscaPerIdUsuariAleshoresRetornaUnUsuari()
    {
        donatUnUsuari(idUsuari);

        Usuari usuariTrobat = usuariRepository.findByIdUsuari(idUsuari);

        assertThat(usuariTrobat.getIdUsuari()).isEqualTo(idUsuari);
    }

    @Test
    public void quanBuscaPerEmailUsuarisNoDeshabilitatsAleshoresRetornaUnUsuari()
    {
        donatDosUsuarisAmbElMateixEmailPeroUnDeshabilitat(idUsuari);

        List<Usuari> usuariTrobat = usuariRepository.findUsuariByEmailAndDeshabilitatIsFalse(email);

        assertThat(usuariTrobat.size()).isEqualTo(1);
        assertThat(usuariTrobat.get(0).getIdUsuari()).isEqualTo(idUsuari);
    }

    private void donatUnUsuari(String idUsuari) {
        Usuari unUsuari = new Usuari();
        unUsuari.setIdUsuari(idUsuari);
        unUsuari.setEmail(email);
        unUsuari.setRol(Rol.CLIENT);
        unUsuari.setContrasenya(unaContrasenya);
        entityManager.persist(unUsuari);
        entityManager.flush();
    }

    private void donatDosUsuarisAmbElMateixEmailPeroUnDeshabilitat(String idUsuari) {
        Usuari unUsuari = new Usuari();
        unUsuari.setIdUsuari(idUsuari);
        unUsuari.setEmail(email);
        unUsuari.setRol(Rol.CLIENT);
        unUsuari.setContrasenya(unaContrasenya);
        entityManager.persist(unUsuari);
        entityManager.flush();

        Usuari unUsuariDeshabilitat = new Usuari();
        unUsuariDeshabilitat.setIdUsuari("unAltreId");
        unUsuariDeshabilitat.setEmail(email);
        unUsuariDeshabilitat.setRol(Rol.CLIENT);
        unUsuariDeshabilitat.setContrasenya(unaContrasenya);
        unUsuariDeshabilitat.setDeshabilitat(true);
        entityManager.persist(unUsuariDeshabilitat);
        entityManager.flush();
    }
}
