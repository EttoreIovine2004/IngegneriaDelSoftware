package Controller;

import Entity.Studente;
import Entity.SistemaStudente;
import Entity.Utente;
import Entity.Notifica;
import Entity.SalaStudio;
import Entity.Prenotazione;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GestoreServiziStudente {

    private final SistemaStudente sistemaStudente;
    private final GestoreAutenticazione auth;

    //Costruttore
    public GestoreServiziStudente(SistemaStudente sistemaStudente, GestoreAutenticazione auth) {
        this.sistemaStudente = sistemaStudente;
        this.auth = auth;
    }

    //Metodo privato di sicurezza
    private boolean checkPermessiStudente() {
        Utente utenteCorrente = auth.getUtenteAttuale();

        if (!(utenteCorrente instanceof Studente)) {
            System.out.println("Accesso negato.");
            return false;
        }
        return true;
    }


    // --- METODI ---

    public List<Notifica> visualizzaNotificheStudente() {
        if (!checkPermessiStudente()) return Collections.emptyList();

        String idStudente = auth.getUtenteAttuale().getIdUtente();
        return sistemaStudente.visualizzaNotificheStudente(idStudente);
    }

    public List<SalaStudio> consultaSaleDisponibili(LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        if (!checkPermessiStudente()) return Collections.emptyList();

        return sistemaStudente.consultaSaleDisponibili(data, oraInizio, oraFine);
    }

    public Map<String, String> verificaFasceOrarieSala(String idSala, LocalDate data) {
        if (!checkPermessiStudente()) return Collections.emptyMap();

        return sistemaStudente.verificaFasceOrarieSala(idSala, data);
    }

    public List<Prenotazione> visualizzaMiePrenotazioniFuture() {
        if (!checkPermessiStudente()) return Collections.emptyList();

        String idStudente = auth.getUtenteAttuale().getIdUtente();
        return sistemaStudente.visualizzaMiePrenotazioniFuture(idStudente);
    }

    public List<Prenotazione> visualizzaMioStorico() {
        if (!checkPermessiStudente()) return Collections.emptyList();

        String idStudente = auth.getUtenteAttuale().getIdUtente();
        return sistemaStudente.visualizzaMioStorico(idStudente);
    }

    private List<String[]> getRighe(List<Prenotazione> storico) {
        List<String[]> datiRiga = new ArrayList<>();

        for (Prenotazione p : storico) {
            String[] riga = {
                    String.valueOf(p.getIdPrenotazione()),
                    p.getPostazione().getCodicePostazione(),
                    p.getData().toString(),
                    p.getOraInizio().toString(),
                    p.getOraFine().toString(),
                    p.getStato().toString()
            };
            datiRiga.add(riga);
        }
        return datiRiga;
    }

    public List<String[]> visualizzaMiePrenotazioniFutureRighe() {
        List<Prenotazione> future = visualizzaMiePrenotazioniFuture();
        return getRighe(future);
    }

    public List<String[]> visualizzaMioStoricoRighe() {
        List<Prenotazione> storico = visualizzaMioStorico();
        return getRighe(storico);
    }

    public List<String[]> cercaSaleDisponibiliFormattate(String dataStr, String oraInizioStr, String oraFineStr) {
        if(!checkPermessiStudente()) return Collections.emptyList();

        LocalDate data = LocalDate.parse(dataStr);
        LocalTime oraInizio = LocalTime.parse(oraInizioStr);
        LocalTime oraFine = LocalTime.parse(oraFineStr);

        List<SalaStudio> saleDisponibili = consultaSaleDisponibili(data, oraInizio, oraFine);
        List<String[]> risultatoFormattato = new ArrayList<>();

        for(SalaStudio sala : saleDisponibili) {
            for(Entity.Postazione postazione : sala.getPostazioni()) {

                boolean haConflitti = false;
                for(Prenotazione esistente : postazione.getPrenotazioni()) {
                    if(esistente.getData().equals(data) && !esistente.getStato().toString().equalsIgnoreCase("ANNULLATA")) {
                        if(oraInizio.isBefore(esistente.getOraFine()) && oraFine.isAfter(esistente.getOraInizio())) {
                            haConflitti = true;
                            break;
                        }
                    }
                }

                if (!haConflitti) {
                    String nomeArea = "Nessuna Area";
                    if (postazione.getAreaTematica() != null) {
                        nomeArea = postazione.getAreaTematica().getNomeArea();
                    }

                    String[] riga = {
                            sala.getIdSala(),
                            sala.getNome(),
                            sala.getDescrizione(),
                            postazione.getCodicePostazione(),
                            nomeArea
                    };
                    risultatoFormattato.add(riga);
                }
            }
        }

        return risultatoFormattato;
    }
}
