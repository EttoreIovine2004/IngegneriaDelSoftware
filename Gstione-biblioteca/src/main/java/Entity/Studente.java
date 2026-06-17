package Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "studente")
public class Studente extends Utente{

    @Column(name = "matricola", length = 45, nullable = false, unique = true)
    private String matricola;

    @Column(name = "numero_accessi_totali", nullable = false)
    private Integer numeroAccessiTotali;

    @OneToMany(mappedBy = "studente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prenotazione> prenotazioni = new ArrayList<>();

    //Costruttore vuoto
    public Studente() {
        super();
    }

    //Costruttore con parametri
    public Studente(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);

        Random random = new Random();
        int numeroCasuale = 100000 + random.nextInt(900000);
        this.matricola = "M" + numeroCasuale;

        this.numeroAccessiTotali = 0;
    }

    // --- GETTER E SETTER ---

    public String getMatricola() {
        return matricola;
    }

    public Integer getNumeroAccessiTotali() {
        return numeroAccessiTotali;
    }
    public void setNumeroAccessiTotali(Integer numeroAccessiTotali) {
        this.numeroAccessiTotali = numeroAccessiTotali;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }
    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }


    // --- METODI ---

    public void incrementaAccessi() {
        if(this.numeroAccessiTotali == null) {
            this.numeroAccessiTotali = 0;
        }
        this.numeroAccessiTotali++;
    }

    public List<Prenotazione> ottieniPrenotazioniFuture() {
        List<Prenotazione> future = new ArrayList<>();
        for(Prenotazione p : this.prenotazioni) {
            if(p.getStato() == StatoPrenotazione.ATTIVA || p.getStato() == StatoPrenotazione.IN_CORSO) {
                future.add(p);
            }
        }
        return future;
    }

    public List<Prenotazione> ottieniStoricoPrenotazioni() {
        List<Prenotazione> storico = new ArrayList<>();
        for(Prenotazione p : this.prenotazioni) {
            if(p.getStato() == StatoPrenotazione.SCADUTA || p.getStato() == StatoPrenotazione.ANNULLATA || p.getStato() == StatoPrenotazione.CONCLUSA) {
                storico.add(p);
            }
        }
        return storico;
    }
}
