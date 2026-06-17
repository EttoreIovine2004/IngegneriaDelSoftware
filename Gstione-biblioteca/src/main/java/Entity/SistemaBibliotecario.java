package Entity;

import Database.GestorePersistenza;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class SistemaBibliotecario {

    private final GestorePersistenza gestorePersistenza;

    //Costruttore
    public SistemaBibliotecario() {
        this.gestorePersistenza = new GestorePersistenza();
    }


    // --- METODI ---

    public boolean registraBibliotecario(String nome, String cognome, String email, String password) {
        Bibliotecario b = new Bibliotecario(nome, cognome, email, password);
        return gestorePersistenza.salva(b);
    }

    public boolean creaSalaStudio(String nome, String descrizione, Integer capienzaTotale, LocalTime orarioApertura, LocalTime orarioChiusura, int numeroPostazioniIniziali, Bibliotecario b) {
        SalaStudio sala = new SalaStudio();
        sala.setNome(nome);
        sala.setDescrizione(descrizione);
        sala.setCapienzaTotale(capienzaTotale);
        sala.setOrarioApertura(orarioApertura);
        sala.setOrarioChiusura(orarioChiusura);
        sala.setResponsabile(b); //Associo la sala al bibliotecario

        List<Postazione> postazioniIniziali = new ArrayList<>();
        for (int i = 1; i <= numeroPostazioniIniziali; i++) {
            String codiceFormattato = String.format("P%02d", i);

            Postazione p = new Postazione(codiceFormattato, sala, null);
            postazioniIniziali.add(p);
        }
        sala.setPostazioni(postazioniIniziali);

        return gestorePersistenza.salva(sala);
    }

    public boolean modificaSalaStudio(String idSala, String nuovoNome, String nuovaDescrizione, Integer nuovaCapienza, LocalTime nuovaApertura, LocalTime nuovaChiusura) {
        SalaStudio sala = gestorePersistenza.trovaPerId(SalaStudio.class, idSala);
        if (sala == null) {
            return false;
        }

        Integer postiGiaOccupati = sala.contaPostazioniOccupate();

        sala.aggiornaDati(nuovoNome, nuovaDescrizione,nuovaCapienza, nuovaApertura, nuovaChiusura, postiGiaOccupati);
        gestorePersistenza.aggiorna(sala);
        return true;
    }

    public boolean eliminaSalaStudio(String idSala) {
        return gestorePersistenza.elimina(SalaStudio.class, idSala);
    }

    public boolean aggiungiAreaTematica(String nomeArea, String tipologia, String idSala) {
        SalaStudio sala = gestorePersistenza.trovaPerId(SalaStudio.class, idSala);
        if(sala != null) {
            AreaTematica area = new AreaTematica(nomeArea, tipologia, sala);
            return gestorePersistenza.salva(area);
        }
        return false;
    }

    public List<Prenotazione> visualizzaStoricoSala(String idSala) {
        SalaStudio sala = gestorePersistenza.trovaPerId(SalaStudio.class, idSala);
        if(sala != null) {
            return sala.ottieniStoricoPrenotazioni();
        }
        return new ArrayList<>(); //Ritorna lista vuota se la sala non esiste
    }

    public List<Prenotazione> visualizzaStoricoStudente(String idUtente) {
        Studente studente = gestorePersistenza.trovaPerId(Studente.class, idUtente);
        if(studente != null) {
            return studente.ottieniStoricoPrenotazioni();
        }
        return new ArrayList<>();
    }

    public String generaStatisticheSala(String idSala, LocalDate data) {
        SalaStudio sala = gestorePersistenza.trovaPerId(SalaStudio.class, idSala);

        if(sala == null) {
            return "Errore: Sala non trovata.";
        }

        int capienzaTotale = sala.contaPostazioniLibere();
        int prenotazioniTotali = sala.contaPrenotazioniGiornaliere(data);
        int nonConfermate = sala.contaPrenotazioniNonConfermate(data);
        double tassoOccupazione = sala.calcolaTassoOccupazione();

        return "=== REPORT SALA: " + sala.getNome() + " (" + data.toString() + ") ===\n" +
                "Prenotazioni totali: " + prenotazioniTotali + "\n" +
                "Prenotazioni non confermate (scadute): " + nonConfermate + "\n" +
                "Tasso di Occupazione Attuale: " + tassoOccupazione + "%";
    }

    public List<Prenotazione> visualizzaPrenotazioniAttiveSala(String idSala) {
        SalaStudio sala = gestorePersistenza.trovaPerId(SalaStudio.class, idSala);
        if(sala != null) {
            return sala.ottieniPrenotazioniAttive();
        }
        return new ArrayList<>(); //Ritorna lista vuota se la sala non esiste
    }

    public int ottieniCapienzaSala(String idSala) {
        SalaStudio sala = gestorePersistenza.trovaPerId(SalaStudio.class, idSala);
        return (sala != null) ? sala.getCapienzaTotale() : 0;
    }
}