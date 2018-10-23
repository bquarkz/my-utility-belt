/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.dynamicsearch;

import bquarkz.utilitybelt.readonlymap.ReadOnlyMap;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;

public class ParseTreeBuilder
{
    // ****************************************************************************************************************
    // Const Fields
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Common Fields
    // ****************************************************************************************************************
    private final boolean                       safe;
    private final boolean                       empty;
    private final ParseTree                     root;
    private final ReadOnlyMap< String, String > dictionary;

    // ****************************************************************************************************************
    // Transient
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Constructors
    // ****************************************************************************************************************
    ParseTreeBuilder()
    {
        this.empty = true;
        this.safe = true;
        this.root = null;
        this.dictionary = null;
    }

    ParseTreeBuilder( boolean safe, Queue< Token > tokens, ReadOnlyMap< String, String > dictionary )
    {
        this.empty = tokens.isEmpty();
        if( !empty )
        {
            this.safe = safe;
            this.dictionary = dictionary;
            this.root = processTokens( tokens );
        }
        else
        {
            this.safe = true;
            this.root = null;
            this.dictionary = null;
        }
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
    public boolean isEmpty()
    {
        return empty;
    }

    public boolean isSafe()
    {
        return safe;
    }

    // ****************************************************************************************************************
    // Methods
    // ****************************************************************************************************************
    private ParseTree processTokens( Queue< Token > tokens )
    {
        ParseTree accumulator = null;
        Token token = null;
        while( ( token = tokens.poll() ) != null )
        {
            switch( ExpressionType.of( token ) )
            {
                case CONTENT:
                {
                    // it makes use of a dictionary
                    var key = token.getSequence().toString();
                    if( key.startsWith( "@" ) )
                    {
                        var realValue = dictionary.get( key.substring( 1 ) );
                        if( realValue != null )
                        {
                            token = new Token( token.getLexico() );
                            token.getSequence().append( realValue );
                        }
                    }

                    if( accumulator == null )
                    {
                        accumulator = ParseTree.createContent( token );
                    } else {
                        accumulator.setRight( ParseTree.createContent( token ) );
                    }
                } break;

                case OPERATOR:
                {
                    if( token.getLexico() == Lexico.NOT_OPERATOR )
                    {
                        if( accumulator == null )
                        {
                            accumulator = ParseTree.createOperator( ParseTree.createContent( tokens.poll() ), token );
                        } else {
                            accumulator.setRight( ParseTree.createOperator( ParseTree.createContent( tokens.poll() ), token ) );
                        }
                    }

                    if( token.getLexico() == Lexico.AND_OPERATOR || token.getLexico() == Lexico.OR_OPERATOR )
                    {
                        accumulator = ParseTree.createOperator( accumulator, token );
                    }
                } break;

                case SEPARATOR:
                {
                    if( token.getLexico() == Lexico.GROUP_OPEN )
                    {
                        int level = 0;
                        final var innerTokens = new ArrayDeque<Token>();
                        while( ( token = tokens.poll() ) != null )
                        {
                            if( token.getLexico() == Lexico.GROUP_OPEN )
                            {
                                level++;
                            }

                            if( token.getLexico() == Lexico.GROUP_CLOSE )
                            {
                                if( level-- == 0 )
                                {
                                    if( accumulator == null )
                                    {
                                        accumulator = processTokens( innerTokens );
                                    } else {
                                        accumulator.setRight( processTokens( innerTokens ) );
                                    }
                                    break;
                                }
                            }

                            innerTokens.add( token );
                        }
                    }
                } break;
            }
        }

        return accumulator;
    }

    public < T > Optional< T > evalute( Evaluation< T > evaluation, Function< String, T > behavior )
    {
        if( evaluation == null ) throw new IllegalArgumentException( "evaluation should not be null" );
        if( behavior == null ) throw new IllegalArgumentException( "behavior should not be null" );

        return isEmpty() ? Optional.empty() : Optional.of( root.evaluate( evaluation, behavior ) );
    }

    // ****************************************************************************************************************
    // Patterns
    // ****************************************************************************************************************
    public enum ExpressionType
    {
        CONTENT,
        OPERATOR,
        SEPARATOR,
        ;

        public static ExpressionType of( Token token )
        {
            switch( token.getLexico() )
            {
                case NOT_OPERATOR:
                case AND_OPERATOR:
                case OR_OPERATOR: return ExpressionType.OPERATOR;

                case GROUP_OPEN:
                case GROUP_CLOSE: return ExpressionType.SEPARATOR;

                default: return ExpressionType.CONTENT;
            }
        }
    }

    private static class Config< T >
    {
        private Evaluation< T > evaluation;
        private Function< String, T > behavior;

        public boolean isValid()
        {
            return evaluation != null && behavior != null;
        }
    }
}