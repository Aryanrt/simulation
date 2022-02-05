import java.awt.List;
import java.util.ArrayList;

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

        Buffer b1 = new Buffer();
        Buffer b2 = new Buffer();
        Buffer b3 = new Buffer();
        Buffer b4 = new Buffer();
        Buffer b5 = new Buffer();
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

        List<Event> fel= new ArrayList<Event>();
        double globalTime = 0;
        double timeLeft1 = ins1.work();
        double timeLeft2 = ins2.work();
        double timeLeft3=0, timeLeft4=0, timeLeft5=0;
        fel.add(new MyEvent(1, globalTime + timeLeft1));
        fel.add(new MyEvent(2, globalTime + timeLeft2));
        List<MyEvent> toBeRemoved = new ArrayList<MyEvent>();
        while(true)
        {
            double min = 10 * globalTime;
            for(MyEvent e: fel)
                if(e.getTime() < globalTime )
                    min = e.getTime();

            //update time
            globalTime = min;
            
            toBeRemoved.clear();
            for(MyEvent e: fel)
            {
                if(e.getTime() != globalTime)
                    continue;

                switch (e.getType())
                {
                    case 1:
                        toBeRemoved.add(e);
                        timeLeft1 = ins1.work();
                        if(timeLeft1 != 0)
                            fel.add(new Event(1, globalTime + timeLeft1));
                        break;

                    case 2:
                        toBeRemoved.add(e);
                        timeLeft2 = ins2.work();
                        if(timeLeft2 != 0)
                            fel.add(new Event(2, globalTime + timeLeft2));
                        break;

                    case 3:
                        toBeRemoved.add(e);
                        timeLeft3 = w1.produce();
                        if(timeLeft3 != 0)
                            fel.add(new Event(3, globalTime + timeLeft3));
                        break;
                        
                    case 4:
                        toBeRemoved.add(e);
                        timeLeft4 = w2.produce();
                        if(timeLeft4 != 0)
                            fel.add(new Event(4, globalTime + timeLeft4));
                        break;

                    case 5:
                        toBeRemoved.add(e);
                        timeLeft5 = w3.produce();
                        if(timeLeft5 != 0)
                            fel.add(new Event(5, globalTime + timeLeft5));
                        break;
                }
            }
            for(MyEvent e : toBeRemoved)
                fel.remove(e);
      
            if(timeLeft1 == 0)
            {
                timeLeft1 = ins1.work();
                if(timeLeft1 != 0)
                    fel.add(new Event(1, globalTime + timeLeft1));
            }
            if(timeLeft2 == 0)
            {
                timeLeft2= ins2.work();
                if(timeLeft2 != 0)
                    fel.add(new Event(2, globalTime + timeLeft2));
            if(timeLeft3 == 0)
            {
                timeLeft3 = w1.produce();
                if(timeLeft3 != 0)
                    fel.add(new Event(3, globalTime + timeLeft3));
            }
            if(timeLeft4 == 0)
            {
                timeLeft4 = w2.produce();
                if(timeLeft4 == 0)
                    fel.add(new Event(4, globalTime + timeLeft4));
            }
            if(timeLeft5 == 0)
            {
                timeLeft5 = w3.produce();
                if(timeLeft5 == 0)
                    fel.add(new Event(5, globalTime + timeLeft5));
            }


            System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
            
        }
    }
}