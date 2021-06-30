#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Jun 26 22:48:52 2021

@author: quangkhanh
"""

import pandas as pd 
import matplotlib.pyplot as plt
import seaborn as sns 
import numpy as np
plt.rcParams['figure.dpi'] = 400
plt.style.use('ggplot')
#%% 
df = pd.read_csv('data/resources/cloudlets_v5.csv')
#%%
n_jobs = 1000
position = [0]
current = 0
while current < df.shape[0]: 
    current += np.random.randint(2, 5)
    if current < df.shape[0]: 
        position.append(current)
if position[-1] >= df.shape[0]: 
    postion[-1] = df.shape[0]
else: 
    position.append(df.shape[0])
#%%
df['jobId'] = int(-1)
last = -1
current = 0
index = 0
while current < len(position) - 1:
    while index < position[current + 1]:
        df.at[index, 'jobId'] = current
        index += 1
    current += 1
df = df.head(n=df.shape[0] - 1)
#%%
job_length = df[['jobId', 'length']].groupby('jobId')\
    .agg(lambda x: int(x.mean())).length
times = df[['jobId', 'coming_time']].groupby('jobId')\
    .agg(lambda x: int(x.mean())).coming_time
#%%
df.length = df.apply(lambda row: job_length[row['jobId']], axis=1)
df.coming_time = df.apply(lambda row: times[row['jobId']], axis=1)
#%%
df.kind = df.length.apply(lambda x: 0 if x < 1000 else 1)
#%%
df = df[['jobId', 'id', 'coming_time', 'priority', 'cpu_cores', 'cpu', 'ram', 'bandwidth',
       'storage', 'length', 'kind']]
#%%
df.to_csv('data/resources/cloudlets_v6.csv', index=False)