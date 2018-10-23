package bquarkz.utilitybelt.dynamicsearch;

import org.junit.Assert;
import org.junit.Test;

public class LexicalAnalyzerTest
{
    @Test
    public void testLexicalSpliterator()
    {
        var tokenizer = new Tokenizer();
        var list = tokenizer.lexicalSpliterator( "\"TEST'{{@TEST~}}" );
        Assert.assertNotNull( list );
        Assert.assertFalse( list.isEmpty() );
        Assert.assertEquals( list.get( 0 ).getSequence().toString(), "\"TEST'" );
        Assert.assertEquals( list.get( 0 ).getLexico(), Lexico.UNDEFINED );

        Assert.assertEquals( list.get( 1 ).getSequence().toString(), "@TEST~" );
        Assert.assertEquals( list.get( 1 ).getLexico(), Lexico.CONTENT );
    }

    @Test
    public void testParser()
    {
        var lexicalAnalyzer = new Tokenizer();
        var list = lexicalAnalyzer.parser( "SEQ1((SEQ2((     SEQ3 && SEQ4 )))) )" );
        Assert.assertNotNull( list );
        Assert.assertFalse( list.isEmpty() );
        Assert.assertEquals( list.get( 0 ).getSequence().toString(), "SEQ1" );
        Assert.assertEquals( list.get( 0 ).getLexico(), Lexico.CONTENT );

        Assert.assertEquals( list.get( 1 ).getSequence().toString(), "((" );
        Assert.assertEquals( list.get( 1 ).getLexico(), Lexico.GROUP_OPEN );

        Assert.assertEquals( list.get( 2 ).getSequence().toString(), "SEQ2" );
        Assert.assertEquals( list.get( 2 ).getLexico(), Lexico.CONTENT );

        Assert.assertEquals( list.get( 3 ).getSequence().toString(), "((" );
        Assert.assertEquals( list.get( 3 ).getLexico(), Lexico.GROUP_OPEN );

        Assert.assertEquals( list.get( 4 ).getSequence().toString(), "SEQ3" );
        Assert.assertEquals( list.get( 4 ).getLexico(), Lexico.CONTENT );

        Assert.assertEquals( list.get( 5 ).getSequence().toString(), "&&" );
        Assert.assertEquals( list.get( 5 ).getLexico(), Lexico.AND_OPERATOR );

        Assert.assertEquals( list.get( 6 ).getSequence().toString(), "SEQ4" );
        Assert.assertEquals( list.get( 6 ).getLexico(), Lexico.CONTENT );

        Assert.assertEquals( list.get( 7 ).getSequence().toString(), "))" );
        Assert.assertEquals( list.get( 7 ).getLexico(), Lexico.GROUP_CLOSE );

        Assert.assertEquals( list.get( 8 ).getSequence().toString(), "))" );
        Assert.assertEquals( list.get( 8 ).getLexico(), Lexico.GROUP_CLOSE );

        Assert.assertEquals( list.get( 9 ).getSequence().toString(), ")" );
        Assert.assertEquals( list.get( 9 ).getLexico(), Lexico.CONTENT );

    }

    @Test
    public void testTokenizer()
    {
        var list = Tokenizer
                .that( "{{\"TEST'@TEST~}}SEQ1((SEQ2((   SEQ3 && SEQ4 )))){{ MALUQUICE || MALUQUICE2 }}" );
        Assert.assertNotNull( list );
        Assert.assertFalse( list.isEmpty() );
        Assert.assertEquals( list.get( 0 ).getSequence().toString(), "\"TEST'@TEST~" );
        Assert.assertEquals( list.get( 1 ).getSequence().toString(), "SEQ1" );
        Assert.assertEquals( list.get( 2 ).getSequence().toString(), "((" );
        Assert.assertEquals( list.get( 3 ).getSequence().toString(), "SEQ2" );
        Assert.assertEquals( list.get( 4 ).getSequence().toString(), "((" );
        Assert.assertEquals( list.get( 5 ).getSequence().toString(), "SEQ3" );
        Assert.assertEquals( list.get( 6 ).getSequence().toString(), "&&" );
        Assert.assertEquals( list.get( 7 ).getSequence().toString(), "SEQ4" );
        Assert.assertEquals( list.get( 8 ).getSequence().toString(), "))" );
        Assert.assertEquals( list.get( 9 ).getSequence().toString(), "))" );
        Assert.assertEquals( list.get( 10 ).getSequence().toString(), " MALUQUICE || MALUQUICE2 " );
    }

}
