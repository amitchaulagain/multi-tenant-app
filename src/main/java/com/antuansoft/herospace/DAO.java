/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antuansoft.herospace;

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
public abstract class DAO<T extends Model> implements CRUD<T> {

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
    public DAO(String connectionURL, String dbname, String collectionName, Class<T> type) throws UnknownHostException {
    

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


    /**
     * Specifies what is the default limit
     *
     * @return
     */
    @Override
    public int getLimit() {
        return 10;
    }

    /**
     * Converts an iterator to a List
     *
     * @param <T>
     * @param iter
     * @return
     */
    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext()) {
            copy.add(iter.next());
        }
        return copy;
    }

    /**
     * Convenience method which returns a count of all documents in collection
     *
     * @return
     */
    @Override
    public long count() {
        return collection.count();
    }

    /**
     * Returns the count of documents matching the qurey
     *
     * @param query
     * @param parameters
     * @return
     */
    @Override
    public long count(String query, Object... parameters) {
        return collection.count(query, parameters);
    }


    /**
     * Returns a list, specifying no query parameters, applying provided limit
     * and skip. Convenience method
     * same as calling list(0,0)
     *
     * @return
     */
    @Override
    public List<T> list() {
        return list(0, 0, null);
    }

    /**
     * Returns list of documents.
     * if you pass 10, 1 it means retrieve 10 records from first page.
     * A number less than 1 is interpreted to be 1.
     * A negative limit defaults to 10.
     *
     * @param limit If 0 is passed, there is no upper limit
     * @param page  if 0 is passed records from first and onwards are included
     * @return
     */
    @Override
    public List<T> list(int limit, int page) {
        return list(limit, page, null);
    }

    /**
     * Returns list of documents.
     * if you pass 10, 1 it means retrieve 10 records from first page.
     * A number less than 1 is interpreted to be 1.
     * A negative limit defaults to 10.
     *
     * @param limit If 0 is passed, there is no upper limit
     * @param page  if 0 is passed records from first and onwards are included
     * @return
     */
    @Override
    public List<T> list(int limit, int page, String sort) {
        if (page < 1) {
            page = 1;
        }
        page = (page < 0) ? -page : page;
        page--;
        limit = (limit < 0) ? 10 : limit;
        return copyIterator(collection.find().limit(limit).skip(page * limit).sort(sort).as(type).iterator());
    }

    /**
     * Returns list of documents.
     * if you pass 10, 1 it means retrieve 10 records from first page.
     * A number less than 1 is interpreted to be 1.
     * A negative limit defaults to 10.
     *
     * @param limit If 0 is passed, there is no upper limit
     * @param page  if 0 is passed records from first and onwards are included
     * @param query query parameter
     * @retun
     */
    @Override
    public List<T> list(int limit, int page, String sort, String query) {
        if (page < 1) {
            page = 1;
        }
        page = (page < 0) ? -page : page;
        page--;
        limit = (limit < 0) ? 10 : limit;
        return copyIterator(collection.find(query).limit(limit).skip(page * limit).sort(sort).as(type).iterator());
    }

    /**
     * Experimental : Returns JSON Array
     *
     * @param limit
     * @param skip
     * @param query
     * @param fields
     * @return json contains list
     */
    @Override
    public String listJSON(int limit, int skip, DBObject query, DBObject fields) {
        DBCollection col = db.getCollection(this.collectionName);
        return col.find(query, fields).limit(limit).skip(skip).toArray().toString();
    }

//    public List<T> list(){
//        return copyIterator(collection.find().as(type).iterator());
//    }    

    /**
     * Returns an object if one exists matching the provided ObjectId
     *
     * @param id
     * @return
     */
    @Override
    public T find(ObjectId id) {
        return collection.findOne(id).as(type);
    }

    /**
     * return appropriate object. Cast it accordingly
     *
     * @param id
     * @param type
     * @return
     */
    @Override
    public Object find(ObjectId id, Class type) {
        return collection.findOne(id).as(type);
    }

    /**
     * Returns an object if one exists matching the provided ObjectId
     *
     * @param id
     * @return
     */
    @Override
    public T find(String id, ResultHandler<T> handler) {
        return collection.findOne(new ObjectId(id)).map(handler);
    }


    /**
     * Returns an object if one exists matching the provided ObjectId in String
     * form. Before making the database call, it is converted into ObjectId
     *
     * @param id
     * @return
     */
    @Override
    public T find(String id) {
        return collection.findOne(new ObjectId(id)).as(type);
    }

    @Override
    public T find(String id, String fields) {
        return collection.findOne(new ObjectId(id)).projection(fields).as(type);
    }

    /**
     * Saves the entity
     * HistoryCRUD only starts on updates
     *
     * @param entity
     */
    @Override
    public Object save(T entity) {
        collection.save(entity);
        return entity.getId();
    }

    /**
     * Update object with id.
     *
     * @param id
     * @param object
     * @return
     */
    @Override
    public void update(ObjectId id, T object) {
        //write existing data.
        //passed object contains modifications (or supposed to, no check done yet)

    }

    /**
     * Deletes a document based on ID. ID is converted into ObjectId.
     * Convenience method
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        collection.remove(new ObjectId(id));
    }

    /**
     * Deletes given document
     *
     * @param id
     */
    @Override
    public void delete(ObjectId id) {
        collection.remove(id);
    }

    /**
     * Aggregate
     *
     * @param pipeline - multiple pipelines
     * @return
     */
    @Override
    public List<T> aggregate(String... pipeline) {
        int i = 0;
        Aggregate agr = null;
        for (String p : pipeline) {
            if (i == 0) {
                agr = collection.aggregate(p);
            } else {
                agr = agr.and(p);
            }
        }

        return (List<T>) agr.as(type);
    }

    /**
     * Aggregate - single pipeline
     *
     * @param pipeline
     * @return
     */
    @Override
    public List<T> aggregate(String pipeline) {
        return (List<T>) collection.aggregate(pipeline).as(type);
    }

    @Override
    public void update(String query, T object) {

    }
}
