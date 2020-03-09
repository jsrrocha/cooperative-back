package com.challenge.cooperative.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.challenge.cooperative.model.Agenda;
import com.challenge.cooperative.model.Voting;

public interface VotingRepository  extends CrudRepository<Voting, Long> {
	public List<Voting> findByAgendaOrderByIdDesc(Agenda agenda);   
}

