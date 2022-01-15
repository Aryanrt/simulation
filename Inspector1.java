import java.util.ArrayList;
import java.util.List;

public class Inspector1 extends Inspector
{
    public Inspector1(List<Buffer> buffers)
    {
        buffers = new ArrayList<Buffer>(buffers);
        timeLeft = 0;
        state =State.IDLE;
    }
    public State work()
    {
        //case of still working, not finished
        if( timeLeft != 0 && --timeLeft > 0)
        {
            state = State.WORKING;
            return state;
        }

        // initial case
        if(state == State.IDLE)
        {
            // read from file or generate statistically
            timeLeft = 100;
            state = State.WORKING;
            return state;
        }
        
        int i;
        for(i=0; i < 2;i++)
        {
            if(buffers.get(0).getSize() == i )
            {
                buffers.get(0).addComponent();
                break;
            }
            else if(buffers.get(1).getSize() == i )
            {
                buffers.get(1).addComponent();
                break;
            }
            else if(buffers.get(2).getSize() == i )
            {
                buffers.get(2).addComponent();
                break;
            }
        }

        //if all buffers full
        if(i == 2)
        {
            state=State.BLOCKED;
        }
        // case of cpmponent placed in queue
        else    
        {
            state = State.WORKING;
            // read from file or generate statistically
            timeLeft = 100;
        }
        return state;

        
    }


}