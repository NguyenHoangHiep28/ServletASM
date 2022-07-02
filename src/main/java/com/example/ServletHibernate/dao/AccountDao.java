package com.example.ServletHibernate.dao;

import com.example.ServletHibernate.db.HibernateUtil;
import com.example.ServletHibernate.model.Account;
import com.example.ServletHibernate.model.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AccountDao {

    public void register(Account account) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // start a transaction
            transaction = session.beginTransaction();
            // save the student object
            session.save(account);
            // commit transaction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List login(String username, String password) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Account A WHERE A.username = :username AND A.password = :password";
            Query query = session.createQuery(hql);
            query.setParameter("username",username);
            query.setParameter("password", password);
            List results = query.list();
            return results;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public List findUserByUsername(String username) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Account A WHERE A.username = :username";
            Query query = session.createQuery(hql);
            query.setParameter("username",username);
            List results = query.list();
            return results;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }
}
