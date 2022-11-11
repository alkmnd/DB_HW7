package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import tables.Event;
import utils.HibernateSessionFactoryUtil;

public class EventDAO {
    public void save(Event o) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(o);
        tx1.commit();
        session.close();
    }
}
