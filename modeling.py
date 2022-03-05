from cgi import MiniFieldStorage
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
    plt.hist(data, density=False, bins=18) 
    plt.ylabel('Frequency')
    plt.xlabel(name);
    plt.savefig(name+'.png')

def calulateWeightedMean(data):
    total=0;
    sampleBin = [0]*18;
    width = (max(data)- min(data))/18;
    minData= min(data);
    binMean =0;

    for i in range(0,18):
        sizeSofar=0;
        for j in range(0,300):
            if data[j] >= i * width + minData and data[j] < ((i+1) * width + minData) :
                sampleBin[i] = sampleBin[i] + 1;

    binMean=0;
    for i in range(0,18):
        binMean = binMean + (sampleBin[i]/300)* (i * width+(i+1) * width)/2 +  minData;

    return binMean;

def calculateX2(observed,expeted,size):
    x2=0;
    for i in range(0,size):
        if expeted[i] == 0:
            continue;
        x2 = x2 + pow((observed[i]-expeted[i]),2)/expeted[i];
    return round(x2,2);

def generateRandomNumbers():
    a=21;
    c=31;
    m=100;
    digits=[0]*300;
    digits[0]=35;

    for i in range(1,300):
        digits[i]=(a*digits[i-1]+c)%m;
    for i in range(0,300):
        digits[i] = round(digits[i]/m,2);

    return digits;

def plotQQExp(data,name):
    # dist = ot.Exponential(1/calulateWeightedMean(data));
    # x = dist.getSample(300);
    # x = np.random.exponential(calulateWeightedMean(data),300);
    generatedSample= [0]*300;
    landa= 1/np.mean(data);
    randomNums = generateRandomNumbers();
    for i in range(0,300):
        generatedSample[i] = (-1/landa)* np.log(1-randomNums[i]);
    x= generatedSample;
    maxSample=0;
    for i in range(0,300):
        if maxSample < generatedSample[i]:
            maxSample= generatedSample[i];
    
    minSample= maxSample;
    index=-1;
    for i in range(0,300):
        if minSample > generatedSample[i]:
            minSample= generatedSample[i];
            index = i;
    width = (max(max(data), maxSample)-(min(min(data), minSample))) /18;
    # widths = [0]*18;
    # sampleBin = [0] * 18;
    # distBin = [0] * 18;

    if name=="servinsp1" or name=="servinsp22":
        size =9;
    if name=="servinsp23":
        size =13;
    if name=="ws1" or name=="ws2":
        size =12;
    if name=="ws3":
        size =11;
    width = (max(max(data), maxSample)-(min(min(data), minSample))) /size;
    minData = (min(min(data), minSample));

    sampleBin = [0] * size;
    distBin = [0] * size;
    
    for i in range (0,size):
        for j in range(0,300):
            if generatedSample[j] >= i * width + minData and generatedSample[j]< (i+1) * width + minData:
                distBin[i] = distBin[i] + 1;
            if data[j] >= i * width + minData and data[j] < (i+1) * width + minData:
                sampleBin[i] = sampleBin[i] + 1;
    distBin[size-1] = 300 - sum(distBin);
    sampleBin[size-1] = 300 - sum(sampleBin);
    
    x2 = calculateX2(sampleBin, distBin,size);

    plt.clf();
    plt.plot(sampleBin, distBin);
    xpoints = ypoints = plt.xlim();
    plt.plot(xpoints, ypoints, linestyle='--', color='k', lw=3, scalex=False, scaley=False);
    plt.title(name+'-QQ-Exponential \n with x2= '+str(x2));
    plt.savefig(name+'-QQ-EXP.png');
    return str(x2);

def plotQQGeom(data,name):

    x = np.random.geometric(1/calulateWeightedMean(data),300);
    maxSample=0;
    for i in range(0,300):
        if maxSample < x[i]:
            maxSample= x[i];
    
    minSample= maxSample;
    for i in range(0,300):
        if minSample > x[i]:
            minSample= x[i];

    if name=="servinsp1" or name=="servinsp22":
        size =9;
    if name=="servinsp23":
        size =13;
    if name=="ws1" or name=="ws2":
        size =12;
    if name=="ws3":
        size =11;

    width = (max(max(data), maxSample)-(min(min(data), minSample))) /size;
    minData = (min(min(data), minSample));
    

    sampleBin = [0] * size;
    distBin = [0] * size;
    
    for i in range (0,size):
        for j in range(0,300):
            if x[j] >= i * width + minData and x[j]< (i+1) * width + minData:
                distBin[i] = distBin[i] + 1;
            if data[j] >= i * width + minData and data[j] < (i+1) * width + minData:
                sampleBin[i] = sampleBin[i] + 1;
    distBin[size-1] = 300 - sum(distBin);
    sampleBin[size-1] = 300 - sum(sampleBin);
    
    x2 = calculateX2(sampleBin, distBin,size);
    # plt.clf();
    # plt.plot(sampleBin, distBin)
    # xpoints = ypoints = plt.xlim()
    # plt.plot(xpoints, ypoints, linestyle='--', color='k', lw=3, scalex=False, scaley=False)
    # plt.title(name+'-QQ-Geometric \n with x2= '+str(x2));
    # plt.savefig(name+'-QQ-GEO.png')
    return str(x2);

