import java.util.List;

abstract class Inspector
{
    enum State
    {
        IDLE,
        BLOCKED,
        WORKING;
    }

    protected String id;
    protected List<Buffer> buffers;
    protected List<Component> components;
    protected Component curreComponent;
    protected Component currentComponent;
    protected int timeLeft;
    protected State state;
  
    
}