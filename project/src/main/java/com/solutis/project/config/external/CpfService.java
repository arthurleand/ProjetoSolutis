package com.solutis.project.config.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.solutis.project.model.form.CpfValidationForm;

@FeignClient(url = "https://cpf-api-almfelipe.herokuapp.com/cpf/", name ="cpfValidator")
public interface CpfService {
	
	@GetMapping("{cpf}")
	CpfValidationForm CpfValidation(@PathVariable("cpf") String cpf);
	
}
