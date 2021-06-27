package com.gcp.codecomp.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gcp.codecomp.models.GameStatus;
import com.gcp.codecomp.models.GuessRequest;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@Component
public class GameApi {
	
	static final Logger logger = LogManager.getLogger(GameApi.class);
	
	@Autowired
	private Environment env;
	
	String baseApi;
	String auth;
	String team;
	String password;
	
	@PostConstruct
	private void postConstruct() {
		team = env.getProperty("team");
		password = env.getProperty("password");
		baseApi = env.getProperty("baseApi");
		byte[] encodedBytes = Base64.getEncoder().encode((team + ":" + password).getBytes());
		this.auth = new String(encodedBytes);
	}
	
	public GameStatus gameStatus() {
		
		GameStatus status = null;
			
			try {
				
				HttpURLConnection conn = getConnection("gamestatus");
				
				if(conn == null){
					logger.info("getting Gamestatus API connection as null");
					return status;
				}
			
				int responseCode = conn.getResponseCode();
			
				if(responseCode < 200 || responseCode > 299) {
					System.out.print("Get game status failed : error code "+responseCode);
					return status;
				}
			
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
				String output;

				output = br.readLine();
			
				ObjectMapper objectMapper = new ObjectMapper();
			
				status = objectMapper.readValue(output, GameStatus.class);
			}
			catch (Exception e) {
				
				e.printStackTrace();
			}
			
			return status;	
	}
	
	public void joinGame() {
		
		try {
			
			HttpURLConnection conn = getConnection("join");
			
			if(conn == null)
			{
				logger.info("Join API Connection received as null | Not Able to Join");
				logger.info("-----------------------------------");
				return;
			}

			int responseCode = conn.getResponseCode();
			
			InputStream inputStream;
			
			if(200 <= responseCode && responseCode <=299)
				inputStream = conn.getInputStream();
			else
				inputStream = conn.getErrorStream();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			String currentLine = in.readLine();
			
			JSONParser responseJsonParser = new JSONParser();
			
			JSONObject joinApiResponse = (JSONObject) responseJsonParser.parse(currentLine);
			
			if(joinApiResponse.containsKey("err"))
			{
				
				if(joinApiResponse.get("err") == null)
				{
					boolean hasDataKey = joinApiResponse.containsKey("data");
				
					if(!hasDataKey || joinApiResponse.get("data") == null)
					{
						logger.info("Not able to Join. Try Again Later");
						logger.info("---------------------------------------------------");
						return;
					}
					else 
					{
						logger.info(joinApiResponse.get("data"));
						logger.info("---------------------------------------------------");
						return;
					}
				}
				else 
				{
					String errMessage = ((HashMap) joinApiResponse.get("err")).get("message") + ", "+ ((HashMap) joinApiResponse.get("err")).get("description");
					logger.info(errMessage);
					logger.info("---------------------------------------------------");
				}
				
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String guess(GuessRequest guesses) {
		try {
			
			HttpURLConnection conn = getConnection("guess");
			if(conn == null)
				return null;
			
			String postJsonData = "";
			
			if(guesses != null)
			{
				ObjectMapper objectMapper = new ObjectMapper();
				postJsonData = objectMapper.writeValueAsString(guesses);
					
			}
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(postJsonData);
			wr.flush();
			wr.close();
			
			int responseCode = conn.getResponseCode();
			
			InputStream inputStream;
			
			if(200 <= responseCode && responseCode <=299)
				inputStream = conn.getInputStream();
			else
				inputStream = conn.getErrorStream();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			
			String currentLine = in.readLine();
			
			return currentLine;
				
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public HttpURLConnection getConnection(String url) {
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers(){
						return null;
					}
					public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
					}
					public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
					}
				}
		};
		
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null,  trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		}
		catch(Exception e) {
			logger.info("Exception in establishing trurst store");
		}
		try {
			
			URL url1 = new URL(baseApi +"/api/"+url);
			HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
			
			if(url.equals("join") || url.equals("guess") )
			{
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Authorization", "Basic "+this.auth);
				conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				conn.setRequestProperty("Accept", "application/json");
				if(url.equals("join")) {
					conn.setFixedLengthStreamingMode(0);
					}
			}
			else if(url.equals("gamestatus")) {
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
			}
			return conn;
		}
		catch(Exception e)
		{
			System.out.println("Sorry cannot connect! Try Again Later");
			return null;
		}
	}

}
