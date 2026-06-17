package Controller;

import Entity.SistemaBibliotecario;
import Entity.SistemaStudente;
import Entity.SistemaUtilita;
import Entity.Utente;
import Entity.Studente;
import Entity.Bibliotecario;


public class GestoreAutenticazione {

    private Utente utenteCorrente;
    private final SistemaUtilita sistemaUtilita;
    private final SistemaStudente sistemaStudente;
    private final SistemaBibliotecario sistemaBibliotecario;

    //Costruttore
    public GestoreAutenticazione(SistemaUtilita sistemaUtilita, SistemaStudente sistemaStudente, SistemaBibliotecario sistemaBibliotecario) {
        this.sistemaUtilita = sistemaUtilita;
        this.sistemaStudente = sistemaStudente;
        this.sistemaBibliotecario = sistemaBibliotecario;
        this.utenteCorrente = null; //All'avvio dell'app nessuno è loggato
    }


    // --- METODI ---

    public boolean eseguiLogin(String email, String password) {
        Utente utenteTrovato = sistemaUtilita.effettuaLogin(email, password);

        if(utenteTrovato != null) {
            this.utenteCorrente = utenteTrovato;
            System.out.println("Login effettuato. Benvenuto " + utenteCorrente.getEmail());
            return true;
        }

        System.out.println("Errore: Credenziali non valide.");
        return false;
    }

    public boolean registraUtente(String nome, String cognome, String email, String password, String ruolo) {

        if("Studente".equalsIgnoreCase(ruolo)) {
            return sistemaStudente.registraStudente(nome, cognome, email, password);

        } else if("Bibliotecario".equalsIgnoreCase(ruolo)) {
            return sistemaBibliotecario.registraBibliotecario(nome, cognome, email, password);

        }

        System.out.println("Errore: Ruolo '" + ruolo + "' non riconosciuto dal sistema.");
        return false;
    }

    public void eseguiLogout() {
        //Distruggiamo la sessione resettando la variabile a null
        this.utenteCorrente = null;
        System.out.println("Logout effettuato con successo. Arrivederci.");
    }

    public Utente getUtenteAttuale() {
        return this.utenteCorrente;
    }

    public String getMatricolaStudenteLoggato() {
        if (this.utenteCorrente instanceof Studente s) {
            return s.getMatricola();
        }
        return "";
    }

    public Integer getContatoreAccessiStudenteLoggato() {
        if (this.utenteCorrente instanceof Studente s) {
            return s.getNumeroAccessiTotali();
        }
        return 0;
    }

    public boolean isStudenteLoggato() {
        return this.utenteCorrente instanceof Studente;
    }

    public boolean isBibliotecarioLoggato() {
        return this.utenteCorrente instanceof Bibliotecario;
    }
}
