package com.antuansoft.mongodb.connection;



import com.antuansoft.mongodb.repositories.UserRepositoryDao;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;




public abstract class MongoConnection {

    public MongoClient mm=null;
    public   String dbConfig;
    HttpServletRequest request;


    MongoConnection(HttpServletRequest request) throws UnknownHostException {
        this.request=request;
        getMongoConnection();
    }
    public String getClientSchema(HttpServletRequest request) {
        dbConfig= (String) request.getSession().getAttribute("dbConfig");
        return  dbConfig;
    }

    abstract void setCollection() throws UnknownHostException;

    public DB getMongoConnection() throws UnknownHostException {
        if(mm==null){
          mm = new MongoClient("localhost", 27017);
        }
        return mm.getDB(getClientSchema(request));
    }
}
