import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class ConnectionConfiguration {

    private static final SessionFactory sessionFactory;

    static {
        Configuration configuration = new Configuration().configure();
        configuration.addAnnotatedClasses(User.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        sessionFactory.close();
    }
}
