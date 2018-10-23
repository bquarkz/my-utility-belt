package bquarkz.utilitybelt.dynamicsearch;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathInits;
import com.querydsl.core.types.dsl.StringPath;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ParseTreeBuilderTest
{
    @Test
    @Ignore
    public void printEvaluate()
    {
        new Grammar()
                .withDictionary( new MyDictionary() )
                .apply( Tokenizer
                                .that( "done (( {{ rack oven }} && !!completed ))" ) )
                .evalute( new StringEvaluation(), s -> s ).ifPresent( s -> System.out.println( s ) );
    }

    @Test
    public void testLogicManipulation()
    {
        new Grammar()
                .apply( Tokenizer.that( "false (( true && !! false ))" ) )
                .evalute( new BooleanEvaluation(), s -> Boolean.valueOf( s ) ).ifPresent( b -> Assert.assertTrue( b ) );

        new Grammar()
                .apply( Tokenizer
                                .that( "(( false || !! false )) && (( true && !! false ))" ) )
                .evalute( new BooleanEvaluation(), s -> Boolean.valueOf( s ) ).ifPresent( b -> Assert.assertTrue( b ) );

        new Grammar()
                .apply( Tokenizer
                                .that( "(( false || (( false || !! false )) )) && (( true && !! false ))" ) )
                .evalute( new BooleanEvaluation(), s -> Boolean.valueOf( s ) ).ifPresent( b -> Assert.assertTrue( b ) );
    }

    @Test
    public void testEvaluate()
    {
        new Grammar()
                .withDictionary( new MyDictionary() )
                .apply( Tokenizer
                                .that( "A (( (( B && @inprog )) D (( E && F )) ))" ) )
                .evalute( new StringEvaluation(), s -> s ).ifPresent( s ->
                    Assert.assertEquals( "( A OR ( ( ( B AND IN PROGRESS ) OR D ) OR ( E AND F ) ) )", s ) );
    }

    @Test
    public void testEvaluateWithQueryDSLEvaluation()
    {
        new Grammar()
                .withDictionary( new MyDictionary() )
                .apply( Tokenizer.that( "A && (( {{ B C D }} || !! E ))" ) )
                .evalute( new QueryDslEvaluation(), s -> ExpressionUtils.anyOf(
                        QEntity.entity.description.containsIgnoreCase( s ),
                        QEntity.entity.type.containsIgnoreCase( s ) ) )
                .ifPresent( p -> Assert.assertEquals(
                        "(containsIc(entity.description,A) || containsIc(entity.type,A)) && "
                                + "(containsIc(entity.description,B C D) || containsIc(entity.type,B C D) || "
                                + "!(containsIc(entity.description,E) || containsIc(entity.type,E)))", p.toString() ) );
    }

    // A dummy class for QueryDSL stuff
    private static class QEntity extends EntityPathBase< String >
    {
        private static final PathInits INITS = PathInits.DIRECT2;

        public QEntity( String variable )
        {
            this( String.class, PathMetadataFactory.forVariable( variable ), INITS );
        }

        public QEntity( Class<? extends String> type, PathMetadata metadata, @Nullable PathInits inits )
        {
            super( type, metadata, inits );
        }

        public static final QEntity entity = new QEntity( "entity" );

        public final StringPath description = createString( "description");

        public final StringPath type = createString( "type");
    }

    // just to extract the output to test
    public static class StringEvaluation implements Evaluation< String >
    {
        @Override public BiFunction< String, String, String > getAND()
        {
            return ( left, right ) -> new StringBuilder( "( " )
                    .append( left )
                    .append( " AND " )
                    .append( right )
                    .append( " )" )
                    .toString();
        }

        @Override public BiFunction< String, String, String > getOR()
        {
            return ( left, right ) -> new StringBuilder( "( " )
                    .append( left )
                    .append( " OR " )
                    .append( right )
                    .append( " )" )
                    .toString();
        }

        @Override public Function< String, String > getNOT()
        {
            return ( left ) -> new StringBuilder( "( !" )
                    .append( left )
                    .append( " )" )
                    .toString();
        }
    }

    private static class BooleanEvaluation implements Evaluation< Boolean >
    {

        @Override public BiFunction<Boolean, Boolean, Boolean> getAND()
        {
            return ( left, right ) -> left && right;
        }

        @Override public BiFunction<Boolean, Boolean, Boolean> getOR()
        {
            return ( left, right ) -> left || right;
        }

        @Override public Function<Boolean, Boolean> getNOT()
        {
            return ( left ) -> !left;
        }
    }
}