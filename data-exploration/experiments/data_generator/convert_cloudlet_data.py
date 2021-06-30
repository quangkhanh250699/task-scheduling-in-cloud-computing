#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Apr 17 09:53:25 2021

@author: quangkhanh
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

plt.rcParams['figure.dpi'] = 360
plt.style.use('ggplot')
#%%
df = pd.read_csv('data/extracted_data/task_info.csv')
#%%
df['cpu_cores'] = 2
df['bandwidth'] = 0
df['kind'] = 0
df = df.reset_index()
#%%
cloudlets = df[['index', 'start time', 'priority', 'cpu_cores', 
                'CPU request', 'memory request', 'bandwidth', 
                'disk space request', 'length', 'kind']]

cloudlets.columns = ['id', 'coming_time', 'priority', 'cpu_cores', 
                     'cpu', 'ram', 'bandwidth', 'storage', 'length', 'kind']
#%%
coming_time = np.random.randint(0, 3600, size=cloudlets.shape[0])
coming_time.sort()
cloudlets.coming_time = coming_time
#%%
cloudlets.length = (cloudlets.length / 4).apply(lambda x: int(x))
#%%
cloudlets.kind = (cloudlets.length > 300).astype('int')
cloudlets.length = cloudlets.length.apply(lambda x: x if x <= 300 else 100000)
#%%
cloudlets.to_csv('/home/quangkhanh/F/Graduation Project/Programs/datacenter-simulation/src/main/resources/test_data/cloudlets_v5.csv', index=False)
#%%
cpu_min = 0.01
cpu_max = 0.03
ram_min = 1
ram_max = 4
bw = 0
size = 100
coming_time = np.random.randint(0, 1500, size=size)

from data_generator.generate_cloudlet_data import generate_long_term
long_cloudlets = generate_long_term(coming_time, cpu_min, cpu_max,
                                    ram_min, ram_max,
                                    bw, bw, 
                                    size,
                                    np.full(size, 1))
long_cloudlets = pd.DataFrame(long_cloudlets)
#%%
cloudlets = pd.concat([cloudlets, long_cloudlets], axis=0)
#%%
cloudlets = cloudlets.sort_values(by='coming_time')
cloudlets.id = range(cloudlets.shape[0])
#%%
sns.scatterplot(data=cloudlets, x='cpu', y='ram', hue='kind')
#%%
sns.jointplot(data=cloudlets, x='cpu', y='ram')
#%%
cloudlets.to_csv('/home/quangkhanh/F/Graduation Project/Programs/cloudsim-plus-examples/src/main/resources/test_data/cloudlets_v5.csv', index=False)