package femcoworking.servidor.Services;

import femcoworking.servidor.Exceptions.BadRequestException;
import femcoworking.servidor.Models.Usuari;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Fernando Puertas
 */
@Service
@Primary
public class ControlAcces {

    private final Map<String, String> codisAccesAcreditats = new HashMap<>();

    public String GenerarCodiAcces(Usuari usuari)
    {
        String codiAcces = UUID.randomUUID().toString();
        codisAccesAcreditats.put(codiAcces, usuari.getIdUsuari());
        return codiAcces;
    }

    public String ValidarCodiAcces(String codiAcces) {
        if (!codisAccesAcreditats.containsKey(codiAcces)){
            throw new BadRequestException("Codi d'accés no vàlid");
        }
        return codisAccesAcreditats.get(codiAcces);
    }

    public void EliminarCodiAcces(String codiAcces) {
        codisAccesAcreditats.remove(codiAcces);
    }

    public void ValidarUsuariSenseAccesPrevi(Usuari usuari) {
        if (codisAccesAcreditats.containsValue(usuari.getIdUsuari()))
        {
            throw new BadRequestException("Aquest usuari ja te accés. Per obtenir un nou codi primer ha de finalitzar sessió");
        }
    }
}
