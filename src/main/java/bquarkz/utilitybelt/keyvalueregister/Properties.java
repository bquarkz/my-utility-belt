/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.keyvalueregister;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.*;

public class Properties extends LinkedList< Property >
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************
    private static final long serialVersionUID = -7956748181112092508L;

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************
    private Map< String, String > map;

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
    public Properties()
    {
        super();
        init( null );
    }

    @JsonCreator
    public Properties( Collection< Property > batch )
    {
        super( batch );
        init( batch );
    }

    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    private void init( Collection< Property > batch )
    {
        this.map = new LinkedHashMap<>();
        if( batch != null )
        {
            for( Property p : batch )
            {
                map.put( p.getKey(), p.getValue() );
            }
        }
    }
    
    public Map< String, String > getMap()
    {
        return map;
    }
    
    public void add( String key, String value )
    {
        Property p = new Property( key, value );
        this.add( p );
        this.map.put( p.getKey(), p.getValue() );
    }
    
    public void put( String key, String value )
    {
        this.add( key, value );
    }
    
    public String get( String key )
    {
        return map.get( key );
    }

    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
}
