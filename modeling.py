from cmath import sqrt
import numpy as np
import matplotlib.pyplot as plt
import statsmodels.api as sm
import scipy.stats as stats
from scipy.stats import geom
import openturns as ot
from openturns.viewer import View

def plotHistograms(data, name):
    plt.clf();
    plt.hist(data, density=False, bins=binSize) 
    plt.ylabel('Frequency')
    plt.xlabel(name);
    plt.savefig(name+'.png')

def plotQQExp(data,name):
    dist = ot.Exponential(1/np.mean(data));
    x = dist.getSample(300);
    maxSample=0;
    for i in range(0,300):
        if maxSample < x[i][0]:
            maxSample= x[i][0];
    width = max(max(data), maxSample)/17;
    
    sampleBin = [0] * 17;
    distBin = [0] * 17;
    for i in range (0,17):
        for j in range(0,300):
            if x[j][0] >= i * width and x[j][0] < (i+1) * width:
                distBin[i] = distBin[i] + 1;
            if data[j] >= i * width and data[j] < (i+1) * width:
                sampleBin[i] = sampleBin[i] + 1;

    plt.clf();
    plt.plot(sampleBin, distBin)
    xpoints = ypoints = plt.xlim()
    plt.plot(xpoints, ypoints, linestyle='--', color='k', lw=3, scalex=False, scaley=False)
    plt.savefig(name+'-QQ-EXP.png')



# load data
servinsp1 = np.loadtxt( 'servinsp1.dat' )
servinsp22 = np.loadtxt( 'servinsp22.dat' )
servinsp23 = np.loadtxt( 'servinsp23.dat' )
ws1 = np.loadtxt( 'ws1.dat' )
ws2 = np.loadtxt( 'ws2.dat' )
ws3 = np.loadtxt( 'ws3.dat' )


#  Plot histograms
binSize= round(sqrt(300).real);
plotHistograms(servinsp1,'servinsp1');
plotHistograms(servinsp22,'servinsp22');
plotHistograms(servinsp23,'servinsp23');
plotHistograms(ws1,'ws1');
plotHistograms(ws2,'ws2');
plotHistograms(ws3,'ws3');

data_points = np.random.normal(0, 1, 100) 
sm.qqplot(servinsp1, line ='45',fit=True,dist=stats.norm)

# x = ot.Normal().getSample(100)
# y = ot.Uniform().getSample(1000)
# # g = ot.VisualTest.DrawQQplot(x, y)
# plt.clf();
# plt.plot(x,y)
# plt.show()
plotQQExp(servinsp1,'servinsp1');
plotQQExp(servinsp22,'servinsp22');
plotQQExp(servinsp23,'servinsp23');
plotQQExp(ws1,'ws1');
plotQQExp(ws2,'ws2');
plotQQExp(ws3,'ws3');






