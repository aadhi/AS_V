/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vegtox.restaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.Collection;
import java.util.HashSet;
import javax.inject.Singleton;
import org.json.simple.*;
import org.json.simple.parser.*;


/**
 *
 * @author aselvaraj
 */
@Singleton
@Path("/restaurant")
public class RestaurantResource 
{
    private static final String CONSUMER_KEY = "LhQ6Ayq46PGDCc58IJU4nA";
    private static final String CONSUMER_SECRET = "CZW9ND3Mel3eFLt9m8P6qnL6FXE";
    private static final String TOKEN = "6CWxM85qQpVu6ABjZ9LRkawE_RWFahuz";
    private static final String TOKEN_SECRET = "zMkg-yHs7wieHHY7hj1goWCnjEQ";

  
    // Logging instance.
    protected Logger m_pLogging = null;
    
    
    protected TrustManager[] pCustomTrustStore = new TrustManager[]
    {
        new X509TrustManager() 
        {
            //
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            //
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
                //No need to implement.
            }

            //
            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            {
                //No need to implement.
            }
        }
    };
    
    
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
            //pFileHandler = new FileHandler("C:\\Users\\aselvaraj\\Documents\\NetBeansProjects\\VegTox-SS\\web\\WEB-INF\\VegTox.log");  
            pFileHandler = new FileHandler("C:\\Temp\\VegTox.log");  
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
        
        // Initialise SSL context with a custom trust store that accepts all the certificates.
        //
        SSLContext pSSLContext;
        try 
        {
            pSSLContext = SSLContext.getInstance("TLS");
            pSSLContext.init(null, pCustomTrustStore, SecureRandom.getInstance("SHA1PRNG"));
            HttpsURLConnection.setDefaultSSLSocketFactory(pSSLContext.getSocketFactory());
            //
            m_pLogging.log(Level.INFO, "SSL Context has been set with custom trust store.");
        } 
        catch (NoSuchAlgorithmException ex) 
        {
            m_pLogging.log(Level.SEVERE, null, ex);
        } 
        catch (KeyManagementException ex) 
        {
            m_pLogging.log(Level.SEVERE, null, ex);
        }
    }

    // Get method of the servlet.
    //
    @GET
    @Path("/getRestaurant/{place}")
    @Produces({MediaType.APPLICATION_JSON})
    public String restaurantSearch(@PathParam("place") String place) throws IOException 
    {
        HashSet<RestaurantInfo> pRestaurantInfos = new HashSet<RestaurantInfo>();
        restaurantSearchVegGuide(place, pRestaurantInfos);
        restaurantSearchYelp("vegetarian", place, pRestaurantInfos);
        
        JSONArray pJSONArray = new JSONArray();
        pJSONArray.addAll(pRestaurantInfos);
        String sJSONReturn = pJSONArray.toJSONString();
        
        //System.out.println(YelpData);
        //System.out.println(VegGuideData);
        //YelpData.put(3,VegGuideData);
        //
        // Output to console for debug purpose.
        //System.out.println("Output from hashset...");
        //System.out.println(sJSONReturn);
        /*for (RestaurantInfo pRestaurantInfo : pRestaurantInfos)
        {
            System.out.println("Name:" + pRestaurantInfo.m_sName + ",Address:" + pRestaurantInfo.m_sAddress);
        }*/
        
        return sJSONReturn;
        
    }
    
    public void restaurantSearchVegGuide(String place, Collection<RestaurantInfo> pRestaurantInfos)
    {
        //
        Logger pLogging = getLogging();
        //
        String vegguide_url = "https://www.vegguide.org/search/by-address/";
        StringBuilder vegGuideData = new StringBuilder();
        URL url;
        try 
        {
            url = new URL(vegguide_url+place);
            
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            
            // Dump all SSL certificate info.
	    // print_https_cert(conn);
             
            if (conn.getResponseCode() != 200) 
            {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) 
            {
                //System.out.println(output);
                vegGuideData.append(output);
            }
            //conn.disconnect();

        } 
        catch (Exception pEx) 
        {
            pLogging.log(Level.SEVERE, pEx.getMessage());
        }

        String sSearchResponseJSON = vegGuideData.toString();
        JSONParser pJSONParser = new JSONParser();
        JSONObject pJSONObjectBusinesses = null;
        try 
        {
            pJSONObjectBusinesses = (JSONObject) pJSONParser.parse(sSearchResponseJSON);
            //System.out.println(response.toJSONString());

            JSONArray pParamEntries = (JSONArray) pJSONObjectBusinesses.get("entries");
            JSONObject pJSONObjectEntry;
            String sName;
            String sAddress = "";
            String sURLWeb;
            String sShortDesc = "";
            String sURLReview;
            if (pParamEntries != null)
            {
                for (Object pObjectEntry : pParamEntries) 
                {
                    pJSONObjectEntry = (JSONObject)pObjectEntry;
                    sName = (String)pJSONObjectEntry.get("name");
                    //
                    StringBuilder pBuffer = new StringBuilder();
                    pBuffer.append((String)pJSONObjectEntry.get("address1"));
                    pBuffer.append(", ");
                    pBuffer.append((String)pJSONObjectEntry.get("city"));
                    pBuffer.append(" ");
                    pBuffer.append((String)pJSONObjectEntry.get("postal_code"));
                    sAddress = pBuffer.toString();
                    //
                    sURLWeb = (String)pJSONObjectEntry.get("website");
                    //
                    sURLReview = (String)pJSONObjectEntry.get("reviews_uri");
                    //
                    JSONObject pParamLongDesc = (JSONObject)pJSONObjectEntry.get("long_description");
                    if (pParamLongDesc != null)
                    {
                        sShortDesc = (String)pParamLongDesc.get("text/vnd.vegguide.org-wikitext");
                    }
                    //
                    RestaurantInfo pRestaurantInfo = new RestaurantInfo(sName, sAddress, sURLWeb, sShortDesc, sURLReview);
                    //
                    // Append the RestaurantInfo instance to the list.
                    pRestaurantInfos.add(pRestaurantInfo);
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
    
    private void restaurantSearchYelp(String term, String location, Collection<RestaurantInfo> pRestaurantInfos) 
    {
        //
        Logger pLogging = getLogging();
        //
        YelpAPI pYelpApi = new YelpAPI(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
        String sSearchResponseJSON = pYelpApi.searchForBusinessesByLocation(term, location);

        JSONParser pJSONParser = new JSONParser();
        JSONObject pJSONObjectBusinesses = null;
        try 
        {
            pJSONObjectBusinesses = (JSONObject) pJSONParser.parse(sSearchResponseJSON);
            //System.out.println(response.toJSONString());

            JSONArray pParamBusinesses = (JSONArray) pJSONObjectBusinesses.get("businesses");

            JSONObject pJSONObjectBusiness;
            String sName;
            String sAddress = "";
            String sURLWeb;
            String sURLReview;
            if (pParamBusinesses != null)
            {
                for (Object pObjectBusiness : pParamBusinesses) 
                {
                    pJSONObjectBusiness = (JSONObject)pObjectBusiness;
                    sName = (String)pJSONObjectBusiness.get("name");
                    //
                    JSONObject pParamLocation = (JSONObject)pJSONObjectBusiness.get("location");
                    if (pParamLocation != null)
                    {
                        // Get "address" parameter which is an array.
                        JSONArray pParamAddresses = (JSONArray)pParamLocation.get("address");
                        StringBuilder pBuffer = new StringBuilder();
                        for (Object pObjectAddress : pParamAddresses)
                        {
                            if (pBuffer.length() > 0)
                                pBuffer.append(", ");
                            pBuffer.append((String)pObjectAddress);
                        }
                        if (pBuffer.length() > 0)
                            pBuffer.append(", ");
                        // Get "city" and append to the address.
                        pBuffer.append((String)pParamLocation.get("city"));
                        pBuffer.append(" ");
                        // Get "postal_code" and append to the address.
                        pBuffer.append((String)pParamLocation.get("postal_code"));
                        //
                        sAddress = pBuffer.toString();
                    }
                    //
                    sURLWeb = (String)pJSONObjectBusiness.get("url");
                    sURLReview = sURLWeb; // Yelp doesn't have a dedicated URL for reviews. But web URL has reviews as well.
                    //
                    RestaurantInfo pRestaurantInfo = new RestaurantInfo(sName, sAddress, sURLWeb, null, sURLReview);
                    //
                    // Append the RestaurantInfo instance to the list.
                    pRestaurantInfos.add(pRestaurantInfo);
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
    
   
    private void print_https_cert(HttpsURLConnection con)
    {
     
        if(con!=null)
        {
          try 
          {
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
}
