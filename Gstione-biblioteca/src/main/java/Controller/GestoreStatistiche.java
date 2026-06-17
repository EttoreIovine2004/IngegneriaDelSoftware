package Controller;

import Entity.Bibliotecario;
import Entity.Prenotazione;
import Entity.SistemaBibliotecario;
import Entity.Utente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestoreStatistiche {

    private final SistemaBibliotecario sistemaBibliotecario;
    private final GestoreAutenticazione auth;

    //Costruttore
    public GestoreStatistiche(SistemaBibliotecario sistemaBibliotecario, GestoreAutenticazione auth) {
        this.sistemaBibliotecario = sistemaBibliotecario;
        this.auth = auth;
    }

    //Metodo privato di sicurezza
    private boolean checkPermessiBibliotecario() {
        Utente utenteCorrente = auth.getUtenteAttuale();

        if (!(utenteCorrente instanceof Bibliotecario)) {
            System.out.println("Accesso negato: statistiche riservate ai Bibliotecari.");
            return false;
        }
        return true;
    }


    //--- METODI ---

    public List<Prenotazione> visualizzaStoricoSala(String idSala) {
        if (!checkPermessiBibliotecario()) {
            return Collections.emptyList(); //Ritorna una lista vuota invece di null
        }

        return sistemaBibliotecario.visualizzaStoricoSala(idSala);
    }

    public List<Prenotazione> visualizzaStoricoStudente(String idUtente) {
        if (!checkPermessiBibliotecario()) {
            return Collections.emptyList();
        }

        return sistemaBibliotecario.visualizzaStoricoStudente(idUtente);
    }

    public String generaStatisticheSala(String idSala, LocalDate data) {
        if (!checkPermessiBibliotecario()) {
            return "Errore: permessi insufficienti per visualizzare le statistiche.";
        }

        return sistemaBibliotecario.generaStatisticheSala(idSala, data);
    }

    public List<Prenotazione> visualizzaPrenotazioniAttiveSala(String idSala) {
        if (!checkPermessiBibliotecario()) {
            return Collections.emptyList();
        }

        return sistemaBibliotecario.visualizzaPrenotazioniAttiveSala(idSala);
    }

    public List<String[]> visualizzaStoricoStudenteRighe(String idUtente) {
        List<Prenotazione> storico = visualizzaStoricoStudente(idUtente);
        List<String[]> datiFormattati = new ArrayList<>();

        for (Prenotazione p : storico) {
            String idSalaCorrente = (p.getPostazione() != null && p.getPostazione().getSala() != null)
                    ? p.getPostazione().getSala().getIdSala()
                    : "N/D";

            String[] riga = {
                    String.valueOf(p.getIdPrenotazione()),
                    idSalaCorrente,
                    p.getPostazione() != null ? p.getPostazione().getCodicePostazione() : "N/D",
                    idUtente,
                    p.getData() != null ? p.getData().toString() : "N/D",
                    p.getOraInizio() != null ? p.getOraInizio().toString() : "N/D",
                    p.getOraFine() != null ? p.getOraFine().toString() : "N/D",
                    p.getStato() != null ? p.getStato().toString() : "N/D"
            };
            datiFormattati.add(riga);
        }
        return datiFormattati;
    }

    public List<String[]> visualizzaStoricoSalaRighe(String idSala) {
        List<Prenotazione> storico = visualizzaStoricoSala(idSala);
        return getRighe(idSala, storico);
    }

    public List<String[]> visualizzaPrenotazioniAttiveSalaRighe(String idSala) {
        List<Prenotazione> attive = visualizzaPrenotazioniAttiveSala(idSala);
        return getRighe(idSala, attive);
    }

    private List<String[]> getRighe(String idSala, List<Prenotazione> attive) {
        List<String[]> datiFormattati = new ArrayList<>();

        for(Prenotazione p : attive) {
            String[] riga = {
                    String.valueOf(p.getIdPrenotazione()),
                    idSala,
                    p.getPostazione() != null ? p.getPostazione().getCodicePostazione() : "N/D",
                    p.getStudente() != null ? p.getStudente().getIdUtente() : "N/D",
                    p.getData() != null ? p.getData().toString() : "N/D",
                    p.getOraInizio() != null ? p.getOraInizio().toString() : "N/D",
                    p.getOraFine() != null ? p.getOraFine().toString() : "N/D",
                    p.getStato() != null ? p.getStato().toString() : "N/D"
            };
            datiFormattati.add(riga);
        }
        return datiFormattati;
    }

    public int getCapienzaSala(String idSala) {
        if (!checkPermessiBibliotecario()) return 0;
        return sistemaBibliotecario.ottieniCapienzaSala(idSala);
    }
}
