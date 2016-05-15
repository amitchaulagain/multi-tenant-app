package com.antuansoft.history;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

/**
 * Created by nasir on 12/15/13.
 */
public interface HistoryPersistenceStrategy<U extends HistoryAwareModel> {

    /**
     * Write history
     */
    void writeHistory(MongoCollection collection, ObjectId id, U historyModel);
}
