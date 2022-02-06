import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Inspector1 extends Inspector
{
    public boolean created =false;
    List<Double> serviceTime;
    public int index=0;
    public Inspector1(List<Buffer> buffers) throws FileNotFoundException, IOException
    {
        this.serviceTime = new ArrayList<Double>();

        try (BufferedReader br = new BufferedReader(new FileReader(new File("servinsp1.dat")))) 
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
        this.timeLeft = 0;
        this.state =State.IDLE;
    }
    public double bootrap()
    {
        // read from file or generate statistically
        timeLeft = this.serviceTime.get(index++);
        state = State.WORKING;
        this.created = true;
        System.out.println(Main.globalTime+": ins1 starting to inspect c1");
        return timeLeft;        
    }
    public double work()
    {

        if(created)
        {
            System.out.println(Main.globalTime+": ins1 inspected c1");
            created = false;
        }
        int i,j=-1;
        //System.out.println("ins1"+this.buffers.get(0).getSize()+"|"+this.buffers.get(1).getSize()+"|"+this.buffers.get(2).getSize());
        for(i=0; i < 2;i++)
        {
            if(this.buffers.get(0).getSize() == i )
            {
                this.buffers.get(0).addComponent();
                j = 1;
                break;
            }
            else if(this.buffers.get(1).getSize() == i )
            {
                this.buffers.get(1).addComponent();
                j = 2;
                break;
            }
            else if(this.buffers.get(2).getSize() == i )
            {
                this.buffers.get(2).addComponent();
                j = 4;
                break;
            }
        }

        //if all buffers full
        if(i == 2)
        {
            state=State.BLOCKED;
            timeLeft=0;
        }
        // case of component placed in queue
        else    
        {
            System.out.println(Main.globalTime+": ins1 adding C1 to queue " + j);
            if(index == 300)
                return -2; 
            System.out.println(Main.globalTime+": ins1 starting to inspect c1");
            created = true;
            state = State.WORKING;
            
            // read from file or generate statistically
            timeLeft = this.serviceTime.get(index++);
        }

        //return 0 means blocked 
        return timeLeft;

        
    }


}