import java.util.ArrayList;
import java.util.List;

public class Inspector1 extends Inspector
{
    public Inspector1(List<Buffer> buffers, List<Component> components)
    {
        buffers = new ArrayList<Buffer>(buffers);
        components = new ArrayList<Component>(components);
        timeLeft = 0;
        state =State.IDLE;
    }
    public void work()
    {
        //case of still working, not finished
        if( timeLeft != 0 && --timeLeft > 0)
            return;

        // initial case
        if(state == State.IDLE)
        {
            // read from file or generate statistically
            timeLeft = 100;
            state = State.WORKING;
            return;
        }
        
        int i;
        for(i=0; i < 2;i++)
        {
            if(buffers.get(0).getSize() == i )
            {
                buffers.get(0).addComponent();
                break;
            }
            else if(buffers.get(0).getSize() == i )
            {
                buffers.get(0).addComponent();
                break;
            }
            else if(buffers.get(0).getSize() == i )
            {
                buffers.get(0).addComponent();
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

        
    }


}