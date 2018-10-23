/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.spreadsheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Column implements Iterable< String >
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************
    private List< String > column;
    private int index;

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
    public Column( int index, int numberOfRows )
    {
        this.index = index;
        this.column = new ArrayList<>( numberOfRows );
    }

    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    @Override
    public Iterator< String > iterator()
    {
        return column.iterator();
    }
    
    public void fitRowIntoTheColumn( Row row )
    {
        if( index < row.size() )
        {
            String value = row.getColumn( index );
            column.add( value );
        } else {
            column.add( "" );
        }
    }

    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        for( String s : column )
        {
            sb.append( s ).append( ";\n" );
        }
        
        return sb.toString();
    }
}
