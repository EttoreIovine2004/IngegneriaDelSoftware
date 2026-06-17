package Entity;

import jakarta.persistence.*;
import java.util.Random;

@MappedSuperclass
public abstract class Utente {

    @Id
    @Column(name = "id_utente", length = 10, nullable = false)
    private String idUtente;

    @Column(name = "nome", length = 20, nullable = false)
    private String nome;

    @Column(name = "cognome", length = 30, nullable = false)
    private String cognome;

    @Column(name = "email", length = 45, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 45, nullable = false)
    private String password;

    //Costruttore vuoto
    public Utente() {
    }

    //Costruttore con parametri
    public Utente(String nome, String cognome, String email, String password) {
        if(nome == null || nome.trim().isEmpty() || cognome == null || cognome.trim().isEmpty() || email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Tutti i campi dell'utente sono obbligatori!");
        }

        Random random = new Random();
        this.idUtente = "U" + random.nextInt(10000);

        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.password = password;
    }

    // --- GETTER E SETTER ---

    public String getIdUtente() {
        return idUtente;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


    // --- METODI ---

    public boolean verificaPassword(String passwordInserita) {
        if(passwordInserita == null) return false;
        return this.password.equals(passwordInserita);
    }
}
