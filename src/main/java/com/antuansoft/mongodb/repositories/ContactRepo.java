package com.antuansoft.mongodb.repositories;

import org.springframework.data.repository.CrudRepository;

import com.antuansoft.mongodb.domain.Contact;


public interface ContactRepo extends CrudRepository<Contact, String> {
	
	
//	public Contact findByUsername(String username);

}
