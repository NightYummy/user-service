import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class ConnectionConfiguration {

    private final SessionFactory sessionFactory;

    public ConnectionConfiguration(String configFile) {
        Configuration configuration = new Configuration().configure(configFile);
        configuration.addAnnotatedClass(User.class);
        sessionFactory = configuration.buildSessionFactory();
    }

    public ConnectionConfiguration(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public ConnectionConfiguration() {
        this("hibernate.cfg.xml");
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void shutdown() {
        sessionFactory.close();
    }
}
