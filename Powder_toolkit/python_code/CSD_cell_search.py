import sys
#sys.path.append("/dls_sw/apps/ccdc/api-0.7.0/ccdc")
#sys.path.append("/home/sfz19839/Desktop/api")

from ccdc.search import ReducedCellSearch
#from ccdc.collections import namedtuple
from collections import namedtuple

class CSD_cell_search:
    """
    CSD_cell_search Class doc string
    ##################################
    
    CSDCS is designed to enable the user to search for a crystal[or a list of crystals] via
    cell parameters a,b,c alpha,beta,gamma 
    
    Attributes
    ------------------
    angles [list length 3]
    cell_lengths [list length 3]
    lattice_centring [string see ccdc docs for allowed strings]
    
    
    Methods
    ------------------
    
    __init__()
    ------------
    takes 3 arguments self,angles,lengths
    
    search()
    ------------
    uses all atr
    converts lists to named tuples
    calls ccdc ReducedCellSearch using tuples
    returns tuple(h.identifier, h.crystal.cell_lengths, h.crystal.cell_angles, h.crystal.lattice_centring)
     """
    
    
    def __init__(self):
        pass
    def set_angles(self,angles):
        self.angles = angles
    def set_cell_lengths(self,lengths):
        self.cell_lengths = lengths
    def set_lattice_centring(self,centring):
        self.lattice_centring = centring
    def set_number_hits(self,hits):
        self.numhits = hits  
    
    
    def search(self):
        # searches the csd data base for a crystal with known parameters returns best result
        cellA = namedtuple('CellAngles', ['alpha', 'beta', 'gamma']) # create a named tuple for ccdc module
        angles = list(self.angles) # make a list of angles
        more_angles = cellA(float(angles[0]),float(angles[1]),float(angles[2])) # append angles to the tuple
        cellL = namedtuple('CellLengths',['a','b','c'])
        cell_lengths = self.cell_lengths
        more_lengths = cellL(float(cell_lengths[0]),float(cell_lengths[1]),float(cell_lengths[2]))
        
        query = ReducedCellSearch.Query(lengths = more_lengths,angles = more_angles ,lattice_centring = 'primitive' )
        searcher = ReducedCellSearch(query)
        
        hits = searcher.search() # search for all hits
        i = 0
        while i < float(self.numhits):
            h = hits[i]
            print h.identifier, h.crystal.cell_lengths, h.crystal.cell_angles, h.crystal.lattice_centring # print the best result 
            i += 1
        

if __name__ == '__main__':
    CSD = CSD_cell_search()
    x1 = sys.argv[1]
    x2 = sys.argv[2]
    x3 = sys.argv[3]
    CSD.set_cell_lengths([x1,x2,x3])
    Y1 = sys.argv[4]
    Y2 = sys.argv[5]
    Y3 = sys.argv[6]
    myhits = sys.argv[8]
    CSD.set_angles([Y1,Y2,Y3])
    CSD.set_lattice_centring(sys.argv[7])
    CSD.set_number_hits(myhits)
    CSD.search()