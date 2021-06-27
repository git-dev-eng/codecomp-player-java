package com.gcp.codecomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gcp.codecomp.services.Bot;

@SpringBootApplication
public class ReconTool implements CommandLineRunner {
	
	@Autowired
	Bot bot;
	
	@Override
	public void run(String... args) {
		bot.play();
	}

}
