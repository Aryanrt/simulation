from cgi import MiniFieldStorage
from cmath import sqrt
import numpy as np
import matplotlib.pyplot as plt
import statsmodels.api as sm
import scipy.stats as stats
from scipy.stats import geom
import openturns as ot
from openturns.viewer import View
import sys

file = open('modeling-logs.txt', 'w') ;
sys.stdout =file;


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

a=5 ;
c=7;
m=pow(2,8)+1;
digits=[0]*600000;
digits[0]=31;
counter=0;
# a=5 ;
# c=7;
# m=pow(2,8)+1;
# digits=[0]*600000;
# digits[0]=31;
# counter=0;
def generateRandomNumbers():

    global a,c,m,digits,counter;
    generatedNums=300*[0];

    for i in range(1,301):
        digits[counter*300+i]=(a*digits[counter*300+i-1]+c)%m;
        generatedNums[i-1]=digits[counter*300+i]
      
    for i in range(0,300):
        generatedNums[i] = round(generatedNums[i]/m,8);
    counter = counter + 1;

    return generatedNums;

def testForUnifomity(sample,bins):
    # test for 30 bins
    observedBins=bins*[0];
    minn=min(sample);
    maxx=max(sample);
    width=(maxx-minn)/bins;
    for i in range (0,bins):
        for j in range(0,300):
            if sample[j] >= i * width + minn and sample[j]< (i+1) * width + minn:
                observedBins[i] = observedBins[i] + 1;
    
    expectedBins=bins*[300/bins];
    observedBins[bins-1] = observedBins[bins-1] + 300-sum(observedBins);
    print('For '+str(bins)+' Bins');
    print('Generated R:\n'+str(observedBins)+'\nExpected R:\n'+str(expectedBins));
    print('X2 of generated numbers, compare to uniform distribution is '+str(calculateX2(observedBins, expectedBins,bins)));
    print('------------');

    return observedBins;

def testAutoCorrelation(sample, m,i):
    averagePhi=0;
    k=0;
    while (k+1)*m < 300:
        averagePhi = averagePhi + sample[k*m+i]*sample[(k+1)*m+i];
        k = k + 1;
        
    averagePhi = averagePhi / (1 + k) - 0.25;
    sigmaPhi=sqrt(13*k+7).real/(12*(k+1));
    print('testAutoCorrelation for (m= '+str(m)+',i='+str(i)+'):  '+ str(round(averagePhi/sigmaPhi,4)));

def plotQQExp(data,name):

    generatedSample= [0]*300;
    landa= 1/np.mean(data);
    randomNums = generateRandomNumbers();
    print('*******************\n\t'+name+'\n*******************\n');
    print('Generated Random Numbers:');
    print(randomNums);
    print('------------');
    print('Testing for Uniformity:');
    print('------------');
    testForUnifomity(randomNums,10);
    testForUnifomity(randomNums,30);
    testForUnifomity(randomNums,50);
    print('Testing for AutoCorrelation:');
    print('------------');
    testAutoCorrelation(randomNums,1,0);
    testAutoCorrelation(randomNums,5,0);
    testAutoCorrelation(randomNums,10,0);
    print('------------');

    for i in range(0,300):
        generatedSample[i] = (-1/landa)* np.log(1-randomNums[i]);

    print('Generatated Exponential Distribution:');
    print('------------');
    print(list(np.around(np.array(generatedSample),2)));
    print('------------');
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
    print('size of bin\n',len(sampleBin));
    print('observed Bin\n',sampleBin);
    print('expectred Bin\n',distBin);
    print('X2 calcualted\n', x2);
    print('------------------------------------------------------------------------------------------------------------\n\n');
    

    plt.clf();
    plt.plot(sampleBin, distBin);
    xpoints = ypoints = plt.xlim();
    plt.plot(xpoints, ypoints, linestyle='--', color='k', lw=3, scalex=False, scaley=False);
    plt.title(name+'-QQ-Exponential \n with x2= '+str(x2));
    plt.savefig(name+'-QQ-EXP.png');
    return str(x2);

# --------------------------------------------------------------------------------------------------------
# def plotQQGeom(data,name):

#     generatedSample= [0]*300;
#     p= 1/np.mean(data);
#     randomNums = generateRandomNumbers();
#     for i in range(0,300):
#         generatedSample[i] = np.log(1-randomNums[i])/ np.log(1-p);
    
#     maxSample=0;
#     for i in range(0,300):
#         if maxSample < generatedSample[i]:
#             maxSample= generatedSample[i];
    
#     minSample= maxSample;
#     for i in range(0,300):
#         if minSample > generatedSample[i]:
#             minSample= generatedSample[i];

#     if name=="servinsp1" or name=="servinsp22":
#         size =9;
#     if name=="servinsp23":
#         size =15;
#     if name=="ws1":
#         size =12;
#     if name=="ws2":
#         size=17;
#     if name=="ws3":
#         size =14;

#     width = (max(max(data), maxSample)-(min(min(data), minSample))) /18;
#     minData = (min(min(data), minSample));
    

#     sampleBin = [0] * size;
#     distBin = [0] * size;
    
#     for i in range (0,size):
#         for j in range(0,300):
#             if generatedSample[j] >= i * width + minData and generatedSample[j]< (i+1) * width + minData:
#                 distBin[i] = distBin[i] + 1;
#             if data[j] >= i * width + minData and data[j] < (i+1) * width + minData:
#                 sampleBin[i] = sampleBin[i] + 1;
#     distBin[size-1] = 300 - sum(distBin);
#     sampleBin[size-1] = 300 - sum(sampleBin);
    
#     x2 = calculateX2(sampleBin, distBin,size);
#     plt.clf();
#     plt.plot(sampleBin, distBin)
#     xpoints = ypoints = plt.xlim()
#     plt.plot(xpoints, ypoints, linestyle='--', color='k', lw=3, scalex=False, scaley=False)
#     plt.title(name+'-QQ-Geometric \n with x2= '+str(x2));
#     plt.savefig(name+'-QQ-GEO.png')
#     return str(x2);


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

# # QQ for Geomteric
# bs1 = plotQQGeom(servinsp1,'servinsp1');
# bs22 = plotQQGeom(servinsp22,'servinsp22');
# bs23 = plotQQGeom(servinsp23,'servinsp23');
# bw1 = plotQQGeom(ws1,'ws1');
# bw2 = plotQQGeom(ws2,'ws2');
# bw3 = plotQQGeom(ws2,'ws3');

print('\n------------------');
print('Summery\nExponential Chi Square Restlts:');
print('\nins1\t'+as1);
print('ins22\t'+as22);
print('ins23\t'+as23);
print('ws1\t'+aw1);
print('ws2\t'+aw2);
print('ws3\t'+aw3);

# print('Geomteric Chi Square Restlts:');
# print('\nins1\t'+bs1);
# print('ins22\t'+bs22);
# print('ins23\t'+bs23);
# print('ws1\t'+bw1);
# print('ws2\t'+bw2);
# print('ws3\t'+bw3);
# print('\n------------------');
