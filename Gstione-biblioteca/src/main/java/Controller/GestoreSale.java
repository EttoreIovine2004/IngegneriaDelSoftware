package Controller;

import Entity.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class GestoreSale {

    private final SistemaBibliotecario sistemaBibliotecario;
    private final GestoreAutenticazione auth;

    //Costruttore
    public GestoreSale(SistemaBibliotecario sistemaBibliotecario, GestoreAutenticazione auth) {
        this.sistemaBibliotecario = sistemaBibliotecario;
        this.auth = auth;
    }

    //Metodo privato di sicurezza
    private boolean checkPermessiBibliotecario() {
        Utente utenteCorrente = auth.getUtenteAttuale();

        if (!(utenteCorrente instanceof Bibliotecario)) {
            System.out.println("Accesso negato: operazione riservata ai bibliotecari.");
            return false;
        }
        return true;
    }


    // --- METODI ---

    public boolean creaSala(String nome, String descrizione, Integer capienzaTotale, LocalTime orarioApertura, LocalTime orarioChiusura, int numeroPostazioniIniziali) {
        if(!checkPermessiBibliotecario()) return false;

        Bibliotecario creatore = (Bibliotecario) auth.getUtenteAttuale();

        return sistemaBibliotecario.creaSalaStudio(nome, descrizione, capienzaTotale, orarioApertura, orarioChiusura, numeroPostazioniIniziali, creatore);
    }

    public boolean modificaSala(String idSala, String nome, String descrizione, Integer capienzaTotale, LocalTime orarioApertura, LocalTime orarioChiusura) {
        if (!checkPermessiBibliotecario()) return false;

        return sistemaBibliotecario.modificaSalaStudio(idSala, nome, descrizione, capienzaTotale, orarioApertura, orarioChiusura);
    }

    public boolean eliminaSala(String idSala) {
        if (!checkPermessiBibliotecario()) return false;

        return sistemaBibliotecario.eliminaSalaStudio(idSala);
    }

    public boolean aggiungiAreaTematica(String nomeArea, String tipologia, String idSala) {
        if (!checkPermessiBibliotecario()) return false;

        return sistemaBibliotecario.aggiungiAreaTematica(nomeArea, tipologia, idSala);
    }

    public String getCodiceIdentificativoBibliotecario() {
        if (auth.getUtenteAttuale() instanceof Bibliotecario b) {
            return b.getCodiceIdentificativo();
        }
        return "";
    }
}
