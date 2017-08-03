/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vegtox.restaurant;

import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 *
 * Class that holds the information on an individual event
 */
class VenueInfo implements JSONAware{
    // Empty string.
    protected static final String sSTRING_EMPTY = "";
    
    // Name of the event.
    protected String e_sName = sSTRING_EMPTY;
    // Address of the event.
    protected String e_sAddress = sSTRING_EMPTY;
    // Address of the event.
    protected String e_sOrganiserId = sSTRING_EMPTY;
    // Address of the event.
    protected String e_sStartTime = sSTRING_EMPTY;
    // Website of the restaurant.
    protected String e_sEndTime = sSTRING_EMPTY;
    // Brief description.
    protected String e_sDesc = sSTRING_EMPTY;
    // Brief description.
    protected String e_sVenueId = sSTRING_EMPTY;
    protected String e_sVenue = sSTRING_EMPTY;

    
    public VenueInfo(String sName, String sAddress, String sOrganiserId, String sStartTime, String sEndTime, String sDesc, String sVenueId, String sVenue)
    {
        e_sName = sName;
        e_sAddress = sAddress;
        e_sOrganiserId = sOrganiserId;
        e_sStartTime = sStartTime;
        e_sEndTime = sEndTime;
        e_sDesc = sDesc;
        e_sVenueId = sVenueId;
        e_sVenue = sVenue;
    }
    
    
    public String toJSONString() 
    {
        Map<Object, Object> pJSONBeanMap = new HashMap<Object, Object>();
        pJSONBeanMap.put("Name", e_sName);
        pJSONBeanMap.put("Address", e_sAddress);
        pJSONBeanMap.put("OrganiserId", e_sOrganiserId);
        pJSONBeanMap.put("StartTime", e_sStartTime);
        pJSONBeanMap.put("EndTime", e_sEndTime);
        pJSONBeanMap.put("Desc", e_sDesc);
        pJSONBeanMap.put("VenueId", e_sVenueId);
        pJSONBeanMap.put("VenueId", e_sVenue);
        return JSONObject.toJSONString(pJSONBeanMap);
    }
}
