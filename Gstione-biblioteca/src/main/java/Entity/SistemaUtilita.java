package Entity;

import Database.GestorePersistenza;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaUtilita {

    private final GestorePersistenza gestorePersistenza;

    //Costruttore
    public SistemaUtilita() {
        this.gestorePersistenza = new GestorePersistenza();
    }


    // --- METODI ---

    public Utente effettuaLogin(String email, String password) {
        //Cerchiamo se esiste un Utente con questa email nel DB
        List<Utente> risultati = gestorePersistenza.cercaPerCampo(Utente.class, "email", email);

        //Se la lista è vuota, l'email non esiste nel sistema
        if(risultati.isEmpty()) {
            System.out.println("Errore: Utente non trovato.");
            return null;
        }

        Utente utenteTrovato = risultati.getFirst();

        if(utenteTrovato.verificaPassword(password)) {
            return utenteTrovato;
        }

        //Se arriviamo qui, l'email era giusta ma la password era errata
        System.out.println("Errore: Password errata.");
        return null;
    }

    public void apriNotifica(String idNotifica) {
        Notifica n = gestorePersistenza.trovaPerId(Notifica.class, idNotifica);

        if(n != null && !n.isLetta()) {
            n.segnaComeLetta();

            gestorePersistenza.aggiorna(n);
        }
    }

    public void generaNotifichePromemoria() {
        LocalDate oggi = LocalDate.now();
        LocalTime adesso = LocalTime.now();
        LocalTime tra30Minuti = adesso.plusMinutes(30);

        List<Prenotazione> tutteLePrenotazioni = gestorePersistenza.trovaTutti(Prenotazione.class);

        for(Prenotazione p : tutteLePrenotazioni) {

            //Filtriamo solo quelle di OGGI che sono ancora in stato ATTIVA
            if(p.getData().equals(oggi) && p.getStato() == StatoPrenotazione.ATTIVA) {

                //Controlliamo se l'orario di inizio è compreso tra ORA e i prossimi 30 minuti
                if(p.getOraInizio().isAfter(adesso) && (p.getOraInizio().isBefore(tra30Minuti) || p.getOraInizio().equals(tra30Minuti))) {

                    // ATTENZIONE: Bisogna evitare di mandare più notifiche uguali
                    Map<String, Object> filtri = new HashMap<>();
                    filtri.put("destinatario.idUtente", p.getStudente().getIdUtente());
                    filtri.put("prenotazione.idPrenotazione", p.getIdPrenotazione());
                    filtri.put("tipo", "PROMEMORIA");

                    Notifica giaInviata = gestorePersistenza.cercaPrimoPerCampi(Notifica.class, filtri);

                    if(giaInviata == null) {
                        Notifica promemoria = new Notifica("PROMEMORIA", "Promemoria: la tua prenotazione inizia tra meno di 30 minuti!", p.getStudente(), p);
                        gestorePersistenza.salva(promemoria);
                    }
                }
            }
        }
    }

    public List<Notifica> getNotificheUtente(String idUtente) {
        List<Notifica> notifiche = gestorePersistenza.cercaPerCampo(Notifica.class, "destinatario.idUtente", idUtente);

        if (notifiche == null) {
            return new ArrayList<>();
        }

        return notifiche;
    }
}
