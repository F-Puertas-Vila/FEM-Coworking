package femcoworking.servidor;

import femcoworking.servidor.Models.DadesAcces;
import femcoworking.servidor.Models.PeticioCanviContrasenya;
import femcoworking.servidor.Models.Usuari;
import femcoworking.servidor.Models.Rol;
import femcoworking.servidor.Models.PeticioDeshabilitarUsuari;
import femcoworking.servidor.Controllers.UsuariController;
import femcoworking.servidor.Persistence.UsuariRepository;
import femcoworking.servidor.Services.ControlAcces;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(UsuariController.class)
public class UsuariControllerHaDe {

    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper objectmapper;
    @MockBean
    private UsuariRepository usuariRepository;
    @MockBean
    private ControlAcces controlAcces;

    private String email = "email@test.com";
    private final String idUsuari = "unIdDeUsuari";
    private final String idUsuariAdministrador = "idUsuariAdministrador";
    private final String unaContrasenya = "unaContrasenya";
    private final String contrasenyaXifrada = "$2a$10$fiil9KJa0WbPORp4PyxsSuV7fSyjJXC/E5I5CyQbovM1jBgy24qau";
    private final String nom = "unNom";
    private final String cif = "unCif";
    private final String direccio = "unaDireccio";
    private final String poblacio = "unaPoblacio";
    private final String provincia = "unaProvincia";
    private final String codiAcces = "unCodiAcces";

