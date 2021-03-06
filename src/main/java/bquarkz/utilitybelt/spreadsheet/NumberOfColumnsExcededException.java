/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.spreadsheet;

public class NumberOfColumnsExcededException extends RuntimeException
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************
    private static final long serialVersionUID = 3343809954103830301L;

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
    public NumberOfColumnsExcededException( int numberOfColumns, int length )
    {
        super( "Number Of Columns Exceded: max size: " + numberOfColumns + "; actual row size: " + length );
    }

    // ****************************************************************************************
    // Methods
    // ****************************************************************************************

    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
}
