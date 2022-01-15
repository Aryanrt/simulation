import java.util.List;

abstract class Inspector
{

    protected String id;
    protected List<Buffer> buffers;
    protected List<Component> components;
    protected Component curreComponent;
    protected Component currentComponent;
    protected int timeLeft;
    protected State state;
  
    
}