/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.spreadsheet;

import bquarkz.utilitybelt.stringlab.StringLab;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SpreadSheetUtil
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
 
    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    public static final Workbook convert( ISheet sheet )
    {
        var wb = new XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet wbSheet = wb.createSheet();
        int rowCounter = 0;

        {
            var wbRow = wbSheet.createRow( rowCounter++ );
            int columnCounter = 0;
            for( var header : sheet.getHeader() )
            {
                var cell = wbRow.createCell( columnCounter++ );
                cell.setCellValue( header );
            }
        }

        for( var row : sheet )
        {
            var wbRow = wbSheet.createRow( rowCounter++ );
            int columnCounter = 0;
            for( String content : row )
            {
                var cell = wbRow.createCell( columnCounter++ );
                cell.setCellValue( content );
            }
        }

        return wb;
    }

    public static final ISheet createStaticSheetFrom( Workbook wb, String sheetName )
    {
        final org.apache.poi.ss.usermodel.Sheet aSheet = tryToFindASheet( wb, sheetName );
        if( aSheet == null ) return null;

        Row header = createFlexRowFromApacheRow( aSheet.getRow( 0 ) );
        Sheet sheet = new Sheet( header );
        
        for( org.apache.poi.ss.usermodel.Row aRow : apacheRowIterable( aSheet ) )
        {
            Row row = createFlexRowFromApacheRow( aRow );
            sheet.addRow( row );
        }
        
        return sheet;
    }

    static org.apache.poi.ss.usermodel.Sheet tryToFindASheet( Workbook wb, String sheetName )
    {
        org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheet( sheetName );
        
        if( sheet == null )
        {
            for (int i = 0; i < wb.getNumberOfSheets(); i++) 
            {
                String aSheetName = wb.getSheetName( i );
                if( aSheetName.equalsIgnoreCase( sheetName ) )
                {
                    return wb.getSheet( aSheetName );
                }
            }
        }

        return sheet;
    }

    static Iterable< org.apache.poi.ss.usermodel.Row > apacheRowIterable( final org.apache.poi.ss.usermodel.Sheet sheet )
    {
        return () -> {
            Iterator< org.apache.poi.ss.usermodel.Row > iterator = sheet.rowIterator();
            if( iterator.hasNext() ) iterator.next(); // just to discard the header
            return iterator;
        };
    }
    
    private static final Row createFlexRowFromApacheRow( org.apache.poi.ss.usermodel.Row row )
    {
        List< String > cellList = new LinkedList<>();
        for( Cell cell : row )
        {
             cellList.add( getCellContent( cell ) );
        }
        
        return new Row( cellList );
    }
    
    private static String getCellContent( Cell cell )
    {
        if( cell == null ) return null;

        try {
            if( HSSFDateUtil.isCellDateFormatted( cell ) && cell.getDateCellValue() != null )
            {
                // try to convert into date time
                LocalDateTime dateTime = LocalDateTime.parse( cell.getDateCellValue().toString() );
                return dateTime.format( DateTimeFormatter.ISO_LOCAL_DATE_TIME );
            }
        } catch( Exception e ) {}

        String cellContent = null;
        switch( cell.getCellTypeEnum() )
        {
            case STRING: { cellContent = cell.getStringCellValue(); } break;
            case NUMERIC: { cellContent = formatNumber( cell.getNumericCellValue() ); } break;
            case BOOLEAN: { cellContent = ( cell.getBooleanCellValue() ? "1" : "0" ); } break;
            case FORMULA: { cellContent = cell.getCellFormula(); } break;
            default: return null;
        }
        
        return StringLab.trim( cellContent );
    }
    
    private static String formatNumber( double numberDouble )
    {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(0);
        decimalFormat.setMinimumFractionDigits(0);
        decimalFormat.setGroupingUsed( false );
        BigDecimal number = null;
        
        try {
            number = new BigDecimal( numberDouble );
            number = number.setScale( 0 );
        } catch (ArithmeticException ae) {
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setMinimumFractionDigits(0);
            number = new BigDecimal(numberDouble);
        }
        
        return decimalFormat.format( number );
    }


    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
}
