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

a=71;
c=17;
m=pow(2,8)+1;
digits=[0]*600000;
digits[0]=7;
counter=0;

def generateRandomNumbers():

    global a,c,m,digits,counter;
    generatedNums=300*[0];

    for i in range(1,301):
        digits[counter*300+i]=(a*digits[counter*300+i-1]+c)%m;
        generatedNums[i-1]=digits[counter*300+i]
      
    for i in range(0,300):
        generatedNums[i] = round(generatedNums[i]/m,6);
    counter = counter + 1;

    return generatedNums;

def testForUnifomity(sample):
    # test for 30 bins
    observedBins=30*[0];
    minn=min(sample);
    maxx=max(sample);
    width=(maxx-minn)/30;
    for i in range (0,30):
        for j in range(0,300):
            if sample[j] >= i * width + minn and sample[j]< (i+1) * width + minn:
                observedBins[i] = observedBins[i] + 1;
    
    expectedBins=30*[10];
    observedBins[29] = observedBins[29] + 300-sum(observedBins);
    print('Generated R:'+str(observedBins)+'\nExpected R:'+str(expectedBins));
    print('For 30 Bins X2 of generated numbers(R), compare to uniform distribution is '+str(calculateX2(observedBins, expectedBins,30)));

    return observedBins;

def plotQQExp(data,name):

    generatedSample= [0]*300;
    landa= 1/np.mean(data);
    randomNums = generateRandomNumbers();
    testForUnifomity(randomNums);

    for i in range(0,300):
        generatedSample[i] = (-1/landa)* np.log(1-randomNums[i]);

    # get min and max
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

    if name=="servinsp1" or name=="servinsp22":
        size =9;
    if name=="servinsp23":
        size =15;
    if name=="ws1":
        size =12;
    if name=="ws2":
        size=17;
    if name=="ws3":
        size =14;
    width = (max(max(data), maxSample)-(min(min(data), minSample))) /18;
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
    print('Entity\t\t',name);
    print('size\t\t',len(sampleBin));
    print('observed\t',sampleBin);
    print('expectred\t',distBin);
    print('X2\t\t', x2);
    print('------------------------------------------');
    

    plt.clf();
    plt.plot(sampleBin, distBin);
    xpoints = ypoints = plt.xlim();
    plt.plot(xpoints, ypoints, linestyle='--', color='k', lw=3, scalex=False, scaley=False);
    plt.title(name+'-QQ-Exponential \n with x2= '+str(x2));
    plt.savefig(name+'-QQ-EXP.png');
    return str(x2);

# --------------------------------------------------------------------------------------------------------
def plotQQGeom(data,name):

    generatedSample= [0]*300;
    p= 1/np.mean(data);
    randomNums = generateRandomNumbers();
    for i in range(0,300):
        generatedSample[i] = np.log(1-randomNums[i])/ np.log(1-p);
    
    maxSample=0;
    for i in range(0,300):
        if maxSample < generatedSample[i]:
            maxSample= generatedSample[i];
    
    minSample= maxSample;
    for i in range(0,300):
        if minSample > generatedSample[i]:
            minSample= generatedSample[i];

    if name=="servinsp1" or name=="servinsp22":
        size =9;
    if name=="servinsp23":
        size =15;
    if name=="ws1":
        size =12;
    if name=="ws2":
        size=17;
    if name=="ws3":
        size =14;

    width = (max(max(data), maxSample)-(min(min(data), minSample))) /18;
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
    plt.plot(sampleBin, distBin)
    xpoints = ypoints = plt.xlim()
    plt.plot(xpoints, ypoints, linestyle='--', color='k', lw=3, scalex=False, scaley=False)
    plt.title(name+'-QQ-Geometric \n with x2= '+str(x2));
    plt.savefig(name+'-QQ-GEO.png')
    return str(x2);


# load data
servinsp1 = np.loadtxt( 'servinsp1.dat' )
servinsp22 = np.loadtxt( 'servinsp22.dat' )
servinsp23 = np.loadtxt( 'servinsp23.dat' )
ws1 = np.loadtxt( 'ws1.dat' )
ws2 = np.loadtxt( 'ws2.dat' )
ws3 = np.loadtxt( 'ws3.dat' )

# Plot histograms with 18 bins
binSize= round(sqrt(300).real);

plotHistograms(servinsp1,'servinsp1');
plotHistograms(servinsp22,'servinsp22');
plotHistograms(servinsp23,'servinsp23');
plotHistograms(ws1,'ws1');
plotHistograms(ws2,'ws2');
plotHistograms(ws3,'ws3');

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


print('Exponential Chi Square Restlts:');
print('\nins1\t'+as1);
print('ins22\t'+as22);
print('ins23\t'+as23);
print('ws1\t'+aw1);
print('ws2\t'+aw2);
print('ws3\t'+aw3);
print('\n------------------');
print('Geomteric Chi Square Restlts:');
print('\nins1\t'+bs1);
print('ins22\t'+bs22);
print('ins23\t'+bs23);
print('ws1\t'+bw1);
print('ws2\t'+bw2);
print('ws3\t'+bw3);
print('\n------------------');
