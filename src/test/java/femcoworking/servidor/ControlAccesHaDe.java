package femcoworking.servidor;

import femcoworking.servidor.Models.Rol;
import femcoworking.servidor.Models.Usuari;
import femcoworking.servidor.Services.ControlAcces;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class ControlAccesHaDe {

    private final String idUsuari = "unIdDeUsuari";
    private final ControlAcces controlAcces;

    public ControlAccesHaDe() {
        controlAcces = new ControlAcces();
    }

    @Test
    public void GenerarUnCodiDAcces()
    {
        Usuari usuari = donatUnUsuari();

        String codi = controlAcces.GenerarCodiAcces(usuari);

        assertThat(codi).isNotEmpty();
    }

    @Test
    public void ValidarUnCodiDAccesRetornantElIdDeUsuari()
    {
        String codi = donatUnCodiDAccesGeneratPerUnUsuari();

        String idUsuariDelCodi = controlAcces.ValidarCodiAcces(codi);

        assertThat(idUsuariDelCodi).isEqualTo(idUsuari);
    }

    private String donatUnCodiDAccesGeneratPerUnUsuari() {
        Usuari usuari = donatUnUsuari();
        return controlAcces.GenerarCodiAcces(usuari);
    }

    private Usuari donatUnUsuari() {

        Usuari usuari = new Usuari();
        usuari.setIdUsuari(idUsuari);
        return usuari;
    }
}
