package Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "postazione")
public class Postazione {

    @Id
    @Column(name = "codice_postazione", length = 20, nullable = false)
    private String codicePostazione;

    @Column(name = "stato_attuale", length = 20, nullable = false)
    private String statoAttuale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sala", nullable = false)
    private SalaStudio sala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area", nullable = true)
    private AreaTematica areaTematica;

    @OneToMany(mappedBy = "postazione", cascade = CascadeType.ALL)
    private List<Prenotazione> prenotazioni = new ArrayList<>();


    //Costruttore vuoto
    public Postazione() {
    }

    //Costruttore con parametri
    public Postazione(String codicePostazione, SalaStudio sala, AreaTematica areaTematica) {

        if (codicePostazione == null || codicePostazione.trim().isEmpty()) {
            throw new IllegalArgumentException("Il codice della postazione è obbligatorio!");
        }
        if (sala == null) {
            throw new IllegalArgumentException("Errore strutturale: Una postazione DEVE trovarsi dentro una sala studio!");
        }

        this.codicePostazione = codicePostazione;
        this.sala = sala;
        this.areaTematica = areaTematica; //Può essere null
        this.statoAttuale = "Libera";
    }


    // --- GETTER E SETTER ---

    public String getCodicePostazione() {
        return codicePostazione;
    }

    public String getStatoAttuale() {
        return statoAttuale;
    }
    public void setStatoAttuale(String statoAttuale) {
        this.statoAttuale = statoAttuale;
    }

    public SalaStudio getSala() {
        return sala;
    }
    public void setSala(SalaStudio sala) {
        this.sala = sala;
    }

    public AreaTematica getAreaTematica() {
        return areaTematica;
    }
    public void setAreaTematica(AreaTematica areaTematica) {
        this.areaTematica = areaTematica;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }
    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }


    // --- METODI ---

    public void occupa() {
        this.statoAttuale = "Occupata";
    }

    public void libera() {
        this.statoAttuale = "Libera";
    }

    //Verifica se la postazione è attualmente libera
    public boolean verificaDisponibilita() {
        return "Libera".equalsIgnoreCase(this.statoAttuale);
    }
}
