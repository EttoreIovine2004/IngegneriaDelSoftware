package Boundary;

import Controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;

public class FormModificaSala {
    private JPanel panelModificaSala;
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
    private JButton indietroButton;
    private JButton modificaSalaButton;
    private JTextField textFieldIdSala;
    private JLabel labelIdSala;
    private JLabel labelNomeArea;
    private JLabel labelTipologiaArea;
    private JTextField textFieldNomeArea;
    private JTextField textFieldTipoArea;
    private JButton aggiungiAreaTematicaButton;

    private static JFrame frameCorrente;

    public FormModificaSala(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();
                FormProfiloBibliotecario.apri_profilo_bibliotecario(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        modificaSalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idSala = textFieldIdSala.getText().trim();
                String nome = textFieldNome.getText().trim();
                String descrizione = textFieldDescrizione.getText().trim();
                String capienzaStr = textFieldCapienza.getText().trim();
                String aperturaStr = textFieldApertura.getText().trim();
                String chiusuraStr = textFieldChiusura.getText().trim();

                if (idSala.isEmpty() || nome.isEmpty() || descrizione.isEmpty() || capienzaStr.isEmpty() || aperturaStr.isEmpty() || chiusuraStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tutti i campi sono obbligatori.", "Campi Mancanti", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    int capienza = Integer.parseInt(capienzaStr);
                    if (capienza <= 0) {
                        JOptionPane.showMessageDialog(null, "La capienza deve essere un numero positivo.", "Errore Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    LocalTime apertura = LocalTime.parse(aperturaStr);
                    LocalTime chiusura = LocalTime.parse(chiusuraStr);

                    if (!chiusura.isAfter(apertura)) {
                        JOptionPane.showMessageDialog(null, "L'orario di chiusura deve seguire l'orario di apertura.", "Errore Orario", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean successo = gestoreSale.modificaSala(idSala, nome, descrizione, capienza, apertura, chiusura);

                    if (successo) {
                        JOptionPane.showMessageDialog(null, "Sala studio modificata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        indietroButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossibile modificare la sala. Verifica che l'id inserito sia corretto.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "La capienza deve contenere solo cifre numeriche.", "Errore Formato", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Gli orari devono essere inseriti nel formato corretto (HH:MM).", "Errore Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        aggiungiAreaTematicaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idSala = textFieldIdSala.getText().trim();
                String nomeArea = textFieldNomeArea.getText().trim();
                String tipologia = textFieldTipoArea.getText().trim();

                if (idSala.isEmpty() || nomeArea.isEmpty() || tipologia.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci l'id della Sala, il Nome dell'Area e la Tipologia.", "Campi Mancanti", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean successo = gestoreSale.aggiungiAreaTematica(nomeArea, tipologia, idSala);

                if (successo) {
                    JOptionPane.showMessageDialog(null, "Area tematica inserita con successo nella sala " + idSala + "!", "Successo", JOptionPane.INFORMATION_MESSAGE);

                    textFieldNomeArea.setText("");
                    textFieldTipoArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Impossibile aggiungere l'area.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void apri_form_modifica_sala(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Modifica Dati Sala Studio");
        frameCorrente = frame;
        frame.setContentPane(new FormModificaSala(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche).panelModificaSala);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
