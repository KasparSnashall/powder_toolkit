import numpy as np
from Loader import Loader
load = Loader()

class Comparator:
    """
    Comparator Class doc string
    ############################
    This class compares two data sets usually lists but arguments such as 
    filenames (eg .hkl) may be passed to it.
    
    attributes
    ----------------------
    self.data1 = data1 # first known data set
    self.data2 = data2 # second data set
    self.weights = []   # creates weights 
    self.tolerance = 5 # the tolerance used in binary
    self.data_range = None # range for data in form start,stop
    self.optimise_steps = -0.5,0.5,300  #-x,x, number of steps
    self.optimise = False # optimise option
    
    
    
    methods:
    ###########################
    
    __init__()
    --------------------
    takes- 3 arguments self, data1 [str or list], data2 [str or list]
    
    binary()
    ---------------------
    
    uses data1[] and data1[],compares lists using a binary method with a percentage tolerance 
    of first data set (self.tol)
    has optimise (True/False) shifts data to left or right via least squares method for optimal fit
    
    returns percentage match (float)
    
    fraction()
    ----------------------
    uses data1[] and data2[], compares lists using fractional method
    returns average percentage
    
    both()
    ---------------------
    uses output from binary() and fraction() 
    returns average
    
    scale()
    --------------------
    uses data1[],percent[],self.weights[[]]
    applies numpy weighted average
    returns weighted average
    
    get_range()
    -------------------
    
    
    compare()
    --------------------
    main function used to call fraction, binary and both 
    
    
    
    
    """
    
    def __init__(self,data1,data2): 
        #print "Comparator"
        self.data1 = data1 # first known data set
        self.data2 = data2 # second data set
        self.weights = []   # creates weights 
        self.tolerance = 5 # the tolerance used in binary
        self.data_range = None # range for data in form start,stop
        self.optimise_steps = -0.5,0.5,300  #-x,x, number of steps
        self.optimise = False
        
    def get_weights(self):
        return self.weights
        
    def set_weights(self,value):
        self.weights.append(value)
        
    def get_tolerance(self):
        return self.tolerance
    
    def set_tolerance(self,value):
        self.tolerance = value

    def get_data_range(self):
        return self.data_range

    def set_data_range(self,value):
        self.data_range = value
  
    def get_optimise_steps(self):
        return self.optimise_steps

    def set_optimise_steps(self,*value):
        self.optimise_steps = value

    def get_optimise(self):
        return self.optimise

    def set_optimise(self,value):
        self.optimise = value
        
    def binary(self):
        #compare with binary method
        set1, set2 = self.data1,self.data2
        if self.data_range != None:
            # specify data range
            set1,set2 = self.get_range(set1, set2, self.data_range)
        
        if self.optimise == True:
            # if optimisation is requested
            leny = len(set2) # make sure they are the same length
            lenx = len(set1)
            if leny >= lenx: # if lengths not the same the cut the data
                set2 = set2[0:lenx]
            elif lenx > leny:
                set1 = set1[0:leny]
            # create steps
            step1, step2, stepnum = self.optimise_steps
            steps = np.linspace(step1, step2, stepnum) # get a list of steps to try
            shifts = [] # final list of differences
            for step in steps:
                shift = set2 + step #add a step to data
                delx = set1 - shift # calculate the difference between set1,set2+step
                av_shift = np.mean(delx**2) # find the mean value of the difference square (this ensures positive)
                shifts.append(av_shift) # append this to a list
            val, idx = min((val, idx) for (idx, val) in enumerate(shifts)) #find index of min value in difference list
            best_shift = steps[idx] # return the step size used, now called shift
            set2 = set2 + best_shift # add shift to set2
        
        counts = [] # list of 1 and 0 to calculate percentage match
        for i,j in zip(set1,set2):
            tol = float(self.tolerance)*i/100
            if  i-tol <= j and j <= i+tol:
                counts.append(1)
            else:
                counts.append(0)
        if self.weights != []:
            # uses weights
            result = self.scale(counts,set1)
            return 100*result
        else:
            percent = 100*sum(counts)/len(counts)
            return percent
        
    def fraction(self):
        # fraction returns the average fraction of two lists
        set1, set2 = self.data1,self.data2
        if self.data_range != None:
            set1,set2 = self.get_range(set1, set2, self.data_range)
        
        percent = [] # list of final percents
        # appends a list of percents
        for i,j in zip(set1,set2):
            
            print i,j
            if i and j > 0:
                if i > j and i != j:
                    p = 100.0*(j/i)
                    percent.append(p)
                elif j > i and i != j:
                    p = 100.0*(i/j)
                    percent.append(p)
            elif i or j == 0.0:
                if i != j:
                    percent.append(0.0)
                    #may change this
                else:
                    percent.append(100.0)
            else:
                p = 0
                percent.append(p)
        if self.weights != [] :
            # returns a weighted average percent
            result = self.scale(percent,set1)
            return result
        else:
            #returns an average percent
            return np.average(percent)

    def both(self):
        # returns both results average
        fraction = self.fraction()
        binary = self.binary()
        return np.average([fraction, binary])

    def scale(self,percent,set1):
        # weighted average data according to self.weights
        kilos = [1 for i in range(len(set1))] # creates a list of 1s of length set1
        for i in self.weights:
            # fills list
            a = i[0] # start
            b = i[1] # stop
            c = i[2] #weight
            for l in range(b-a+1):
                #replaces kilos[i] with weight value
                kilos[a+l] = c
            else:
                continue
        return np.ma.average(percent,weights = kilos)
    
    def get_range(self,set1,set2,data_range):
        # ranges the data
        x1 = data_range[0] # start of data
        x2 = data_range[1] # end of data
        if x1 or x2 == None:
            # stops incorrect data type
            print "None type in range please set range with valid intergers"
            raise TypeError
        if x1 > x2:
            # build in safe guard
            print "Range start after range stop please set new range with start before stop"
            raise ValueError
        if x1 or x2 < 0:
            # check there are no negatives
            print "Number in range is negative, they must be positive integers"
            raise ValueError
        if x1 == x2:
            # stops a single value being used
            print "range of 1 is not allowed"
            raise ValueError
        
        set1 = set1[x1:x2] # select data range
        set2 = set2[x1:x2]
        return set1,set2
    
    def compare(self,option = None):
        #combines the quality of fraction and comparison of d_spacing
        if option == "b" or "binary":
            return self.binary()
            
        elif option == "f" or "fraction":
            return self.fraction()
            
        else:
            return self.both()    
        
    
        
    