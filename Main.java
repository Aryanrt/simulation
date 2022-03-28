
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;

class Main
{
    public static double globalTime = 0;
    public static DecimalFormat df = new DecimalFormat("####0.00");
    static FileWriter myWriter;
    public static void main(String[] args) throws FileNotFoundException, IOException 
    {

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
        double totalTime1 = 0, totalTime2 = 0, totalTime3 = 0, totalTime4 = 0,totalTime5 = 0;

        List<MyEvent> toBeRemoved = new ArrayList<MyEvent>();
        List<MyEvent> toBeAdded = new ArrayList<MyEvent>();
        Map<Double, Integer> histogram1 = new LinkedHashMap<Double, Integer>();
        Map<Double, Integer> histogram2 = new LinkedHashMap<Double, Integer>();
        Map<Double, Integer> histogram3 = new LinkedHashMap<Double, Integer>();
        Map<Double, Integer> histogram4 = new LinkedHashMap<Double, Integer>();
        Map<Double, Integer> histogram5 = new LinkedHashMap<Double, Integer>();
        Map<Double, Double> throughputs1 = new LinkedHashMap<Double, Double>();
        Map<Double, Double> throughputs2 = new LinkedHashMap<Double, Double>();
        Map<Double, Double> throughputs3 = new LinkedHashMap<Double, Double>();
        Map<Double, Double> throughputs4 = new LinkedHashMap<Double, Double>();
        Map<Double, Double> throughputs5 = new LinkedHashMap<Double, Double>();
        List<Double> arrivals1 = new ArrayList<Double>();
        List<Double> arrivals2 = new ArrayList<Double>();
        List<Double> arrivals3 = new ArrayList<Double>();
        double timeInSystem1 = 0;
        double timeInSystem2 = 0;
        double timeInSystem3 = 0;
        
        log("Addining Initial event for ins1 and Insp2 ");
        fel.add(new MyEvent(1, globalTime + timeLeft1));
        fel.add(new MyEvent(2, globalTime + timeLeft2));
        arrivals1.add(globalTime);
        if( ins2.currentComponent.getName().equals("c2"))
            arrivals2.add(globalTime);
        else
            arrivals3.add(globalTime);
        totalTime1 += timeLeft1;
        totalTime2 += timeLeft2;

        //create the log file or clear it
        File file = new File("logs.txt");
        if( ! file.createNewFile()) 
        {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        }
        
        while(true)
        {

            if(fel.size() == 0)
               // && w1.index == 300 && w2.index == 300 && w3.index == 300 )
            {
                log("");
                log("---------------------------------------------------------------------------------------------------");
                log("No more events to process. Terminating the simulation");
                log("---------------------------------------------------------------------------------------------------");
                log("");
                break;
            }    

            double min = 10000 + globalTime;
            for(MyEvent e: fel)
                min = Math.min(min, e.getTime());

            
            //update time
            globalTime = min;
            //log("global time is"+globalTime);

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
                        if(timeLeft1 != 0 && timeLeft1 != -2)
                        {
                            // log(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                            toBeAdded.add(new MyEvent(1, (globalTime + timeLeft1)));
                            totalTime1 += timeLeft1;
                            arrivals1.add(globalTime);
                        }
                        break;

                    case 2:
                        toBeRemoved.add(e);
                        timeLeft2 = ins2.work();
                        // log("time2 "+timeLeft2);
                        if(timeLeft2 != 0)
                        {
                            // log(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                            toBeAdded.add(new MyEvent(2, (globalTime + timeLeft2)));
                            totalTime2 += timeLeft2;
                            if( ins2.currentComponent.getName().equals("c2"))
                                arrivals2.add(globalTime);
                            else
                                arrivals3.add(globalTime);
                        }
                        break;

                    case 3:
                        toBeRemoved.add(e);
                        timeInSystem1 += (globalTime - arrivals1.get(arrivals1.size()-1));
                        arrivals1.remove(0);

                        timeLeft3 = w1.produce();
                        if(timeLeft3 != 0 && timeLeft3 != -2)
                        {
                            // log(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                            toBeAdded.add(new MyEvent(3, (globalTime + timeLeft3)));
                            totalTime3 += timeLeft3;
                        }
                        break;
                        
                    case 4:
                        toBeRemoved.add(e);
                        timeInSystem1 += (globalTime - arrivals1.get(arrivals1.size()-1));
                        timeInSystem2 += (globalTime - arrivals2.get(arrivals2.size()-1));
                        arrivals1.remove(0);
                        arrivals2.remove(0);
                        timeLeft4 = w2.produce();
                        if(timeLeft4 != 0)
                        {
                            // log(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                            toBeAdded.add(new MyEvent(4, (globalTime + timeLeft4)));
                            totalTime4 += timeLeft4;
                        }
                        break;

                    case 5:
                        toBeRemoved.add(e);
                        timeInSystem1 += (globalTime - arrivals1.get(arrivals1.size()-1));
                        timeInSystem3 += (globalTime - arrivals3.get(arrivals3.size()-1));
                        arrivals1.remove(0);
                        arrivals3.remove(0);

                        timeLeft5 = w3.produce();
                        if(timeLeft5 != 0)
                        {

                            // log(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                            toBeAdded.add(new MyEvent(5, (globalTime + timeLeft5)));
                            totalTime5 += timeLeft5;
                        }
                        break;
                }
            }
            for(MyEvent e : toBeRemoved)
            {
                fel.remove(e);
            }
            for(MyEvent e : toBeAdded)
            {
                fel.add(e);
            }   
      
