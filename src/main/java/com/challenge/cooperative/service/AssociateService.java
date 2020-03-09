package com.challenge.cooperative.service;

import java.util.Map;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.cooperative.model.Associate;
import com.challenge.cooperative.repository.AssociateRepository;

@Service
public class AssociateService {
	
	@Autowired
	private AssociateRepository associateRepository;

	public String validateAssociateMap(Map<String, String> associateMap) {
		
		if (associateMap.get("name") == null) {
			return "Preencha o nome do associado";
		}
		
		return null;
	}
	
	public void createAssociatesToInterface() {
		Iterable<Associate> associates = associateRepository.findAll();
		Long associatesSize = StreamSupport.stream(associates.spliterator(), false).count();
		
		if(associatesSize == 0) {
			Associate associate1 = new Associate();
			associate1.setName("Rafael");
			associateRepository.save(associate1);
			
			Associate associate2 = new Associate();
			associate2.setName("Tobias");
			associateRepository.save(associate2);
			
			Associate associate3 = new Associate();
			associate3.setName("Carol");
			associateRepository.save(associate3);
			
			Associate associate4 = new Associate();
			associate4.setName("Lucas");
			associateRepository.save(associate4);
			
			Associate associate5 = new Associate();
			associate5.setName("Julia");
			associateRepository.save(associate5);
			
			Associate associate6 = new Associate();
			associate6.setName("Ana");
			associateRepository.save(associate6);
		}
	}
}
