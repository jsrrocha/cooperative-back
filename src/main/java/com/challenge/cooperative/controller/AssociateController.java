package com.challenge.cooperative.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.challenge.cooperative.model.Associate;
import com.challenge.cooperative.repository.AssociateRepository;
import com.challenge.cooperative.service.AssociateService;

@RestController
@RequestMapping("associate") 
public class AssociateController { 
	
	@Autowired  
	private AssociateRepository associateRepository;
	
	@Autowired 
	private AssociateService associateService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addAssociate(@RequestBody Map<String, String> associateMap){
		try {
			String errorMessage = associateService.validateAssociateMap(associateMap);
			if(errorMessage != null) {
				return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST); 
			}
			
			Associate associate = new Associate();
			associate.setName(associateMap.get("name").toString());
			associateRepository.save(associate);

			return new ResponseEntity<Associate>(associate, HttpStatus.OK); 

		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
		}
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAssociate(){
		try {
			Iterable<Associate> associate = associateRepository.findAll();
			return new ResponseEntity<>(associate, HttpStatus.OK);  
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); 
		}
	} 
}
