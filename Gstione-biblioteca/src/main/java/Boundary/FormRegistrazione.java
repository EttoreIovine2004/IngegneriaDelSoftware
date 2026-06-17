package Boundary;

import Controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormRegistrazione {
    private JPanel panelRegistrazione;
    private JLabel labelInserimento;
    private JLabel labelNome;
    private JLabel labelCognome;
    private JLabel labelEmail;
    private JLabel labelPassword;
    private JTextField textFieldNome;
    private JTextField textFieldCognome;
    private JTextField textFieldEmail;
    private JPasswordField passwordField;
    private JComboBox comboBoxRuolo;
    private JLabel labelRuolo;
    private JButton buttonRegistrati;
    private JButton buttonIndietro;

    private static JFrame frameCorrente;

    public FormRegistrazione(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        buttonRegistrati.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nome = textFieldNome.getText().trim();
                String cognome = textFieldCognome.getText().trim();
                String email = textFieldEmail.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                String ruoloSelezionato = "";
                if (comboBoxRuolo.getSelectedItem() != null) {
                    ruoloSelezionato = comboBoxRuolo.getSelectedItem().toString();
                }

                if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Compila tutti i campi e seleziona un ruolo!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean successoRegistrazione = gestoreAutenticazione.registraUtente(nome, cognome, email, password, ruoloSelezionato);

                if (successoRegistrazione) {
                    JOptionPane.showMessageDialog(null, "Registrazione completata! Ora puoi accedere.", "Successo", JOptionPane.INFORMATION_MESSAGE);

                    if (frameCorrente != null) {
                        frameCorrente.dispose();
                    }

                    //Rimandiamo l'utente al Login per accedere con le nuove credenziali
                    FormLogin.apri_form_login(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);

                } else {
                    JOptionPane.showMessageDialog(null, "Errore durante la registrazione.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (frameCorrente != null) {
                    frameCorrente.dispose();
                }

                FormSceltaIniziale.apri_scelta_iniziale(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });
    }

    public static JFrame apri_form_registrazione(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Registrazione Nuovo Utente");
        frameCorrente = frame;

        FormRegistrazione form = new FormRegistrazione(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
        frame.setContentPane(form.panelRegistrazione);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);

        return frame;
    }
}
