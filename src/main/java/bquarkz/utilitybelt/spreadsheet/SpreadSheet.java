/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.spreadsheet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpreadSheet
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************
    private Map< String, Sheet > spreadSheet;

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
    {
        spreadSheet = new HashMap<>();
    }
    
    public SpreadSheet( Set< String > names )
    {
        for( String name : names )
        {
            addSheetTo( name, null );
        }
    }
    
    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    public void addSheetTo( String name, Sheet sheet )
    {
        if( !spreadSheet.containsKey( name ) ) throw new SheetNotBeFoundExeception( name );
        
        spreadSheet.put( name, sheet );
    }
    
    public boolean contains( SpreadSheet spreadSheet )
    {
        return contains( spreadSheet.getSheetNames() );
    }
    
    public boolean contains( Iterable< String > names )
    {
        boolean containsAll = true;
        for( String name : names )
        {
            containsAll &= containsSheet( name );
        }
        
        return containsAll;
    }
    
    public Set< String > intersect( SpreadSheet spreadSheet )
    {
        return intersect( spreadSheet.getSheetNames() );        
    }
    
    public Set< String > intersect( Iterable< String > names )
    {
        Set< String > intersection = new HashSet<>();
        for( String name : names )
        {
            if( containsSheet( name ) )
            {
                intersection.add( name );
            }
        }
        
        return intersection;
    }
    
    public boolean containsSheet( String name )
    {
        return spreadSheet.containsKey( name );
    }
    
    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************
    public Iterable< String > getSheetNames()
    {
        return spreadSheet.keySet();
    }
    
    public int getNumberOfSheets()
    {
        return spreadSheet.size();
    }

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
}
