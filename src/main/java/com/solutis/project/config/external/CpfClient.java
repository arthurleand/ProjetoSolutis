package com.solutis.project.config.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.solutis.project.model.dto.CpfDto;

@FeignClient(url = "https://cpf-api-almfelipe.herokuapp.com/cpf/", name ="cpfValidator")
public interface CpfClient {
	
	@GetMapping("{cpf}")
	CpfDto cpfValidation(@PathVariable("cpf") String cpf);
	
}
