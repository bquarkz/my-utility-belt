/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.dynamicsearch;

import bquarkz.utilitybelt.readonlymap.ReadOnlyMap;

import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Some description for new class.
 */
public class Grammar
{
    // ****************************************************************************************************************
    // Const Fields
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Common Fields
    // ****************************************************************************************************************
    private ReadOnlyMap< String, String > dictionary;

    // ****************************************************************************************************************
    // Transient
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Constructors
    // ****************************************************************************************************************
    protected Grammar()
    {
        dictionary = ReadOnlyMap.wrap( new HashMap<>() );
    }

    // ****************************************************************************************************************
    // Factories
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Features
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************************************
    public Grammar withDictionary( Dictionary dictionary )
    {
        this.dictionary = dictionary.get();
        return this;
    }

    // ****************************************************************************************************************
    // Methods
    // ****************************************************************************************************************

    /**
     *
     * @param tokens
     * @return
     */
    boolean check( Tokenization tokens )
    {
        return checkSameNumberOfSeparatorsOpenAndClose( tokens ) &&
                checkConstraintsForExpressionBegin( tokens ) &&
                checkConstraintsForExpressionEnd( tokens ) &&
                checkForWrongSequence( tokens, Lexico.NOT_OPERATOR, Lexico.AND_OPERATOR ) &&
                checkForWrongSequence( tokens, Lexico.NOT_OPERATOR, Lexico.OR_OPERATOR ) &&
                checkForWrongSequence( tokens, Lexico.NOT_OPERATOR, Lexico.GROUP_CLOSE ) &&
                checkForWrongSequence( tokens, Lexico.AND_OPERATOR, Lexico.GROUP_CLOSE ) &&
                checkForWrongSequence( tokens, Lexico.OR_OPERATOR, Lexico.GROUP_CLOSE ) &&
                checkForWrongSequence( tokens, Lexico.GROUP_OPEN, Lexico.AND_OPERATOR ) &&
                checkForWrongSequence( tokens, Lexico.GROUP_OPEN, Lexico.OR_OPERATOR )
                ;
    }

    private boolean checkForWrongSequence( Tokenization tokens, final Lexico... sequence )
    {
        int i = 0;
        for( var token : tokens )
        {
            if( token.getLexico() == sequence[ i++ ] )
            {
                if( i == sequence.length ) return false;
            } else {
                i = 0;
            }
        }

        return true;
    }

    private boolean checkConstraintsForExpressionBegin( Tokenization tokens )
    {
        final var firstToken = tokens.getFirst();
        if( !firstToken.isPresent() ) return true; // true by default, it is empty
        final var firstLexico = firstToken.get().getLexico();
        return firstLexico == Lexico.CONTENT || firstLexico == Lexico.NOT_OPERATOR || firstLexico == Lexico.GROUP_OPEN;
    }

    private boolean checkConstraintsForExpressionEnd( Tokenization tokens )
    {
        final var lastToken = tokens.getLast();
        if( !lastToken.isPresent() ) return true; // true by default, it is empty
        final var lastLexico = lastToken.get().getLexico();
        return lastLexico == Lexico.CONTENT || lastLexico == Lexico.GROUP_CLOSE;
    }

    /**
     * <p>
     * Count how many separators <b>open</b> and <b>close</b> we have on <b>tokens</b>.
     * </p>
     *
     * @param tokens
     * @return
     */
    private boolean checkSameNumberOfSeparatorsOpenAndClose( Tokenization tokens )
    {
        //
        int open_counter = 0;
        int close_counter = 0;
        for( var token : tokens )
        {
            if( token.getLexico() == Lexico.GROUP_OPEN ) open_counter++;
            if( token.getLexico() == Lexico.GROUP_CLOSE ) close_counter++;
        }
        return open_counter == close_counter;
    }

    /**
     *
     * @param tokenization
     * @return
     */
    public ParseTreeBuilder apply( final Tokenization tokenization )
    {
        if( tokenization.isEmpty() ) return new ParseTreeBuilder();
        if( check( tokenization ) )
        {
            // grammar is ok, build a parse tree for a full "content, operators and separators"
            return buildFullParseTree( tokenization );
        } else {
            // grammar is not ok, build a parse tree just for content using OR as default operator
            return buildSafeParseTree( tokenization );
        }
    }

    private ParseTreeBuilder buildSafeParseTree( Tokenization tokenization )
    {
        final var tokens = new ArrayDeque<Token>(); // FIFO by default
        for( var token : tokenization )
        {
            if( token.getLexico() == Lexico.CONTENT )
            {
                tokens.add( token );
                tokens.add( createDefaultTokenOperator() );
            }
        }
        // to remove the last default token operator
        tokens.removeLast();
        return new ParseTreeBuilder( false, tokens, dictionary );
    }

    private ParseTreeBuilder buildFullParseTree( Tokenization tokenization )
    {
        final var tokens = new ArrayDeque<Token>(); // FIFO by default
        for( int i = 0; i < tokenization.size(); i++ )
        {
            final var token = tokenization.get( i );
            tokens.add( token );

            // logic to inject a default operator between two contents, or between a content and open group separator,
            // or between a close group and a open group separators, or between a close group separator and a content
            if( token.getLexico() == Lexico.CONTENT || token.getLexico() == Lexico.GROUP_CLOSE )
            {
                // check next
                var index = i + 1;
                // if the content is the last one do nothing
                if( index < tokenization.size() )
                {
                    var lexico = tokenization.get( index ).getLexico();
                    if( lexico == Lexico.CONTENT || lexico == Lexico.GROUP_OPEN )
                    {
                        tokens.add( createDefaultTokenOperator() );
                    }
                }
            }
        }
        return new ParseTreeBuilder( true, tokens, dictionary );
    }

    private Token createDefaultTokenOperator()
    {
        return new Token( Lexico.OR_OPERATOR );
    }

    // ****************************************************************************************************************
    // Patterns
    // ****************************************************************************************************************
    @FunctionalInterface
    public interface Dictionary
    {
        ReadOnlyMap< String, String > get();
    }
}