/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.processor;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public final class Processor
{
	// ****************************************************************************************
	// Const Fields
	// ****************************************************************************************

    // ****************************************************************************************
	// Common Fields
	// ****************************************************************************************
    @SuppressWarnings( "rawtypes" )
    private final Map< ProcessTicket, FutureAndResult > map;
    private final ThreadFactory factory;
    private final UncaughtExceptionHandler uncaughtExceptionHandler;
    private final ExecutorService executor;
    private long ticketCoutner;
    
	// ****************************************************************************************
	// Constructors
	// ****************************************************************************************
    public Processor()
    {
        this( new ProcessorUncaughtExceptionHandlerDefault() );
    }
    
	public Processor(
	        UncaughtExceptionHandler uncaughtExceptionHandler
	        )
	{
	    this.ticketCoutner = 0L;
	    this.map = new HashMap<>();
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        this.factory = new ProcessFactory( this.uncaughtExceptionHandler );
	    this.executor = Executors.newCachedThreadPool( factory );
	}

	// ****************************************************************************************
	// Methods
	// ****************************************************************************************
	public < T > ProcessTicket< T > execute( IProcess< T > process )
	{
	    process.beforeProcessExecution();
    	Future< T > future = executor.submit( process );
    	ProcessTicket< T > ticket = new ProcessTicket< T >( ticketCoutner++ );
    	map.put( ticket, new FutureAndResult< T >( process, future ) );
    	return ticket;
	}
	
	@SuppressWarnings( { "rawtypes" } )
    public void work()
	{
	    Collection< FutureAndResult > futuresAndResults = map.values();
	    for( FutureAndResult fr : futuresAndResults )
	    {
	        try
	        {
                if( fr.hasResult() )
                {
                    fr.getProcess().afterProcessExecution();
                } else {
                    fr.getProcess().cancelExecutionCallback();
                }
            } catch ( Exception ee ) {
                try {
                    if( fr.getProcess().performProcessTroubleshooting( new Troubleshooting( ee ) ) )
                    {
                        fr.getProcess().afterProcessExecution();
                    }
                } catch( Exception ie ) {
                    throw new TroubleshootingException( fr.getProcess(), ie );
                }
            }
	    }
	}

	@SuppressWarnings( "unchecked" )
    public < T > void cancel( ProcessTicket< T > ticket )
    {
        FutureAndResult< T > fr = map.get( ticket );
        if( fr == null ) return;
        fr.cancel();
    }

	// ****************************************************************************************
	// Getters And Setters Methods
	// ****************************************************************************************
    @SuppressWarnings( "unchecked" )
    protected < T > Future< T > getFuture( ProcessTicket< T > ticket )
    {
        FutureAndResult< T > futureAndResult = map.get( ticket );
        if( futureAndResult == null )
        {
            return null;
        } else {
            return futureAndResult.getFuture();
        }
    }
    
    @SuppressWarnings( "unchecked" )
    public < T > T getResult( ProcessTicket< T > ticket )
    {
        FutureAndResult< T > fr = map.get( ticket );
        if( fr == null ) return null;
        return fr.getResult();
    }

	// ****************************************************************************************
	// Patterns
	// ****************************************************************************************
}
