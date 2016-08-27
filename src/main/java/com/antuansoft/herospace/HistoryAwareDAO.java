package com.antuansoft.herospace;

import com.antuansoft.history.FullCopyHistoryPersistenceStrategy;
import com.antuansoft.history.HistoryAwareModel;
import com.antuansoft.history.HistoryCRUD;
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
public class HistoryAwareDAO<T extends Model, U extends HistoryAwareModel > extends DAO<T> implements HistoryCRUD<U> {

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
    public HistoryAwareDAO(String connectionURL, String dbname, String collectionName, Class<T> type, Class<U> historyType) throws UnknownHostException {
        super(connectionURL, dbname, collectionName, type);
        this.historyType = historyType;
        this.history = jongo.getCollection(this.collectionName+getCollectionSuffix());
        this.history.withWriteConcern(WriteConcern.SAFE);
        this.historyType = historyType;


    }

    @Override
    public void deleteHistory(ObjectId referenceId){
        history.remove("{ref : #}",referenceId);
    }

    /**
     * determines if a collection is to be versioned
     *
     * @return true if versioned, false otherwise; by default it is false
     */
    @Override
    public boolean isVersioned() {
        return false;
    }

    /**
     * By convention, the name of the history collection becomes
     * collection.history i.e if a collection is called
     * users, its shadow history collection will be called <code>users.history</code>
     * <p/>
     * Hence this method should return history if history is to be added at the end
     *
     * @return by default ".history" is returned
     */
    @Override
    public String getCollectionSuffix() {
        return ".history";
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
        if (isVersioned()){
            strategy.writeHistory(history,id,(U)find(id,historyType));
        }
    }

    @Override
    public void update(String query, T object) {

    }

    /**
     * Retrieve a specific version
     *
     * @param referenceId
     * @param version
     * @return
     */
    @Override
    public U getVersion(ObjectId referenceId, int version) {
        return null;
    }

    /**
     * List all versions
     *
     * @param referenceId
     * @return
     */
    @Override
    public List<U> listVersions(ObjectId referenceId) {
        return null;
    }

    @Override
    protected void ensureIndex() {

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
        super.update(id, object);
        writeHistory(historyPersistenceStrategy, id);
    }
}
