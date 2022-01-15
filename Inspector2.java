import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Inspector2 extends Inspector
{
    Random rand;

    public Inspector2(List<Buffer> buffers, List<Component> components)
    {
        buffers = new ArrayList<Buffer>(buffers);
        components = new ArrayList<Component>(components);
        timeLeft = 0;
        state =State.IDLE;
        rand = new Random();
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
            currentComponent= components.get(rand.nextInt(2));
            state = State.WORKING;
            return;
        }
        
       //case of finished or blocked
       if(curreComponent.getName() == "C2" && buffers.get(0).getSize() < 2)
       {
            buffers.get(0).addComponent();
            state = State.WORKING;
            // read from file or generate statistically
            timeLeft = 100;
            return;
       }
        //case of finished or blocked
        if(curreComponent.getName() == "C3" && buffers.get(1).getSize() < 2)
        {
            buffers.get(0).addComponent();
            state = State.WORKING;
            // read from file or generate statistically
            timeLeft = 100;
            return;
        }
        
    }


}