package com.antuansoft.mongodb.connection;

import com.antuansoft.mongodb.domain.Order;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 5/6/16.
 */

public class UserRepository extends MongoConnection {


    // DBCollection collection;

    MongoCollection collection;

    public UserRepository(HttpServletRequest request) throws UnknownHostException {
        super(request);
        setCollection();
    }


    @Override
    public void setCollection() throws UnknownHostException {
        Jongo jongo = new Jongo(getMongoConnection());

        //collection = getMongoConnection().getCollection("order");
        collection=jongo.getCollection("order");
    }

    public List<Order> findAll() throws IOException {

        MongoCursor<Order> all = collection.find().as(Order.class);
        List<Order> orders = new ArrayList<Order>();
        while (all.hasNext()) {
            orders.add(all.next());
        }
        return orders;

    }

}
