class Host: 
    cpu_cores: int
    mips: int
    ram: int
    storage: int
    bandwidth: int
    

import pandas as pd 
import numpy as np

    
kind = 1
cpu_cores_min = 4
mips = [1000, 2000, 4000]
ram = [4096, 8012, 12288]
storage = 2 ** 10 * 32
bandwidth = 16
N_VMS = 10

vm_info = []

for i in range(N_VMS):
    vm = {}
    vm['kind'] = i
    vm['cpu_cores'] = cpu_cores_min
    vm['mips'] = mips[np.random.randint(0, 3)]
    vm['ram'] = ram[np.random.randint(0, 3)]
    vm['storage'] = storage
    vm['bandwidth'] = bandwidth
    vm_info.append(vm)
    
df = pd.DataFrame(vm_info)
df.to_csv('data/vms_v3.csv', index=False)
#%%
df.to_csv('/home/quangkhanh/F/Graduation Project/Programs/cloudsim-plus-examples/src/main/resources/test_data/vms_v3.csv', index=False)
