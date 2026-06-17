package Controller;

import Entity.Notifica;
import Entity.SistemaUtilita;

import java.util.ArrayList;
import java.util.List;

public class GestoreNotifiche {

    private final SistemaUtilita sistemaUtilita;
    private final GestoreAutenticazione auth;

    //Costruttore
    public GestoreNotifiche(SistemaUtilita sistemaUtilita, GestoreAutenticazione auth) {
        this.sistemaUtilita = sistemaUtilita;
        this.auth = auth;
    }


    // --- METODI ---

    public void apriNotifica(String idNotifica) {
        if(auth.getUtenteAttuale() == null) {
            return;
        }

        sistemaUtilita.apriNotifica(idNotifica);
    }

    public void generaNotifichePromemoria() {
        sistemaUtilita.generaNotifichePromemoria();
    }

    public List<String[]> visualizzaMieNotificheFormattate() {
        String idStudente = auth.getUtenteAttuale().getIdUtente();

        List<Notifica> mieNotifiche = sistemaUtilita.getNotificheUtente(idStudente);

        List<String[]> datiFormattati = new ArrayList<>();

        for (Notifica n : mieNotifiche) {
            String[] riga = {
                    n.getIdNotifica(),
                    n.getMessaggio(),
                    n.isLetta() ? "[LETTA] " : "[NUOVA] "
            };
            datiFormattati.add(riga);
        }

        return datiFormattati;
    }
}
