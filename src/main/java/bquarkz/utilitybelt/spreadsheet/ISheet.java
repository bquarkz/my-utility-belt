/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.spreadsheet;

public interface ISheet extends Iterable< Row >
{
    int getNumberOfColumns();
    int getNumberOfRows();
    Column getColumn( int index );
    Row getRow( int index );
    Column getColumn( HeaderColumn header );
    Column getColumn( String header );
    String[] getHeader();
    boolean compareHeader( HeaderColumn... header );
    boolean compareHeader( String... outHeader );
    boolean compareHeader( ISheet sheet );
}