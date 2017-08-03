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
 * Class that holds the information on an individual restaurant.
 * 
 */
public class RestaurantInfo implements JSONAware
{
    
    // Empty string.
    protected static final String sSTRING_EMPTY = "";
    
    // Name of the restaurant.
    protected String m_sName = sSTRING_EMPTY;
    // Address of the restaurant.
    protected String m_sAddress = sSTRING_EMPTY;
    // Website of the restaurant.
    protected String m_sURLWeb = sSTRING_EMPTY;
    // Brief description.
    protected String m_sShortDesc = sSTRING_EMPTY;
    // URL for the reviews.
    protected String m_sURLReview = sSTRING_EMPTY;
    
    public RestaurantInfo(String sName, String sAddress, String sURLWeb, String sShortDesc, String sURLReview)
    {
        m_sName = sName;
        m_sAddress = sAddress;
        m_sURLWeb = sURLWeb;
        m_sShortDesc = sShortDesc;
        m_sURLReview = sURLReview;
    }
    
    // Hash code implementation.
    //
    @Override
    public int hashCode()
    {
        int iHash = 0;
        if (m_sName != null)
        {
            String sNameLowerCase = m_sName.toLowerCase();
            iHash = sNameLowerCase.hashCode();
        }
        return iHash;
    }

    // Equals method.
    //
    @Override
    public boolean equals(Object pObject) 
    {
        if (pObject == null)
        {
            return false;
        }
        if (getClass() != pObject.getClass()) 
        {
            return false;
        }
        final RestaurantInfo pRestInfo = (RestaurantInfo) pObject;
        String sNameCur = (m_sName != null) ? m_sName.toLowerCase() : null;
        String sNameOther = (pRestInfo.m_sName != null) ? pRestInfo.m_sName.toLowerCase() : null;
        return ((sNameCur == sNameOther) || (sNameCur != null && sNameCur.equals(sNameOther)));
    }

    @Override
    public String toJSONString() 
    {
        Map<Object, Object> pJSONBeanMap = new HashMap<Object, Object>();
        pJSONBeanMap.put("Name", m_sName);
        pJSONBeanMap.put("Address", m_sAddress);
        pJSONBeanMap.put("URLWeb", m_sURLWeb);
        pJSONBeanMap.put("URLReview", m_sURLReview);
        pJSONBeanMap.put("ShortDesc", m_sShortDesc);
        return JSONObject.toJSONString(pJSONBeanMap);
    }
    
}
