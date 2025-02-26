package org.example.dao;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.example.Dto.CreateCompanyDTO;
import org.example.Dto.PaymentDTO;
import org.example.configuration.SessionFactoryUtil;
import org.example.entity.Company;
import org.example.entity.Employee;
import org.example.entity.Vehicle;
import org.example.exceptions.CompanyException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompanyDao {

    public static void createCompany(@Valid Company company) {
        Transaction transaction = null;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(company);
            transaction.commit();
        }catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new CompanyException("Error saving company", e);
        }catch (ConstraintViolationException e){
            throw new CompanyException("Error saving company" + e.getMessage(), e);
        }
    }
    public static void saveCompanyDto(CreateCompanyDTO createCompanyDto) {
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Company company = new Company();
            company.setName(createCompanyDto.getName());
            session.save(company);
            transaction.commit();
        }
    }
    public static Company getCompanyById(long id){
        Company company;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            company = session.get(Company.class,id);
            transaction.commit();
        }
        return company;
    }
    public static List<Company > getCompanies(){
        List<Company> companies;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            companies =session.createQuery("Select c from Company c", Company.class).getResultList();
            transaction.commit();
        }
        return  companies;
}

    public static Company updateCompany(@Valid Company company){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(company);
            transaction.commit();
        }
        return company;
}
public static void deleteCompany(Company company){
        try(Session session=SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.delete(company);
            transaction.commit();
        }
}
public static Set<Employee> getCompanyEmployee(long id){
        Company company;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            company = session.createQuery("select c from Company c" +
                                    " join fetch c.employees" +
                                    " where c.id = :id",
                    Company.class)
                    .setParameter("id",id)
                    .getSingleResult();
            transaction.commit();
        }
        return company.getEmployees();
}

    public static Company updateCompanyNoEmployee(Company company){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Set<Employee> employees = new HashSet<>();
            company.setEmployees(employees);
            session.save(company);
            transaction.commit();
        }
        return company;
    }

    public static Set<Vehicle> getCompanyVehicles(long id){
        Company company;
        Set<Vehicle>vehicele;
        try(Session sessin = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = sessin.beginTransaction();
            company = sessin.createQuery("Select c from Company c " +
                    "join fetch c.vehicles " +
                    "where c.id = :id",
                    Company.class).setParameter("id",id).getSingleResult();
        }
        return company.getVehicles();
    }
    public static List<Company> companiesWithNameLike(String name){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Company> cr =cb.createQuery(Company.class);
            Root<Company>root = cr.from(Company.class);
            cr.select(root).where(cb.like(root.get("name"), "%" + name + "%"));
            Query query = session.createQuery(cr);
            List<Company> companies = query.getResultList();
            return companies;
        }
    }
    public static BigDecimal getIncome(long id){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            Company company = session.createQuery("select c from Company c" +
                                    " where c.id = :id",
                            Company.class)
                    .setParameter("id",id)
                    .getSingleResult();
            transaction.commit();
            return company.getIncome();
        }
    }
    public static List<Company> getCompaniesSortedByName(){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            List<Company> companies = session.createQuery("select c from Company c" +
                                    " order by c.name ASC",
                            Company.class)
                    .getResultList();
            transaction.commit();
            return companies;
        }

    }
    public static List<Company> getCompaniesSortedByIncome(){
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            List<Company> companies = session.createQuery("select c from Company c" +
                                    " order by c.income DESC",
                            Company.class)
                    .getResultList();
            transaction.commit();
            return companies;
        }
    }
    public static List<PaymentDTO> getPayments(long id){
        List<PaymentDTO> payments;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            payments = session.createQuery(
                    "select new org.example.Dto.PaymentDTO(p.id, p.paid, p.cost) from Payment p " +
                            "join p.toCompany c " +
                            "where c.id = :id ",
                    PaymentDTO.class)
                    .setParameter("id", id)
                    .getResultList();
            transaction.commit();
        }
        return payments;
    }
}
