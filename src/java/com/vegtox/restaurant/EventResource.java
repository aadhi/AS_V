/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vegtox.restaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Verb;

/**
 *
 * @author Aadhi.Selvaraj
 */
@Path("/event")
public class EventResource {
    
    // Logging instance.
    protected Logger m_pLogging = null;
    // Initialise method that is invoked once when the servlet is started.
    //
    @PostConstruct
    public void init() 
    {
        // Initialise logging instance.
        m_pLogging = Logger.getLogger("com.VegTox.Logging");
        FileHandler pFileHandler;  
        
        try 
        {
            // This block configure the logger with handler and formatter.  
            pFileHandler = new FileHandler("C:\\Users\\aadhi.selvaraj\\VegTox.log");  
            //pFileHandler = new FileHandler("C:\\Temp\\VegTox.log");  
            m_pLogging.addHandler(pFileHandler);
            SimpleFormatter pFormatter = new SimpleFormatter();  
            pFileHandler.setFormatter(pFormatter);  

            m_pLogging.log(Level.INFO, "Servlet initialising...");
            m_pLogging.log(Level.INFO, "Logging instance initialised.");
            // Use the below code to disable logging from console window.
            //pLogging.setUseParentHandlers(false); 

        } 
        catch (SecurityException e) 
        {  
            e.printStackTrace();  
        } 
        catch (IOException e) 
        {  
            e.printStackTrace();  
        }  
        
        
    }
    
    @GET
    @Path("/getEvent/{place}")
    @Produces({MediaType.APPLICATION_JSON})
    public String eventSearch(@PathParam("place") String place) throws IOException 
    {
        HashSet<EventInfo> pEventInfos = new HashSet<EventInfo>();
        eventBriteSearch(place, pEventInfos);
        
        JSONArray pJSONArray = new JSONArray();
        pJSONArray.addAll(pEventInfos);
        String sJSONReturn = pJSONArray.toJSONString();
        return sJSONReturn;
    }
    
