package org.example.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Company;
import org.example.entity.Transportation;
import org.example.entity.Transportation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.List;

public class TransportationDao {
    public static void createTransportation(Transportation transportation){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(transportation);
            transaction.commit();
        }
    }
    public static Transportation getTransportationById(long id){
        Transportation transportation;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            transportation = session.get(Transportation.class,id);
            transaction.commit();
        }
        return transportation;
    }
    public static List<Transportation > getTransportations(){
        List<Transportation> transportations;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            transportations =session.createQuery("Select t from Transportation t", Transportation.class).getResultList();
            transaction.commit();
        }
        return  transportations;
    }
    public static Transportation updateTransportation(Transportation transportation){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(transportation);
            transaction.commit();
        }
        return transportation;
    }
    public static void deleteTransportation(Transportation transportation){
        try(Session session=SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(transportation);
            transaction.commit();
        }
    }
    public static BigDecimal getTransportationCost(long id){
        Transportation transportation = getTransportationById(id);
        BigDecimal cost = null;
        try{

            transportation = TransportationDao.getTransportationById(id);
            cost = transportation.getPrice();
        }catch(NullPointerException e){
            System.out.println(e);
        }
        return  cost;
    }
    public static List<Transportation> getTransportationsSortByDestinationASC(){
        List<Transportation> transportationList;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            transportationList = session.createQuery("select t from Transportation t" +
                                    " order by t.destination ASC",
                            Transportation.class)
                    .getResultList();
            transaction.commit();

        }
        return transportationList;
    }
    public static List<Transportation> getTransportationsSortByDestinationDESC(){
        List<Transportation> transportationList;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            transportationList = session.createQuery("select t from Transportation t" +
                                    " order by t.destination DESC",
                            Transportation.class)
                    .getResultList();
            transaction.commit();

        }
        return transportationList;
    }
    public static Long  count(){
        Long count;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Query<?> query = session.createQuery("select count(*) from Transportation t");
            count = (Long) query.uniqueResult();
            transaction.commit();
        }
        return count;
    }
    public static Long  countByCompany(long id){
        Long count;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Query<?> query = session.createQuery("select count(*) from Transportation t" +
                    " where id=:id").setParameter("id",id);
            count = (Long) query.uniqueResult();
            transaction.commit();
        }
        return count;
    }
    public static BigDecimal  moneyEarnedByCompany(long id){
        BigDecimal sum;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Query<?> query = session.createQuery("select sum(price) from Transportation t " +
                    "where id =:id").setParameter("id",id);
            sum = (BigDecimal) query.uniqueResult();
            transaction.commit();
        }
        return sum;
    }

}
