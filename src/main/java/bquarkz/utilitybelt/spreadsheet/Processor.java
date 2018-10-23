/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.spreadsheet;

public interface Processor< T >
{
    T process( Row row ) throws ProcessCanNotBeDone;
    Row revert( T entity ) throws RevertCanNotBeDone;
}