    @Test
    public void registrarUnNouUsuari()
        throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuari registrat correctament amb el id")));
    }

    @Test
    public void retornarErrorSiNoSInformaLEmailAlRegistrarUnNouUsuari()
        throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, null, unaContrasenya, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp email és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLaContrasenyaAlRegistrarUnNouUsuari()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, null, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp contrasenya és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElRolAlRegistrarUnNouUsuari()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, null, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp rol és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElNomAlRegistrarUnNouUsuariAmbRolProveidor()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.PROVEIDOR, null, cif, direccio, poblacio, provincia);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp nom és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaElCifAlRegistrarUnNouUsuariAmbRolProveidor()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.PROVEIDOR, nom, null, direccio, poblacio, provincia);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp CIF és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLaDireccioAlRegistrarUnNouUsuariAmbRolProveidor()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.PROVEIDOR, nom, cif, null, poblacio, provincia);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp direcció és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLaPoblacioAlRegistrarUnNouUsuariAmbRolProveidor()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.PROVEIDOR, nom, cif, direccio, null, provincia);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp població és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLaProvinciaAlRegistrarUnNouUsuariAmbRolProveidor()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.PROVEIDOR, nom, cif, direccio, poblacio, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp provincia és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiJaExisteixUnUsuariActiuAmbElEmailIndicatAlRegistrarUnNouUsuari()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.CLIENT, null, null, null, null, null);
        DonatUnUsuariRegistratAmbElMateixEmail();

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Ja existeix un usuari actiu amb aquest email", result.getResolvedException().getMessage()));
    }

    @Test
    public void inicialitzarElIdentificadorDeUsuariAlRegistarUnNouUsuari()
        throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuari registrat correctament amb el id")));

        assertThat(nouUsuari.getIdUsuari()).isNotNull();
    }

    @Test
    public void inicialitzarLaContrasenyaXifradaAlRegistarUnNouUsuari()
            throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuari registrat correctament amb el id")));

        assertThat(nouUsuari.getEmail()).isNotEqualTo(unaContrasenya);
    }

    @Test
    public void guardarElNouUsuari()
        throws Exception {

        Usuari nouUsuari = donatUnNouUsuari(idUsuari, email, unaContrasenya, Rol.CLIENT, null, null, null, null, null);

        mvc.perform(post("/registre")
                .content(objectmapper.writeValueAsString(nouUsuari))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuari registrat correctament amb el id")));

        Mockito.verify(usuariRepository).save(Mockito.any());
    }

    @Test
    public void logarUnUsuariRetornantUnCodiDAcces()
            throws Exception {

        DadesAcces dadesAcces = donatLesDadesDAccesDUnUsuari(email, unaContrasenya);
        Usuari usuari = donatUnUsuariRegistrat();
        Mockito.when(controlAcces.GenerarCodiAcces(usuari)).thenReturn(codiAcces);

        mvc.perform(post("/login")
                .content(objectmapper.writeValueAsString(dadesAcces))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(codiAcces));
    }

    @Test
    public void retornarErrorSiNoSInformaLEmailAlLogarUnUsuari()
            throws Exception {

        DadesAcces dadesAcces = donatLesDadesDAccesDUnUsuari(null, unaContrasenya);

        mvc.perform(post("/login")
                .content(objectmapper.writeValueAsString(dadesAcces))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp email és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoSInformaLaContrasenyaAlLogarUnUsuari()
            throws Exception {

        DadesAcces dadesAcces = donatLesDadesDAccesDUnUsuari(email, null);

        mvc.perform(post("/login")
                .content(objectmapper.writeValueAsString(dadesAcces))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp contrasenya és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiNoExisteixUnUsuariActiuAmbElEmailIndicatAlLogarUnUsuari()
            throws Exception {

        DadesAcces dadesAcces = donatLesDadesDAccesDUnUsuari(email, unaContrasenya);
        donatUsuariNoRegistrat();

        mvc.perform(post("/login")
                .content(objectmapper.writeValueAsString(dadesAcces))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("L'usuari no existeix", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiLaContrasenyaNoEsCorrectaAlLogarUnUsuari()
            throws Exception {

        DadesAcces dadesAcces = donatLesDadesDAccesDUnUsuari(email, "unaContrasenyaErronea");
        donatUnUsuariRegistrat();

        mvc.perform(post("/login")
                .content(objectmapper.writeValueAsString(dadesAcces))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("La contrasenya no és vàlida", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiAlLogarUnUsuariDeshabilitat()
            throws Exception {

        DadesAcces dadesAcces = donatLesDadesDAccesDUnUsuari(email, unaContrasenya);
        Usuari usuari = donatUnUsuariRegistrat();
        usuari.setDeshabilitat(true);

        mvc.perform(post("/login")
                .content(objectmapper.writeValueAsString(dadesAcces))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("L'usuari està deshabilitat", result.getResolvedException().getMessage()));
    }

    @Test
    public void finalitzarSessioDUnUsuari()
            throws Exception {

        DadesAcces dadesAcces = donatLesDadesDAccesDUnUsuari(email, unaContrasenya);
        donatUnUsuariRegistrat();
        donatUnCodiDAccesValid();

        mvc.perform(get("/logout/" + codiAcces)
                .content(objectmapper.writeValueAsString(dadesAcces))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Sessió finalitzada")));
    }

    @Test
    public void canviarLaContrasenyaDUnUsuari()
            throws Exception {

        PeticioCanviContrasenya peticio = donadaUnaPeticioDeNovaContrasenya("novaContrasenya");
        Usuari usuari = donatUnUsuariRegistrat();
        donatUnCodiDAccesValid();

        mvc.perform(put("/canviarContrasenya/" + codiAcces)
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Contrasenya modificada")));

        assertThat(usuari.getContrasenya()).isNotEqualTo(unaContrasenya);
    }

    @Test
    public void retornarErrorSiNoSInformaAlCanviarLaContrasenyaDUnUsuari()
            throws Exception {

        PeticioCanviContrasenya peticio = donadaUnaPeticioDeNovaContrasenya(null);
        donatUnUsuariRegistrat();
        donatUnCodiDAccesValid();

        mvc.perform(put("/canviarContrasenya/" + codiAcces)
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp contrasenya és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void deshabilitarUnUsuari()
        throws Exception {

        PeticioDeshabilitarUsuari peticio = donadaUnaPeticioDeDeshabilitarUsuari(idUsuari);
        Usuari usuari = donatUnUsuariRegistrat();
        donatUnUsuariAdministrador();
        donatUnCodiDAccesValidPelUsuariAdministrador();

        mvc.perform(delete("/baixa/" + codiAcces)
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuari deshabilitat")));

        assertThat(usuari.isDeshabilitat()).isTrue();

    }

    @Test
    public void retornarErrorSiNoSInformaElIdUsuariAlDeshabilitarUnUsuari()
            throws Exception {

        PeticioDeshabilitarUsuari peticio = donadaUnaPeticioDeDeshabilitarUsuari(null);
        donatUnUsuariAdministrador();
        donatUnCodiDAccesValidPelUsuariAdministrador();

        mvc.perform(delete("/baixa/" + codiAcces)
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("El camp id usuari és obligatori", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiUnUsuariNoAdministradorIntentaDeshabilitarUnUsuari()
            throws Exception {

        PeticioDeshabilitarUsuari peticio = donadaUnaPeticioDeDeshabilitarUsuari(idUsuari);
        donatUnUsuariRegistrat();
        donatUnCodiDAccesValid();

        mvc.perform(delete("/baixa/" + codiAcces)
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Aquest usuari no té permís d'administrador", result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarErrorSiSIntentaDeshabilitarUnUsuariQueNoExisteix()
            throws Exception {

        String idUsuariNoExistent = "unIdUsuariNoExistent";
        PeticioDeshabilitarUsuari peticio = donadaUnaPeticioDeDeshabilitarUsuari(idUsuariNoExistent);
        donatUnUsuariAdministrador();
        donatUnCodiDAccesValidPelUsuariAdministrador();

        mvc.perform(delete("/baixa/" + codiAcces)
                .content(objectmapper.writeValueAsString(peticio))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals("No s'ha trobat cap usuari amb l'identificador "+idUsuariNoExistent, result.getResolvedException().getMessage()));
    }

    @Test
    public void retornarLaLlistaDUsuarisRegistrats()
            throws Exception {

        donatUnUsuariAdministrador();
        donatUnCodiDAccesValidPelUsuariAdministrador();

        mvc.perform(get("/usuaris/" + codiAcces)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void retornarErrorSiUnUsuariNoAdministradorIntentaLlistaElsUsuaris()
            throws Exception {

        donatUnUsuariRegistrat();
        donatUnCodiDAccesValid();

        mvc.perform(get("/usuaris/" + codiAcces)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals("Aquest usuari no té permís d'administrador", result.getResolvedException().getMessage()));
    }

    private DadesAcces donatLesDadesDAccesDUnUsuari(String emailPeticio, String contrasenyaPeticio) {
        DadesAcces dades = new DadesAcces();
        dades.setEmail(emailPeticio);
        dades.setContrasenya(contrasenyaPeticio);
        return dades;
    }

    private Usuari donatUnUsuariRegistrat() {
        Usuari usuari = donatUnNouUsuari(idUsuari, email, contrasenyaXifrada, Rol.CLIENT, null, null, null, null, null);
        List<Usuari> usuaris = new ArrayList<Usuari>(Arrays.asList(usuari));
        Mockito.when(usuariRepository.findUsuariByEmailAndDeshabilitatIsFalse(email)).thenReturn(usuaris);
        Mockito.when(usuariRepository.findByIdUsuari(idUsuari)).thenReturn(usuari);
        return usuari;
    }

    private Usuari donatUnUsuariAdministrador() {
        Usuari usuari = donatUnNouUsuari(idUsuariAdministrador, email, contrasenyaXifrada, Rol.ADMINISTRADOR, null, null, null, null, null);
        Mockito.when(usuariRepository.findByIdUsuari(idUsuariAdministrador)).thenReturn(usuari);
        return usuari;
    }

    private void donatUsuariNoRegistrat() {
        Mockito.when(usuariRepository.findUsuariByEmailAndDeshabilitatIsFalse(email)).thenReturn(new ArrayList<Usuari>());
    }

    private void DonatUnUsuariRegistratAmbElMateixEmail() {
        List<Usuari> usuari = new ArrayList<Usuari>(Arrays.asList(new Usuari()));
        Mockito.when(usuariRepository.findUsuariByEmailAndDeshabilitatIsFalse(email)).thenReturn(usuari);
    }

    private Usuari donatUnNouUsuari(String idUsuari, String email, String unaContrasenya, Rol rol, String nom, String cif, String direccio, String poblacio, String provincia) {

        Usuari usuari = new Usuari();
        usuari.setIdUsuari(idUsuari);
        usuari.setEmail(email);
        usuari.setContrasenya(unaContrasenya);
        usuari.setRol(rol);
        usuari.setNom(nom);
        usuari.setCifEmpresa(cif);
        usuari.setDireccio(direccio);
        usuari.setPoblacio(poblacio);
        usuari.setProvincia(provincia);

        return usuari;
    }

    private void donatUnCodiDAccesValid() {
        Mockito.when(controlAcces.ValidarCodiAcces(codiAcces)).thenReturn(idUsuari);
    }

    private void donatUnCodiDAccesValidPelUsuariAdministrador() {
        Mockito.when(controlAcces.ValidarCodiAcces(codiAcces)).thenReturn(idUsuariAdministrador);
    }

    private PeticioCanviContrasenya donadaUnaPeticioDeNovaContrasenya(String novaContrasenya) {
        PeticioCanviContrasenya peticio = new PeticioCanviContrasenya();
        peticio.setContrasenya(novaContrasenya);
        return peticio;
    }

    private PeticioDeshabilitarUsuari donadaUnaPeticioDeDeshabilitarUsuari(String idUsuari) {
        PeticioDeshabilitarUsuari peticio = new PeticioDeshabilitarUsuari();
        peticio.setIdUsuari(idUsuari);
        return peticio;
    }
}
