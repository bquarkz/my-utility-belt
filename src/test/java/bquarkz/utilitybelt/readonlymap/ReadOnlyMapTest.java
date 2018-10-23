package bquarkz.utilitybelt.readonlymap;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ReadOnlyMapTest
{
    @Test
    public void testMapWrapping()
    {
        Map< String, String > map = new HashMap<>();
        map.put( "1", "1" );
        map.put( "2", "2" );
        map.put( "3", "3" );
        ReadOnlyMap< String, String > romap = ReadOnlyMap.wrap( map );
        Assert.assertNotNull( romap );
        Assert.assertFalse( romap == map );
        Assert.assertTrue( romap.size() == map.size() );
        String romap1 = romap.get( "1" );
        String map1 = map.get( "1" );
        Assert.assertTrue( romap1 == map1 );
    }
    
    @Test
    public void testReadOnlyWithDefaultGet()
    {
        final String notIncludedValue = "is";
        
        Map< String, String > map = new HashMap<>();
        map.put( "1", "1" );
        map.put( "2", "2" );
        map.put( "3", "3" );
        
        ReadOnlyMap< String, String > romap = ReadOnlyMap.wrap( map, ( key ) -> { return key + "OK"; } );
        Assert.assertNotNull( romap );
        Assert.assertFalse( romap == map );
        Assert.assertTrue( romap.size() == map.size() );
        String romap1 = romap.get( "1" );
        String map1 = map.get( "1" );
        Assert.assertTrue( romap1 == map1 );
        
        String romap2 = romap.get( notIncludedValue );
        Assert.assertTrue( romap2.equals( notIncludedValue + "OK" ) );
    }
    
}
