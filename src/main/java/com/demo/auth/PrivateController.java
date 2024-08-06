package com.demo.auth;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {
	
	@PostMapping()
    public String post(){
        return "POST";
    }
	
	@DeleteMapping()
    public String delete(){
        return "DELETE";
    }
}
