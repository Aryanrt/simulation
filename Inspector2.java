import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Inspector2 extends Inspector
{
    Random rand;
    public boolean created =false;
    List<Double> serviceTime2;
    List<Double> serviceTime3;
    public int index2 =0;
    public int index3 =0;
    Generator generator2, generator3;

    public Inspector2(List<Buffer> buffers, List<Component> components) throws FileNotFoundException, IOException
    {
        generator2 = new Generator(0.06);
        generator3 = new Generator(0.05);
        // this.serviceTime2 = new ArrayList<Double>();
        // try (BufferedReader br = new BufferedReader(new FileReader(new File("servinsp22.dat")))) 
        // {
        //     String line;
        //     int i=0;
        //     while ((line = br.readLine()) != null) 
        //     {
        //         if (i++ == 300)
        //             break;
        //         this.serviceTime2.add(Double.parseDouble(line));
        //     }
        // }
        // this.serviceTime3 = new ArrayList<Double>();
        // try (BufferedReader br = new BufferedReader(new FileReader(new File("servinsp23.dat")))) 
        // {
        //     String line;
        //     int i=0;
        //     while ((line = br.readLine()) != null) 
        //     {
        //         if (i++ == 300)
        //             break;
        //         this.serviceTime3.add(Double.parseDouble(line));
        //     }
        // }

        this.buffers = buffers;
        this.components = new ArrayList<Component>(components);
        this.timeLeft = 0;
        this.state =State.IDLE;
        this.rand = new Random();
    }
    public double bootrap() throws IOException
    {
        // read from file or generate statistically
        
        state = State.WORKING;
        this.currentComponent= components.get(rand.nextInt(2));
        if(currentComponent.getName().equalsIgnoreCase("C2"))
        {
            //this.timeLeft = this.serviceTime2.get(index2++);
            index2++;
            this.timeLeft = this.generator2.next();
        } 
        else
        {
            //this.timeLeft = this.serviceTime3.get(index3++); 
            this.timeLeft = this.generator3.next(); 
            index3++;
        }

        Main.log(Main.df.format(Main.globalTime)+": ins2 starting to inspect "+ this.currentComponent.getName());
        this.created = true;
        return timeLeft;        
    }

    public double work() throws IOException
    {
        this.timeLeft=0;
        if(this.created)
        {
            Main.log(Main.df.format(Main.globalTime)+": ins2 inspected  "+((currentComponent.getName().equals("C2")?index2:index3)+1)+"th "+currentComponent.getName());
            this.created = false;
        }
       //case of finished or blocked for C2
       if(currentComponent.getName().equalsIgnoreCase("C2") && buffers.get(0).getSize() < 2)
       {
            this.buffers.get(0).addComponent();
            this.currentComponent= components.get(rand.nextInt(2));
            Main.log(Main.df.format(Main.globalTime)+": ins2 adding C2 to queue 3");
            
            if(index2 == 300)
                return -2;
            Main.log(Main.df.format(Main.globalTime)+": ins2 starting to inspect "+ this.currentComponent.getName());
            this.state = State.WORKING;
            // read from file or generate statistically
            //this.timeLeft = this.serviceTime2.get(index2++);
            index2++;
            this.timeLeft = this.generator2.next();
            
            this.created = true;
       }
        //case of finished or blocked for C3
        else if(currentComponent.getName().equalsIgnoreCase("C3") && buffers.get(1).getSize() < 2)
        {
            this.currentComponent= components.get(rand.nextInt(2));
            Main.log(Main.df.format(Main.globalTime)+": ins2 adding C3 to queue 5");
            if(index3 == 300)
                return -2;
            Main.log(Main.df.format(Main.globalTime)+": ins2 starting to inspect "+ this.currentComponent.getName());
            this.buffers.get(1).addComponent();
            this.state = State.WORKING;
            // read from file or generate statistically
            //this.timeLeft = this.serviceTime3.get(index3++);
            index3=0;
            this.timeLeft = this.generator3.next();
            this.created = true;
        }

        // return 0 means I'm blocked!
        return timeLeft;
        
    }

}