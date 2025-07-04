package com.Database;

import com.hibernateClases.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.logging.Logger;

public class UserService {
    public static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private final SessionFactory dbConnection ;

    public UserService(){
        dbConnection = DBConnection.getSessionFactory();
    }

    public void saveUser (User user){
        try (Session session = dbConnection.openSession()){
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
    }

    public void readAllUsers(){
       try( Session session = dbConnection.openSession()) {
           Query query = session.createQuery("FROM User",User.class);
           List<User> list = query.list();
           for (User user: list){
               String out = user.toString();
               System.out.println(out);
           }
       }
    }
    public User readUserById(int Id){
        try( Session session = dbConnection.openSession()) {
            User user = session.find(User.class,Id);
            return user;
        }
    }
    public User readUserByUserName(String name){
        try( Session session = dbConnection.openSession()) {
            Query<User> query = session.createQuery(" FROM User WHERE name = :name", User.class);
            query.setParameter("name",name);
            try {
                return query.getSingleResult();
            } catch (jakarta.persistence.NoResultException e) {
                return null;
            }

        }
    }

    public String readUserByEmail (String email){
       try(Session session = dbConnection.openSession()){
           Query<User> query = session.createQuery("FROM User Where email = :email", User.class);
           query.setParameter("email",email);
           User user = query.getSingleResult();
           return user.toString();
       }
    }

    public void closeSession (){
        dbConnection.close();
    }

}
