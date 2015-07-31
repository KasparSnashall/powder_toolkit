import sys
#sys.path.append("/dls_sw/apps/ccdc/api-0.7.0/ccdc")
sys.path.append("/home/sfz19839/Desktop/api")

from ccdc.search import ReducedCellSearch
from ccdc.collections import namedtuple


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
        1
    def Printme(self):
        return "here"
    
    def get_angles(self):
        return self.angles
    def set_angles(self,angles):
        print angles
        self.angles = angles
    def get_cell_lengths(self):
        
        return self.cell_lengths
    def set_cell_lengths(self,lengths):
        print lengths
        self.cell_lengths = lengths
    def get_lattice_centring(self):
        return self.lattice_centring
    def set_lattice_centring(self,centring):
        self.lattice_centring = centring
    
    def search(self):
        # searches the csd data base for a crystal with known parameters returns best result
        cellA = namedtuple('CellAngles', ['alpha', 'beta', 'gamma']) # create a named tuple for ccdc module
        angles = list(self.angles) # make a list of angles
        more_angles = cellA(angles[0],angles[1],angles[2]) # append angles to the tuple
        
        cellL = namedtuple('CellLengths',['a','b','c'])
        cell_lengths = self.cell_lengths
        more_lengths = cellL(cell_lengths[0],cell_lengths[1],cell_lengths[2])
        
        query = ReducedCellSearch.Query(lengths = more_lengths,angles = more_angles ,lattice_centring = 'primitive' )
        searcher = ReducedCellSearch(query)
        
        hits = searcher.search() # search for all hits
        h = hits[0] # top hit
        print h.identifier, h.crystal.cell_lengths, h.crystal.cell_angles, h.crystal.lattice_centring # print the best result 

