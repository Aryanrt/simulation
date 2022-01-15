import java.util.ArrayList;
import java.util.List;

public class WorkStation
{
    enum State
    {
        IDLE,
        BLOCKED,
        WORKING;
    }
    public String id;
    public List<Buffer> buffers;
    public int timeLeft;
    public State state;

    public WorkStation(List<Buffer> buffers)
    {
        buffers = new ArrayList<Buffer>(buffers);
        timeLeft = 0;
        state =State.IDLE;
    }
    public void produce()
    {
        //case of still working, not finished
        if( timeLeft != 0 && --timeLeft > 0)
            return;

        //case of w1
        if(buffers.size() == 1)
        {
            if(buffers.get(0).getSize() > 0)
            {
                buffers.get(0).removeComponent();
                // read from file or generate statistically
                timeLeft = 100;
                state=State.WORKING;
                return;
            }
        }
    
        if(buffers.get(0).getSize() > 0 && buffers.get(1).getSize() > 0)
        {
            buffers.get(0).removeComponent();
            buffers.get(1).removeComponent(); 
            // read from file or generate statistically
            timeLeft = 100;
            state=State.WORKING;
        }
            
    }
}