            if(timeLeft3 == 0 || timeLeft3 == -1)
            {
                if(timeLeft3 == -1)
                    timeLeft3 = w1.bootstrap();
                else
                    timeLeft3 = w1.produce();
                if(timeLeft3 != 0 && timeLeft3 != -1)
                {
                    //timeInSystem1 += timeLeft3 - arrivals1.get(arrivals1.size()-1);
                    // arrivals1.remove(arrivals1.size()-1);                    
                    // log(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                    totalTime3 += timeLeft3;
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
                    // timeInSystem1 += timeLeft4 - arrivals1.get(arrivals1.size()-1);
                    // timeInSystem2 += timeLeft4 - arrivals2.get(arrivals2.size()-1);
                    // arrivals1.remove(arrivals1.size()-1);
                    // arrivals2.remove(arrivals2.size()-1);
                    // log(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                    // log("adding finish time for w2 "+ (globalTime + timeLeft4));
                    totalTime4 += timeLeft4;
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
                    // timeInSystem1 += timeLeft5 - arrivals1.get(arrivals1.size()-1);
                    // timeInSystem3 += timeLeft5 - arrivals3.get(arrivals3.size()-1);
                    // arrivals1.remove(arrivals1.size()-1);
                    // arrivals3.remove(arrivals3.size()-1);
                    totalTime5 += timeLeft5;
                    // log(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                    // log("adding finish time for w3 "+ (globalTime + timeLeft5));
                    fel.add(new MyEvent(5, globalTime + timeLeft5));                    
                }
            }
            if(timeLeft1 == 0)
            {
                timeLeft1 = ins1.work();
                if(timeLeft1 != 0)
                {
                    arrivals1.add(globalTime);
             //       log(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                    totalTime1 += timeLeft1;
                    fel.add(new MyEvent(1, globalTime + timeLeft1));
                }
            }
            if(timeLeft2 == 0)
            {
                timeLeft2= ins2.work();
                if(timeLeft2 != 0)
                {
                    if( ins2.currentComponent.getName().equals("c2"))
                        arrivals2.add(globalTime);
                    else
                        arrivals3.add(globalTime);
                 //   log(b1.getSize()+"|"+b2.getSize()+"|"+b3.getSize()+"|"+b4.getSize()+"|"+b5.getSize()+"|");
                    totalTime2 += timeLeft2;
                    fel.add(new MyEvent(2, globalTime + timeLeft2));
                }
            }


            //populate the histograms of each buffer occupancy
            histogram1.put(globalTime, b1.getSize());
            histogram2.put(globalTime, b2.getSize());
            histogram3.put(globalTime, b3.getSize());
            histogram4.put(globalTime, b4.getSize());
            histogram5.put(globalTime, b5.getSize());

            //update throughputs
            throughputs1.put(globalTime, (1+ins1.index)/(globalTime/60));
            throughputs2.put(globalTime, (1+ins2.index2 + 1+ins2.index3)/(globalTime/60));
            throughputs3.put(globalTime, (1+w1.index)/(globalTime/60));
            throughputs4.put(globalTime, (1+w2.index)/(globalTime/60));
            throughputs5.put(globalTime, (1+w3.index)/(globalTime/60));

