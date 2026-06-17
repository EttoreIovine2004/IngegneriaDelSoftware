package Boundary;

import Controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormCheckIn {
    private JPanel panelCheckIn;
    private JLabel labelCheckIn;
    private JTextField textFieldIdPrenotazione;
    private JLabel labelPrenotazione;
    private JButton confermaCheckInButton;
    private JButton indietroButton;
    private JButton verificaStatoPrenotazioneButton;
    private JLabel labelStato;

    private static JFrame frameCorrente;

    public FormCheckIn(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        confermaCheckInButton.setEnabled(false);
        labelStato.setText("Nessuna prenotazione verificata.");

        verificaStatoPrenotazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codice = textFieldIdPrenotazione.getText().trim();

                if (codice.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci un codice valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String stato = gestorePrenotazioni.verificaStatoPrenotazione(codice);

                labelStato.setText("Stato: " + stato);

                if(stato.equalsIgnoreCase("Attiva")) {
                    confermaCheckInButton.setEnabled(true);
                } else {
                    confermaCheckInButton.setEnabled(false);
                }
            }
        });

        confermaCheckInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codice = textFieldIdPrenotazione.getText().trim();

                boolean successo = gestorePrenotazioni.effettuaCheckIn(codice);

                if(successo) {
                    JOptionPane.showMessageDialog(null, "Check-in effettuato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    if(frameCorrente != null) frameCorrente.dispose();
                    FormProfiloStudente.apri_profilo_studente(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
                } else {
                    JOptionPane.showMessageDialog(null, "Errore durante il check-in. Riprova.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();
                FormProfiloStudente.apri_profilo_studente(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });
    }

    public static void apri_form_checkin(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Effettua Check-In");
        frameCorrente = frame;
        frame.setContentPane(new FormCheckIn(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche).panelCheckIn);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
