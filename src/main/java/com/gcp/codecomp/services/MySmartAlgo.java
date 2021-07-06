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
		
		//Remove myself, I don't want to guess my secret and lose one chance(out of 5) in a single request to score points . Do I? :)
		if(participantsMap.containsKey(env.getProperty("team")))
			participantsMap.remove(env.getProperty("team"));
		
		List<String> keysAsArray = new ArrayList<String>(participantsMap.keySet());
		
		for(int count = 0; count < 5; count++)
		{
			
			long randomGuess = Math.round(r.nextFloat() * Math.pow(10,gameParameters.getSecretLength()));
			
			// Check if there are other participants joined
			if(participantsMap != null && keysAsArray.size() > 0 )
			{
				int index = r.nextInt(keysAsArray.size());
				
				Participant p = participantsMap.get(keysAsArray.get(index));
				
			
				// I only guess for teams who are alive and for those whose 
				// secret I haven't cracked yet. Am I already not smart? Everyone says I am a dumb bot. :(
				// Help me to prove them wrong. Make me smarter.	
				if( p != null &&  p.getIsAlive() && ( p.getKilledBy() == null || !p.getKilledBy().contains(env.getProperty("team")) ) )
				{
					Guess guess = new Guess();
					guess.setTeam(keysAsArray.get(index));
					guess.setGuess(Long.toString(randomGuess));
					guesses.add(guess);
				}
					
			}
			
		}
		
		guessRequest.setGuesses(guesses);
		
		return guessRequest;
	}
	
}
