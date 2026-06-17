package Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "prenotazione")
public class Prenotazione {

    @Id
    @Column(name = "id_prenotazione", length = 10, nullable = false)
    private String idPrenotazione;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "ora_inizio", nullable = false)
    private LocalTime oraInizio;

    @Column(name = "ora_fine", nullable = false)
    private LocalTime oraFine;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato", length = 20, nullable = false)
    private StatoPrenotazione stato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_studente", nullable = false)
    private Studente studente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codice_postazione", nullable = false)
    private Postazione postazione;

    @OneToMany(mappedBy = "prenotazione", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notifica> notifiche = new ArrayList<>();


    //Costruttore vuoto
    public Prenotazione() {
    }

    //Costruttore con parametri
    public Prenotazione(LocalDate data, LocalTime oraInizio, LocalTime oraFine, Studente studente, Postazione postazione) {

        if(data == null || oraInizio == null || oraFine == null) {
            throw new IllegalArgumentException("Data, ora inizio e ora fine sono obbligatorie!");
        }
        if(oraInizio.isAfter(oraFine) || oraInizio.equals(oraFine)) {
            throw new IllegalArgumentException("L'orario di inizio deve essere precedente all'orario di fine!");
        }
        if(studente == null) {
            throw new IllegalArgumentException("Una prenotazione deve essere associata a uno studente!");
        }
        if(postazione == null) {
            throw new IllegalArgumentException("Una prenotazione deve essere associata a una postazione!");
        }

        Random random = new Random();
        this.idPrenotazione = "PR" + (1000 + random.nextInt(9000));

        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.studente = studente;
        this.postazione = postazione;

        this.stato = StatoPrenotazione.ATTIVA;

        this.postazione.occupa();
    }

    // --- GETTER E SETTER ---

    public String getIdPrenotazione() {
        return this.idPrenotazione;
    }

    public LocalDate getData() {
        return this.data;
    }
    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getOraInizio() {
        return this.oraInizio;
    }
    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalTime getOraFine() {
        return this.oraFine;
    }
    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public StatoPrenotazione getStato() {
        return this.stato;
    }
    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    public Studente getStudente() {
        return this.studente;
    }
    public void setStudente(Studente studente) {
        this.studente = studente;
    }

    public Postazione getPostazione() {
        return this.postazione;
    }
    public void setPostazione(Postazione postazione) {
        this.postazione = postazione;
    }

    public List<Notifica> getNotifiche() {
        return this.notifiche;
    }
    public void setNotifiche(List<Notifica> notifiche) {
        this.notifiche = notifiche;
    }


    // --- METODI ---

    public boolean effettuaCheckIn() {
        if(verificaCheckInEffettuato()) {
            return false;
        }

        if(verificaScadenza()) {
            return false;
        }

        this.stato = StatoPrenotazione.IN_CORSO;

        if(this.studente != null) {
            this.studente.incrementaAccessi();
        }
        return true;
    }

    public boolean verificaScadenza() {
        LocalDateTime finePrenotazione = LocalDateTime.of(this.data, this.oraInizio);
        LocalDateTime adesso = LocalDateTime.now();

        if(this.stato == StatoPrenotazione.SCADUTA) {
            return true;
        }

        if(this.stato == StatoPrenotazione.ATTIVA && adesso.isAfter(finePrenotazione)) {
            this.stato = StatoPrenotazione.SCADUTA;

            if(this.postazione != null) {
                this.postazione.libera();
            }
            return true;
        }

        return false;
    }

    public boolean verificaCheckInEffettuato() {
        return this.stato == StatoPrenotazione.IN_CORSO || this.stato == StatoPrenotazione.CONCLUSA;
    }

    public boolean annulla() {
        LocalDateTime orarioLimite = LocalDateTime.of(this.data, this.oraInizio).minusHours(1);
        LocalDateTime adesso = LocalDateTime.now();

        if(adesso.isAfter(orarioLimite)) {
            return false;
        }

        if(this.stato == StatoPrenotazione.ATTIVA) {
            this.stato = StatoPrenotazione.ANNULLATA;

            if(this.postazione != null) {
                this.postazione.libera();
            }
            return true;
        }
        return false;
    }
}
