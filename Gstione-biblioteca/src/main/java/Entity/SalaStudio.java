package Entity;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


@Entity
@Table(name = "sala_studio")
public class SalaStudio {

    @Id
    @Column(name = "id_sala", length = 45, nullable = false)
    private String idSala;

    // @Column collega la variabile alla colonna. Definiamo anche la lunghezza massima
    @Column(name = "nome", length = 45, nullable = false)
    private String nome;

    @Column(name = "descrizione", length = 45)
    private String descrizione;

    @Column(name = "capienza_totale", nullable = false)
    private Integer capienzaTotale;

    @Column(name = "orario_apertura", nullable = false)
    private LocalTime orarioApertura;

    @Column(name = "orario_chiusura", nullable = false)
    private LocalTime orarioChiusura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bibliotecario")
    private Bibliotecario bibliotecarioResponsabile;

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Postazione> postazioni = new ArrayList<>();

    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AreaTematica> areeTematiche = new ArrayList<>();

    //Costruttore vuoto
    public SalaStudio() {
    }

    //Costruttore completo
    public SalaStudio(String nome, String descrizione, Integer capienzaTotale, LocalTime orarioApertura, LocalTime orarioChiusura, List<Postazione> postazioniIniziali) {

        if(nome == null) {
            throw new IllegalArgumentException("Una sala studio deve avere un nome!");
        }
        if(capienzaTotale == null) {
            throw new IllegalArgumentException("La sala studio deve avere una capienza massima definita!");
        }
        if(orarioApertura == null || orarioChiusura == null) {
            throw new IllegalArgumentException("Orario di apertura e orario di chiusura sono obbligatori!");
        }
        if(orarioApertura.isAfter(orarioChiusura) || orarioApertura.equals(orarioChiusura)) {
            throw new IllegalArgumentException("L'orario di apertura deve essere precedente all'orario di chiusura!");
        }
        if(postazioniIniziali == null || postazioniIniziali.isEmpty())
            throw new IllegalArgumentException("Errore: una sala deve avere almeno una postazione!");

        Random random = new Random();
        int numeroCasuale = random.nextInt(10000);

        this.idSala = "S" + numeroCasuale;
        this.nome = nome;
        this.descrizione = descrizione;
        this.capienzaTotale = capienzaTotale;
        this.orarioApertura = orarioApertura;
        this.orarioChiusura = orarioChiusura;

        this.postazioni = postazioniIniziali;
        for (Postazione postazione : postazioniIniziali) postazione.setSala(this);
    }

    // --- GETTER E SETTER ---

