package com.challenge.cooperative.util;


import java.util.List;

import com.challenge.cooperative.model.Agenda;
import com.challenge.cooperative.model.Associate;
import com.challenge.cooperative.model.Vote;
import com.challenge.cooperative.model.Voting;
import com.challenge.cooperative.repository.AgendaRepository;
import com.challenge.cooperative.repository.AssociateRepository;
import com.challenge.cooperative.repository.VoteRepository;
import com.challenge.cooperative.repository.VotingRepository;

public class TestUtil {

	public Agenda createAgendaToTest(AgendaRepository agendaRepository) {
		String name = "Teste abrindo uma votação em uma pauta";
		Agenda agenda = agendaRepository.findByName(name);
		if (agenda == null) {
			agenda = new Agenda();
			agenda.setName(name);
			agendaRepository.save(agenda);
		} 
		return agenda;
	}
	
	public void deleteAgendaToTest(AgendaRepository agendaRepository) {
		String name = "Teste abrindo uma votação em uma pauta";
		Agenda agenda = agendaRepository.findByName(name);
		agendaRepository.delete(agenda);
	}
	
	public Associate createAssociateToTest(AssociateRepository associateRepository) {
		String name = "Associado 1";
		Associate associate = associateRepository.findByName(name);
		if (associate == null) { 
			associate = new Associate();
			associate.setName(name);
			associateRepository.save(associate);
		} 
		return associate;
	} 
	
	public void deleteAssociateToTest(AssociateRepository associateRepository) {
		String name = "Associado 1";
		Associate associate = associateRepository.findByName(name);
		associateRepository.delete(associate);
	}
	
	public void deleteVotingAgendaToTest(Agenda agenda,
				VotingRepository votingRepository, VoteRepository voteRepository) {
		
		List<Voting> votingList = votingRepository.findByAgendaOrderByIdDesc(agenda);
		List<Vote> votes = voteRepository.findByVoting(votingList.get(0));
		voteRepository.deleteAll(votes);
		votingRepository.delete(votingList.get(0));
	}

}
