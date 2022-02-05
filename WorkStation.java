import java.util.ArrayList;
import java.util.List;

public class WorkStation
{
    public int id;
    public List<Buffer> buffers;
    public int timeLeft;
    public State state;
    public boolean created=false;

    public WorkStation(List<Buffer> buffers, int id)
    {
        this.buffers = new ArrayList<Buffer>(buffers);
        this.id = id;
        this.state =State.WORKING;
        
    }
    public double bootstrap()
    {
        // read from file or generate statistically
        this.timeLeft = -1;
        //case of w1
        if(buffers.size() == 1)
        {
            // System.out.println("produced p1");
            if(buffers.get(0).getSize() > 0)
            {
                buffers.get(0).removeComponent();
                // read from file or generate statistically
                timeLeft = 1000;
                state=State.WORKING;
            }
        //0 means idle
        }
        else if(buffers.get(0).getSize() > 0 && buffers.get(1).getSize() > 0)
        {
            buffers.get(0).removeComponent();
            buffers.get(1).removeComponent(); 
            // read from file or generate statistically
            timeLeft = 2000;
            state=State.WORKING;
        }
        if( this.timeLeft != -1)
            this.created=true;

        return timeLeft;        
    }

    public double produce()
    {
        timeLeft =0;
        //case of still working, not finished
        // if( timeLeft != 0 && --timeLeft > 0)
        // {
        //     System.out.println("should never happen!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //     return null;
        // }

        //case of w1
        if(buffers.size() == 1)
        {
            if(this.created)
            {
                created = false;
                System.out.println("produced p1");
            }
            // System.out.println("produced p1");
            if(buffers.get(0).getSize() > 0)
            {
                buffers.get(0).removeComponent();
                // read from file or generate statistically
                timeLeft = 1000;
                state=State.WORKING;
                this.created =true;
            }
            //0 means idle
            return timeLeft;  
        }
        // if(buffers.size()> 1)
        //     System.out.println(id+" "+buffers.get(0).getSize()+" | "+ buffers.get(1).getSize());

        if(this.created)
        {
            created = false;
            if(buffers.get(0).productID == 2)
                System.out.println("produced p2");
            else
                System.out.println("produced p3");
        }

        else if(buffers.get(0).getSize() > 0 && buffers.get(1).getSize() > 0)
        {
            
            created=true;
            buffers.get(0).removeComponent();
            buffers.get(1).removeComponent(); 
            // read from file or generate statistically
            timeLeft = 1000;
            state=State.WORKING;
        }

        return timeLeft;            
    }
}