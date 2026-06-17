package Database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static JpaUtil instance;
    private static EntityManagerFactory emf;

    // Costruttore privato per impedire "new JpaUtil()" all'esterno
    private JpaUtil() {
        //"BibliotecaPU" deve coincidere con il name in persistence.xml
        emf = Persistence.createEntityManagerFactory("BibliotecaPU");
    }

    public static JpaUtil getInstance() {
        if (instance == null) {
            instance = new JpaUtil();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void chiudi() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
