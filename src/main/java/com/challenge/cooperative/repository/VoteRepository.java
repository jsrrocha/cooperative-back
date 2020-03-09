package com.challenge.cooperative.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.challenge.cooperative.model.Vote;
import com.challenge.cooperative.model.Voting;

public interface VoteRepository  extends CrudRepository<Vote, Long> {
	public List<Vote> findByVoting(Voting voting); 
}
