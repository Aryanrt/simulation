import java.util.ArrayList;
import java.util.List;

public class Inspector1 extends Inspector
{
    public Inspector1(List<Buffer> buffers)
    {
        this.buffers = new ArrayList<Buffer>(buffers);
        this.timeLeft = 0;
        this.state =State.IDLE;
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
        //System.out.println("ins1"+this.buffers.get(0).getSize()+"|"+this.buffers.get(1).getSize()+"|"+this.buffers.get(2).getSize());
        for(i=0; i < 2;i++)
        {
            if(this.buffers.get(0).getSize() == i )
            {
                this.buffers.get(0).addComponent();
                break;
            }
            else if(this.buffers.get(1).getSize() == i )
            {
                this.buffers.get(1).addComponent();
                break;
            }
            else if(this.buffers.get(2).getSize() == i )
            {
                this.buffers.get(2).addComponent();
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
            System.out.println("adding C1");
            state = State.WORKING;
            // read from file or generate statistically
            timeLeft = 100;
        }
        return state;

        
    }


}