def plotQQPoisson(data,name):
    dist = ot.Poisson(calulateWeightedMean(data));
    x = dist.getSample(300);
    maxSample=0;
    for i in range(0,300):
        if maxSample < x[i][0]:
            maxSample= x[i][0];
    minSample= maxSample;
    for i in range(0,300):
        if minSample > x[i][0]:
            minSample= x[i][0];
    width = (max(max(data), maxSample)-(min(min(data), minSample))) /18;
    
    sampleBin = [0] * 18;
    distBin = [0] * 18;
    for i in range (0,18):
        for j in range(0,300):
            if x[j][0] >= i * width and x[j][0] < (i+1) * width:
                distBin[i] = distBin[i] + 1;
            if data[j] >= i * width and data[j] < (i+1) * width:
                sampleBin[i] = sampleBin[i] + 1;
    
    # Case of samples equal to the maximum of both samples(which is not taken care of above)
    distBin[16] = distBin[16] + (300-sum(distBin));
    sampleBin[16] = sampleBin[16] + (300-sum(sampleBin));

    x2 = calculateX2(sampleBin, distBin,18);
    plt.clf();
    plt.plot(sampleBin, distBin)
    xpoints = ypoints = plt.xlim()
    plt.plot(xpoints, ypoints, linestyle='--', color='k', lw=3, scalex=False, scaley=False)
    plt.title(name+'-QQ-POISSON \n with x2= '+str(x2));
    plt.savefig(name+'-QQ-POISSON.png')
    return str(x2);


# load data
servinsp1 = np.loadtxt( 'servinsp1.dat' )
servinsp22 = np.loadtxt( 'servinsp22.dat' )
servinsp23 = np.loadtxt( 'servinsp23.dat' )
ws1 = np.loadtxt( 'ws1.dat' )
ws2 = np.loadtxt( 'ws2.dat' )
ws3 = np.loadtxt( 'ws3.dat' )
generateRandomNumbers();
ot.RandomGenerator.SetSeed(77)

# Plot histograms with 18 bins
binSize= round(sqrt(300).real);

plotHistograms(servinsp1,'servinsp1');
plotHistograms(servinsp22,'servinsp22');
plotHistograms(servinsp23,'servinsp23');
plotHistograms(ws1,'ws1');
plotHistograms(ws2,'ws2');
plotHistograms(ws3,'ws3');

# calulateWeightedMean(servinsp1,'servinsp1');
# calulateWeightedMean(servinsp22,'servinsp22');
# calulateWeightedMean(servinsp23,'servinsp23');
# calulateWeightedMean(ws1,'ws1');
# calulateWeightedMean(ws2,'ws2');
# calulateWeightedMean(ws3,'ws3');


# QQ for Expnential
as1 = plotQQExp(servinsp1,'servinsp1');
as22 = plotQQExp(servinsp22,'servinsp22');
as23 = plotQQExp(servinsp23,'servinsp23');
aw1 = plotQQExp(ws1,'ws1');
aw2 = plotQQExp(ws2,'ws2');
aw3 = plotQQExp(ws3,'ws3');

# QQ for Geomteric
bs1 = plotQQGeom(servinsp1,'servinsp1');
bs22 = plotQQGeom(servinsp22,'servinsp22');
bs23 = plotQQGeom(servinsp23,'servinsp23');
bw1 = plotQQGeom(ws1,'ws1');
bw2 = plotQQGeom(ws2,'ws2');
bw3 = plotQQGeom(ws2,'ws3');

# QQ for Possion
cs1 =plotQQPoisson(servinsp1,'servinsp1');
cs22 = plotQQPoisson(servinsp22,'servinsp22');
cs23 = plotQQPoisson(servinsp23,'servinsp23');
cw1 = plotQQPoisson(ws1,'ws1');
cw2 = plotQQPoisson(ws2,'ws2');
cw3 = plotQQPoisson(ws3,'ws3');


print('------------------');
print(as1 + ' '+ bs1+ ' '+ cs1);
print(as22 + ' '+ bs22+ ' '+ cs22);
print(as23 + ' '+ bs23+ ' '+ cs23);
print(aw1 + ' '+ bw1+ ' '+ cw1);
print(aw2 + ' '+ bw2+ ' '+ cw2);
print(aw3 + ' '+ bw3+ ' '+ cw3);
