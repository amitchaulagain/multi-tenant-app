package com.antuansoft.service;

import com.antuansoft.herospace.Person;
import com.antuansoft.herospace.PersonDAO;
import com.antuansoft.init.Thing;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by amit on 5/12/16.
 */

public class ClientService {

//@Autowired Thing thing;
    PersonDAO personDAO;

    public ClientService() throws UnknownHostException {

       personDAO= new PersonDAO();
    }


    public void sayMeHero() {
//        String ss = thing.getName();
//        System.out.println(thing.getName());
        long count = personDAO.count();
        System.out.println(count);


    }


    public List<Person> getAllPersons() {
        return personDAO.list();
    }


    @Async
    public Future<Person> findUser(String user) throws InterruptedException {
        System.out.println("Looking up " + user);
        final Person results = new Person();
        results.firstName="Hero";
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return new Future<Person>() {
            @Override
            public boolean cancel(boolean b) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Person get() throws InterruptedException, ExecutionException {
                return results;
            }

            @Override
            public Person get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return null;
            }
        };
    }

    public String doSlowWork() {
        return "Home calling home";
    }

    public Person savePerson(Person p) {
          return (Person) personDAO.save(p);
    }
}
