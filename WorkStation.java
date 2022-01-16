import java.util.ArrayList;
import java.util.List;

public class WorkStation
{
    public int id;
    public List<Buffer> buffers;
    public int timeLeft;
    public State state;

    public WorkStation(List<Buffer> buffers, int id)
    {
        this.buffers = new ArrayList<Buffer>(buffers);
        this.timeLeft = 1000;
        this.id = id;
        this.state =State.WORKING;
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
                timeLeft = 1000;
                state=State.WORKING;
                System.out.println("produced P" +id);
                return state;
            }
        }
        // if(buffers.size()> 1)
        //     System.out.println(id+" "+buffers.get(0).getSize()+" | "+ buffers.get(1).getSize());

        if(buffers.get(0).getSize() > 0 && buffers.get(1).getSize() > 0)
        {
            buffers.get(0).removeComponent();
            buffers.get(1).removeComponent(); 
            System.out.println("produced P" +id);
            // read from file or generate statistically
            timeLeft = 1000;
            state=State.WORKING;
            return state;
        }
        return State.BLOCKED;
            
    }
}