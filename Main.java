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

        while(true)
        {
            State state1 = ins1.work();
            State state2 = ins2.work();
            State state3 = w1.produce();
            State state4 = w2.produce();
            State state5 = w3.produce();
            if(state1 == State.BLOCKED)
                ins1.work();
            if(state2 == State.BLOCKED)
                ins2.work();
                
            if(state3 == State.BLOCKED)
                w1.produce();
            if(state4 == State.BLOCKED)
                w2.produce();
            if(state5 == State.BLOCKED)
                w3.produce();

            System.out.println(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
            
        }
    }
}