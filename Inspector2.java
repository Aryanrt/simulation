import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Inspector2 extends Inspector
{
    Random rand;

    public Inspector2(List<Buffer> buffers, List<Component> components)
    {
        this.buffers = new ArrayList<Buffer>(buffers);
        this.components = new ArrayList<Component>(components);
        this.timeLeft = 0;
        this.state =State.IDLE;
        this.rand = new Random();
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
            this.currentComponent= components.get(rand.nextInt(2));
            this.state = State.WORKING;
            return state;
        }
        
        //System.out.println("ins2"+this.buffers.get(0).getSize()+"|"+this.buffers.get(1).getSize());
       //case of finished or blocked for C2
       if(currentComponent.getName().equalsIgnoreCase("C2") && buffers.get(0).getSize() < 2)
       {
           System.out.println("adding C2");
            this.buffers.get(0).addComponent();
            this.state = State.WORKING;
            // read from file or generate statistically
            this.timeLeft = 100;
            this.currentComponent= components.get(rand.nextInt(2));
            return state;
       }
        //case of finished or blocked for C3
        if(currentComponent.getName().equalsIgnoreCase("C3") && buffers.get(1).getSize() < 2)
        {
            System.out.println("adding C3");
            this.buffers.get(1).addComponent();
            this.state = State.WORKING;
            // read from file or generate statistically
            this.timeLeft = 100;
            this.currentComponent= components.get(rand.nextInt(2));
            return state;
        }

        return State.BLOCKED;
        
    }


}