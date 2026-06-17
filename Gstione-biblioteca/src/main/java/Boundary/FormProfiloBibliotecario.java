package Boundary;

import Controller.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FormProfiloBibliotecario {
    private JPanel panelBibliotecario;
    private JLabel labelCodice;
    private JLabel labelTarghettaCodice;
    private JButton modificaSalaButton;
    private JButton creaSalaButton;
    private JButton eliminaSalaButton;
    private JButton visualizzaStatisticheButton;
    private JButton logoutButton;
    private JButton visualizzaInfoSaleButton;
    private JTable tableSale;
    private JScrollPane scrollSale;
    private JTextField textFieldIdSala;
    private JLabel labelIdSala;

    private static JFrame frameCorrente;
    private final DefaultTableModel modelloTabella;

    public FormProfiloBibliotecario(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        modelloTabella = new DefaultTableModel(new Object[]{"ID Sala", "Nome Sala", "Descrizione", "Capienza", "Apertura", "Chiusura"}, 0);
        tableSale.setModel(modelloTabella);

        if (gestoreAutenticazione.getUtenteAttuale() != null) {
            labelCodice.setText(gestoreSale.getCodiceIdentificativoBibliotecario());
        } else {
            labelCodice.setText("N/D");
        }

        visualizzaInfoSaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idSalaFiltro = textFieldIdSala.getText().trim();

                if(idSalaFiltro.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci l'id di una sala studio per vedere le prenotazioni attive.", "Campo Richiesto", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                List<String[]> prenotazioniFormattate = gestoreStatistiche.visualizzaPrenotazioniAttiveSalaRighe(idSalaFiltro);

                modelloTabella.setRowCount(0);
                for(String[] riga : prenotazioniFormattate) {
                    modelloTabella.addRow(riga);
                }

                if(prenotazioniFormattate.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nessuna prenotazione attiva trovata per la sala inserita.", "Informazione", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        creaSalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();

                FormCreaSala.apri_form_crea_sala(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        modificaSalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = tableSale.getSelectedRow();
                String idSalaSelezionata = "";

                if (rigaSelezionata != -1) {
                    idSalaSelezionata = modelloTabella.getValueAt(rigaSelezionata, 0).toString();
                }

                if (frameCorrente != null) frameCorrente.dispose();

                FormModificaSala.apri_form_modifica_sala(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        eliminaSalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();

                FormEliminaSala.apri_form_elimina_sala(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        visualizzaStatisticheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();

                FormStatistiche.apri_form_statistiche(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gestoreAutenticazione.eseguiLogout();
                if (frameCorrente != null) frameCorrente.dispose();
                // Ritorna alla schermata iniziale del Eseguibile.Main
                FormSceltaIniziale.apri_scelta_iniziale(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });
    }

    public static void apri_profilo_bibliotecario(GestoreAutenticazione auth, GestoreServiziStudente servizi, GestoreNotifiche notifiche, GestorePrenotazioni prenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Profilo Bibliotecario");
        frameCorrente = frame;
        frame.setContentPane(new FormProfiloBibliotecario(auth, servizi, notifiche, prenotazioni, gestoreSale, gestoreStatistiche).panelBibliotecario);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
