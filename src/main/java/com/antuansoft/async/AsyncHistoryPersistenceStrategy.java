package com.antuansoft.async;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

/**
 * Created by nasir on 12/15/13.
 */
public interface AsyncHistoryPersistenceStrategy<U extends AsyncHistoryAwareDAO> {

    /**
     * Write history
     */
    void writeHistory(MongoCollection collection, ObjectId id, U historyModel);
}
