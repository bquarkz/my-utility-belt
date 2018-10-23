/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.processor;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface IProcess< T > extends Callable< T >
{
    // ****************************************************************************************
    // Constants
    // ****************************************************************************************

    // ****************************************************************************************
    // Static
    // ****************************************************************************************
    static < T > IProcess< T > wrap( Callable<T> callable )
    {
        return callable::call;
    }

    // ****************************************************************************************
    // Default Implementations
    // ****************************************************************************************
    default void beforeProcessExecution()
    {
    }
    
    default void afterProcessExecution()
    {
    }
    
    default void cancelExecutionCallback()
    {
    }

    default boolean performProcessTroubleshooting( Troubleshooting troubleshooting )
    {
        return false;
    }

    // ****************************************************************************************
    // Contracts
    // ****************************************************************************************
}
