/**
 * Copyright (C) 2017 - Xetec - Flexmaint
 *
 * @author "Nilton Constantino" <nilton@xetec.com>
 *
 * Everything about the respective software copyright can be found in the
 * "LICENSE" file included in the project source tree.
 *
 */
package bquarkz.utilitybelt.stringlab;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class StringLabTest
{
    public static final String[] validFormatsForEmail = new String[] {
            "me@xetec.com",
            "me@xetec.com.ie",
            "me.me@xetec.com",
            "me+me@xetec.com",
            "me-me@xetec.com",
            "me.123@xetec.com",
            "me+123@xetec.com",
            "me-123@xetec.com",
            "me@123xetec.com",
            "me@123.xetec.com",
            "me@123-xetec.com",
    };
    
    public static final String[] invalidFormatsForEmail = new String[] {
            "me@123+xetec.com",
            "me@.xetec.com",
            "me-123@..com",
            "me-123@.com",
            "me-123@.com.com",
            "me@%*.com",
            "me..2017@xetec.com",
            "me.@xetec.com",
            "me@me@xetec.com",
            "me.me@xetec.com.1a",
    };

    @Test
    public void testEmailValidFormats()
    {
        List< String > emailList = Arrays.asList( validFormatsForEmail );
        for( String s : emailList )
        {
            boolean result = StringLab.isValidEmailFormat( s );
            Assert.assertTrue( result );
        }

    }
    
    @Test
    public void testEmailNotValidFormats()
    {
        List< String > emailList = Arrays.asList( invalidFormatsForEmail );
        for( String s : emailList )
        {
            boolean result = StringLab.isValidEmailFormat( s );
            Assert.assertFalse( result );
        }
    }
    
    @Test
    public void testGetCurlyBraceContent()
    {
        Optional< String > c1 = StringLab.getCurlyBraceContent( "  {Content} " );
        Assert.assertTrue( c1.get().equals( "Content" ) );
    }
    
    @Test( expected=NoSuchElementException.class )
    public void testGetCurlyBraceContentThrowExceptionWhenMessageIsNull()
    {
        StringLab.getCurlyBraceContent( null ).get();
    }
    
    @Test( expected=NoSuchElementException.class )
    public void testGetCurlyBraceContentThrowExceptionWhenMessageIsEmpty()
    {
        StringLab.getCurlyBraceContent( "" ).get();
    }
    
    @Test( expected=NoSuchElementException.class )
    public void testGetCurlyBraceContentThrowExceptionWhenMessageIsMalFormedAfter()
    {
        StringLab.getCurlyBraceContent( "{C" ).get();
    }
    
    @Test( expected=NoSuchElementException.class )
    public void testGetCurlyBraceContentThrowExceptionWhenMessageIsMalFormedBefore()
    {
        StringLab.getCurlyBraceContent( "C}" ).get();
    }

    @Test
    public void testIsNumber()
    {
        Assert.assertTrue( StringLab.isNumber( null ) );
        Assert.assertTrue( StringLab.isNumber( "" ) );
        Assert.assertTrue( StringLab.isNumber( "1223" ) );
        Assert.assertTrue( StringLab.isNumber( "1223,22" ) );
        Assert.assertTrue( StringLab.isNumber( "1223.22" ) );
        Assert.assertTrue( StringLab.isNumber( "1.223,22" ) );
        Assert.assertTrue( StringLab.isNumber( "1,223.22" ) );

        Assert.assertFalse( StringLab.isNumber( "1,,,,1...11" ) );
        Assert.assertFalse( StringLab.isNumber( "notNumericAtAll" ) );
        Assert.assertFalse( StringLab.isNumber( "a23" ) );
        Assert.assertFalse( StringLab.isNumber( "12a" ) );
        Assert.assertFalse( StringLab.isNumber( "1a3" ) );
        Assert.assertFalse( StringLab.isNumber( "1222,2a" ) );
        Assert.assertFalse( StringLab.isNumber( "1222!22" ) );
        Assert.assertFalse( StringLab.isNumber( "122a.22" ) );
        Assert.assertFalse( StringLab.isNumber( "122a,22" ) );
    }


}
