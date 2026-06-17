package Boundary;

import Controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormSceltaIniziale {
    private JPanel mainPanel;
    private JLabel labelNome;
    private JButton registrazioneButton;
    private JButton loginButton;

    private static JFrame frameCorrente;

    public FormSceltaIniziale(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (frameCorrente != null) {
                    frameCorrente.dispose();
                }

                FormLogin.apri_form_login(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        registrazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (frameCorrente != null) {
                    frameCorrente.dispose();
                }

                FormRegistrazione.apri_form_registrazione(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });
    }

    public static void apri_scelta_iniziale(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        JFrame frame = new JFrame("Benvenuto - Sistema Biblioteca");
        frameCorrente = frame;

        FormSceltaIniziale form = new FormSceltaIniziale(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
        frame.setContentPane(form.mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centra sullo schermo
        frame.setVisible(true);
        frame.setResizable(false);
    }
}