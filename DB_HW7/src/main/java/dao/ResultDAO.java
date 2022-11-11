package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import tables.Country;
import tables.Result;
import utils.HibernateSessionFactoryUtil;

public class ResultDAO {
    public void save(Result o) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(o);
        tx1.commit();
        session.close();
    }
}
