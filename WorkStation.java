import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkStation
{
    public int id;
    public List<Buffer> buffers;
    public double timeLeft;
    public State state;
    public boolean created=false;
    List<Double> serviceTime;
    public int index =0;

    public WorkStation(List<Buffer> buffers, int id) throws FileNotFoundException, IOException
    {
        this.serviceTime = new ArrayList<Double>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("ws"+id+".dat")))) 
        {
            String line;
            int i=0;
            while ((line = br.readLine()) != null) 
            {
                if (i++ == 300)
                    break;
                this.serviceTime.add(Double.parseDouble(line));
            }
        }

        this.buffers = buffers;
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
                this.timeLeft = this.serviceTime.get(index++);
                state=State.WORKING;
                System.out.println(Main.globalTime+": W1 starting");
            }
        //0 means idle
        }
        else if(buffers.get(0).getSize() > 0 && buffers.get(1).getSize() > 0)
        {
            if(buffers.get(1).productID == 2)
                System.out.println(Main.globalTime+": W2 starting");
            else
                System.out.println(Main.globalTime+": W3 starting");
            
            buffers.get(0).removeComponent();
            buffers.get(1).removeComponent(); 
            // read from file or generate statistically
            this.timeLeft = this.serviceTime.get(index++);
            state=State.WORKING;
        }
        if( this.timeLeft != -1)
            this.created=true;

        return timeLeft;        
    }

    public double produce()
    {
        timeLeft =0;

        //case of w1
        if(buffers.size() == 1)
        {
            if(this.created)
            {
                created = false;
                System.out.println(Main.globalTime+": w1 produced p1");
            }
            // System.out.println("produced p1");
            if(buffers.get(0).getSize() > 0)
            {
                if(index == 300)
                    return -2;

                System.out.println(Main.globalTime+": w1 starting");
                buffers.get(0).removeComponent();
                
                // read from file or generate statistically
                this.timeLeft = this.serviceTime.get(this.index++);
                state=State.WORKING;
                this.created =true;
            }
            //0 means idle
            return timeLeft;  
        }

        if(this.created)
        {
            created = false;
            if(buffers.get(1).productID == 2)
                System.out.println(Main.globalTime+": w2 produced p2");
            else
                System.out.println(Main.globalTime+": w3 produced p3");
        }

        else if(buffers.get(0).getSize() > 0 && buffers.get(1).getSize() > 0)
        {
            if(buffers.get(1).productID == 2)
                System.out.println(Main.globalTime+": w2 starting");
            else
                System.out.println(Main.globalTime+": w3 starting");

            created=true;
            buffers.get(0).removeComponent();
            buffers.get(1).removeComponent(); 
            // read from file or generate statistically
            this.timeLeft = this.serviceTime.get(index++);
            state=State.WORKING;
        }

        return timeLeft;            
    }
}