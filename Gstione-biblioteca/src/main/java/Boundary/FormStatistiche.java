package Boundary;

import Controller.*;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class FormStatistiche {
    private JPanel panelStatistiche;
    private JLabel labelOcupate;
    private JLabel labelLibere;
    private JLabel labelTasso;
    private JLabel labelPrenGiorn;
    private JLabel labelPrenNonConf;
    private JLabel labelNumLibere;
    private JLabel labelNumOccupate;
    private JLabel labelNumTasso;
    private JLabel labelNumPrenGiorn;
    private JLabel labelNumPrenNonConf;
    private JLabel labelStatistiche;
    private JLabel labelData;
    private DatePicker datePicker;
    private JButton visualizzaStatisticheButton;
    private JLabel labelMatricola;
    private JTextField textFieldMatricola;
    private JTable tablePrenotazioniStudente;
    private JScrollPane scrollPrenotazioniStudente;
    private JTextField textFieldIdSala;
    private JScrollPane scrollPrenotazioniSala;
    private JTable tablePrenotazioniSala;
    private JButton indietroButton;

    private static JFrame frameCorrente;
    private final DefaultTableModel modelloTabellaSala;
    private final DefaultTableModel modelloTabellaStudente;

    public FormStatistiche(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        //Colonne tabella sala
        modelloTabellaSala = new DefaultTableModel(
                new Object[]{"ID Prenotazione", "Postazione", "Matricola Studente", "Ora Inizio", "Ora Fine", "Stato"}, 0
        );
        tablePrenotazioniSala.setModel(modelloTabellaSala);

        //Colonne tabella studente
        modelloTabellaStudente = new DefaultTableModel(
                new Object[]{"ID Prenotazione", "ID Sala", "Postazione", "Data", "Ora Inizio", "Ora Fine", "Stato"}, 0
        );
        tablePrenotazioniStudente.setModel(modelloTabellaStudente);

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();
                FormProfiloBibliotecario.apri_profilo_bibliotecario(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        visualizzaStatisticheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idSala = textFieldIdSala.getText().trim();
                String matricola = textFieldMatricola.getText().trim();
                LocalDate dataScelta = datePicker.getDate();

                if (idSala.isEmpty() || dataScelta == null) {
                    JOptionPane.showMessageDialog(null, "ID Sala e la data sono obbligatori per calcolare le statistiche.", "Campi Mancanti", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String report = gestoreStatistiche.generaStatisticheSala(idSala, dataScelta);

                if (report.contains("Errore")) {
                    JOptionPane.showMessageDialog(null, report, "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    String[] righeReport = report.split("\n");
                    for (String riga : righeReport) {
                        if (riga.contains("Prenotazioni totali:")) {
                            labelNumPrenGiorn.setText(riga.split(":")[1].trim());
                        } else if (riga.contains("Prenotazioni non confermate")) {
                            labelNumPrenNonConf.setText(riga.split(":")[1].trim());
                        } else if (riga.contains("Tasso di Occupazione")) {
                            labelNumTasso.setText(riga.split(":")[1].trim());
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Feedback testuale parziale inserito nelle etichette.");
                }

                String dataSceltaStr = dataScelta.toString();

                List<String[]> storicoSalaTotale = gestoreStatistiche.visualizzaStoricoSalaRighe(idSala);
                modelloTabellaSala.setRowCount(0);

                int occupateOggi = 0;
                for (String[] riga : storicoSalaTotale) {
                    if (riga[4].equals(dataSceltaStr)) {
                        modelloTabellaSala.addRow(riga);
                        occupateOggi++;
                    }
                }

                labelNumOccupate.setText(String.valueOf(occupateOggi));
                int capienzaTotale = gestoreStatistiche.getCapienzaSala(idSala);
                if (capienzaTotale > 0) {
                    labelNumLibere.setText(String.valueOf(capienzaTotale - occupateOggi));
                } else {
                    labelNumLibere.setText("N/D");
                }

                modelloTabellaStudente.setRowCount(0);
                if (!matricola.isEmpty()) {
                    List<String[]> storicoStudenteTotale = gestoreStatistiche.visualizzaStoricoStudenteRighe(matricola);

                    int trovateOggiStudente = 0;
                    for (String[] riga : storicoStudenteTotale) {
                        if (riga[4].equals(dataSceltaStr)) {
                            modelloTabellaStudente.addRow(riga);
                            trovateOggiStudente++;
                        }
                    }

                    if (trovateOggiStudente == 0) {
                        JOptionPane.showMessageDialog(null, "Lo studente non ha prenotazioni per la data selezionata.", "Info Studente", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }

    public static void apri_form_statistiche(GestoreAutenticazione auth, GestoreServiziStudente servizi, GestoreNotifiche notifiche, GestorePrenotazioni prenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Cruscotto Amministrativo Analisi e Monitoraggio");
        frameCorrente = frame;
        frame.setContentPane(new FormStatistiche(auth, servizi, notifiche, prenotazioni, gestoreSale, gestoreStatistiche).panelStatistiche);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
