package com.challenge.cooperative.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.challenge.cooperative.model.Agenda;
import com.challenge.cooperative.repository.AgendaRepository;
import com.challenge.cooperative.repository.AssociateRepository;
import com.challenge.cooperative.model.Associate;
import com.challenge.cooperative.model.Vote;
import com.challenge.cooperative.model.Voting;
import com.challenge.cooperative.repository.VoteRepository;
import com.challenge.cooperative.repository.VotingRepository;
import com.challenge.cooperative.service.AgendaService;


@RestController 
@RequestMapping("agenda")
public class AgendaController {

	@Autowired 
	private AgendaRepository agendaRepository; 
	
	@Autowired 
	private VotingRepository votingRepository; 
	
	@Autowired 
	private VoteRepository voteRepository; 
	
	@Autowired 
	private AgendaService agendaService;
	
	@Autowired
	private AssociateRepository associateRepository;
	

	@PostMapping("/add")
	public ResponseEntity<?> addAgenda(@RequestBody Map<String, String> agendaMap){
		try {
			String errorMessage = agendaService.validateAgendaMap(agendaMap);
			if(errorMessage != null) {
				return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST); 
			}
			Agenda agenda =  new Agenda();
			agenda.setName(agendaMap.get("name"));
			agendaRepository.save(agenda);
			
			return new ResponseEntity<>(HttpStatus.OK);   

		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST); 
		} 
	}

	@GetMapping("/")
	public ResponseEntity<?> getAgendas(){
		try {
			Iterable<Agenda> agendas = agendaRepository.findAll();
			return new ResponseEntity<>(agendas, HttpStatus.OK);  
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
		}
	} 

	@Transactional 
	@PostMapping("/{id}/voting/session/open/{time}")
	public ResponseEntity<?> openAgendaVotingSession(@PathVariable Long id, @PathVariable Integer time, HttpSession session){
		try {
			String errorMessage = agendaService.validateOpenSessionInput(id, time);
			if(errorMessage != null) {
				return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST); 
			}
			Optional<Agenda> optionalAgenda = agendaRepository.findById(id); 
			
			Voting voting = new Voting();
			voting.setName("Votação da pauta " + id.toString()); 
			voting.setAgenda(optionalAgenda.get());
			voting = votingRepository.save(voting);
			
			session.setAttribute("agendaId",id.toString());
			session.setMaxInactiveInterval(time * 60);

			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
		}
	}

    @Transactional 
	@PostMapping("/{id}/voting")
	public ResponseEntity<?> AgendaVoting(@PathVariable Long id, @RequestBody Map<String, Object> voteMap){
		try {
			String errorMessage = agendaService.validateVotingInput(id, voteMap);
			if(errorMessage != null) {
				return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST); 
			}
			Optional<Agenda> agenda = agendaRepository.findById(id); 
			List<Voting> votingList = votingRepository.findByAgendaOrderByIdDesc(agenda.get());
			Optional<Associate> opcionalAssociate = associateRepository
					.findById(Long.parseLong(voteMap.get("associate").toString()));
			
			Vote vote =  new Vote();
			vote.setVote(voteMap.get("vote").toString().equals("Sim") ? true : false); 
			vote.setAssociate(opcionalAssociate.get());
			vote.setVoting(votingList.get(0));
			voteRepository.save(vote);  

			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
		}
	}
  
    @Transactional
    @PostMapping("/{id}/voting/result") 
    public ResponseEntity<?> resultVotingAgenda(@PathVariable Long id){
    	try {
    		
    		String errorMessage = agendaService.validateResultInput(id);
			if(errorMessage != null) {
				return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST); 
			}
			
			Optional<Agenda> agenda = agendaRepository.findById(id); 
			List<Voting> votingList = votingRepository.findByAgendaOrderByIdDesc(agenda.get());
			
    		List<Vote> votes = votingList.get(0).getVotes();
            Long optionYes = votes.stream().filter(vote-> vote.isVote() == true).count();
            Long optionNo = votes.stream().filter(vote-> vote.isVote() == false).count(); 

            String result =  agendaService.getResult(optionYes, optionNo);
    		
    		Map<String,String> response = agendaService.buildResponseMap(optionYes, optionNo, result); 
			return new ResponseEntity<Map<String,String>>(response,HttpStatus.OK);
			
    	}catch (Exception e) {
    		e.printStackTrace();
    		return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
    	}
    }
}
