import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Inspector2 extends Inspector
{
    Random rand;
    public boolean created =false;

    public Inspector2(List<Buffer> buffers, List<Component> components)
    {
        this.buffers = buffers;
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
        System.out.println(Main.globalTime+": ins2 starting to inspect "+ this.currentComponent.getName());
        this.created = true;
        return timeLeft;        
    }
    public double work()
    {
        this.timeLeft=0;
        if(this.created)
        {
            System.out.println(Main.globalTime+": ins2 inspected "+currentComponent.getName());
            this.created = false;
        }
       //case of finished or blocked for C2
       if(currentComponent.getName().equalsIgnoreCase("C2") && buffers.get(0).getSize() < 2)
       {
            this.buffers.get(0).addComponent();
            this.currentComponent= components.get(rand.nextInt(2));
            System.out.println(Main.globalTime+": ins2 adding C2 to queue 3");
            System.out.println(Main.globalTime+": ins2 starting to inspect "+ this.currentComponent.getName());
            this.state = State.WORKING;
            // read from file or generate statistically
            this.timeLeft = 100;
            
            this.created = true;
       }
        //case of finished or blocked for C3
        else if(currentComponent.getName().equalsIgnoreCase("C3") && buffers.get(1).getSize() < 2)
        {
            this.currentComponent= components.get(rand.nextInt(2));
            System.out.println(Main.globalTime+": ins2 adding C3 to queue 5");
            System.out.println(Main.globalTime+": ins2 starting to inspect "+ this.currentComponent.getName());
            this.buffers.get(1).addComponent();
            this.state = State.WORKING;
            // read from file or generate statistically
            this.timeLeft = 100;            
            this.created = true;
        }

        // return 0 means I'm blocked!
        return timeLeft;
        
    }


}