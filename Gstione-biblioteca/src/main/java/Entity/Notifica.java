package Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

@Entity
@Table(name = "notifica")
public class Notifica {

    @Id
    @Column(name = "id_notifica", length = 15, nullable = false)
    private String idNotifica;

    @Column(name = "tipo", length = 50, nullable = false)
    private String tipo;

    @Column(name = "messaggio", length = 255, nullable = false)
    private String messaggio;

    @Column(name = "data_arrivo", nullable = false)
    private LocalDateTime dataArrivo;

    @Column(name = "letta", nullable = false)
    private boolean letta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destinatario", nullable = false)
    private Studente destinatario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prenotazione", nullable = false)
    private Prenotazione prenotazione;

    //Costruttore vuoto
    public Notifica() {
    }

    //Costruttore con parametri
    public Notifica(String tipo, String messaggio, Studente destinatario, Prenotazione prenotazione) {
        if(tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Il tipo di notifica è obbligatorio!");
        }
        if(messaggio == null || messaggio.trim().isEmpty()) {
            throw new IllegalArgumentException("Il messaggio non può essere vuoto!");
        }
        if(destinatario == null) {
            throw new IllegalArgumentException("La notifica deve avere un destinatario!");
        }
        if(prenotazione == null) {
            throw new IllegalArgumentException("La notifica deve essere associata a una prenotazione!");
        }

        Random random = new Random();
        this.idNotifica = "NOT" + (10000 + random.nextInt(90000));

        this.tipo = tipo;
        this.messaggio = messaggio;
        this.destinatario = destinatario;
        this.prenotazione = prenotazione;

        this.dataArrivo = LocalDateTime.now();
        this.letta = false;
    }

    // --- GETTER E SETTER ---

    public String getIdNotifica() {
        return idNotifica;
    }

    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMessaggio() {
        return messaggio;
    }
    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public LocalDateTime getDataArrivo() {
        return dataArrivo;
    }
    public void setDataArrivo(LocalDateTime dataArrivo) {
        this.dataArrivo = dataArrivo;
    }

    public boolean isLetta() {
        return letta;
    }
    public void setLetta(boolean letta) {
        this.letta = letta;
    }

    public Studente getDestinatario() {
        return destinatario;
    }
    public void setDestinatario(Studente destinatario) {
        this.destinatario = destinatario;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }
    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }


    // --- METODI ---

    public void segnaComeLetta() {
        this.letta = true;
    }
}
