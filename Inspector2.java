import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Inspector2 extends Inspector
{
    Random rand;
    public boolean created =false;

    public Inspector2(List<Buffer> buffers, List<Component> components)
    {
        this.buffers = new ArrayList<Buffer>(buffers);
        this.components = new ArrayList<Component>(components);
        this.timeLeft = 0;
        this.state =State.IDLE;
        this.rand = new Random();
    }
    public double bootrap()
    {
        // read from file or generate statistically
        timeLeft = 100;
        state = State.WORKING;
        this.currentComponent= components.get(rand.nextInt(2));
        this.created = true;
        return timeLeft;        
    }
    public double work()
    {
        //case of still working, not finished
        // if( timeLeft != 0 && --timeLeft > 0)
        // {
        //     state = State.WORKING;
        //     System.out.println("should never happen!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //     return null;
        // }

        // initial case
        // if(state == State.IDLE)
        // {
        //     // read from file or generate statistically
        //     timeLeft = 100;
        //     this.currentComponent= components.get(rand.nextInt(2));
        //     this.state = State.WORKING;
        //     return timeLeft;
        // }
        
        //System.out.println("ins2"+this.buffers.get(0).getSize()+"|"+this.buffers.get(1).getSize());


        if(this.created)
        {
            System.out.println("inspected"+currentComponent.getName());
            this.created = false;
        }
       //case of finished or blocked for C2
       if(currentComponent.getName().equalsIgnoreCase("C2") && buffers.get(0).getSize() < 2)
       {
            this.buffers.get(0).addComponent();
            System.out.println("adding C2");
            this.state = State.WORKING;
            // read from file or generate statistically
            this.timeLeft = 100;
            this.currentComponent= components.get(rand.nextInt(2));
            this.created = true;
       }
        //case of finished or blocked for C3
        else if(currentComponent.getName().equalsIgnoreCase("C3") && buffers.get(1).getSize() < 2)
        {
            System.out.println("adding C3");
            this.buffers.get(1).addComponent();
            this.state = State.WORKING;
            // read from file or generate statistically
            this.timeLeft = 100;
            this.currentComponent= components.get(rand.nextInt(2));
            this.created = true;
        }

        // return 0 means I'm blocked!
        return timeLeft;
        
    }


}