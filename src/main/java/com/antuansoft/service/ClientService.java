package com.antuansoft.service;

import com.antuansoft.herospace.PersonDAO;
import com.antuansoft.init.Thing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by amit on 5/12/16.
 */
@Service
public class ClientService {

    @Autowired
    Thing thing;
    @Autowired
    PersonDAO personDAO;

    ClientService() {

        //   sayMeHero();
    }


    public void sayMeHero() {
        String ss = thing.getName();
        System.out.println(thing.getName());
        long count = personDAO.count();
        System.out.println(count);


    }

}
