package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import tables.Country;
import tables.Olympic;
import utils.HibernateSessionFactoryUtil;

public class OlympicDAO {
    public void save(Olympic o) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(o);
        tx1.commit();
        session.close();
    }
}
