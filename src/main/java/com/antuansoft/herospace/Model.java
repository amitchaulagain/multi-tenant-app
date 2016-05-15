/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.antuansoft.herospace;

/**
 * Basic expectations of a model are defined in this interface
 * We can have models which use ObjectID
  * Annotate whichever it is by specifying the relevant type
 * @author nasir
 */
public interface Model<T>{
    /**
     * Returns ID
     * @return 
     */
    public T getId();

    /**
     * Any fields if modified by user represent an error/risk,
     * then compute a hash of these fields to include in your forms
     * and recompute that hash when fields are submitted to verify
     * integrity of critical data
     * 
     * A usual example is to calculate hash on ID. If changed, you could be
     * modifying another record. So hash the ID and send the hash along with
     * ID. If there is a change in ID, hash and computed hash of ID will not 
     * match. Conversely, if hash is modified, you know there has been 
     * tampering somewhere (XSRF).
     * 
     * Returns a hash of critical fields to prevent against XSRF attacks
     * @return 
     */
//    public String getHash();

}
