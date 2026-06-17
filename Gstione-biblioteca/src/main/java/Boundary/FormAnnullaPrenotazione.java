package Boundary;

import Controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormAnnullaPrenotazione {
    private JPanel panelAnnullaPrenotazione;
    private JTextField textFieldIdPrenotazione;
    private JLabel labelPren;
    private JButton annullaPrenotazioneButton;
    private JButton indietroButton;

    private static JFrame frameCorrente;

    public FormAnnullaPrenotazione(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();
                FormProfiloStudente.apri_profilo_studente(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        annullaPrenotazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idPrenotazione = textFieldIdPrenotazione.getText().trim();

                if (idPrenotazione.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci un ID prenotazione valido.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean successo = gestorePrenotazioni.annullaPrenotazione(idPrenotazione);

                if (successo) {
                    JOptionPane.showMessageDialog(null, "La prenotazione è stata annullata con successo.", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    indietroButton.doClick(); // Ritorna al profilo studente in automatico
                } else {
                    JOptionPane.showMessageDialog(null, "Impossibile annullare la prenotazione. Controlla che l'id sia corretto e attivo.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void apri_form_annulla_prenotazione(GestoreAutenticazione auth, GestoreServiziStudente servizi, GestoreNotifiche notifiche, GestorePrenotazioni prenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Annulla Prenotazione");
        frameCorrente = frame;

        frame.setContentPane(new FormAnnullaPrenotazione(auth, servizi, notifiche, prenotazioni, gestoreSale, gestoreStatistiche).panelAnnullaPrenotazione);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 250);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