            log(df.format(globalTime)+": Future Event List"+ fel);
            log("Buffer contents --> buffer1: " + b1.getSize()+" buffer2:"+b2.getSize()+" buffer3:"+b3.getSize()+" buffer4:"+b4.getSize()+" buffer5:"+b5.getSize());
            log(""+arrivals1);
            System.out.println(timeInSystem1);
            log("");
            
        }
        
        BigDecimal BigglobalTime = new BigDecimal(globalTime);

        
        log("Total Busy Percentage: \n\tInsector1: " + df.format(100*totalTime1/globalTime) + " | Insector2: "+ df.format(100*totalTime2/globalTime) 
        + " | WorkStation1: "+ df.format(100*totalTime3/globalTime)+ " | WorkStation2: "+ df.format(100*totalTime4/globalTime) + " | WorkStation3: "
        + df.format(100*totalTime5/globalTime));
        log("");
        
        log("avarage buffer occupancy: \n\tBuffer1: "+ areaUnderHistogram(histogram1).divide(BigglobalTime,4, RoundingMode.HALF_DOWN) + " | Buffer2: "
        + areaUnderHistogram(histogram2).divide(BigglobalTime,4, RoundingMode.HALF_DOWN) + " | Buffer3: "+ areaUnderHistogram(histogram3).divide(BigglobalTime,4, RoundingMode.HALF_DOWN)
         + " | Buffer4: "+ areaUnderHistogram(histogram4).divide(BigglobalTime,4, RoundingMode.HALF_DOWN) 
         + " | Buffer5: " + areaUnderHistogram(histogram5).divide(BigglobalTime,4, RoundingMode.HALF_DOWN) );
         log("");

        log("production \n\tComponent1: "+ (1+ins1.index) + " | Component2: "+ (1+ins2.index2) + " | Component3: "+ (1+ins2.index3)
        + " | Product1: "+ (w1.index+1) + " | Product2: " + (1+w2.index) +" | Product3: " + (1+w3.index));
        log("");
        
        log("Throuput \n\tComponent1: "+ df.format((1+ins1.index)/(globalTime/60)) + " | Component2: "+ df.format((1+ins2.index2)/(globalTime/60)) 
        + " | Component3: "+ df.format((1+ins2.index3)/(globalTime/60)) + " | Product1: "+ df.format((w1.index+1)/(globalTime/60)) 
        + " | Product2: " + df.format((1+w2.index)/(globalTime/60)) +" | Product3: " + df.format((1+w3.index)/(globalTime/60)));
        log("");

        log("Final Buffer contents \n\tbuffer1: " + b1.getSize()+" | buffer2: "+b2.getSize()+" | buffer3: "+b3.getSize()+" | buffer4: "+b4.getSize()
        +" | buffer5: "+b5.getSize());
        log("");

        log("Final entities blocked \n\tInspector1: " + (timeLeft1==0?"yes":"no") + " | Inspector2: "+ (timeLeft2==0?"yes":"no") + " | Worstation1: "
        + (timeLeft3==0?"yes":"no") + " | Worstation2: "+ (timeLeft4==0?"yes":"no") + " | Worstation3: "+ (timeLeft5==0?"yes":"no"));
        log("");

        log(timeInSystem1+" "+timeInSystem2+" "+timeInSystem3+" ");
        log("Average time in system \n\tProduct 1: " + (timeInSystem1/60)/(w1.index+w2.index+w3.index) + "\tProduct 2: " + (timeInSystem2/60)/w2.index + 
            "\tProduct 3: " + (timeInSystem3/60)/w3.index );
        log("");

        //GraphPanel panel = new GraphPanel();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GraphPanel.createAndShowGui(throughputs1);
            }
         });
        
    }

    static BigDecimal areaUnderHistogram(Map<Double, Integer> histogram)
    {        
        int prev = 0;
        double prevTime = 0;
        BigDecimal area = new BigDecimal(prevTime);
        
        for(double key: histogram.keySet())
        {
            double d = (key - prevTime) * prev;
            BigDecimal dd = new BigDecimal(d);
            area = area.add(dd);
            prev = histogram.get(key);
            prevTime = key;
        } 
        return area;

    }
    public static void log(String s) throws IOException
    {
        Main.myWriter = new FileWriter("verification.logs", true);
        myWriter.write(s+"\n");
        myWriter.close();
        
    }
}