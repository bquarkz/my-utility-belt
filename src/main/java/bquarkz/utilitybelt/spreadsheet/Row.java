/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.spreadsheet;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Row implements Iterable< String >
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************
    private List< String > row;
    
    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
    public Row( List< String > row )
    {
        this.row = row;
    }
    
    public Row( String ...row )
    {
        this.row = Arrays.asList( row );
    }

    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    @Override
    public Iterator< String > iterator()
    {
        return row.iterator();
    }
    
    public String getColumn( HeaderColumn columnIndex )
    {
        return row.get( columnIndex.getIndex() );
    }
    
    String getColumn( int index )
    {
        return row.get( index );
    }

    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************
    public int size()
    {
        return row.size();
    }

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        for( String s : row )
        {
            sb.append( s ).append( "; " );
        }
        
        return sb.toString();
    }
}
