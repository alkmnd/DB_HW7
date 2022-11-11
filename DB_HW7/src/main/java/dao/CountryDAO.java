package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import tables.Country;
import utils.HibernateSessionFactoryUtil;

public class CountryDAO {

    public void save(Country o) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(o);
        tx1.commit();
        session.close();
    }
}
