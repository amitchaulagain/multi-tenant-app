package com.antuansoft.async;

import com.antuansoft.herospace.Person;

import org.bson.types.ObjectId;

import java.util.Date;


/**
 * Created by nasir on 12/16/13.
 */
public class AsyncHistoryAwarePerson extends Person implements  AsyncHistoryAwareModel<ObjectId> {


    @Override
    public void setReference(ObjectId id) {

    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void incrementVersion() {

    }

    @Override
    public Date createdAt() {
        return null;
    }

    @Override
    public String createdBy() {
        return null;
    }

    @Override
    public void setId(ObjectId object) {

    }
}
