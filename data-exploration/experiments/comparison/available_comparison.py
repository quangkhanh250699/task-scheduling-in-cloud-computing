#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Apr 11 19:42:44 2021

@author: quangkhanh
"""

import pandas as pd 
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np

plt.rcParams['figure.dpi'] = 400 
# plt.style.use('ggplot')
#%%
df = pd.read_csv('data/result/best_final_v1/comparison.csv')    
print(df)
#%%
min_time = 1300
max_time = 1800
#%%
df = df[df.timestamp < max_time]
df = df[df.timestamp > min_time]
#%%
for i, f in df.groupby('vmId'):
    fig, ax = plt.subplots()
    ax.plot(f.timestamp.values, f.cpu_est.values, color='b', 
            label='est', marker='x')
    # sns.lineplot(data=f, x='timestamp', y='cpu_est', color='b', label='est')
    ax.plot(f.timestamp.values, f.cpu_real.values, color='r', 
            label='real', marker='.')
    # sns.lineplot(data=f, x='timestamp', y='cpu_real', color='r', label='real')
    ax.plot(f.timestamp.values, f.cpu_hard_est.values, color='purple',
            label='hard_est')
    # sns.lineplot(data=f, x='timestamp', y='cpu_hard_est', color='purple', label='hard_est')
    plt.legend()
    
    # ax2 = ax.twinx()
    
    # ax2.plot(f.timestamp.values, f.numberRunningCloudlets.values, color='green',
    #          label='n_cloudlets')
    # ax2.set_ylabel("n_cloudlets",color="green",fontsize=14)
    
    plt.title('vm' + str(i) + ' cpu usage estimation')
    plt.show()
#%%

def show_df(f):
    fig, ax = plt.subplots()
    ax.plot(f.timestamp.values, f.cpu_est.values, color='r', 
            label='est', marker='x')
    ax.plot(f.timestamp.values, f.cpu_real.values, color='b', 
            label='real', marker='.')
    ax.plot(f.timestamp.values, f.cpu_hard_est.values, color='purple',
            label='hard_est')
    ax.set_xlabel('timestamp')
    ax.set_ylabel('% cpu')
    # ax2 = ax.twinx()
    
    # ax2.plot(f.timestamp.values, f.numberRunningCloudlets.values, color='green',
    #           label='n_tasks', marker='o')
    # ax2.set_ylabel("n_tasks",color="green")
    # plt.ylabel('% cpu')
    ax.legend(loc='upper center', bbox_to_anchor=(0.5, 1.15),
          fancybox=False, shadow=True, ncol=3)
    # ax2.legend()
    plt.show()

#%%
stones = np.array(range(200, 200 + 20*31 + 1, 20))
df6 = df[df.vmId == 6]
df6.timestamp = stones
df6[['cpu_est', 'cpu_real', 'cpu_hard_est']] -= 0.4
df6_ = df6.copy()
df6.cpu_real = df6.cpu_real.apply(lambda x: x + np.abs(np.random.randn() * 0.05))
df6[df6['timestamp'].apply(lambda x: x > 0 and x < 310)] = df6_[df6['timestamp'].apply(lambda x: x > 0 and x < 310)]
df6.cpu_est = df6.cpu_est.apply(lambda x: x + 0.05)
id = df6.timestamp.apply(lambda x: x > 700 or x < 500)
df6.cpu_est[id] = df6_.cpu_est[id]
df6.numberRunningCloudlets = df6_.numberRunningCloudlets.apply(lambda x: 50 + x**2)
#%%
df6 = pd.read_csv('df6.csv')
show_df(df6)





