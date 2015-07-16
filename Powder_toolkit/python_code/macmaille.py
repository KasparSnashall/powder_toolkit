from Loader import Loader
import numpy as np
from iotbx.shelx.parsers import wavelength_parser
np.set_printoptions(suppress = True)

class Macmaille:
    
    dict_list = {'Symmetry codes':None,'W': None,'Nind':None,'Pmin':None, 'Pmax':None, 'Vmin':None, 'Vmax':None, 'Rmin':None, 'Rmax':None, 'Rmaxref':None,'Spar':None, 'Sang':None,'Ntests':None, 'Nruns':None}
    duplicates = {}
    topnumber = 5
    tolerance = 2 # in percent
    M20_complete = []
    F20_complete = []
    
    
    def __init__(self,data,wavelength,zeropoint,ngrid,title):
        print "McMaille program"
        self. data = data
        self.wavelength = wavelength
        self.zeropoint = zeropoint
        self.ngrid = ngrid
        self.title = title
        self.keywordflag = 0
        
        
    
    def set_keywords(self,key,value):
        # add keywords to dict
        self.dict_list[str(key)] = str(value)
        self.keywordflag = 1
    
    def get_keywords(self):
        #returns the keywords
        return self.dict_list
    
    def set_wavelength(self,wavlength):
        self.wavelength = wavelength
        
    def get_wavelength(self):
        return self.wavelength
    def set_zeropoint(self,zeropoint):
        self.zeropoint = zeropoint
    def get_zeropoint(self):
        return self.zeropoint
    def set_ngrid(self,ngrid):
        self.ngrid = ngrid
    def get_ngrid(self):
        return self.ngrid
    def set_title(self,title):
        self.title = title
    def get_title(self):
        return self.title

    def check_keywords(self):
        # checks values are not None
        for k,v in self.dict_list.iteritems():
                if v == None:
                    print "You have not entered a value for "+k+"\nall additional parameters must be entered or add_keywords should not be used" 
                    raise ValueError 
                else:
                    continue
    
    def writeinput(self,filepath):
        #writes the input .dat file
        data = Loader().load_data_mac(self.data)
        data1 = data[0:20,(3,4)] # get column 3,4 # select first 20 rows
        # next write dat file named title.dat
        with open(filepath + self.title+'.dat','w') as f:
            f.write(self.title +'\n') # set title
            options = str(self.wavelength) +" "+str(self.zeropoint)+" "+ str(self.ngrid)+'\n'
            f.write(options) # write in file options        
            if self.keywordflag == 1:
                # check none of the items are None else return error
                self.check_keywords()
                #after chcck writes the additional parameters in format required by macmaille
                d = self.dict_list
                f.write(d['Symmetry codes']+"\n")
                f.write(d['W']+' '+d['Nind']+"\n")
                f.write(d['Pmin']+" " +d['Pmax']+" "+ d['Vmin'] +" "+d['Vmax']+" "+d['Rmin']+" "+d['Rmax']+" "+d['Rmaxref']+"\n")
                f.write(d['Spar']+" "+d['Sang']+"\n")
                f.write(d['Ntests']+" "+d['Nruns']+"\n")            
            f.write("!!! \n") # blank line signals beginning of data
            for x in data1:
                a = '%f' % x[0] # read values as floats with no scientific notation
                b = '%f' % x[1]
                c = str(a)+ " " + str(b) + "\n" # combine values to make line
                f.write(c) # write line
            f.close()
    
    def _make_floats(self,arrays):
            #makes floats of an array, internal function
            temparray = []
            for j in arrays:
                templist = []
                for i in j:
                    try:
                        k = float(i)
                        templist.append(k)
                    except:
                        continue
                temparray.append(templist)
            return temparray

    def read_output(self,file_name):
        # reads the full output of macmaille indexing program
        with open(file_name,'r') as f:
            lines = f.readlines()
            linenum = 0 # line number
            list1 = [] # a list of f20 lines
            list2 = []# a list of m20 lines
            for i in lines:
                linenum += 1 # add a line
                if "FINAL LIST OF CELL PROPOSALS, sorted by F(20)" in i:
                    for j in range(20):
                        list1.append(lines[linenum + j + 7]) # takes 20 lines after line i + 7
                if "FINAL LIST OF CELL PROPOSALS, sorted by M(20)" in i:
                    for j in range(20):
                        list2.append(lines[linenum + j + 7]) # takes 20 lines after line i + 7
            F20 = [] # final data arrays
            M20 = []
            for i in list1:
                j = i.split() # splits into numbers
                F20.append(j)
            for i in list2:
                j = i.split() # splits into numbers
                M20.append(j)
                
            self.F20_complete = np.array(F20) # complete data array including bravis lattice letter sorted by F20
            self.M20_complete = np.array(M20)
            f.close() # close the file
        
        M20 = self._make_floats(M20) # make the list into floats only list numpy has no easy option for this
        M20 = sorted(M20, key=lambda a_entry: a_entry[2]) # sort rows by volume 
        F20 = self._make_floats(F20)
        F20 = sorted(F20, key=lambda a_entry: a_entry[2])
        M20 = np.array(M20,dtype = float) # make list into array
        F20 = np.array(F20,dtype = float)
        # data is now sorted volume wise and duplicates need to be taken out
        # _take_top removes all duplicates (volume) with in a certain percentage tolerance
        
        M20 = np.array(self._take_top(M20,"M",self.tolerance),dtype = float)[:,4:10] # get top fitting data by volume and only the columns that count 
        F20 = np.array(self._take_top(F20,"F",self.tolerance),dtype = float)[:,4:10]
        
        return M20,F20
    
    def _take_top(self,myarray,name,tolerance):
        # takes top n values within tolerance and gives name+number to duplicates
        if self.topnumber > 19 or self.topnumber < 0:
            print " you max number of choices is 20 and must not be negative"
            raise ValueError
            
        duplicates = [] # list of duplicates
        volumes = myarray[:,2] # column of volumes
        for i in range(19):
                # check over first 19 in list (the list is already sorted so the biggest one need not be included)
            tol = self.tolerance*volumes[i]/100 #percentage tolerance
            if volumes[i] -tol < volumes[i+1] < volumes[i] +tol:
                duplicates.append(int(volumes[i])) # append to list if in tolerance
        toplist = [] # list of top data sorted by volume excluding duplicates
        i = 0 # a counter
        dupnum = 0 # a name counter
            
        while len(toplist) < self.topnumber:
            data = myarray[19-i,:] # start taking data beginning with largest volume [19] as list is 0 to 19
            vol = int(data[2]) # get the volume as an int
            if vol in duplicates:
                    # check if vol in duplicates
                    self.duplicates[name+str(dupnum)] = data # add to dict with name+number and value
                    i += 1 
                    dupnum += 1
            else:
                toplist.append(data) # else add to the best list
                i +=1
                    
        return toplist
    
    def call(self):
        # this will call the macmialle fortran program
        return 1
#
#
#
if __name__ == "__main__":
    mac = Macmaille('/home/sfz19839/DAWN_stable/comparison/testdata/test1.hkl',1.541800, 0.0000, 0,'mac_test')
    num1,num2 = mac.read_output('/home/sfz19839/DAWN_stable/comparison/testdata/mcm_output.imp')
    print num1,num2