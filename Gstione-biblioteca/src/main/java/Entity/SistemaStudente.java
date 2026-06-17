package Entity;

import Database.GestorePersistenza;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class SistemaStudente {

    private final GestorePersistenza gestorePersistenza;

    //Costruttore
    public SistemaStudente() {
        this.gestorePersistenza = new GestorePersistenza();
    }


    // --- METODI ---

    public boolean registraStudente(String nome, String cognome, String email, String password) {
        Studente s = new Studente(nome, cognome, email, password);
        return gestorePersistenza.salva(s);
    }

    public boolean effettuaPrenotazione(String idUtente, String codicePostazione, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        Studente studente = gestorePersistenza.trovaPerId(Studente.class, idUtente);
        Postazione postazione = gestorePersistenza.trovaPerId(Postazione.class, codicePostazione);

        if(studente == null || postazione == null) {
            return false;
        }

        for(Prenotazione esistente : postazione.getPrenotazioni()) {
            if(esistente.getData().equals(data) && !(esistente.getStato() == StatoPrenotazione.ANNULLATA)) {

                if(oraInizio.isBefore(esistente.getOraFine()) && oraFine.isAfter(esistente.getOraInizio())) {
                    System.out.println("Errore: La postazione è già prenotata da un altro studente in quegli orari.");
                    return false;
                }
            }
        }

        Prenotazione p = new Prenotazione(data, oraInizio, oraFine, studente, postazione);
        gestorePersistenza.salva(p);

        Notifica nConferma = new Notifica("CONFERMA", "Prenotazione confermata per il " + data + " dalle " + oraInizio + " alle " + oraFine + ".", studente, p);
        gestorePersistenza.salva(nConferma);

        return true;
    }

    public boolean annullaPrenotazione(String idPrenotazione) {
        Prenotazione p = gestorePersistenza.trovaPerId(Prenotazione.class, idPrenotazione);

        if(p == null) {
            return false;
        }

        if(p.annulla()) {
            gestorePersistenza.aggiorna(p);

            Notifica nCancellazione = new Notifica("CANCELLAZIONE", "La tua prenotazione del " + p.getData() + " delle " + p.getOraInizio() + " è stata annullata.", p.getStudente(), p);
            gestorePersistenza.salva(nCancellazione);
            return true;
        }

        return false;
    }

    public List<Notifica> visualizzaNotificheStudente(String idUtente) {
        return gestorePersistenza.cercaPerCampo(Notifica.class, "destinatario.idUtente", idUtente);
    }

    public List<SalaStudio> consultaSaleDisponibili(LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        List<SalaStudio> tutteLeSale = gestorePersistenza.trovaTutti(SalaStudio.class);
        List<SalaStudio> saleDisponibili = new ArrayList<>();

        for(SalaStudio sala : tutteLeSale) {
            if(sala.verificaDisponibilita(data, oraInizio, oraFine)) {
                saleDisponibili.add(sala); //Questa sala ha almeno una postazione libera
            }
        }

        return saleDisponibili;
    }

    public Map<String, String> verificaFasceOrarieSala(String idSala, LocalDate data) {
        Map<String, String> palinsesto = new LinkedHashMap<>();

        SalaStudio sala = gestorePersistenza.trovaPerId(SalaStudio.class, idSala);
        if(sala == null) return palinsesto;

        List<String[]> fasceFisse = Arrays.asList(
                new String[] {"08:00", "10:00"},
                new String[] {"10:00", "12:00"},
                new String[] {"12:00", "14:00"},
                new String[] {"14:00", "16:00"},
                new String[] {"16:00", "18:00"},
                new String[] {"18:00", "20:00"}
        );

        for(String[] fascia : fasceFisse) {
            LocalTime inizio = LocalTime.parse(fascia[0]);
            LocalTime fine = LocalTime.parse(fascia[1]);

            String slotTesto = fascia[0] + " - " + fascia[1];

            if(sala.verificaDisponibilita(data, inizio, fine)) {
                palinsesto.put(slotTesto, "PRENOTABILE");
            } else {
                palinsesto.put(slotTesto, "COMPLETA");
            }
        }

        return palinsesto;
    }

    public List<Prenotazione> visualizzaMiePrenotazioniFuture(String idUtente) {
        Studente studente = gestorePersistenza.trovaPerId(Studente.class, idUtente);
        if(studente != null) {
            return studente.ottieniPrenotazioniFuture();
        }
        return new ArrayList<>();
    }

    public List<Prenotazione> visualizzaMioStorico(String idUtente) {
        Studente studente = gestorePersistenza.trovaPerId(Studente.class, idUtente);
        if(studente != null) {
            return studente.ottieniStoricoPrenotazioni();
        }
        return new ArrayList<>();
    }

    public boolean confermaCheckIn(String idPrenotazione) {
        Prenotazione p = gestorePersistenza.trovaPerId(Prenotazione.class, idPrenotazione);

        if(p != null) {
            boolean checkInRiuscito = p.effettuaCheckIn();

            gestorePersistenza.aggiorna(p);
            gestorePersistenza.aggiorna(p.getPostazione());

            if (checkInRiuscito) {
                gestorePersistenza.aggiorna(p.getStudente());
            }

            return checkInRiuscito;
        }
        return false;
    }

    public String getStatoPrenotazione(String idPrenotazione) {
        Prenotazione p = gestorePersistenza.trovaPerId(Prenotazione.class, idPrenotazione);

        if (p == null) {
            return "Inesistente";
        }

        return p.getStato().toString();
    }
}
