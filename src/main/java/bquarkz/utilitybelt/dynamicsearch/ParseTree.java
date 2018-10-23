/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.dynamicsearch;

import java.util.function.Function;

public class ParseTree
{
    // ****************************************************************************************************************
    // Const Fields
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Common Fields
    // ****************************************************************************************************************
    private final Token     token;
    private       ParseTree left;
    private       ParseTree right;

    // ****************************************************************************************************************
    // Transient
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Constructors
    // ****************************************************************************************************************
    protected ParseTree( Token token )
    {
        this.token = token;
    }

    // ****************************************************************************************************************
    // Factories
    // ****************************************************************************************************************

    /**
     * <p>
     * Create a leaf for a single content
     * </p>
     *
     * @return
     */
    public static ParseTree createContent( Token token )
    {
        return new ParseTree( token );
    }

    /**
     * <p>
     *     Create a node for an operator
     * </p>
     * @param left
     * @param operator
     * @return
     */
    public static ParseTree createOperator( ParseTree left, Token operator )
    {
        var o = new ParseTree( operator );
        o.setLeft( left );
        return o;
    }

    // ****************************************************************************************************************
    // Features
    // ****************************************************************************************************************

    // ****************************************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************************************

    public Token getToken()
    {
        return token;
    }

    public ParseTree getLeft()
    {
        return left;
    }

    void setLeft( ParseTree left )
    {
        this.left = left;
    }

    public ParseTree getRight()
    {
        return right;
    }

    void setRight( ParseTree right )
    {
        this.right = right;
    }

    // ****************************************************************************************************************
    // Methods
    // ****************************************************************************************************************
    public < T > T evaluate( Evaluation< T > evaluation, Function< String, T > behavior )
    {
        switch( token.getLexico() )
        {
            case AND_OPERATOR:
                return evaluation
                        .getAND()
                        .apply( left.evaluate( evaluation, behavior ),
                                right.evaluate( evaluation, behavior ) );

            case OR_OPERATOR:
                return evaluation
                        .getOR()
                        .apply( left.evaluate( evaluation, behavior ),
                                right.evaluate( evaluation, behavior ) );

            case NOT_OPERATOR:
                return evaluation
                        .getNOT()
                        .apply( left.evaluate( evaluation, behavior ) );

            default:
                return behavior.apply( token.getSequence().toString().trim() );
        }
    }

    // ****************************************************************************************************************
    // Patterns
    // ****************************************************************************************************************
}