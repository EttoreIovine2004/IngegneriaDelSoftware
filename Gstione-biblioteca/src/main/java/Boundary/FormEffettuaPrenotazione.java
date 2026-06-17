package Boundary;

import Controller.*;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class FormEffettuaPrenotazione {
    private JPanel panelEffettuaPrenotazione;
    private JButton cercaSaleDisponibiliButton;
    private JTextField textFieldOraFine;
    private JTextField textFieldOraInizio;
    private JLabel labelPrenotazione;
    private JLabel labelData;
    private JLabel labelOraInizio;
    private JLabel labelOraFine;
    private JTable tableSale;
    private JScrollPane scrollSale;
    private DatePicker datePicker;
    private JButton confermaPrenotazioneButton;
    private JButton indietroButton;

    private static JFrame frameCorrente;
    private final DefaultTableModel modelloTabella;

    public FormEffettuaPrenotazione(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        modelloTabella = new DefaultTableModel(new Object[]{"ID Sala", "Nome Sala", "Descrizione", "Codice postazione", "Area tematica"}, 0);
        tableSale.setModel(modelloTabella);

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();
                FormProfiloStudente.apri_profilo_studente(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        cercaSaleDisponibiliButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate data = datePicker.getDate();

                if (data == null) {
                    JOptionPane.showMessageDialog(null, "Seleziona una data valida dal calendario.", "Errore", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    LocalTime inizio = LocalTime.parse(textFieldOraInizio.getText().trim());
                    LocalTime fine = LocalTime.parse(textFieldOraFine.getText().trim());

                    if (!fine.isAfter(inizio)) {
                        JOptionPane.showMessageDialog(null, "L'ora di fine deve essere successiva all'ora di inizio.", "Errore Orario", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    List<String[]> saleDisponibili = gestoreServiziStudente.cercaSaleDisponibiliFormattate(data.toString(), inizio.toString(), fine.toString());

                    modelloTabella.setRowCount(0);
                    for (String[] riga : saleDisponibili) {
                        modelloTabella.addRow(riga);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Inserisci gli orari nel formato corretto (HH:MM).", "Errore Formato", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        confermaPrenotazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = tableSale.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(null, "Seleziona una sala dalla tabella per continuare.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String codicePostazione = modelloTabella.getValueAt(rigaSelezionata, 3).toString();
                LocalDate data = datePicker.getDate();

                try {
                    LocalTime inizio = LocalTime.parse(textFieldOraInizio.getText().trim());
                    LocalTime fine = LocalTime.parse(textFieldOraFine.getText().trim());

                    boolean successo = gestorePrenotazioni.effettuaPrenotazione(codicePostazione, data, inizio, fine);

                    if (successo) {
                        JOptionPane.showMessageDialog(null, "Prenotazione salvata con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                        indietroButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "Errore: La postazione è già occupata", "Errore", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Si è verificato un errore durante nel controllo dei dati temporali.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void apri_form_effettua_prenotazione(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni , GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Effettua Prenotazione");
        frameCorrente = frame;
        frame.setContentPane(new FormEffettuaPrenotazione(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche).panelEffettuaPrenotazione);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
