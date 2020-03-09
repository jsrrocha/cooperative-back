package com.challenge.cooperative.repository;

import org.springframework.data.repository.CrudRepository;

import com.challenge.cooperative.model.Agenda;

public interface AgendaRepository  extends CrudRepository<Agenda, Long> {
	public Agenda findByName(String name);
}
