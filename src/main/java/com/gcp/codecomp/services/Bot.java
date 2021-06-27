package com.gcp.codecomp.services;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.gcp.codecomp.services.GameApi;
import com.gcp.codecomp.services.MySmartAlgo;
import com.gcp.codecomp.models.GameStatus;
import com.gcp.codecomp.models.GuessRequest;

@Component
public class Bot {
	
	private static final Logger logger = LogManager.getLogger(Bot.class);
	
	@Autowired
	GameApi gameApi;

	@Autowired
	MySmartAlgo msl;
	
	@Autowired
	private Environment env;
	
	public void play() {
		
		try {
			
			String myTeamName = env.getProperty("team");
			String myPassword = env.getProperty("password");
			
			//Add Checks for empty team and password
			if(myTeamName == null || myPassword == null)
			{
				logger.info("Received null team name or password, please update team details in application.properties");
				return;
			}
			
			if(myTeamName.trim().isEmpty() || myPassword.trim().isEmpty())
			{
				logger.info("Received empty team name or password, please update team details in application.properties");
				return;
			}
			
			logger.info("Playing with team name -->: "+myTeamName+ " Password is --> "+myPassword);
			
			while(true) {

				GameStatus status = gameApi.gameStatus();
				
				if(status == null)
				{
					logger.info("Game status recevied as null. Please try again later");
					continue;
				}
				
				if(status.getError() != null)
				{
					logger.info(status.getError());
					continue;
				}
				
				if(status.getGameParameters() == null ||status.getGameParameters().getGameId() == null || 
						status.getGameParameters().getRoundId() == null || status.getGameParameters().getRoundNumber() == null || 
						status.getGameParameters().getStatus() == null || status.getGameParameters().getSecretLength() == null)
				{	
					logger.info("Null parameters in game status. Please try again later");
					continue;
				}

				logger.info("---------------------------------------------------");
				logger.info("Request Id : "+status.getRequestId());
				
				boolean  haveIJoinedRound = status.getGameParameters().getParticipants().stream().anyMatch(member -> member.getTeamId().equals(myTeamName));
				
				String state = status.getGameParameters().getStatus();
				
				if(state != null && state.equals("Joining")) {
					if(!haveIJoinedRound)
					{
						gameApi.joinGame();
					}
					else {
						logger.info("You have joined the round. Wait for Running Phase to start");
						logger.info("---------------------------------------------------");
					}
				}
				else if(state != null && state.equals("Running"))
				{
					//Check if I have Joined
					if(haveIJoinedRound) 
					{
						
						// I am dumb. Please make me smart,I know you are very good at this :)
						GuessRequest guesses = msl.nextGuess(status);
						String response = gameApi.guess(guesses);
						
						
						//Hint: Store result in a map and use it for next guess wisely. This will pay dividends :)
						logger.info(response);	
						logger.info("---------------------------------------------------");
					}
					else {
						
						logger.info("Team is not joined in this round, Please wait for Joining Phase");
						logger.info("---------------------------------------------------");
						
					}
					
				}
				else {
					logger.info("Please wait for Joining Phase");
					logger.info("---------------------------------------------------");
				}
				Thread.sleep(5*1000);
				
			}
			
	}
	catch(Exception e) {
		e.printStackTrace();
	}

	}

}
