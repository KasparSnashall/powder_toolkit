import sys
import math
import scisoftpy as dnp
import os
#import scisoft
#import numpy as np

class Loader:
    """
    Loader Class doc string
    ###########################
    Class designed to handle all loading of files or data sets
    
    Attributes
    -----------------
    None
    
    Methods
    -------------------
    
    __init__()
    -------------
    1 argument self
    pass
    
    load_data()
    -----------
    used only in tests (to be removed)
    4 arguments self,data1[list or str], data2[list or str], colnum = 3 [int column to be used]
    calls other functions to make data list
    or if all data is list
    returns data1,data2
    
    load_hkl_data()
    ------------------
    2 arguments self, str filepath
    loads a hkl file using scisoftpy
    loads third column
    returns list
    
    load()
    -------------
    main function
    input string filename
    output error or switch to appropriate loading mechanism
    """
    
    def __init__(self,data,myrange,upper,lower):#,upper,lower):
        self.data = data #filepath
        self.myrange = myrange # range boolean string
        self.upper = upper # upper limit
        self.lower = lower # lower limit
        
    def load_data(self,data1,data2,colnum = 3):
        # load in all data
        print "Loading data"
        
        data = data1,data2
        print data
        new_data = []
        for d in data:
            print d
            # find the data files or continue
            if type(d) == str:
                if os.path.exists(d):
                    # check if path is there
                    # find file type if not known then raise type error
                    if '.hkl' in d:
                        new_data.append(self.load_hkl_data(d))
                    else:
                        print "data file type not known, try making it into another type?"
                        raise TypeError
                else:
                    print "No such file exists"
                    raise ValueError
        print "data loaded successfully"
        print new_data
        return new_data[0],new_data[1]
        
        
    def load_hkl_data(self):
        # loads a hkl file colnum  is 3,4 for d_space,intensity respectively
        d1 =  dnp.io.load(self.data,format='text')
        d1 = d1[0] # get the data array from dnp array
        d1 = d1[:,3] # return the 4th column intensity
        d1 = d1[1:] # skip the header, del or pop is not allowed
        d1 = [float(i) for i in d1] # make floats
        if self.myrange == True:
            d1 = d1[int(self.lower):int(self.upper)] # as its hkl you want these as ints
            
        return d1

    
    def load(self):
        if self.myrange == 'false':
            self.myrange = False
        elif self.myrange == 'true':
            self.myrange = True
            try:
                self.upper = float(self.upper)
                self.lower = float(self.lower)
                if self.upper < self.lower:
                    return "Upper limit must be greater then lower limit"

            except:
                return "Range must be numbers"
            
        if os.path.exists(self.data):
            # check if path is there
            # find file type if not known then raise type error
            if '.hkl' in self.data:
                return self.load_hkl_data()
            if '.xye' in self.data:
                pass
            if '.gsas' in self.data:
                pass
            else:
                return "Data file type "+os.path.splitext(self.data)[1]+" not known, try a different type or ensure it has correct file extension."""
        else:
            return "No such file exists"
            
    

