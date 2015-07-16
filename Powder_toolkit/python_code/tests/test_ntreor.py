import sys
sys.path.append("../..")
from python_code.ntreor import Ntreor
from python_code.Loader import Loader

class test:
    
    def read_test(self):
        data = Loader().load_data_ntreor('python_code/testdata/test1.hkl')
        nt = Ntreor(data,'treor90_output','python_code/testdata/')
        output = nt.read_output()
        assert output != None
    
    def write_test(self):
        data = Loader().load_data_ntreor('python_code/testdata/test1.hkl')
        nt = Ntreor(data[:,3],'ntreor_test','python_code/testdata/')
        nt.set_keywords('CHOICE', 2)
        nt.write_input()
        with open('python_code/testdata/ntreor_test.dat','r') as f:
            with open('python_code/testdata/ntreor_input_test.txt','r') as g:
                glines = g.readlines()
                flines = f.readlines()
                for l1,l2 in zip(glines,flines):
                    assert l1 == l2
                g.close()
            f.close()

    def call_test(self):
        nt = Ntreor('blank data','ntreor_test','python_code/testdata/')
        assert nt.call() == 0
    
    def help_test(self):
        nt = Ntreor('blank data','ntreor_test','python_code/testdata')
        helper = nt._keylist_()
        assert helper == None
        
if __name__ == '__main__':
    test().call_test()        