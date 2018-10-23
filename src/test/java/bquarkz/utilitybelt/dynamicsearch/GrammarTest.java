package bquarkz.utilitybelt.dynamicsearch;

import org.junit.Assert;
import org.junit.Test;

public class GrammarTest
{
    private static final Grammar grammar = new Grammar();

    @Test
    public void testBeginTokens()
    {
        Assert.assertFalse( grammar.check( Tokenizer.that( "))" ) ) );
        Assert.assertFalse( grammar.check( Tokenizer.that( "&&" ) ) );
        Assert.assertFalse( grammar.check( Tokenizer.that( "||" ) ) );
    }

    @Test
    public void testEndTokens()
    {
        Assert.assertFalse( grammar.check( Tokenizer.that( "((" ) ) );
        Assert.assertFalse( grammar.check( Tokenizer.that( "!!" ) ) );
        Assert.assertFalse( grammar.check( Tokenizer.that( "&&" ) ) );
        Assert.assertFalse( grammar.check( Tokenizer.that( "||" ) ) );
    }

    @Test
    public void testCountGroups()
    {
        Assert.assertTrue( grammar.check( Tokenizer.that( "(())" ) ) );
        Assert.assertFalse( grammar.check( Tokenizer.that( "(())((" ) ) );
    }

    @Test
    public void testBasSequences()
    {
        Assert.assertFalse( grammar.check( Tokenizer.that( "1 !! && 2" ) ) ); // not and
        Assert.assertFalse( grammar.check( Tokenizer.that( "1 !! || 2" ) ) ); // not or
        Assert.assertFalse( grammar.check( Tokenizer.that( "1 (( 2 !! )) 3" ) ) ); // not close
        Assert.assertFalse( grammar.check( Tokenizer.that( "1 (( 2 && )) 3" ) ) ); // and close
        Assert.assertFalse( grammar.check( Tokenizer.that( "1 (( 2 || )) 3" ) ) ); // or close
        Assert.assertFalse( grammar.check( Tokenizer.that( "1 (( && 2 ))" ) ) ); // open and
        Assert.assertFalse( grammar.check( Tokenizer.that( "1 (( || 2 ))" ) ) ); // open and
    }

    @Test
    public void test()
    {
        var pt = grammar.apply( Tokenizer.that( "1 (( 2 3 )) && 4" ) );
    }

}