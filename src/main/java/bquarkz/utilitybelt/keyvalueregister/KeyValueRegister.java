/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.keyvalueregister;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class KeyValueRegister< T > implements Iterable< String >
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************
    private Map< String, T > map;

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
    public KeyValueRegister() {
        map = new HashMap<>();
    }
    
    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    public void register( KeyValue< T > kv ) {
        register( kv.getKey(), kv.getValue() );
    }
    
    public void register( String key, T value ) {
        map.put( key, value );
    }

    @Override
    public Iterator< String > iterator() {
        return map.keySet().iterator();
    }
    
    public T get( String key ) {
        return map.get( key );
    }
    
    public int size() {
        return map.size();
    }

    public void clear() {
        map.clear();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void remove( String key ) {
        map.remove( key );
    }
    
    public boolean checkForkey( String key ) {
        return map.containsKey( key );
    }
    
    public boolean checkForValue( T value ) {
        return map.containsValue( value );
    }

    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
}