package Boundary;

import Controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;

public class FormCreaSala {
    private JPanel panelCreaSala;
    private JLabel labelCreazione;
    private JLabel labelChiusura;
    private JLabel labelApertura;
    private JLabel labelCapienza;
    private JLabel labelDescrizione;
    private JLabel labelNome;
    private JTextField textFieldNome;
    private JTextField textFieldDescrizione;
    private JTextField textFieldCapienza;
    private JTextField textFieldApertura;
    private JTextField textFieldChiusura;
    private JButton creaSalaStudioButton;
    private JButton indietroButton;
    private JLabel labelPostazioni;
    private JTextField textFieldPostazioni;

    private static JFrame frameCorrente;

    public FormCreaSala(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();

                FormProfiloBibliotecario.apri_profilo_bibliotecario(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        creaSalaStudioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = textFieldNome.getText().trim();
                String descrizione = textFieldDescrizione.getText().trim();
                String capienzaStr = textFieldCapienza.getText().trim();
                String aperturaStr = textFieldApertura.getText().trim();
                String chiusuraStr = textFieldChiusura.getText().trim();
                String postazioniStr = textFieldPostazioni.getText().trim();

                if (nome.isEmpty() || descrizione.isEmpty() || capienzaStr.isEmpty() || aperturaStr.isEmpty() || chiusuraStr.isEmpty() || postazioniStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tutti i campi sono obbligatori.", "Campi Mancanti", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int capienza = Integer.parseInt(capienzaStr);
                    if (capienza <= 0) {
                        JOptionPane.showMessageDialog(null, "La capienza deve essere un numero intero positivo.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int numeroPostazioni = Integer.parseInt(postazioniStr);
                    if (numeroPostazioni < 1) {
                        JOptionPane.showMessageDialog(null, "La sala deve contenere almeno una postazione iniziale.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    LocalTime apertura = LocalTime.parse(aperturaStr);
                    LocalTime chiusura = LocalTime.parse(chiusuraStr);

                    if (!chiusura.isAfter(apertura)) {
                        JOptionPane.showMessageDialog(null, "L'orario di chiusura deve essere successivo a quello di apertura.", "Errore Orario", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean successo = gestoreSale.creaSala(nome, descrizione, capienza, apertura, chiusura, numeroPostazioni);

                    if (successo) {
                        JOptionPane.showMessageDialog(null, "Nuova sala studio creata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        indietroButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossibile creare la sala. Controlla i parametri inseriti.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "I campi capienza e postazioni devono contenere solo cifre numeriche.", "Errore Formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gli orari devono essere inseriti nel formato corretto (HH:MM).", "Errore Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void apri_form_crea_sala(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Registrazione Nuova Sala");
        frameCorrente = frame;
        frame.setContentPane(new FormCreaSala(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche).panelCreaSala);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
