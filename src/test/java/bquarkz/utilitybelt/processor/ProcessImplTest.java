package bquarkz.utilitybelt.processor;

import java.util.Random;

public class ProcessImplTest implements IProcess< Integer >
{
    // ****************************************************************************************
    // Const Fields
    // ****************************************************************************************
    private static final long NUMBER_FROM_HELL = -666L; // it smells trouble
    private static final Random random = new Random( System.currentTimeMillis() );
    
    // ****************************************************************************************
    // Common Fields
    // ****************************************************************************************
    private boolean before;
    private boolean after;
    private boolean cancel;
    private int result;
    private boolean troubleshooting;
    private long time;

    // ****************************************************************************************
    // Constructors
    // ****************************************************************************************
    public ProcessImplTest()
    {
        this( -1L );
    }
    
    public ProcessImplTest( long time )
    {
        this.time = time;
    }

    // ****************************************************************************************
    // Methods
    // ****************************************************************************************
    @Override
    public boolean performProcessTroubleshooting(
            Troubleshooting troubleshooting
            )
    {
        this.troubleshooting = true;
        
        return true; // trouble solved
    }

    @Override
    public Integer call() throws Exception
    {
        result = random.nextInt();
        if( time == NUMBER_FROM_HELL )
        {
            throw new TroubleshootingException( this );
        }
        
        if( time > 0 ) Thread.sleep( time );
        return result;
    }

    @Override
    public void beforeProcessExecution()
    {
        before = true;
    }

    @Override
    public void afterProcessExecution()
    {
        after = true;
    }
    
    @Override
    public void cancelExecutionCallback()
    {
        cancel = true;
    }

    // ****************************************************************************************
    // Getters And Setters Methods
    // ****************************************************************************************
    void putIntoTrouble()
    {
        time = NUMBER_FROM_HELL;
    }
    
    boolean isInTrouble()
    {
        return time == NUMBER_FROM_HELL;
    }
    
    boolean isTroubleshootingPerfomed()
    {
        return troubleshooting;
    }

    boolean isBeforeProcessExecuted()
    {
        return before;
    }

    boolean isAfterProcessExecuted()
    {
        return after;
    }
    
    boolean isCancelProcessExecuted()
    {
        return cancel;
    }

    int getResult()
    {
        return result;
    }

    // ****************************************************************************************
    // Patterns
    // ****************************************************************************************
}
