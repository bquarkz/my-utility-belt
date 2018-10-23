/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.readonlymap;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A simple read-only map interface that wraps (and protected) any map implementation.
 */
public interface ReadOnlyMap< K, V > extends Iterable< V >
{
    // ****************************************************************************************
    // Constants
    // ****************************************************************************************

    // ****************************************************************************************
    // Static
    // ****************************************************************************************

    /**
     *
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    static < K, V > ReadOnlyMap< K, V > wrap( final Map<K, V> map )
    {
        return wrap( map, ( key ) -> null );
    }

    /**
     * wrap it with a "default value" for keys that doesn't exists on keyset
     *
     * @param map
     * @param defaultForNull
     * @param <K>
     * @param <V>
     * @return
     */
    static < K, V > ReadOnlyMap< K, V > wrap( final Map<K, V> map, final Function<K, V> defaultForNull )
    {
        return new ReadOnlyMap<>()
        {
            @Override
            public Set< K > keySet()
            {
                return map.keySet();
            }

            @Override
            public V get( K key )
            {
                V v = map.get( key );
                if( v == null )
                {
                    return defaultForNull.apply( key );
                } else {
                    return v;
                }
            }

            @Override
            public int size()
            {
                return map.size();
            }

            @Override
            public boolean containsRegister( K key )
            {
                return map.containsKey( key );
            }

            @Override public Stream<V> values()
            {
                return map.values().stream();
            }

            @Override
            public Iterator< V > iterator()
            {
                return map.values().iterator();
            }

            @Override
            public List< V > getValuesOrderedBy( List< K > keys )
            {
                final List< V > orderedValues = new ArrayList<>( keys.size() );
                keys.forEach( ( k ) -> {
                    V v = map.get( k );
                    if( v == null ) return; // do nothing
                    orderedValues.add( v );
                });

                return orderedValues;
            }
        };
    }

    // ****************************************************************************************
    // Default Implementations
    // ****************************************************************************************

    // ****************************************************************************************
    // Contracts
    // ****************************************************************************************
    Set< K > keySet();
    V get( K key );
    List< V > getValuesOrderedBy( List<K> keys );
    int size();
    boolean containsRegister( K key );
    Stream< V > values();
}
