/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.processor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FutureAndResult< T >
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************
    private final Future< T > future;
    private final IProcess< T > process;
    private T result;

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
    protected FutureAndResult( IProcess< T > process, Future< T > future )
    {
        this.process = process;
        this.future = future;
        this.result = null;
    }

    // ****************************************************************************************
    // Factories
    // ****************************************************************************************

    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************
    protected Future< T > getFuture()
    {
        return future;
    }

    protected T getResult()
    {
        return result;
    }
    
    protected IProcess< T > getProcess()
    {
        return process;
    }
    
    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    protected boolean cancel()
    {
        return future.cancel( true );
    }
    
    protected boolean hasResult() throws ExecutionException
    {
        if( future.isCancelled() ) return false;
        
        try
        {
            result = future.get();
        } catch ( InterruptedException e ) {
            return false;
        }
        return true;
    }

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************

}
