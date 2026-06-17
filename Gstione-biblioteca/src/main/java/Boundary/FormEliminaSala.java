package Boundary;

import Controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormEliminaSala {
    private JPanel panelEliminazione;
    private JLabel labelElimina;
    private JTextField textFieldIdSala;
    private JLabel labelIdSala;
    private JButton eliminaSalaButton;
    private JButton indietroButton;

    private static JFrame frameCorrente;

    public FormEliminaSala(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();

                FormProfiloBibliotecario.apri_profilo_bibliotecario(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });

        eliminaSalaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idSala = textFieldIdSala.getText().trim();

                if (idSala.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci un ID Sala valido per procedere.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int scelta = JOptionPane.showConfirmDialog(
                        null,
                        "Sei sicuro di voler eliminare definitivamente la sala " + idSala + "?\nQuesta azione potrebbe rimuovere le postazioni collegate.",
                        "Conferma Eliminazione",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (scelta == JOptionPane.YES_OPTION) {
                    boolean successo = gestoreSale.eliminaSala(idSala);

                    if (successo) {
                        JOptionPane.showMessageDialog(null, "La sala studio è stata eliminata con successo.", "Operazione Completata", JOptionPane.INFORMATION_MESSAGE);
                        indietroButton.doClick();
                    } else {
                        JOptionPane.showMessageDialog(null, "Impossibile eliminare la sala. Verifica che l'ID sia corretto.", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public static void apri_form_elimina_sala(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Elimina Sala Studio");
        frameCorrente = frame;

        frame.setContentPane(new FormEliminaSala(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche).panelEliminazione);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 250);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
