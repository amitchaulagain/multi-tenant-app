/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antuansoft.async;

import com.antuansoft.herospace.AuthenticationUtil;
import com.antuansoft.herospace.Person;
import com.antuansoft.history.HistoryPersistenceStrategy;
import com.antuansoft.init.MongoConnection;
import org.bson.types.ObjectId;
import com.antuansoft.async.AsyncHistoryAwarePerson;
import com.antuansoft.async.AsyncHistoryAwareDAO;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author nasir
 *
 */

public class AsyncPersonDAO extends AsyncHistoryAwareDAO<Person, AsyncHistoryAwarePerson> {




    public AsyncPersonDAO(String connectionURL, String dbname, String collectionName, Class<Person> type) throws UnknownHostException {

        super(connectionURL, dbname, collectionName, Person.class, AsyncHistoryAwarePerson.class);
        String hero=connectionURL;
    }

    public AsyncPersonDAO(MongoConnection mc) throws UnknownHostException {
        super(mc.connectionURL,mc.dbName,mc.collectionName,Person.class, AsyncHistoryAwarePerson.class);
    }

    public AsyncPersonDAO() throws UnknownHostException{
        super("localhost:27017", AuthenticationUtil.getCurrentUser().getClient_schema(), "person", Person.class, AsyncHistoryAwarePerson.class);


    }




}
