package com.antuansoft.history;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

/**
 * Created by nasir on 12/15/13.
 * Writes a whole document
 */
public class FullCopyHistoryPersistenceStrategy<T extends HistoryAwareModel> implements HistoryPersistenceStrategy<T>{

    /**
     * Write history
     * Clear the id and set a reference field instead
     * every time a new object is inserted. Reference field keeps track of original id
     *
     * Ensure there is an index on the reference field
     * @param id
     */
    @Override
    public void writeHistory(MongoCollection collection, ObjectId id, T object) {
        System.out.println("Persisting full copy history for " + id.toString() + " version ("+object.getVersion()+")");
        object.setId(null);
        object.setReference(id);
        collection.save(object);
    }
}
