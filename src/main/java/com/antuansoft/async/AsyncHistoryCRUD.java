package com.antuansoft.async;

import org.bson.types.ObjectId;

import java.util.List;

/**
 * If a model is to be versioned, ie keep track of changes in documents, it should implement this interface
 * Created by nasir on 12/15/13.
 */
public interface AsyncHistoryCRUD<U extends AsyncHistoryAwareModel> {
    /**
     *
     * determines if a collection is to be versioned
     * @return true if versioned, false otherwise
     */
    public boolean isVersioned();

    /**
     * By convention, the name of the history collection becomes
     * collection.history i.e if a collection is called
     * users, its shadow history collection will be called <code>users.history</code>
     *
     * Hence this method should return history if history is to be added at the end
     * @return
     */
    public String  getCollectionSuffix();

    /**
     * Actually write the history based on the strategy.
     * Some strategy might return a new document,
     * another strategy might return a diff  only.
     * or simply some audit data.
     *
     * Regardless, whatever the strategy returns, persist it
     * @param strategy
     * @return
     */
    public void writeHistory(AsyncHistoryPersistenceStrategy strategy, ObjectId id);

    /**
     * Delete history for the given object
     * removes all objects
     * @param referenceId
     */
    public void deleteHistory(ObjectId referenceId);

    /**
     * Retrieve a specific version
     * @param referenceId
     * @param version
     * @return
     */
    public U getVersion(ObjectId referenceId, int version);

    /**
     * List all versions
     * @param referenceId
     * @return
     */
    public List<U> listVersions(ObjectId referenceId);
}
