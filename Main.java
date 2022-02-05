
import java.util.ArrayList;
import java.util.List;


class Main
{

    public static void main(String[] args) 
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

        ArrayList<Buffer> buffers = new ArrayList<Buffer>();
        buffers.add(b1);
        buffers.add(b2);
        buffers.add(b4);
        Inspector1 ins1 = new Inspector1(buffers);
        buffers.clear();
        buffers.add(b3);
        buffers.add(b5);
        Inspector2 ins2 = new Inspector2(buffers,components);

        buffers.clear();
        buffers.add(b1);
        WorkStation w1 = new WorkStation(buffers,1);
        buffers.clear();
        buffers.add(b2);
        buffers.add(b3);
        WorkStation w2 = new WorkStation(buffers,2);
        buffers.clear();
        buffers.add(b4);
        buffers.add(b5);
        WorkStation w3 = new WorkStation(buffers,3);

        List<MyEvent> fel= new ArrayList<MyEvent>();
        double globalTime = 0;
        double timeLeft1 = ins1.bootrap();
        double timeLeft2 = ins2.bootrap();
        double timeLeft3=-1, timeLeft4=-1, timeLeft5=-1;
        System.out.println("Addining event for " + (globalTime + timeLeft1) + " and " + (globalTime + timeLeft2));
        fel.add(new MyEvent(1, globalTime + timeLeft1));
        fel.add(new MyEvent(2, globalTime + timeLeft2));
        List<MyEvent> toBeRemoved = new ArrayList<MyEvent>();
        List<MyEvent> toBeAdded = new ArrayList<MyEvent>();
        while(true)
        {
            
            double min = 10000 + globalTime;
            for(MyEvent e: fel)
            {
                System.out.println("event list "+ e.getTime());
                min = Math.min(min, e.getTime());
            }

            
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
                        System.out.println("timeLeft1 "+(globalTime + timeLeft1));
                        if(timeLeft1 != 0)
                            toBeAdded.add(new MyEvent(1, (globalTime + timeLeft1)));
                        break;

                    case 2:
                        toBeRemoved.add(e);
                        timeLeft2 = ins2.work();
                        System.out.println("timeLeft2 "+(globalTime + timeLeft2));
                        if(timeLeft2 != 0)
                            toBeAdded.add(new MyEvent(2, (globalTime + timeLeft2)));
                        break;

                    case 3:
                        toBeRemoved.add(e);
                        timeLeft3 = w1.produce();
                        System.out.println("timeLeft3 "+(globalTime + timeLeft3));
                        if(timeLeft3 != 0)
                            toBeAdded.add(new MyEvent(3, (globalTime + timeLeft3)));
                        break;
                        
                    case 4:
                        toBeRemoved.add(e);
                        timeLeft4 = w1.produce();
                        System.out.println("timeLeft4 "+(globalTime + timeLeft4));
                        if(timeLeft4 != 0)
                            toBeAdded.add(new MyEvent(4, (globalTime + timeLeft4)));
                        break;

                    case 5:
                        toBeRemoved.add(e);
                        timeLeft5 = w1.produce();
                        System.out.println("timeLeft5 "+(globalTime + timeLeft5));
                        if(timeLeft5 != 0)
                            toBeAdded.add(new MyEvent(5, (globalTime + timeLeft5)));
                        break;
                }
            }
            for(MyEvent e : toBeRemoved)
                fel.remove(e);
            for(MyEvent e : toBeAdded)
                fel.add(e);
                
      
            if(timeLeft1 == 0)
            {
                timeLeft1 = ins1.work();
                if(timeLeft1 != 0)
                    fel.add(new MyEvent(1, globalTime + timeLeft1));
            }
            if(timeLeft2 == 0)
            {
                timeLeft2= ins2.work();
                if(timeLeft2 != 0)
                    fel.add(new MyEvent(2, globalTime + timeLeft2));
            }
            if(timeLeft3 == 0 || timeLeft3 == -1)
            {
                if(timeLeft3 == -1)
                    timeLeft3 = w1.bootstrap();
                else
                    timeLeft3 = w1.produce();
                if(timeLeft3 != 0 && timeLeft3 != -1)
                {
                    System.out.println("added 3 event");
                    fel.add(new MyEvent(3, globalTime + timeLeft3));
                }
            }
            if(timeLeft4 == 0 || timeLeft4 == -1)
            {
                if(timeLeft4 == -1)
                timeLeft4 = w1.bootstrap();
                else
                timeLeft4 = w1.produce();
                if(timeLeft4 != 0 && timeLeft4 != -1)
                {
                    fel.add(new MyEvent(3, globalTime + timeLeft4));
                    System.out.println("added 4 event");
                }
            }
            if(timeLeft5 == 0 || timeLeft5 == -1)
            {
                if(timeLeft5 == -1)
                    timeLeft5 = w1.bootstrap();
                else
                    timeLeft5 = w1.produce();
                if(timeLeft5 != 0 && timeLeft5 != -1)
                {
                    fel.add(new MyEvent(3, globalTime + timeLeft5));
                    System.out.println("added 5 event");
                }
            }


            System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
            
        }
    }
}