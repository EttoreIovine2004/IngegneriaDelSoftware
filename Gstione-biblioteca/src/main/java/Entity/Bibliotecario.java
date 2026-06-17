package Entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "bibliotecario")
public class Bibliotecario extends Utente{

    @Column(name = "codice_identificativo", length = 20, nullable = false, unique = true)
    private String codiceIdentificativo;

    @OneToMany(mappedBy = "bibliotecarioResponsabile", fetch = FetchType.LAZY)
    private List<SalaStudio> saleGestite = new ArrayList<>();

    //Costruttore vuoto
    public Bibliotecario() {
        super();
    }

    //Costruttore con parametri
    public Bibliotecario(String nome, String cognome, String email, String password) {
        super(nome, cognome, email, password);

        Random random = new Random();
        int numeroCasuale = 1000 + random.nextInt(9000);
        this.codiceIdentificativo = "B" + numeroCasuale; // Es: "B4592"
    }

    // --- GETTER ---

    public String getCodiceIdentificativo() {
        return codiceIdentificativo;
    }

    public List<SalaStudio> getSaleGestite() {
        return this.saleGestite;
    }
}
