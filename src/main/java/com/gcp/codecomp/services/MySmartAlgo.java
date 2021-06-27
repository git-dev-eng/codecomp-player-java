package com.gcp.codecomp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.gcp.codecomp.models.GameParameters;
import com.gcp.codecomp.models.GameStatus;
import com.gcp.codecomp.models.Guess;
import com.gcp.codecomp.models.GuessRequest;
import com.gcp.codecomp.models.Participant;

@Component
public class MySmartAlgo {
	
	@Autowired
	Environment env;

	public GuessRequest nextGuess(GameStatus status) {
		
		GuessRequest guessRequest = new GuessRequest();
		
		List<Guess> guesses = new ArrayList<Guess>();
		
		Random r = new Random();
		
		GameParameters gameParameters = status.getGameParameters();
		
		Map<String, Participant> participantsMap = gameParameters.getParticipants().stream().collect(Collectors.toMap(Participant::getTeamId, Participant -> Participant));;
		
		//Remove myself, I don't want to guess my secret and lose one chance(out of 5) to score points . Do I? :)
		if(participantsMap.containsKey(env.getProperty("team")))
			participantsMap.remove(env.getProperty("team"));
		
		List<String> keysAsArray = new ArrayList<String>(participantsMap.keySet());
		
		for(int count = 0; count < 5; count++)
		{
			
			long randomGuess = Math.round(r.nextFloat() * Math.pow(10,gameParameters.getSecretLength()));
			
			if(	participantsMap != null && keysAsArray.size() > 0 && 
					participantsMap.get(keysAsArray.get(r.nextInt(keysAsArray.size()))) != null && 
					participantsMap.get(keysAsArray.get(r.nextInt(keysAsArray.size()))).getIsAlive()
				)
			{
				
				Guess guess = new Guess();
				guess.setTeam(keysAsArray.get(r.nextInt(keysAsArray.size())));
				guess.setGuess(Long.toString(randomGuess));
				guesses.add(guess);
				
			}
			
		}
		
		guessRequest.setGuesses(guesses);
		
		return guessRequest;
	}
	
}
