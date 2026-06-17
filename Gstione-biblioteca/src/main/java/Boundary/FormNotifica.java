package Boundary;

import Controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class FormNotifica {
    private JPanel panelNotifica;
    private JLabel labelNotifica;
    private JScrollPane scrollNotifiche;
    private JList listNotifiche;
    private JButton buttonIndietro;

    private static JFrame frameCorrente;
    private final List<String> idNotifiche;
    private final List<String> testiNotifiche;

    public FormNotifica(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        idNotifiche = new ArrayList<>();
        testiNotifiche = new ArrayList<>();

        caricaNotifiche(gestoreNotifiche);

        listNotifiche.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int indiceSelezionato = listNotifiche.getSelectedIndex();

                    if (indiceSelezionato != -1) {
                        String idReale = idNotifiche.get(indiceSelezionato);
                        gestoreNotifiche.apriNotifica(idReale);

                        String testoCompleto = testiNotifiche.get(indiceSelezionato);

                        JOptionPane.showMessageDialog(null, testoCompleto, "Dettaglio Notifica", JOptionPane.INFORMATION_MESSAGE);

                        caricaNotifiche(gestoreNotifiche);
                    }
                }
            }
        });

        buttonIndietro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frameCorrente != null) frameCorrente.dispose();
                FormProfiloStudente.apri_profilo_studente(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche);
            }
        });
    }

    private void caricaNotifiche(GestoreNotifiche gestoreNotifiche) {
        DefaultListModel<String> modelloLista = new DefaultListModel<>();

        idNotifiche.clear();
        testiNotifiche.clear();

        List<String[]> notificheFormattate = gestoreNotifiche.visualizzaMieNotificheFormattate();

        for (String[] n : notificheFormattate) {
            idNotifiche.add(n[0]);
            testiNotifiche.add(n[1]);
            modelloLista.addElement(n[2] + n[1]);
        }

        listNotifiche.setModel(modelloLista);
    }

    public static void apri_form_notifica(GestoreAutenticazione gestoreAutenticazione, GestoreServiziStudente gestoreServiziStudente, GestoreNotifiche gestoreNotifiche, GestorePrenotazioni gestorePrenotazioni, GestoreSale gestoreSale, GestoreStatistiche gestoreStatistiche) {
        JFrame frame = new JFrame("Le mie Notifiche");
        frameCorrente = frame;
        frame.setContentPane(new FormNotifica(gestoreAutenticazione, gestoreServiziStudente, gestoreNotifiche, gestorePrenotazioni, gestoreSale, gestoreStatistiche).panelNotifica);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
