package com.antuansoft.history;


import com.antuansoft.herospace.Model;

import java.util.Date;

/**
 * Created by nasir on 12/16/13.
 */
public interface HistoryAwareModel<T> extends Model<T> {
    /**
     * set the reference to given ID
     * Used when saving to history collection, a reference is maintained to most upto date object in the active collection
     * @param id
     */
    public void setReference(T id);

    /**
     * get document version number
     * @return a monotonically increasing number
     */
    public int getVersion();

    /**
     * Increment version
     */
    public void incrementVersion();

    /**
     * When was the change created
     * @return
     */
    public Date createdAt();

    /**
     * who was responsible
     * @return
     */
    public String createdBy();


    /**
     * Set id
     * @param object
     */
    public void setId(T object);

}
