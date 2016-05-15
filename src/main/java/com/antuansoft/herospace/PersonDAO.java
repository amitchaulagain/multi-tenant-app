/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antuansoft.herospace;

import com.antuansoft.history.HistoryPersistenceStrategy;
import com.antuansoft.init.MongoConnection;
import org.bson.types.ObjectId;

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

public class PersonDAO extends HistoryAwareDAO<Person, HistoryAwarePerson>{




    public PersonDAO(String connectionURL, String dbname, String collectionName, Class<Person> type) throws UnknownHostException {

        super(connectionURL, dbname, collectionName, Person.class, HistoryAwarePerson.class);
        String hero=connectionURL;
    }

    public PersonDAO(MongoConnection mc) throws UnknownHostException {
        super(mc.connectionURL,mc.dbName,mc.collectionName,Person.class, HistoryAwarePerson.class);
    }

    public PersonDAO() throws UnknownHostException{
        super("localhost:27017", AuthenticationUtil.getCurrentUser().getClient_schema(), "person", Person.class, HistoryAwarePerson.class);


    }




    @Override
    public void update(ObjectId id, Person person) {
        super.update(id,person);
        collection.update(id).with("{$set: {fn: #, ln: #, createdAt: #, createdBy: #}, $inc: {version: 1}}", person.firstName, person.lastName, new Date(), "anon");
    }

    @Override
    public void update(String query, Person object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * determines if a collection is to be versioned
     *
     * @return true if versioned, false otherwise; by default it is false
     */
    @Override
    public boolean isVersioned() {
        return true;
    }

    /**
     * By convention, the name of the history collection becomes
     * collection.history i.e if a collection is called
     * users, its shadow history collection will be called <code>users.history</code>
     * <p/>
     * Hence this method should return history if history is to be added at the end
     *
     * @return by default "history" is returned
     */
    @Override
    public String getCollectionSuffix() {
        return "hist";
    }

    /**
     * Actually write the history based on the strategy.
     * Some strategy might return a new document,
     * another strategy might return a diff  only.
     * or simply some audit data.
     * <p/>
     * Regardless, whatever the strategy returns, persist it
     *
     * @param strategy
     * @param id
     */
    @Override
    public void writeHistory(HistoryPersistenceStrategy strategy, ObjectId id) {
        HistoryAwarePerson person = (HistoryAwarePerson)find(id,HistoryAwarePerson.class);
        strategy.writeHistory(history,id,person);
    }

    /**
     * Retrieve a specific version
     *
     * @param referenceId
     * @param version
     * @return
     */
    @Override
    public HistoryAwarePerson getVersion(ObjectId referenceId, int version) {
        return history.findOne("{ref: #, version: #}", referenceId,version).as(HistoryAwarePerson.class);
    }


    /**
     * List all versions
     *
     * @param referenceId
     * @return
     */
    @Override
    public List<HistoryAwarePerson> listVersions(ObjectId referenceId) {

        final Iterator<HistoryAwarePerson> iterator = history.find("{ref: #}", referenceId).as(HistoryAwarePerson.class).iterator();
        List<HistoryAwarePerson> list = new ArrayList<>();
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
        return list;
    }
}
