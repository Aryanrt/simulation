public class MyEvent 
{
    
    int type;
    Buffer buffer;
    double time;

    public MyEvent(int type, double time)
    {
        this.type = type;
        // this.buffer = buffer;
        this.time = time;
    }
    double getTime()
    {
        return time;
    }
    int getType()
    {
        return type;
    }
    public String toString()
    {
        return (this.type == 1?"Ins1":this.type == 2?"Ins2":this.type == 3?"w1":this.type == 4?"w2":"w3") + " for time:"+ this.time;
    }
    
}
