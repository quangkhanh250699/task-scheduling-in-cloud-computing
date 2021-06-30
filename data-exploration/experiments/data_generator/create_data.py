class Cloudlet: 
    
    def __init__(self, comming_time, cpu_cores, cpu, ram, storage, bandwidth, length):
        self.comming_time = comming_time
        self.cpu_cores = cpu_cores
        self.ram = ram
        self.storage = storage 
        self.bandwidth = bandwidth 
        self.length = length
        
def write_to_file(content: str, file_path: str):
    with open(file_path, 'w') as file: 
        file.write(content)
        

import random 
import json 
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plot 
import numpy as np 

#%%


cpu_cores = 2
storage_min = 8
memory_min = 4
bandwidth_min = 4
length_min = 200

N_TASKS = 1000


cloudlets = []
for i in range(N_TASKS):
    cloudlet = {}
    cloudlet['comming_time'] = random.randint(i, i+10)
    cloudlet['cpu_cores'] = cpu_cores
    cloudlet['cpu'] = random.randint(1, 60) / 100
    cloudlet['ram'] = random.randint(1, 64) * memory_min / 10
    cloudlet['storage'] = random.randint(1, 32) * storage_min 
    cloudlet['bandwidth'] = random.randint(1, 16) * bandwidth_min 
    cloudlet['length'] = random.randint(1, 100) * length_min
    cloudlets.append(cloudlet)
    
json_string = json.dumps(cloudlets, indent=4)

write_to_file(json_string, "cloudlets_v1.json")

df = pd.DataFrame(cloudlets)
df = df.sort_values(by="comming_time")
df['id'] = list(range(df.shape[0]))
df = df[['id', 'comming_time', 'cpu_cores', 'cpu', 'ram', 'storage', 'bandwidth', 'length']]
print(df)

#%%
df.to_csv("data/cloudlets_v1.csv", index=False)
