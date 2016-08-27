/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antuansoft.async;

import com.antuansoft.herospace.Model;
import com.mongodb.*;
import org.bson.types.ObjectId;
import org.jongo.Aggregate;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.ResultHandler;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Defines a generic Data Access Object (DAO). It provides frequently used
 * methods such as list, find, delete, update, save.
 * <p/>
 * It saves boiler plate.
 *
 * @author nasir
 */
public abstract class AsyncDAO<T extends Model> implements AsyncCRUD<T> {

    public MongoClient client = null;
    public DB db = null;
    public Jongo jongo = null;
    public MongoCollection collection = null;
    public Class<T> type;
    public String collectionName;


    /**
     * Create a DAO for given collection, using the URL and DB
     *
     * @param connectionURL
     * @param dbname
     * @param collectionName
     * @param type           Type needs to be provided
     * @throws UnknownHostException Usually thrown when invalid connection URL
     *                              is provided, or database is not running
     */
    public AsyncDAO(String connectionURL, String dbname, String collectionName, Class<T> type) throws UnknownHostException {
    

        //connect to replica set if contains a comma
        if (connectionURL.contains(",")) {
            String replicaSet[] = connectionURL.split("[,/]");
            //we ditch the first 2 since they do not contain the address
            List<ServerAddress> servers = new ArrayList<>(replicaSet.length);
            for(int i=0; i<replicaSet.length;i++){
                if (replicaSet[i].contains(":")){
                    System.out.println(replicaSet[i]);
                    String []address = replicaSet[i].split(":");
                    if (address.length == 2){
                        ServerAddress server = new ServerAddress(address[0],Integer.parseInt(address[1]));
                        System.out.println(server.toString());
                        servers.add(server);
                    }
                }
            }
            client = new MongoClient(servers);
        } else {
            client = new MongoClient(connectionURL);
        }

        db = client.getDB(dbname);
        jongo = new Jongo(db);
        this.collectionName = collectionName;
        this.collection = jongo.getCollection(this.collectionName);
        this.collection.withWriteConcern(WriteConcern.JOURNALED);
        this.type = type;
        ensureIndex();
    }

    abstract protected void ensureIndex();



}
