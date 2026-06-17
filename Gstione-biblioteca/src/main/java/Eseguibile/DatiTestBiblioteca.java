package Eseguibile;

import Controller.GestoreAutenticazione;
import Controller.GestoreSale;

import java.time.LocalTime;

public class DatiTestBiblioteca {
    public static void popola(GestoreAutenticazione auth, GestoreSale sale) {
        System.out.println("[DB-POPULATE] Inserimento utenti e sale di test...");

        auth.registraUtente("Mario", "Rossi", "mario.rossi@studente.it", "password123", "STUDENTE");
        auth.registraUtente("Anna", "Verdi", "anna.verdi@bibliotecaria.it", "admin456", "BIBLIOTECARIO");

        LocalTime apertura = LocalTime.of(9, 0);   //09:00
        LocalTime chiusura = LocalTime.of(18, 30); //18:30

        sale.creaSala("Sala Alfa", "Nella sala sono presenti 20 postazioni dotate di computer.", 50, apertura, chiusura, 4);
    }
}
