package com.challenge.cooperative.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.challenge.cooperative.model.Agenda;
import com.challenge.cooperative.model.Associate;
import com.challenge.cooperative.model.Vote;
import com.challenge.cooperative.model.Voting;
import com.challenge.cooperative.repository.AgendaRepository;
import com.challenge.cooperative.repository.AssociateRepository;
import com.challenge.cooperative.repository.VotingRepository;

@Service
public class AgendaService {
	@Autowired
	private AgendaRepository agendaRepository;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private VotingRepository votingRepository;

	@Autowired
	private AssociateRepository associateRepository;
	

	public String validateAgendaMap(Map<String, String> agendaMap) {
		
		if (agendaMap.get("name") == null) {
			return "Preencha o nome da pauta";
		}

		String name = agendaMap.get("name");
		Agenda agendaExists = agendaRepository.findByName(name);
		if (agendaExists != null) {
			return "Já existe uma agenda com esse nome";
		}
		return null;
	}

	public String validateOpenSessionInput(Long id, Integer time) {

		Optional<Agenda> optionalAgenda = agendaRepository.findById(id);
		if (!optionalAgenda.isPresent()) {
			return "Pauta não existe";
		}
	
		if (time <= 0) {
			return "Preencha o tempo da sessão com um valor maior que 0";
		}

		List<Map<String, String>> sessionAttributes = sessionService.findAllSessionAttributes();
		boolean foundOpenSession = sessionService.isAgendaSessionOpen(sessionAttributes, id.toString());
		if (foundOpenSession) {
			return "Já existe uma votação aberta para essa pauta";
		}

		return null;
	}

	public String validateVotingInput(Long id, Map<String, Object> voteMap) {

		Optional<Agenda> agenda = agendaRepository.findById(id);
		if (!agenda.isPresent()) {
			return "Agenda não existe";
		}

		List<Voting> votingList = votingRepository.findByAgendaOrderByIdDesc(agenda.get());
		if (votingList.isEmpty()) {
			return "Não existe votação para essa pauta";
		}

		List<Map<String, String>> sessionAttributes = sessionService.findAllSessionAttributes();
		boolean foundOpenSession = sessionService.isAgendaSessionOpen(sessionAttributes, id.toString());
		if (!foundOpenSession) {
			return "Não existe votação aberta para essa pauta";
		}

		if (voteMap.get("vote") == null) {
			return "Preencha o voto";
		}

		if (voteMap.get("associate") == null) {
			return "Preencha o associado";
		}

		Optional<Associate> opcionalAssociate = associateRepository
				.findById(Long.parseLong(voteMap.get("associate").toString()));
		if (!opcionalAssociate.isPresent()) {
			return "Associado não existe";
		}

		Associate associate = opcionalAssociate.get();
		Voting voting = votingList.get(0); 
		if (associateAlreadyVoted(id, associate, voting.getVotes())) {
			return "Associado já votou nessa pauta";
		}

		return null;
	}

	public boolean associateAlreadyVoted(Long agendaId, Associate associate, List<Vote> votes) {
		return votes.stream()
				.filter(vote -> vote.getAssociate().equals(associate)).count() > 0;
	}

	public String validateResultInput(Long id) {
		
		Optional<Agenda> agenda = agendaRepository.findById(id);
		if (!agenda.isPresent()) {
			return "Agenda não existe";
		}
		
		List<Voting> votingList = votingRepository.findByAgendaOrderByIdDesc(agenda.get());
		if (votingList.isEmpty()) {
			return "Não existe votação para essa pauta";
		}
		
		List<Map<String, String>> sessionAttributes = sessionService.findAllSessionAttributes();
		boolean foundOpenSession = sessionService
				.isAgendaSessionOpen(sessionAttributes,votingList.get(0).getAgenda().getId().toString());
		if (foundOpenSession) {
			return "Votação para essa pauta ainda está aberta";
		}

		return null;
	} 

	public String getResult(Long optionYes, Long optionNo) {
		String result = "";
		
		if (optionYes > optionNo) {
			result = "Pauta aprovada";
		} else if (optionYes < optionNo) {
			result = "Pauta reprovada";
		} else {
			result = "Votação deu empate";
		}

		return result;
	}

	public Map<String, String> buildResponseMap(Long optionYes, Long optionNo, String result) {
		
		Map<String, String> response = new HashMap<String, String>();
		response.put("yesCount", optionYes.toString());
		response.put("noCount", optionNo.toString());
		response.put("result", result);

		return response;
	}

}
