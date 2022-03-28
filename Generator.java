public class Generator 
{
    int a, c, m, prev;
    double landa;
    public Generator(double landa)
    {
        this.a=5;
        this.c=7;
        this.m = (int) (Math.pow(2, 8)+1);
        this.prev=31;
        this.landa=landa;
    }   

    public double nextRandomNumber()
    {
        this.prev = (a * prev + c) % m;
        return this.prev;
    }
    public double next()
    {  
        double result;
        result = (-1/landa) * Math.log(1-nextRandomNumber()/m);
        return Math.max(result,0.0001);
    }

}
