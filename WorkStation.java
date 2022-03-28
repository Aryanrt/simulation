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
    Generator generator;

    public WorkStation(List<Buffer> buffers, int id) throws FileNotFoundException, IOException
    {
        if( id == 1)
            generator = new Generator(0.22);
        else if( id == 2)
            generator = new Generator(0.09);
        else
            generator = new Generator(0.11);
        // this.serviceTime = new ArrayList<Double>();
        // try (BufferedReader br = new BufferedReader(new FileReader(new File("ws"+id+".dat")))) 
        // {
        //     String line;
        //     int i=0;
        //     while ((line = br.readLine()) != null) 
        //     {
        //         if (i++ == 300)
        //             break;
        //         this.serviceTime.add(Double.parseDouble(line));
        //     }
        // }

        this.buffers = buffers;
        this.id = id;
        this.state =State.WORKING;
        
    }
    public double bootstrap() throws IOException
    {
        // read from file or generate statistically
        this.timeLeft = -1;
        //case of w1
        if(buffers.size() == 1)
        {
            // Main.log("produced p1");
            if(buffers.get(0).getSize() > 0)
            {
                buffers.get(0).removeComponent();
                // read from file or generate statistically
                //this.timeLeft = this.serviceTime.get(index++);
                index++;
                this.timeLeft = this.generator.next();
                state=State.WORKING;
               // Main.log(Main.df.format(Main.globalTime)+": W1 starting");
            }
        //0 means idle
        }
        else if(buffers.get(0).getSize() > 0 && buffers.get(1).getSize() > 0)
        {
            // if(buffers.get(1).productID == 2)
            //     Main.log(Main.df.format(Main.globalTime)+": W2 starting");
            // else
            //     Main.log(Main.df.format(Main.globalTime)+": W3 starting");
            
            buffers.get(0).removeComponent();
            buffers.get(1).removeComponent(); 
            // read from file or generate statistically
            //this.timeLeft = this.serviceTime.get(index++);
            index++;
            this.timeLeft = this.generator.next();
            state=State.WORKING;
        }
        if( this.timeLeft != -1)
            this.created=true;

        return timeLeft;        
    }

    public double produce() throws IOException
    {
        timeLeft =0;

        //case of w1
        if(buffers.size() == 1)
        {
            if(this.created)
            {
                created = false;
                Main.log(Main.df.format(Main.globalTime)+": w1 produced "+ (index+1)+"th p1");
            }
            // Main.log("produced p1");
            if(buffers.get(0).getSize() > 0)
            {
                // if(index == 300)
                //     return -2;

                Main.log(Main.df.format(Main.globalTime)+": w1 starting");
                buffers.get(0).removeComponent();
                
                // read from file or generate statistically
                //this.timeLeft = this.serviceTime.get(this.index++);
                index++;
                this.timeLeft = this.generator.next();
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
                Main.log(Main.df.format(Main.globalTime)+": w2 produced "+ (index+1)+"th p2");
            else
                Main.log(Main.df.format(Main.globalTime)+": w3 produced "+ (index+1)+"th p3");
        }

        else if(buffers.get(0).getSize() > 0 && buffers.get(1).getSize() > 0)
        {
            if(buffers.get(1).productID == 2)
                Main.log(Main.df.format(Main.globalTime)+": w2 starting");
            else
                Main.log(Main.df.format(Main.globalTime)+": w3 starting");

            created=true;
            buffers.get(0).removeComponent();
            buffers.get(1).removeComponent(); 
            // read from file or generate statistically
            //this.timeLeft = this.serviceTime.get(index++);
            index++;
            this.timeLeft = this.generator.next();
            state=State.WORKING;
        }

        return timeLeft;            
    }
}