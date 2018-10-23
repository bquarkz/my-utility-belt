/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.dynamicsearch;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface Tokenization extends Iterable<Token>
{
    // ****************************************************************************************
    // Constants
    // ****************************************************************************************

    // ****************************************************************************************
    // Static
    // ****************************************************************************************
    static Tokenization wrap( final List< Token > tokens )
    {
        return new Tokenization()
        {
            @Override public Token get( int index )
            {
                return tokens.get( index );
            }

            @Override public Optional< Token > getFirst()
            {
                return isEmpty() ? Optional.empty() : Optional.of( get( 0 ) );
            }

            @Override public Optional< Token >  getLast()
            {
                return isEmpty() ? Optional.empty() : Optional.of( get( tokens.size() - 1 ) );
            }

            @Override public boolean isEmpty()
            {
                return tokens.isEmpty();
            }

            @Override public int size()
            {
                return tokens.size();
            }

            @Override public Iterator<Token> iterator()
            {
                return tokens.iterator();
            }

            @Override public void forEach( Consumer<? super Token> action )
            {
                tokens.forEach( action );
            }

            @Override public Spliterator<Token> spliterator()
            {
                return tokens.spliterator();
            }

        };
    }

    // ****************************************************************************************
    // Default Implementations
    // ****************************************************************************************

    // ****************************************************************************************
    // Contracts
    // ****************************************************************************************
    Token get( int index );
    Optional< Token > getFirst();
    Optional< Token > getLast();
    boolean isEmpty();
    int size();
}