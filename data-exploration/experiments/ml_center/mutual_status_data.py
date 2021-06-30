#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Apr 25 17:18:17 2021

@author: quangkhanh
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
import random

plt.rcParams['figure.dpi'] = 360
plt.style.use('ggplot')
#%%
df = pd.read_csv('data/learning_data/status_data_v3.csv')
cloudlet = pd.read_csv('data/resources/cloudlets_v3.csv')
print(df.info())
#%% 
cpu_request = {row.iloc[0]: row.iloc[1] for ind, row in cloudlet[['id', 'cpu']].iterrows()}
#%%
long_cloudlet = set(df[df.duration > 1000].cloudletId)
df = df[df.cloudletId.apply(lambda x: x not in long_cloudlet)]
#%%
class RunningData:
    
    def __init__(self, duration, A, B, cpuA, cpuB):
        self.duration = duration
        self.A = A
        self.B = B
        self.cpuA = cpuA
        self.cpuB = cpuB
        
def fill_nan_status(data):
    data = data.copy()
    for c in data: 
        for i in range(len(data[c])):
            if pd.isna(data[c].iloc[i]):
                data[c].iloc[i] = data[c].iloc[i-1]
    return data

def convert_data(running: RunningData):
    duration = running.duration
    cpuA = np.full_like(duration, fill_value=running.cpuA)
    cpuB = np.full_like(duration, fill_value=running.cpuB)
    return np.array([duration, cpuA, cpuB, running.A, running.B]).T
#%%

running_data = []
for _, queue_df in df.groupby('queueId'):
    for _, vm_df in queue_df.groupby('vmID'):
        data_df = vm_df.sort_values(by='duration')
        try: 
            data_status = data_df.pivot(index='duration', columns='cloudletId', 
                                        values='status')
        except:
            continue
        data_status = fill_nan_status(data_status)
        duration = np.array(data_status.index)
        cloudlet_ids = data_status.columns.values
        # choose all of cloudlet running pair
        for i in range(cloudlet_ids.shape[0] - 1):
            for j in range(i + 1, cloudlet_ids.shape[0]):
                A = data_status[cloudlet_ids[i]].values
                B = data_status[cloudlet_ids[j]].values
                cpuA = cpu_request[cloudlet_ids[i]]
                cpuB = cpu_request[cloudlet_ids[j]]
                running_data.append(RunningData(duration, A, B, cpuA, cpuB))
        
#%% make learning data
random.shuffle(running_data)
learning_data = []
for d in running_data:
    learning_data.append(convert_data(d))
learning_data = np.concatenate(learning_data, axis=0)
#%% drop nan
mask = np.isnan(learning_data)
mask = np.apply_along_axis(lambda x: not any(x), axis=1, arr=mask)
not_nan_data = learning_data[mask]
#%% save data
cleaned_df = pd.DataFrame(not_nan_data, 
                          columns=['duration', 'cpuA', 'cpuB', 'A', 'B'])
cleaned_df.to_csv('data/learning_data/mutual_status_data.csv', index=False)
