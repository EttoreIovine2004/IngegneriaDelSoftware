package Boundary;

import Controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormLogin {
    private JTextField textFieldEmail;
    private JPanel loginPanel;
    private JLabel labelEmail;
    private JLabel labelPassword;
    private JButton buttonLogin;
    private JPasswordField passwordField;
    private JButton buttonIndietro;
    private JLabel labelBenvenuto;

    private static JFrame frameCorrente;

    public FormLogin(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String email = textFieldEmail.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if(email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci tutte le credenziali!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean successoLogin = gestoreAutenticazione.eseguiLogin(email, password);

                if (successoLogin) {
                    JOptionPane.showMessageDialog(null, "Login effettuato! Benvenuto.", "Successo", JOptionPane.INFORMATION_MESSAGE);

                    if (frameCorrente != null) {
                        frameCorrente.dispose();
                    }

                    if (gestoreAutenticazione.isStudenteLoggato()) {
                        FormProfiloStudente.apri_profilo_studente(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
                    } else if (gestoreAutenticazione.isBibliotecarioLoggato()) {
                        FormProfiloBibliotecario.apri_profilo_bibliotecario(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Credenziali errate. Riprova.", "Errore Autenticazione", JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
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

    public static JFrame apri_form_login(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Accesso al sistema");
        frameCorrente = frame;

        FormLogin form = new FormLogin(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
        frame.setContentPane(form.loginPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centra sullo schermo
        frame.setVisible(true);
        frame.setResizable(false);

        return frame;
    }
}
