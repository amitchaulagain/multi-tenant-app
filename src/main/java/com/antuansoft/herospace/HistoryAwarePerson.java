package com.antuansoft.herospace;

import com.antuansoft.history.HistoryAwareModel;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by nasir on 12/16/13.
 */
public class HistoryAwarePerson extends Person implements HistoryAwareModel<ObjectId> {
    /**
     * set the reference to given ID
     * Used when saving to history collection, a reference is maintained to most upto date object in the active collection
     *
     * @param id
     */
    @Override
    public void setReference(ObjectId id) {
        this.reference = id;
    }

    /**
     * get document version number
     *
     * @return a monotonically increasing number
     */
    @Override
    public int getVersion() {
        return version;
    }

    /**
     * Increment version
     */
    @Override
    public void incrementVersion() {
        version++;
    }

    /**
     * When was the change created
     *
     * @return
     */
    @Override
    public Date createdAt() {
        return new Date();
    }

    /**
     * who was responsible
     *
     * @return
     */
    @Override
    public String createdBy() {
        return null;
    }

    /**
     * Set id
     *
     * @param object
     */
    @Override
    public void setId(ObjectId object) {
        this.id = object;
    }

}
