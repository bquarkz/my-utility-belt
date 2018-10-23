package bquarkz.utilitybelt.processor;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ProcessorTest
{
    @Test
    public void testProcessorWorkOK()
    {
        Processor processor = new Processor();
        ProcessImplTest process = new ProcessImplTest();
        ProcessTicket< Integer > ticket = processor.execute( process );
        processor.work();
        Integer result = processor.getResult( ticket );
        Assert.assertTrue( result.equals( process.getResult() ) );
        Assert.assertTrue( process.isBeforeProcessExecuted() );
        Assert.assertTrue( process.isAfterProcessExecuted() );
        Assert.assertFalse( process.isCancelProcessExecuted() );
        Assert.assertFalse( process.isTroubleshootingPerfomed() );
    }
    
    @Test
    public void testProcessorWorkCancel()
    {
        long MAX = 60_000L; // one minute :)
        long t0 = System.currentTimeMillis();
        Processor processor = new Processor();
        ProcessImplTest process = new ProcessImplTest( MAX );
        ProcessTicket< Integer > ticket = processor.execute( process );

        processor.cancel( ticket );
        
        processor.work();

        Assert.assertFalse( process.isTroubleshootingPerfomed() );
        Assert.assertTrue( process.isCancelProcessExecuted() );
        Assert.assertNull( processor.getResult( ticket ) );
        long tf = System.currentTimeMillis();
        long total = tf - t0;
        Assert.assertTrue( total < MAX );
    }
    
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Test
    public void testProcessWorkPoolExecution()
    {
        long MAX = 500L;
        long t0 = System.currentTimeMillis();
        Processor processor = new Processor();
        Map< ProcessTicket, ProcessImplTest > map = new HashMap<>();
        for( int i = 0; i < 100; i++ )
        {
            ProcessImplTest process = new ProcessImplTest( MAX );
            ProcessTicket< Integer > ticket = processor.execute( process );
            map.put( ticket, process );
        }
        
        processor.work();
        
        for( ProcessTicket ticket : map.keySet() )
        {
            ProcessImplTest process = map.get( ticket );
            Assert.assertTrue( processor.getResult( ticket ).equals( map.get( ticket ).getResult() ) );
            Assert.assertTrue( process.isBeforeProcessExecuted() );
            Assert.assertTrue( process.isAfterProcessExecuted() );
            Assert.assertFalse( process.isCancelProcessExecuted() );
            Assert.assertFalse( process.isTroubleshootingPerfomed() );
        }
        
        long tf = System.currentTimeMillis();
        long total = tf - t0;
        Assert.assertTrue( total < ( MAX + ( MAX * 0.3 ) ) );
    }
    
    @Test
    public void testProcessorWorkTroubleshooting()
    {
        long MAX = 60_000L; // one minute :)
        long t0 = System.currentTimeMillis();
        Processor processor = new Processor();
        ProcessImplTest process = new ProcessImplTest();
        process.putIntoTrouble();
        
        ProcessTicket< Integer > ticket = processor.execute( process );
        processor.work();
        Assert.assertTrue( process.isTroubleshootingPerfomed() );
        Assert.assertFalse( process.isCancelProcessExecuted() );
        Assert.assertNull( processor.getResult( ticket ) );
        long tf = System.currentTimeMillis();
        long total = tf - t0;
        Assert.assertTrue( total < MAX );
    }
    
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Test
    public void testProcessWorkPoolExecutionWithTroubleshooting()
    {
        long MAX = 60_000L; // :000000 one minute
        long t0 = System.currentTimeMillis();
        Processor processor = new Processor();
        Map< ProcessTicket, ProcessImplTest > map = new HashMap<>();
        for( int i = 0; i < 100; i++ )
        {
            ProcessImplTest process = null;
            if( i % 2 == 0 )
            {
                process = new ProcessImplTest( MAX );
                process.putIntoTrouble();
            } else {
                process = new ProcessImplTest();
            }
            ProcessTicket< Integer > ticket = processor.execute( process );
            map.put( ticket, process );
        }
        
        processor.work();
        
        for( ProcessTicket ticket : map.keySet() )
        {
            ProcessImplTest process = map.get( ticket );
            if( process.isInTrouble() )
            {
                Assert.assertTrue( process.isBeforeProcessExecuted() );
                Assert.assertTrue( process.isAfterProcessExecuted() ); // because the trouble everytime was solved (default is not)
                Assert.assertFalse( process.isCancelProcessExecuted() ); // no cancel execution
                Assert.assertTrue( process.isTroubleshootingPerfomed() ); // troubleshooting was perfomed
                Assert.assertNull( processor.getResult( ticket ) );
            } else {
                Assert.assertTrue( processor.getResult( ticket ).equals( map.get( ticket ).getResult() ) );
                Assert.assertTrue( process.isBeforeProcessExecuted() );
                Assert.assertTrue( process.isAfterProcessExecuted() );
                Assert.assertFalse( process.isCancelProcessExecuted() );
                Assert.assertFalse( process.isTroubleshootingPerfomed() );
            }
        }
        long tf = System.currentTimeMillis();
        long total = tf - t0;
        Assert.assertTrue( total < MAX );
    }
}