    public void eventBriteSearch(String place, Collection<EventInfo> pEventInfos)
    {
        
        String API_HOST = "https://www.eventbriteapi.com/v3/events/search";
        String token = "?token=PKX3EKFFWOSCWP5EBPX6";
        String location = "&location.address='windsor,uk'";
        String query = "&q='vegetarian'";
        String expand = "&expand=venue'";
        String sort = "&sort_by=''";
        
        //OAuthRequest request = new OAuthRequest(Verb.GET, API_HOST + token + location + query);
        StringBuilder eventBriteData = new StringBuilder();
        
        Logger pLogging = getLogging();
        
        try {
        //String https_url = API_HOST + token + location + query;

        URL url = new URL(API_HOST+token+location+query);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        //pEventInfos = new HashSet<EventInfo>();    
        //HashSet<EventInfo> pEventInfos = new HashSet<EventInfo>();
        
        
	// optional default is GET
	con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
                    
        
       if (con.getResponseCode() != 200) 
            {
                throw new RuntimeException("Failed : HTTP error code : " + con.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));

            String output;
            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) 
            {
                //System.out.println(output);
                eventBriteData.append(output);
            }
            
            } 
        catch (Exception pEx) 
        {
            pLogging.log(Level.SEVERE, pEx.getMessage());
        }
        String sSearchResponseJSON = eventBriteData.toString();
        m_pLogging.log(Level.INFO, sSearchResponseJSON);        
        JSONParser pJSONParser = new JSONParser();
        JSONObject pJSONObjectBusinesses = null;
        try 
        {
            pJSONObjectBusinesses = (JSONObject) pJSONParser.parse(sSearchResponseJSON);
            //System.out.println(response.toJSONString());

            JSONArray pParamEntries = (JSONArray) pJSONObjectBusinesses.get("top_match_events");
            JSONObject pJSONObjectEntry;
            String sName = "";
            String sAddress = "";
            String sOrganiserId = "";
            String sStartTime = "";
            String sEndTime="";
            String sDesc = "";
            String sVenueId="";
            String sVenueDetails="";
            String sVenueAddress_1="";
             //EventInfo pEventInfo = new EventInfo(sName, sAddress, sOrganiserId, sStartTime, sEndTime, sDesc, sVenueId);
                    //
                    // Append the RestaurantInfo instance to the list.
                    //pEventInfos.add(pEventInfo);
                    //System.out.println("Name : " + sName + ", Address : " + sAddress + ", OrganiseId : " + sOrganiserId);
            if (pParamEntries != null)
            {
                for (Object pObjectEntry : pParamEntries) 
                {
                    pJSONObjectEntry = (JSONObject)pObjectEntry;
                   System.out.println(pObjectEntry.toString());
                    //sName = (String)pJSONObjectEntry.get("name.text");
                    
                    JSONObject pParamName = (JSONObject)pJSONObjectEntry.get("name");
                    if (pParamName != null)
                    {
                        sName = (String)pParamName.get("text");
                    }
                    
                    //
                    //StringBuilder pBuffer = new StringBuilder();
                    //pBuffer.append((String)pJSONObjectEntry.get("address1"));
                    //pBuffer.append((String)pJSONObjectEntry.get("organizer_id"));
                    //pBuffer.append((String)pJSONObjectEntry.get("start.local"));
                    sAddress = (String)pJSONObjectEntry.get("id");
                    sOrganiserId = (String)pJSONObjectEntry.get("organizer_id");
                    //sStartTime = (String)pJSONObjectEntry.get("start.local");
                    //sEndTime = (String)pJSONObjectEntry.get("end.local");
                    
                    JSONObject pParamStart = (JSONObject)pJSONObjectEntry.get("start");
                    if (pParamStart != null)
                    {
                        sStartTime = (String)pParamStart.get("local");
                    }
                    
                    JSONObject pParamEnd = (JSONObject)pJSONObjectEntry.get("end");
                    if (pParamEnd != null)
                    {
                        sEndTime = (String)pParamEnd.get("local");
                    }
                    
                    //sDesc = (String)pJSONObjectEntry.get("description");
                    sVenueId = (String)pJSONObjectEntry.get("venue_id");
                    
                    JSONObject pParamVenue = (JSONObject)pJSONObjectEntry.get("venue.address");
                    if (pParamVenue != null)
                    {
                        //JSONObject pParamAddress = (JSONObject)pJSONObjectEntry.get("address");
                        //    if(pParamAddress != null) 
                        //    {
                                sVenueDetails = (String)pParamVenue.get("localized_multi_line_address_display");
                        //    }
                        
                    }
                    
                    JSONObject pParamDesc = (JSONObject)pJSONObjectEntry.get("description");
                    if (pParamDesc != null)
                    {
                        sDesc = (String)pParamDesc.get("text");
                    }
                    JSONObject pParamVenues = (JSONObject)pJSONObjectEntry.get("venue.address");
                    if (pParamVenues != null)
                    {
                        
                            sVenueAddress_1 = (String)pParamVenues.get("address_1");
                            
                    }
                    
                    
                    //
                    //JSONObject pParamDesc = (JSONObject)pJSONObjectEntry.get("description");
                    
                    //
                    EventInfo pEventInfo = new EventInfo(sName, sAddress, sOrganiserId, sStartTime, sEndTime, sDesc, sVenueId, sVenueDetails, sVenueAddress_1);
                    //
                    // Append the RestaurantInfo instance to the list.
                    pEventInfos.add(pEventInfo);
                    //System.out.println("Name : " + sName + ", Address : " + sAddress + ", URL : " + sURLWeb);
                    
                    
                    

                }
            }
        } 
        catch (ParseException pEx) 
        {
            pLogging.log(Level.SEVERE, "Error: could not parse JSON response:");
            pLogging.log(Level.SEVERE, sSearchResponseJSON);
        }
    }
    
    
    // Get the logging instance.
    public Logger getLogging()
    {
        synchronized(this)
        {
            return m_pLogging;
        }
    }

    private void print_https_cert(HttpsURLConnection con) {
        if(con!=null){

        try {

          System.out.println("Response Code : " + con.getResponseCode());
          System.out.println("Cipher Suite : " + con.getCipherSuite());
          System.out.println("\n");

          Certificate[] certs = con.getServerCertificates();
          for(Certificate cert : certs){
             System.out.println("Cert Type : " + cert.getType());
             System.out.println("Cert Hash Code : " + cert.hashCode());
             System.out.println("Cert Public Key Algorithm : "
                                      + cert.getPublicKey().getAlgorithm());
             System.out.println("Cert Public Key Format : "
                                      + cert.getPublicKey().getFormat());
             System.out.println("\n");
          }

          } catch (SSLPeerUnverifiedException e) {
                  e.printStackTrace();
          } catch (IOException e){
                  e.printStackTrace();
        }

        }
    }

    private void print_content(HttpsURLConnection con) {
        if(con!=null){

            try {

               System.out.println("****** Content of the URL ********");
               BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(con.getInputStream()));

               String input;

               while ((input = br.readLine()) != null){
                  System.out.println(input);
               }
               br.close();

            } catch (IOException e) {
               e.printStackTrace();
            }

       }
    }
    
}


