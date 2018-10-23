/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.spreadsheet;

import java.util.*;

public class Sheet implements ISheet
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************
    private static final int DEFAULT_NUMBER_OF_ROWS = 100;

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************
    private int numberOfColumns;
    
    List< Row > rows;
    List< Column > columns;

    private Map< String, Integer > header;

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
    public Sheet( HeaderColumn ...header )
    {
        this( header.length );
        this.header = new HashMap<>();
        for( int i = 0; i < header.length; i++ )
        {
            this.header.put( header[ i ].getName(), header[ i ].getIndex() );            
        }
    }
    
    public Sheet( String ...header )
    {
        this( header.length );
        this.header = new HashMap<>();
        for( int i = 0; i < header.length; i++ )
        {
            this.header.put( header[ i ], i );            
        }
    }
    
    Sheet( int numberOfColumns )
    {
        this( numberOfColumns, DEFAULT_NUMBER_OF_ROWS );
    }
    
    Sheet( int numberOfColumns, int numberOfRows )
    {
        this.numberOfColumns = numberOfColumns;
        this.rows = new ArrayList<>( numberOfRows );
        this.columns = new ArrayList<>( numberOfColumns );
        for( int i = 0; i < numberOfColumns; i++ )
        {
            columns.add( new Column( i, numberOfRows ) );
        }
    }
    
    public Sheet( List< String > header )
    {
        this( header.size() );
        this.header = new HashMap<>();
        for( int i = 0; i < header.size(); i++ )
        {
            this.header.put( header.get( i ), i );            
        }
    }

    public Sheet( Row header )
    {
        this( header.size() );
        this.header = new HashMap<>();
        for( int i = 0; i < header.size(); i++ )
        {
            this.header.put( header.getColumn( i ), i );            
        }
    }

    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    private final void _addRow( Row row )
    {
        rows.add( row );
        for( Column column : columns )
        {
            column.fitRowIntoTheColumn( row );
        }
    }
    
    public void addRow( Row row )
    {
        if( row.size() > numberOfColumns ) throw new NumberOfColumnsExcededException( numberOfColumns, row.size() );
        _addRow( row );
    }
    
    public void addRow( String ...row )
    {
        if( row.length > numberOfColumns ) throw new NumberOfColumnsExcededException( numberOfColumns, row.length );
        Row rowToAdd = new Row( row );
        _addRow( rowToAdd );
    }

    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************
    @Override
    public int getNumberOfColumns()
    {
        return numberOfColumns;
    }
    
    @Override
    public int getNumberOfRows()
    {
        return rows.size();
    }
    
    @Override
    public Iterator< Row > iterator()
    {
        return rows.iterator();
    }
    
    @Override
    public Column getColumn( int index )
    {
        if( index > columns.size() || index < 0 ) return null;
        return columns.get( index );
    }
    
    @Override
    public Row getRow( int index )
    {
        if( index > rows.size() || index < 0 ) return null;
        return rows.get( index );
    }
    
    @Override
    public Column getColumn( HeaderColumn header )
    {
        return getColumn( header.getIndex() );
    }
    
    @Override
    public Column getColumn( String header )
    {
        Integer index = this.header.get( header );
        if( index == null ) return null;
        
        return getColumn( index.intValue() );
    }
    
    @Override
    public String[] getHeader()
    {
        String[] header = new String[ this.header.keySet().size() ];
        
        for( String h : this.header.keySet() )
        {
            Integer index = this.header.get( h );
            header[ index.intValue() ] = h;
        }
        
        return header;
    }
    
    @Override
    public boolean compareHeader( String... outHeader )
    {
        String[] inHeader = this.getHeader();
        
        if( inHeader.length != outHeader.length ) return false;
        
        for( int i = 0; i < inHeader.length; i++ )
        {
            if( !inHeader[ i ].equalsIgnoreCase( outHeader[ i ] ) )
            {
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        for( String h : header.keySet() )
        {
            result = prime * result + ( ( h == null ) ? 0 : h.hashCode() );
        }
        
        return result;
    }
    
    @Override
    public boolean compareHeader( ISheet sheet )
    {
        return compareHeader( sheet.getHeader() );
    }

    @Override
    public boolean compareHeader( HeaderColumn... outHeader )
    {
        String[] inHeader = this.getHeader();
        
        if( inHeader.length != outHeader.length ) return false;
        
        for( int i = 0; i < inHeader.length; i++ )
        {
            if( !inHeader[ i ].equalsIgnoreCase( outHeader[ i ].getName() ) )
            {
                return false;
            }
        }
        
        return true;
    }


    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        for( String s : getHeader() )
        {
            sb.append( s ).append( "; " );
        }
        
        for( Row row : this )
        {
            sb.append( "\n" ).append( row );
        }
        
        return sb.toString();
    }
}
