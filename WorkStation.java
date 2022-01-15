import java.util.ArrayList;
import java.util.List;

public class WorkStation
{
    public String id;
    public List<Buffer> buffers;
    public int timeLeft;
    public State state;

    public WorkStation(List<Buffer> buffers)
    {
        buffers = new ArrayList<Buffer>(buffers);
        timeLeft = 100;
        state =State.WORKING;
    }
    public State produce()
    {
        //case of still working, not finished
        if( timeLeft != 0 && --timeLeft > 0)
            return State.WORKING;

        //case of w1
        if(buffers.size() == 1)
        {
            if(buffers.get(0).getSize() > 0)
            {
                buffers.get(0).removeComponent();
                // read from file or generate statistically
                timeLeft = 100;
                state=State.WORKING;
                return state;
            }
        }
    
        if(buffers.get(0).getSize() > 0 && buffers.get(1).getSize() > 0)
        {
            buffers.get(0).removeComponent();
            buffers.get(1).removeComponent(); 
            // read from file or generate statistically
            timeLeft = 100;
            state=State.WORKING;
            return state;
        }
        return State.BLOCKED;
            
    }
}