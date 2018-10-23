/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.keyvalueregister;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A POJO that represent a common property
 */
public class Property implements KeyValue< String >
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************
    private String key;
    private String value;

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
    public Property(
            KeyValue< String > property
            )
    {
        this.key = property.getKey();
        this.value = property.getValue();        
    }

    public Property( String key )
    {
        this( key, (String)null );
    }

    public Property( String key, boolean value )
    {
        this( key, Boolean.valueOf( value ) );
    }

    public Property( String key, Boolean value )
    {
        this( key, value == null ? null : value.toString() );
    }

    public Property( String key, float value )
    {
        this( key, Float.valueOf( value ) );
    }

    public Property( String key, Float value )
    {
        this( key, value == null ? null : value.toString() );
    }

    public Property( String key, double value )
    {
        this( key, Double.valueOf( value ) );
    }

    public Property( String key, Double value )
    {
        this( key, value == null ? null : value.toString() );
    }

    public Property( String key, int value )
    {
        this( key, Integer.valueOf( value ) );
    }

    public Property( String key, Integer value )
    {
        this( key, value == null ? null : value.toString() );
    }

    @JsonCreator
    public Property(
            @JsonProperty( "key" ) String key,
            @JsonProperty( "value" ) String value
    )
    {
        this.key = key;
        this.value = value;
    }

    // ****************************************************************************************
    // Factory
    // ****************************************************************************************
    public static Property of( String key )
    {
        return new Property( key );
    }

    public static Property of( KeyValue< String > property )
    {
        return new Property( property );
    }

    public static Property of( String key, String value )
    {
        return new Property( key, value );
    }

    public static Property of( String key, boolean value )
    {
        return new Property( key, value );
    }

    public static Property of( String key, Boolean value )
    {
        return new Property( key, value );
    }

    public static Property of( String key, float value )
    {
        return new Property( key, value );
    }

    public static Property of( String key, Float value )
    {
        return new Property( key, value );
    }

    public static Property of( String key, double value )
    {
        return new Property( key, value );
    }

    public static Property of( String key, Double value )
    {
        return new Property( key, value );
    }

    public static Property of( String key, int value )
    {
        return new Property( key, value );
    }

    public static Property of( String key, Integer value )
    {
        return new Property( key, value );
    }

    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    @Override
    public String getKey()
    {
        return key;
    }
    
    public void setKey( String key )
    {
        this.key = key;
    }

    @Override
    public String getValue()
    {
        return value;
    }
    
    public void setValue( String value )
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return "Property [key=" + key + ", value=" + value + "]";
    }

    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
}
