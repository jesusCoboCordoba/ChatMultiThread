package com.Database;

import com.hibernateClases.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MessageService {
    private final SessionFactory dbConnection ;
    public MessageService () {
        dbConnection = DBConnection.getSessionFactory();
    }

    public void saveMessage (Message message){

        try(Session session = dbConnection.openSession()){
            Transaction transaction = session.beginTransaction();
            message.setMessageDate(LocalDate.now());
            message.setMessageHour(LocalTime.now());
            session.persist(message);
            transaction.commit();
        }
    }

    public Message readMessageById (int Id){
        try(Session session = dbConnection.openSession()){
            Query<Message> query = session.createQuery("from Message where id = :id", Message.class);
            query.setParameter("id",String.valueOf(Id));
            Message message = query.getSingleResult();
            return message;
        }
    }

    public List<Message> readLast10Messages (){
        try(Session session = dbConnection.openSession()){
            Query<Message> query = session.createQuery("from Message", Message.class);
            query.setMaxResults(10);
            List<Message> messageList = query.list();
            return messageList;
        }
    }

    public void closeSession (){
        dbConnection.close();
    }

}
