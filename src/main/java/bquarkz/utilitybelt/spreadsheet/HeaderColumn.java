/*
 * Copyleft (C) 2018
 * @author "Nilton Constantino" aka bQUARKz <bquarkz@gmail.com>
 */
package bquarkz.utilitybelt.spreadsheet;

public interface HeaderColumn
{
    static HeaderColumn wrap( final String name, final int index )
    {
        return new HeaderColumn()
        {
            @Override public String getName()
            {
                return name;
            }

            @Override public int getIndex()
            {
                return index;
            }
        };
    }

    /**
     *
     * @return
     */
    String getName();

    /**
     * The first index is 0;
     * @return
     */
    int getIndex();
}
