class Host: 
    id: int
    cpu_cores: int
    mips: int
    ram: int
    storage: int
    bandwidth: int
    

import pandas as pd 
import random 


id = 1
cpu_cores = 4
mips = 10000
ram = 4096 * 2
storage = 2 ** 20
bandwidth = 128

host_info = [{
    'id': id,
    'cpu_cores': cpu_cores,
    'mips': mips,
    'ram': ram,
    'storage': storage,
    'bandwidth': bandwidth
}]

df = pd.DataFrame(host_info)
df.to_csv('hosts_v1.csv', index=False)
