package Eseguibile;

import Boundary.FormSceltaIniziale;

import Controller.*;

import Database.JpaUtil;
import Entity.SistemaStudente;
import Entity.SistemaUtilita;
import Entity.SistemaBibliotecario;

import javax.swing.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static ScheduledExecutorService scheduler;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[SYSTEM] Chiusura della connessione al database...");
            try {
                if (scheduler != null) {
                    scheduler.shutdown();
                }

                JpaUtil.chiudi();
                System.out.println("[SYSTEM] Database disconnesso con successo.");
            } catch (Exception e) {
                System.err.println("[ERRORE] Errore durante la chiusura del DB: " + e.getMessage());
            }
        }));

        SwingUtilities.invokeLater(() -> {
            try {
                SistemaStudente sistemaStudente = new SistemaStudente();
                SistemaUtilita sistemaUtilita = new SistemaUtilita();
                SistemaBibliotecario sistemaBibliotecario = new SistemaBibliotecario();

                GestoreAutenticazione gestoreAutenticazione = new GestoreAutenticazione(sistemaUtilita, sistemaStudente, sistemaBibliotecario);
                GestoreServiziStudente gestoreServiziStudente = new GestoreServiziStudente(sistemaStudente, gestoreAutenticazione);
                GestoreNotifiche gestoreNotifiche = new GestoreNotifiche(sistemaUtilita, gestoreAutenticazione);
                GestorePrenotazioni gestorePrenotazioni = new GestorePrenotazioni(sistemaStudente, gestoreAutenticazione);
                GestoreSale gestoreSale = new GestoreSale(sistemaBibliotecario, gestoreAutenticazione);
                GestoreStatistiche gestoreStatistiche = new GestoreStatistiche(sistemaBibliotecario, gestoreAutenticazione);

                DatiTestBiblioteca.popola(gestoreAutenticazione, gestoreSale);

                scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(() -> {
                    try {
                        System.out.println("[BACKGROUND] Controllo scadenze e generazione promemoria automatico...");
                        gestoreNotifiche.generaNotifichePromemoria();
                    } catch (Exception e) {
                        System.err.println("[ERRORE BACKGROUND] Errore nel task dei promemoria: " + e.getMessage());
                    }
                }, 0, 5, TimeUnit.MINUTES);

                FormSceltaIniziale.apri_scelta_iniziale(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);

                System.out.println("[SYSTEM] Applicazione avviata correttamente.");
            } catch (Exception e) {
                System.err.println("[ERRORE CRITICO] Impossibile avviare l'applicazione:");
                e.printStackTrace();
            }
        });
    }
}
