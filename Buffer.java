public class Buffer 
{
    public int size = 0;
    public int capacity = 2;
    public int productID;

    public Buffer(int productID)
    {
        this.productID = productID;
    }
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
    public int getProductID()
    {
        return this.productID;
    }

}
