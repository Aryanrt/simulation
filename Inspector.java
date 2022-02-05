import java.util.List;

abstract class Inspector
{

    protected String id;
    protected List<Buffer> buffers;
    protected List<Component> components;
    protected Component currentComponent;
    protected double timeLeft;
    protected State state;
  
    
}