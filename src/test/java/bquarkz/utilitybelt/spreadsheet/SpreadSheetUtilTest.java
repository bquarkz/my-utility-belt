package bquarkz.utilitybelt.spreadsheet;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SpreadSheetUtilTest
{
    private static byte[] createByteArray()
    {
        String sheetName = "Sheet01";
    
        try( HSSFWorkbook wb = new HSSFWorkbook() )
        {
            HSSFSheet sheet = wb.createSheet(sheetName) ;
    
            {
                HSSFRow row = sheet.createRow( 0 );
                for( int c = 0; c < 5; c++ )
                {
                    HSSFCell cell = row.createCell( c );
                    cell.setCellValue( "header - c: " + c );
                }
            }
    
            {
                for( int r = 1; r < 6; r++ )
                {
                    HSSFRow row = sheet.createRow( r );
                    for( int c = 0; c < 5; c++ )
                    {
                        HSSFCell cell = row.createCell( c );
                        cell.setCellValue( "cell content - r: " + ( r - 1 ) + " c: " + c );
                    }
                }
            }

            try( ByteArrayOutputStream baos = new ByteArrayOutputStream() )
            {
                wb.write( baos );
                return baos.toByteArray();
            } catch( IOException ex ) {
                ex.printStackTrace();
            }
            
        } catch( IOException ex ) {
            ex.printStackTrace();
        }
    
        return new byte[] {};
    }
    
    private byte[] bytes = createByteArray();
    private Workbook wb;

    @Before
    public void before() throws IOException, InvalidFormatException, EncryptedDocumentException, org.apache.poi.openxml4j.exceptions.InvalidFormatException
    {
        wb = WorkbookFactory.create( new ByteArrayInputStream( bytes ) );
    }
    
    @Test
    public void shouldReturnNullToAnInvalidSheetName()
    {
        Sheet sheet = SpreadSheetUtil.tryToFindASheet( wb, "ALALALALa" );
        Assert.assertNull( sheet );
    }
    
    @Test
    public void testFindSheetOkWithExactName()
    {
        Sheet sheet = SpreadSheetUtil.tryToFindASheet( wb, "Sheet01" );
        Assert.assertNotNull( sheet );
    }
    
    @Test
    public void testFindSheetOkWithNameNotCaseSensitive()
    {
        Sheet sheet = SpreadSheetUtil.tryToFindASheet( wb, "shEEt01" );
        Assert.assertNotNull( sheet );
    }
    
    @Test
    public void testSheetContent()
    {
        ISheet sheet = SpreadSheetUtil.createStaticSheetFrom( wb, "Sheet01" );
        
        for( Row row : sheet )
        {
            for( String cell : row )
            {
                Assert.assertTrue( cell.startsWith( "cell content" ) );
            }
        }
    }
    
    @Test
    public void testSpecificSheetContent()
    {
        ISheet sheet = SpreadSheetUtil.createStaticSheetFrom( wb, "Sheet01" );
        
        int r = 3;
        int c = 3;
        
        Row row = sheet.getRow( r );
        String cell = row.getColumn( c );
        
        Assert.assertTrue( cell, cell.equals( "cell content - r: " + r + " c: " + c ) );
    }
}
