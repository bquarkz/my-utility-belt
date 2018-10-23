/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.processor;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessFactory implements java.util.concurrent.ThreadFactory
{
	// ****************************************************************************************
	// Const Fields
	// ****************************************************************************************

    // ****************************************************************************************
	// Common Fields
	// ****************************************************************************************
    private static final AtomicInteger poolNumber = new AtomicInteger( 1 );
    private final ThreadGroup group;
    private final AtomicLong threadNumber = new AtomicLong( 1 );
    private final String namePrefix;
    private final UncaughtExceptionHandler uncaughtExceptionHandler;
    
	// ****************************************************************************************
	// Constructors
	// ****************************************************************************************
    protected ProcessFactory()
    {
        this( null );
    }
    
    protected ProcessFactory(
            UncaughtExceptionHandler handler
            )
    {
        SecurityManager s = System.getSecurityManager();
        this.group = ( s != null ) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
        this.uncaughtExceptionHandler = ( handler != null ? handler : new ProcessorUncaughtExceptionHandlerDefault() );
    }
    
	// ****************************************************************************************
	// Methods
	// ****************************************************************************************
    @Override
    public Thread newThread( Runnable runnable )
    {
        Thread thread = new Thread( group, runnable, namePrefix + threadNumber.getAndIncrement(), 0 );
        if( thread.isDaemon() )
        {
            thread.setDaemon( false );
        }
        
        if( thread.getPriority() != Thread.NORM_PRIORITY )
        {
            thread.setPriority( Thread.NORM_PRIORITY );
        }
        
        thread.setUncaughtExceptionHandler( uncaughtExceptionHandler );
        
        return thread;
    }

	// ****************************************************************************************
	// Getters And Setters Methods
	// ****************************************************************************************

	// ****************************************************************************************
	// Patterns
	// ****************************************************************************************
}