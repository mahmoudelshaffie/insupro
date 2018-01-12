package com.insupro.modules.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insupro.modules.data.Module;
import com.insupro.modules.data.ModulesRep;

@RestController
@RequestMapping("/modules")
public class ModulesRESTCtrl {

	@Autowired
	private ModulesRep modulesRep;
	
	@GetMapping
	public Iterable<Module> getAllModules() {
		return this.modulesRep.findAll();
	}
}
