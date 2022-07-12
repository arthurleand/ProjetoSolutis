package com.solutis.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.solutis.project.config.external.CpfClient;
import com.solutis.project.model.dto.CpfDto;

import lombok.extern.java.Log;

@Service
@Log
public class CpfService {
	
	@Autowired
	private CpfClient cpfClient;

	public CpfService(CpfClient cpfClient) {
		this.cpfClient = cpfClient;
	}
	
	@Retryable(maxAttempts = 3, backoff = @Backoff(delay=2000, multiplier = 2))
	public boolean validateCpf(String cpf){
        log.info("Validating CPF");
        return cpfClient.cpfValidation(cpf).getIsValid();
    }
	
	@Recover
	public boolean saveCpf(String cpf){

        CpfDto cpfDto = new CpfDto();
        cpfDto.setCpf(cpf);
        cpfDto.setIsValid(true);

        log.info("The service cannot be accessed, but the cpf has been saved!");
        return cpfDto.getIsValid();
    }

}
