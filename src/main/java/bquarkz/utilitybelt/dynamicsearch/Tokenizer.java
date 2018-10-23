/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.dynamicsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Tokenizer
{
    // ****************************************************************************************************************
    // Const Fields
    // ****************************************************************************************************************
    private static final char[] BASIC_SEPARATOR = { ' ' };
    private static final char[] UNPARSEABLE_OPEN  = { '{', '{' };
    private static final char[] UNPARSEABLE_CLOSE = { '}', '}' };

    // ****************************************************************************************************************
    // Common Fields
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Transient
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Constructors
    // ****************************************************************************************************************
    Tokenizer()
    {
    }

    // ****************************************************************************************************************
    // Factories
    // ****************************************************************************************************************
    public static Tokenization that( final String sentence )
    {
        return new Tokenizer().tokenization( sentence );
    }

    // ****************************************************************************************************************
    // Features
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Methods
    // ****************************************************************************************************************

    /**
     *
     * @param fullSentence
     * @return
     */
    Tokenization tokenization( final String fullSentence )
    {
        List<Token> tokens = new ArrayList<>();
        for( var token : lexicalSpliterator( fullSentence ) )
        {
            if( token.isParseable() )
            {
                tokens.addAll( parser( token.getSequence() ) );
            } else {
                tokens.add( token );
            }
        }
        return Tokenization.wrap( tokens );
    }

    /**
     *
     * @param fullString
     * @return
     */
    List<Token> lexicalSpliterator( final String fullString )
    {
        final List<Token> sequences = new ArrayList<>();
        Optional< Integer > match = null;
        var current = new Token( Lexico.UNDEFINED );
        for( int posix = 0; posix < fullString.length(); posix++ )
        {
            match = findMatcher( posix, fullString, UNPARSEABLE_OPEN );
            if( match.isPresent() )
            {
                posix += match.get();
                sequences.add( current );
                current = new Token( Lexico.CONTENT );
                continue;
            }

            match = findMatcher( posix, fullString, UNPARSEABLE_CLOSE );
            if( match.isPresent() )
            {
                posix += match.get();
                if( !current.isParseable() )
                {
                    sequences.add( current );
                    current = new Token( Lexico.UNDEFINED );
                }
                continue;
            }

            current.getSequence().append( fullString.charAt( posix ) );
        }

        if( current.isValidToken() )
        {
            sequences.add( current );
        }

        return sequences;
    }

    /**
     *
     * @param sequence
     * @return
     */
    List<Token> parser( final StringBuilder sequence )
    {
        return parser( sequence.toString() );
    }

    /**
     *
     * @param sequence
     * @return
     */
    List<Token> parser( final String sequence )
    {
        final List<Token> lexers = new ArrayList<>();
        Optional< Integer > match = null;
        var current = new Token( Lexico.CONTENT );
        for( int posix = 0; posix < sequence.length(); posix++ )
        {
            match = findMatcher( posix, sequence, BASIC_SEPARATOR );
            if( match.isPresent() )
            {
                posix += match.get();
                if( current.isValidToken() )
                {
                    lexers.add( current );
                    current = new Token( Lexico.CONTENT );
                }
                continue;
            }

            match = findMatcher( posix, sequence, Lexico.AND_OPERATOR.getContent() );
            if( match.isPresent() )
            {
                posix += match.get();
                if( current.isValidToken() )
                {
                    lexers.add( current );
                    current = new Token( Lexico.CONTENT );
                }
                lexers.add( new Token( Lexico.AND_OPERATOR ) );
                continue;
            }

            match = findMatcher( posix, sequence, Lexico.OR_OPERATOR.getContent() );
            if( match.isPresent() )
            {
                posix += match.get();
                if( current.isValidToken() )
                {
                    lexers.add( current );
                    current = new Token( Lexico.CONTENT );
                }
                lexers.add( new Token( Lexico.OR_OPERATOR ) );
                continue;
            }

            match = findMatcher( posix, sequence, Lexico.NOT_OPERATOR.getContent() );
            if( match.isPresent() )
            {
                posix += match.get();
                if( current.isValidToken() )
                {
                    lexers.add( current );
                    current = new Token( Lexico.CONTENT );
                }
                lexers.add( new Token( Lexico.NOT_OPERATOR ) );
                continue;
            }

            match = findMatcher( posix, sequence, Lexico.GROUP_OPEN.getContent() );
            if( match.isPresent() )
            {
                posix += match.get();
                if( current.isValidToken() )
                {
                    lexers.add( current );
                    current = new Token( Lexico.CONTENT );
                }
                lexers.add( new Token( Lexico.GROUP_OPEN ) );
                continue;
            }

            match = findMatcher( posix, sequence, Lexico.GROUP_CLOSE.getContent() );
            if( match.isPresent() )
            {
                posix += match.get();
                if( current.isValidToken() )
                {
                    lexers.add( current );
                    current = new Token( Lexico.CONTENT );
                }
                lexers.add( new Token( Lexico.GROUP_CLOSE ) );
                continue;
            }

            current.getSequence().append( sequence.charAt( posix ) );
        }

        if( current.isValidToken() )
        {
            lexers.add( current );
        }

        return lexers;
    }

    /**
     * Try to find a matcher content inside full string.
     *
     * @param posix
     * @param fullString
     * @param matcher
     * @return number to increase into posix if it finds a matcher otherwise -1
     */
    public static Optional< Integer > findMatcher( final int posix, final String fullString, final char[] matcher )
    {
        int counter = 0;
        for( ; counter < matcher.length; counter++ )
        {
            var index = counter + posix;
            if( index >= fullString.length() ) break; // first full stop [ end of string ]
            if( fullString.charAt( index ) != matcher[ counter ] ) break; // second full stop [ it match ]
        }
        return matcher.length == counter ? Optional.of( matcher.length - 1 ) : Optional.empty();
    }

    // ****************************************************************************************************************
    // Patterns
    // ****************************************************************************************************************
}
