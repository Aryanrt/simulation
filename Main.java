
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class Main
{
    public static double globalTime = 0;
    public static void main(String[] args) throws FileNotFoundException, IOException 
    {
        // File file = new File("")
        // try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        //     String line;
        //     while ((line = br.readLine()) != null) {
        //     // process the line.
        //     }
        //}

        Buffer b1 = new Buffer(1);
        Buffer b2 = new Buffer(1);
        Buffer b3 = new Buffer(2);
        Buffer b4 = new Buffer(1);
        Buffer b5 = new Buffer(3);
        Component c2 = new Component("c2");
        Component c3 = new Component("c3");
        ArrayList<Component> components = new ArrayList<Component>();
        components.add(c2);
        components.add(c3);

        ArrayList<Buffer> buffers1 = new ArrayList<Buffer>();
        buffers1.add(b1);
        buffers1.add(b2);
        buffers1.add(b4);
        Inspector1 ins1 = new Inspector1(buffers1);

        ArrayList<Buffer> buffers2 = new ArrayList<Buffer>();
        buffers2.clear();
        buffers2.add(b3);
        buffers2.add(b5);
        Inspector2 ins2 = new Inspector2(buffers2,components);

        ArrayList<Buffer> buffers3 = new ArrayList<Buffer>();
        buffers3.clear();
        buffers3.add(b1);
        WorkStation w1 = new WorkStation(buffers3,1);

        ArrayList<Buffer> buffers4 = new ArrayList<Buffer>();
        buffers4.clear();
        buffers4.add(b2);
        buffers4.add(b3);
        WorkStation w2 = new WorkStation(buffers4,2);

        ArrayList<Buffer> buffers5 = new ArrayList<Buffer>();
        buffers5.clear();
        buffers5.add(b4);
        buffers5.add(b5);
        WorkStation w3 = new WorkStation(buffers5,3);

        List<MyEvent> fel= new ArrayList<MyEvent>();
        
        double timeLeft1 = ins1.bootrap();
        double timeLeft2 = ins2.bootrap();
        double timeLeft3=-1, timeLeft4=-1, timeLeft5=-1;
        // System.out.println("Addining event for " + (globalTime + timeLeft1) + " and " + (globalTime + timeLeft2));
        fel.add(new MyEvent(1, globalTime + timeLeft1));
        fel.add(new MyEvent(2, globalTime + timeLeft2));
        List<MyEvent> toBeRemoved = new ArrayList<MyEvent>();
        List<MyEvent> toBeAdded = new ArrayList<MyEvent>();
        boolean flag1=false, flag2=false, flag3=false, flag4=false, flag5=false;
        while(true)
        {
            System.out.println( ins1.index +" "+ins2.index2  +" "+ins2.index3+" "+w1.index+" "+w2.index+" "+w3.index);
            if(ins1.index == 300 && ins2.index2 == 300 && ins2.index3 == 300 )
               // && w1.index == 300 && w2.index == 300 && w3.index == 300 )
            {
                System.out.println("all done");
                break;
            }    
            double min = 10000 + globalTime;
            for(MyEvent e: fel)
                min = Math.min(min, e.getTime());

            
            //update time
            globalTime = min;
            //System.out.println("global time is"+globalTime);

            toBeRemoved.clear();
            toBeAdded.clear();
            for(MyEvent e: fel)
            {
                if(e.getTime() != globalTime)
                    continue;

                switch (e.getType())
                {
                    case 1:
                        toBeRemoved.add(e);
                        timeLeft1 = ins1.work();
                        if( timeLeft1 == -2 )
                            flag1 = true;
                        if(timeLeft1 != 0)
                        {
                            // System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                            toBeAdded.add(new MyEvent(1, (globalTime + timeLeft1)));
                        }
                        break;

                    case 2:
                        toBeRemoved.add(e);
                        timeLeft2 = ins2.work();
                        // System.out.println("time2 "+timeLeft2);
                        if(timeLeft2 != 0)
                        {
                            // System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                            toBeAdded.add(new MyEvent(2, (globalTime + timeLeft2)));
                        }
                        break;

                    case 3:
                        toBeRemoved.add(e);
                        timeLeft3 = w1.produce();
                        if(timeLeft3 != 0)
                        {
                            // System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                            toBeAdded.add(new MyEvent(3, (globalTime + timeLeft3)));
                        }
                        break;
                        
                    case 4:
                        toBeRemoved.add(e);
                        timeLeft4 = w2.produce();
                        if(timeLeft4 != 0)
                        {
                            // System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                            toBeAdded.add(new MyEvent(4, (globalTime + timeLeft4)));
                        }
                        break;

                    case 5:
                        toBeRemoved.add(e);
                        timeLeft5 = w3.produce();
                        if(timeLeft5 != 0)
                        {
                            // System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                            toBeAdded.add(new MyEvent(5, (globalTime + timeLeft5)));
                        }
                        break;
                }
            }
            for(MyEvent e : toBeRemoved)
                fel.remove(e);
            for(MyEvent e : toBeAdded)
                fel.add(e);
                
      
            if(timeLeft3 == 0 || timeLeft3 == -1)
            {
                if(timeLeft3 == -1)
                    timeLeft3 = w1.bootstrap();
                else
                    timeLeft3 = w1.produce();
                if(timeLeft3 != 0 && timeLeft3 != -1)
                {
                    // System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                    fel.add(new MyEvent(3, globalTime + timeLeft3));
                }
            }
            if(timeLeft4 == 0 || timeLeft4 == -1)
            {
                if(timeLeft4 == -1)
                timeLeft4 = w2.bootstrap();
                else
                timeLeft4 = w2.produce();
                if(timeLeft4 != 0 && timeLeft4 != -1)
                {
                    // System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                    // System.out.println("adding finish time for w2 "+ (globalTime + timeLeft4));
                    fel.add(new MyEvent(4, globalTime + timeLeft4));                    
                }
            }
            if(timeLeft5 == 0 || timeLeft5 == -1)
            {
                if(timeLeft5 == -1)
                    timeLeft5 = w3.bootstrap();
                else
                    timeLeft5 = w3.produce();
                if(timeLeft5 != 0 && timeLeft5 != -1)
                {
                    // System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                    // System.out.println("adding finish time for w3 "+ (globalTime + timeLeft5));
                    fel.add(new MyEvent(5, globalTime + timeLeft5));                    
                }
            }
            if(timeLeft1 == 0)
            {
                timeLeft1 = ins1.work();
                if(timeLeft1 != 0)
                {
             //       System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                    fel.add(new MyEvent(1, globalTime + timeLeft1));
                }
            }
            if(timeLeft2 == 0)
            {
                timeLeft2= ins2.work();
                if(timeLeft2 != 0)
                {
                 //   System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                    fel.add(new MyEvent(2, globalTime + timeLeft2));
                }
            }


            System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
            
        }
    }
}