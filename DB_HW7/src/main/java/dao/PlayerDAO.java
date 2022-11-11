package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import tables.Country;
import tables.Player;
import utils.HibernateSessionFactoryUtil;

public class PlayerDAO {
    public void save(Player o) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(o);
        tx1.commit();
        session.close();
    }
}
