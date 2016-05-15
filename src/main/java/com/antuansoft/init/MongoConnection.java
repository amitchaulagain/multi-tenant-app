package com.antuansoft.init;

/**
 * Created by amit on 5/13/16.
 */


public class MongoConnection {

       public String name;
    public String connectionURL;
    public String dbName;
    public String collectionName;

    public MongoConnection() {

    }


    public MongoConnection(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}