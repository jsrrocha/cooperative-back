package com.challenge.cooperative.repository;

import org.springframework.data.repository.CrudRepository;
import com.challenge.cooperative.model.Associate;

public interface AssociateRepository  extends CrudRepository<Associate, Long> {
	public Associate findByName(String name);
}