    public String getIdSala() {
        return idSala;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getCapienzaTotale() {
        return capienzaTotale;
    }
    public void setCapienzaTotale(Integer capienzaTotale) {
        this.capienzaTotale = capienzaTotale;
    }

    public LocalTime getOrarioApertura() {
        return orarioApertura;
    }
    public void setOrarioApertura(LocalTime orarioApertura) {
        this.orarioApertura = orarioApertura;
    }

    public LocalTime getOrarioChiusura() {
        return orarioChiusura;
    }
    public void setOrarioChiusura(LocalTime orarioChiusura) {
        this.orarioChiusura = orarioChiusura;
    }

    public Bibliotecario getResponsabile() {
        return this.bibliotecarioResponsabile;
    }
    public void setResponsabile(Bibliotecario responsabile) {
        this.bibliotecarioResponsabile = responsabile;
    }

    public List<Postazione> getPostazioni() {
        return postazioni;
    }
    public void setPostazioni(List<Postazione> postazioni) {
        this.postazioni = postazioni;
    }

    public List<AreaTematica> getAreeTematiche() {
        return areeTematiche;
    }
    public void setAreeTematiche(List<AreaTematica> areeTematiche) {
        this.areeTematiche = areeTematiche;
    }


    // --- METODI ---

    public boolean verificaDisponibilita(LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        if(oraInizio.isBefore(this.orarioApertura) || oraFine.isAfter(this.orarioChiusura)) {
            return false; //La sala è chiusa in questi orari
        }

        for(Postazione p : this.postazioni) {
            boolean postazioneLibera = true;

            for(Prenotazione pren : p.getPrenotazioni()) {
                if(pren.getData().equals(data) && (pren.getStato() == StatoPrenotazione.ATTIVA || pren.getStato() == StatoPrenotazione.IN_CORSO)) {
                    if(oraInizio.isBefore(pren.getOraFine()) && oraFine.isAfter(pren.getOraInizio())) {
                        postazioneLibera = false; //Questa postazione è occupata
                        break; //è inutile guardare le altre prenotazioni di questa postazione, possiamo passiamo alla prossima
                    }
                }
            }

            //Se troviamo una postazione libera per la fascia oraria richiesta, la sala è disponibile
            if(postazioneLibera) {
                return true;
            }
        }

        return false; //Tutte le postazioni sono occupate in quella data e in quella fascia oraria
    }

    public double calcolaTassoOccupazione() {
        if(this.capienzaTotale == 0) return 0.0;
        return ((double) contaPostazioniOccupate() / this.capienzaTotale) * 100.0;
    }

    public Integer contaPostazioniOccupate() {
        Integer contatore = 0;

        //Iteriamo su tutte le postazioni
        for (Postazione p : postazioni) {
            if (p.getStatoAttuale() != null && p.getStatoAttuale().equalsIgnoreCase("Occupata")) {
                contatore++;
            }
        }
        return contatore;
    }

    public Integer contaPostazioniLibere() {
        return this.capienzaTotale - contaPostazioniOccupate();
    }

    public int contaPrenotazioniGiornaliere(LocalDate data) {
        int totale = 0;
        for(Postazione p : this.postazioni) {
            for(Prenotazione pren : p.getPrenotazioni()) {
                if(pren.getData().equals(data)) {
                    totale++;
                }
            }
        }
        return totale;
    }

    public int contaPrenotazioniNonConfermate(LocalDate data) {
        int nonConfermate = 0;
        for(Postazione p : this.postazioni) {
            for(Prenotazione pren : p.getPrenotazioni()) {
                if(pren.getData().equals(data) && pren.getStato() == StatoPrenotazione.SCADUTA) {
                    nonConfermate++;
                }
            }
        }
        return nonConfermate;
    }

    public List<Prenotazione> ottieniPrenotazioniAttive() {
        List<Prenotazione> attive = new ArrayList<>();

        for(Postazione p : this.postazioni) {
            for(Prenotazione pren : p.getPrenotazioni()) {
                if(pren.getStato() == StatoPrenotazione.ATTIVA || pren.getStato() == StatoPrenotazione.IN_CORSO) {
                    attive.add(pren);
                }
            }
        }
        return attive;
    }

    public List<Prenotazione> ottieniStoricoPrenotazioni() {
        List<Prenotazione> storico = new ArrayList<>();

        for(Postazione p : this.postazioni) {
            for(Prenotazione pren : p.getPrenotazioni()) {
                if(!(pren.getStato() == StatoPrenotazione.ATTIVA)) {
                    storico.add(pren);
                }
            }
        }
        return storico;
    }

    public void aggiornaDati(String nuovoNome,  String nuovaDescrizione, Integer nuovaCapienza, LocalTime nuovaApertura, LocalTime nuovaChiusura, int postiGiaOccupati) {

        if(!verificaCapienzaResidua(nuovaCapienza, postiGiaOccupati))
            throw new IllegalArgumentException("Impossibile ridurre la capienza: i posti sono già occupati.");

        this.setNome(nuovoNome);
        this.setDescrizione(nuovaDescrizione);
        this.setCapienzaTotale(nuovaCapienza);
        this.setOrarioApertura(nuovaApertura);
        this.setOrarioChiusura(nuovaChiusura);

        //Subito dopo aver modificato i dati andiamo a verificare la validità di questi ultimi
        validaFormatoDati();
    }

    public void validaFormatoDati() {

        //Verifica nome vuoto o nullo
        if(this.nome == null || this.nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto!");
        }

        //Verifica nome troppo lungo
        if(this.nome.length() > 45) {
            throw new IllegalArgumentException("Nome troppo lungo!");
        }

        //Verifica descrizione troppo lunga
        if(this.descrizione != null && this.descrizione.length() > 45) {
            throw new IllegalArgumentException("La descrizione supera il limite di caratteri!");
        }

        //Verifica capienza negativa o zero
        if(this.capienzaTotale == null || this.capienzaTotale <= 0) {
            throw new IllegalArgumentException("Capienza non valida!");
        }

        //Verifica orari nulli
        if(this.orarioApertura == null) {
            throw new IllegalArgumentException("Formato orario apertura errato!");
        }
        if(this.orarioChiusura == null) {
            throw new IllegalArgumentException("Formato orario chiusura errato!");
        }

        //Verifica chiusura antecedente ad apertura
        if(!this.orarioChiusura.isAfter(this.orarioApertura)) {
            throw new IllegalArgumentException("L'orario di chiusura deve essere successivo all'apertura!");
        }
    }

    public boolean verificaCapienzaResidua(Integer nuovaCapienza, int postiGiaOccupati) {
        return nuovaCapienza >= postiGiaOccupati;
    }
}
