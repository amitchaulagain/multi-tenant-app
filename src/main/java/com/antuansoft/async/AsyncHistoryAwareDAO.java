package com.antuansoft.async;

import com.antuansoft.herospace.Model;
import com.antuansoft.history.FullCopyHistoryPersistenceStrategy;
import com.antuansoft.history.HistoryPersistenceStrategy;
import com.mongodb.WriteConcern;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by nasir on 12/20/13.
 */
//public class HistoryAwareDAO<T extends Model, U extends HistoryAwareModel> extends DAO<T> implements AsyncHistoryCRUD<U> {
public class AsyncHistoryAwareDAO<T extends Model, U extends AsyncHistoryAwareModel > extends AsyncDAO<T> implements AsyncHistoryCRUD<U> {

    private final HistoryPersistenceStrategy historyPersistenceStrategy = new FullCopyHistoryPersistenceStrategy();
    private Class<U> historyType;
    protected MongoCollection history = null;

    /**
     * Create a DAO for given collection, using the URL and DB
     *
     * @param connectionURL
     * @param dbname
     * @param collectionName
     * @param type           Type needs to be provided
     * @param historyType
     * @throws UnknownHostException Usually thrown when invalid connection URL
     *                              is provided, or database is not running
     */

    public AsyncHistoryAwareDAO(String connectionURL, String dbname, String collectionName, Class<T> type, Class<U> historyType) throws UnknownHostException {
        super(connectionURL, dbname, collectionName, type);
        this.historyType = historyType;
        this.history = jongo.getCollection(this.collectionName+getCollectionSuffix());
        this.history.withWriteConcern(WriteConcern.SAFE);
        this.historyType = historyType;


    }


    @Override
    protected void ensureIndex() {

    }

    @Override
    public boolean isVersioned() {
        return false;
    }

    @Override
    public String getCollectionSuffix() {
        return null;
    }

    @Override
    public void writeHistory(AsyncHistoryPersistenceStrategy strategy, ObjectId id) {

    }

    @Override
    public void deleteHistory(ObjectId referenceId) {

    }

    @Override
    public U getVersion(ObjectId referenceId, int version) {
        return null;
    }

    @Override
    public List<U> listVersions(ObjectId referenceId) {
        return null;
    }
}
