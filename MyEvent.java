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
    
}
