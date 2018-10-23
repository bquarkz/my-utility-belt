/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.stringlab;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringLab
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************
    // Pattern RFC5322: https://www.ietf.org/rfc/rfc5322.txt
    // regex source: http://howtodoinjava.com/regex/java-regex-validate-email-address/
    // https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
    private static final String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String numberRegex = "[0-9]+[.,]?[0-9]*[.,]?[0-9]*";

    private static final Pattern emailPattern = Pattern.compile( emailRegex );
    private static final Pattern numberPattern = Pattern.compile( numberRegex );

    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************

    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    public static boolean isEmpty( String string )
    {
        if( string == null ) return true;
        if( string.isEmpty() ) return true;
        if( string.trim().isEmpty() ) return true;

        return false;
    }
    
    public static boolean isNotEmpty( String string )
    {
        return !isEmpty( string );
    }

    public static boolean isBounded( String string, int min, int max )
    {
        // if string is null she didn't reach the min or max size and by default it is bounded
        if( string == null ) return true;
        
        int length = string.length();
        
        if( length < min ) return false;
        if( length > max ) return false;
        
        return true;
    }
    
    public static boolean isNotBounded( String string, int min, int max )
    {
        return !isBounded( string, min, max );
    }

    public static boolean isNotBounded( String string, int max )
    {
        return !isBounded( string, max );
    }
    
    public static boolean isBounded( String string, int max )
    {
        return isBounded( string, 0, max );
    }
    
    public static boolean isValidEmailFormat( String email )
    {
        if( StringLab.isEmpty( email ) ) return true;
        
        Matcher matcher = emailPattern.matcher( email );
        return matcher.matches();
    }
    
    public static boolean isNotValidEmailFormat( String email )
    {
        return !isValidEmailFormat( email );
    }
    
    public static Optional< String > getCurlyBraceContent( String message )
    {
        if( isEmpty( message ) ) return Optional.empty();
        
        String rawMessage = message.trim();
        if( !rawMessage.startsWith( "{" ) || !rawMessage.endsWith( "}" ) ) return Optional.empty();
        return Optional.of( rawMessage.substring( 1, rawMessage.length() - 1 ) );        
    }
    
    public static Optional< String > wrapInCurlyBrace( String message )
    {
        if( isEmpty( message ) ) return Optional.empty();
        String rawMessage = message.trim();
        return Optional.of( new StringBuilder( "{" ).append( rawMessage ).append( "}" ).toString() );        
    }

    public static boolean match( String pattern, Set< String > variables )
    {
        final int patternHashCode = pattern.hashCode();
        for( var v : variables )
        {
            final int variableHashCode = v.hashCode();
            if( variableHashCode == patternHashCode ) return true;
        }
        
        return false;
    }

    public static boolean isNumber( String string )
    {
        if( isEmpty( string ) ) return true;

        Matcher matcher = numberPattern.matcher( string );
        return matcher.matches();
    }

    public static String trim( String cellContent )
    {
        if( cellContent == null ) return null;
        return cellContent.trim();
    }

    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
}
