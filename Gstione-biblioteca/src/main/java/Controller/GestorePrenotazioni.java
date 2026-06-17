package Controller;

import Entity.Studente;
import Entity.SistemaStudente;
import Entity.Utente;
import java.time.LocalDate;
import java.time.LocalTime;

public class GestorePrenotazioni {

    private final SistemaStudente sistemaStudente;
    private final GestoreAutenticazione auth;

    //Costruttore
    public GestorePrenotazioni(SistemaStudente sistemaStudente, GestoreAutenticazione auth) {
        this.sistemaStudente = sistemaStudente;
        this.auth = auth;
    }

    //Metodo privato di sicurezza
    private boolean checkPermessiStudente() {
        Utente utenteCorrente = auth.getUtenteAttuale();

        if (!(utenteCorrente instanceof Studente)) {
            System.out.println("Accesso negato: operazione riservata agli studenti registrati.");
            return false;
        }
        return true;
    }


    // --- METODI ---

    public boolean effettuaPrenotazione(String codicePostazione, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        if (!checkPermessiStudente()) return false;

        Studente studente = (Studente) auth.getUtenteAttuale();

        return sistemaStudente.effettuaPrenotazione(studente.getIdUtente(), codicePostazione, data, oraInizio, oraFine);
    }

    public boolean annullaPrenotazione(String idPrenotazione) {
        if (!checkPermessiStudente()) return false;

        return sistemaStudente.annullaPrenotazione(idPrenotazione);
    }

    public boolean effettuaCheckIn(String idPrenotazione) {
        if (!checkPermessiStudente()) return false;

        return sistemaStudente.confermaCheckIn(idPrenotazione);
    }

    public String verificaStatoPrenotazione(String idPrenotazione) {
        if (!checkPermessiStudente()) {
            return "Inesistente";
        }

        String stato = sistemaStudente.getStatoPrenotazione(idPrenotazione);

        if (stato == null) {
            return "Inesistente";
        }

        return stato;
    }
}
