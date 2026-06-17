package Boundary;

import Controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FormProfiloStudente {
    private JPanel panelStudente;
    private JLabel labelMatricola;
    private JLabel labelNome;
    private JLabel labelCognome;
    private JLabel labelContatoreAccessi;
    private JLabel fieldMatricola;
    private JLabel fieldNome;
    private JLabel fieldCognome;
    private JLabel fieldContatoreAccessi;
    private JButton visualizzaStoricoButton;
    private JButton visualizzaPrenFutureButton;
    private JButton effettuaPrenotazioneButton;
    private JButton annullaPrenotazioneButton;
    private JButton visualizzaNotificheButton;
    private JButton effettuaCheckInButton;
    private JButton logoutButton;
    private JLabel labelStudente;
    private JScrollPane scrollPrenotazioni;
    private JTable tablePrenotazioni;
    private JLabel labelTabella;

    private static JFrame frameCorrente;

    public FormProfiloStudente(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        fieldMatricola.setText(gestoreAutenticazione.getMatricolaStudenteLoggato());
        fieldNome.setText(gestoreAutenticazione.getUtenteAttuale().getNome());
        fieldCognome.setText(gestoreAutenticazione.getUtenteAttuale().getCognome());
        fieldContatoreAccessi.setText(String.valueOf(gestoreAutenticazione.getContatoreAccessiStudenteLoggato()));

        scrollPrenotazioni.setVisible(false);
        labelTabella.setText("");

        visualizzaStoricoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String[] colonne = {"ID prenotazione", "Codice postazione", "Data", "Ora inizio", "Ora fine", "Stato"};
                DefaultTableModel modelloStorico = new DefaultTableModel(colonne, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                List<String[]> storicoDati = gestoreServiziStudente.visualizzaMioStoricoRighe();

                for (String[] riga : storicoDati) {
                    modelloStorico.addRow(riga);
                }

                tablePrenotazioni.setModel(modelloStorico);
                scrollPrenotazioni.setVisible(true);
            }
        });

        visualizzaPrenFutureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String[] colonne = {"ID prenotazione", "Codice postazione", "Data", "Ora inizio", "Ora fine", "Stato"};
                DefaultTableModel modelloFuture = new DefaultTableModel(colonne, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                List<String[]> futureDati = gestoreServiziStudente.visualizzaMiePrenotazioniFutureRighe();

                for (String[] riga : futureDati) {
                    modelloFuture.addRow(riga);
                }

                tablePrenotazioni.setModel(modelloFuture);
                scrollPrenotazioni.setVisible(true);
            }
        });

        effettuaPrenotazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();
                FormEffettuaPrenotazione.apri_form_effettua_prenotazione(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        annullaPrenotazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();
                FormAnnullaPrenotazione.apri_form_annulla_prenotazione(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        visualizzaNotificheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormNotifica.apri_form_notifica(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        effettuaCheckInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormCheckIn.apri_form_checkin(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int conferma = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler uscire?", "Logout", JOptionPane.YES_NO_OPTION);
                if (conferma == JOptionPane.YES_OPTION) {
                    gestoreAutenticazione.eseguiLogout();

                    if (frameCorrente != null) frameCorrente.dispose();
                    FormSceltaIniziale.apri_scelta_iniziale(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
                }
            }
        });
    }

    public static JFrame apri_profilo_studente(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Dashboard Studente");
        frameCorrente = frame;

        FormProfiloStudente form = new FormProfiloStudente(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
        frame.setContentPane(form.panelStudente);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(900, 650);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return frame;
    }
}