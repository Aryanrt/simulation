public class Buffer 
{

    public Component c;
    private int size;
    private int capacity;
    public WorkStation w;

    public void addComponent()
    {
        this.size++;
    }
    public void removeComponent()
    {
        this.size--;
    }
    public int getSize()
    {
        return this.size;
    }
    public int getCapacity()
    {
        return this.capacity;
    }

}
