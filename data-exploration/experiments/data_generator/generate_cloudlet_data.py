#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Apr  4 09:00:23 2021

@author: quangkhanh
"""

import pandas as pd
import numpy as np 
import matplotlib.pyplot as plt
import seaborn as sns 
plt.rcParams['figure.dpi'] = 360
#%%

priority_factor = pd.DataFrame([
        (0.8, ),
        (0.15, ),
        (0.05, )
    ], columns=('prob', ))

kind_factor = pd.DataFrame([
        (0.1, 0.9),
        (0.8, 0.2),
        (1.0, 0.0)    
    ], columns=("long_term", "short_term"))

#%% 

CPU_CORES = 2
STORAGE_MIN = 8 
STORAGE_MAX = 16
LONG_TERM_LENGTH = 100000


def generate_long_term(coming_times,
                       cpu_min, cpu_max,
                       ram_min, ram_max,
                       bw_min, bw_max, 
                       size,
                       priorities):
    cloudlets = []
    for i in range(size):
        cloudlet = {} 
        cloudlet['coming_time'] = coming_times[i]
        cloudlet['priority'] = priorities[i]
        cloudlet['cpu_cores'] = CPU_CORES
        cloudlet['cpu'] = round(np.random.rand() * (cpu_max - cpu_min) + cpu_min, 2)
        cloudlet['ram'] = round(np.random.rand() * (ram_max - ram_min) + ram_min, 2)
        cloudlet['bandwidth'] = int(np.random.rand()*(bw_max - bw_min) + bw_min)
        cloudlet['storage'] = int(np.random.rand() * (STORAGE_MAX - STORAGE_MIN) + STORAGE_MIN)
        cloudlet['length'] = LONG_TERM_LENGTH
        cloudlet['kind'] = 1
        cloudlets.append(cloudlet)
    return cloudlets
#%%

coeff_1 = 500
coeff_2 = 2
coeff_3 = 20

noise_mean = 100
noise_var = 20

def generate_short_term(coming_times,
                        mean, sigma,
                        bw_min, bw_max, 
                        size, 
                        priorities): 
    resources = np.random.multivariate_normal(mean, sigma, size=size)
    resources = np.abs(resources) + 0.001
    noise = np.random.randn(size) * noise_var + noise_mean
    cloudlets = []
    for i in range(size):
        resource = resources[i, :]
        cloudlet = {}
        cloudlet['coming_time'] = coming_times[i]
        cloudlet['priority'] = priorities[i]
        cloudlet['cpu_cores'] = CPU_CORES
        cloudlet['cpu'] = resource[0] if resource[0] < 1 else 0.9
        cloudlet['ram'] = resource[1]
        cloudlet['bandwidth'] = int(np.random.rand()*(bw_max - bw_min) + bw_min)
        cloudlet['storage'] = int(np.random.rand() * (STORAGE_MAX - STORAGE_MIN) + STORAGE_MIN)
        cloudlet['length'] = int(np.abs(coeff_1 * resource[0] + coeff_2 * resource[1] + coeff_3))
        cloudlet['kind'] = 0
        cloudlets.append(cloudlet)
    return cloudlets 

#%% 
N_CLOUDLETS = 3000

BW_MIN = 0
BW_MAX = 0
CPU_MIN = 0.02
CPU_MAX = 0.1
RAM_MIN = 0.2
RAM_MAX = 1
MEAN = [0.04, 4]
SIGMA = [[0.0000001, 0.09], [0.09, 1]]

MIN_COMING_TIME = 0 
MAX_COMING_TIME = 3000

priorities = np.random.choice(a=[0, 1], size=N_CLOUDLETS, p=[0.9, 0.1])

N_PRODUCTION = priorities.sum()
N_FREE = N_CLOUDLETS - N_PRODUCTION

production_type = np.random.choice(a=[0, 1], size=N_PRODUCTION, p=[0.2, 0.8])
free_type = np.random.choice(a=[0, 1], size=N_FREE, p=[0.9, 0.1])

cloudlet_type = []
pro_i = -1
fre_i = -1
for i in range(priorities.shape[0]):
    if priorities[i] == 1: 
        pro_i += 1
        cloudlet_type.append(production_type[pro_i])
    else: 
        fre_i += 1
        cloudlet_type.append(free_type[fre_i])

cloudlet_type = np.array(cloudlet_type)
N_LONG_TERM = sum(cloudlet_type)
N_SHORT_TERM = N_CLOUDLETS - N_LONG_TERM

short_term_priorities = priorities[np.where(cloudlet_type==0)]
long_term_priorities = priorities[np.where(cloudlet_type==1)]

coming_times = np.random.randint(MIN_COMING_TIME, MAX_COMING_TIME, N_CLOUDLETS)
short_coming_time = coming_times[:N_SHORT_TERM]
long_coming_time = coming_times[N_LONG_TERM:]
short_cloudlets = generate_short_term(short_coming_time, MEAN, SIGMA, BW_MIN, BW_MAX, N_SHORT_TERM, short_term_priorities)
long_cloudlets = generate_long_term(long_coming_time, CPU_MIN, CPU_MAX, RAM_MIN, RAM_MAX, BW_MIN, BW_MAX, N_LONG_TERM, long_term_priorities)

cloudlets = short_cloudlets + long_cloudlets

df = pd.DataFrame(cloudlets).sort_values(by='coming_time')
df['id'] = list(range(df.shape[0]))
df = df[['id', 'coming_time', 'priority', 'cpu_cores', 'cpu', 'ram', 
              'bandwidth', 'storage', 'length', 'kind']]

#%%
sns.scatterplot(data=df, x='cpu', y='ram', hue='priority')
#%%
df.to_csv('data/cloudlets_v4.csv', index=False)
#%%
df.to_csv('/home/quangkhanh/F/Graduation Project/Programs/cloudsim-plus-examples/src/main/resources/test_data/cloudlets_v4.csv', index=False)
#%%
df = pd.read_csv('data/cloudlets_v3.csv')
    
    
    
    

