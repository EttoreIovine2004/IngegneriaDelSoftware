package Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "area_tematica")
public class AreaTematica {

    @Id
    @Column(name = "id_area", length = 45, nullable = false)
    private String idArea;

    @Column(name = "nome_area", length = 45, nullable = false)
    private String nomeArea;

    @Column(name = "tipologia", length = 45)
    private String tipologia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sala", nullable = false)
    private SalaStudio sala;

    @OneToMany(mappedBy = "areaTematica")
    private List<Postazione> postazioni = new ArrayList<>();


    //Costruttore vuoto
    public AreaTematica() {
    }

    //Costruttore con parametri
    public AreaTematica(String nomeArea, String tipologia, SalaStudio sala) {

        if(nomeArea == null || tipologia == null) {
            throw new IllegalArgumentException("Un area tematica non può non avere un nome o una tipologia!");
        }
        if(sala == null) {
            throw new IllegalArgumentException("Errore: l'area tematica deve appartenere a una sala studio!");
        }

        Random random = new Random();
        this.idArea = "AT" + random.nextInt(10000);

        this.nomeArea = nomeArea;
        this.tipologia = tipologia;
        this.sala = sala;
    }

    // --- GETTER E SETTER ---

    public String getIdArea() {
        return idArea;
    }

    public String getNomeArea() {
        return nomeArea;
    }
    public void setNomeArea(String nomeArea) {
        this.nomeArea = nomeArea;
    }

    public String getTipologia() {
        return tipologia;
    }
    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public SalaStudio getSala() {
        return sala;
    }
    public void setSala(SalaStudio sala) {
        this.sala = sala;
    }

    public List<Postazione> getPostazioni() {
        return postazioni;
    }
    public void setPostazioni(List<Postazione> postazioni) {
        this.postazioni = postazioni;
    }


    // -- METODI ---

    public String getDettagli() {
        return "Area: " + this.nomeArea + "; Tipologia: " + this.tipologia + "; Situata in: " + this.nomeArea;
    }
}
