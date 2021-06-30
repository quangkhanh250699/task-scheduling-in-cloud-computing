#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Apr 21 20:19:49 2021

@author: quangkhanh
"""

import pandas as pd
import matplotlib.pyplot as plt 
import numpy as np
import seaborn as sns

plt.rcParams['figure.dpi'] = 360
plt.style.use('ggplot')
#%%
df = pd.read_csv('data/new/my_algo_cloudlet_running.csv')
queue_match = pd.read_csv('data/new/short_queue.csv')
#%%
cloudlet = pd.merge(df, queue_match,
                    left_on='cloudletId',
                    right_on='taskId',
                    how='inner')
#%%
running_cloudlet = cloudlet[['queueId', 'cloudletId', 'startTime', 'timestamp',
                             'numberRunningCloudlets', 'cpuRequest', 'mode']]
running_cloudlet['duration'] = running_cloudlet.timestamp - running_cloudlet.startTime
running_cloudlet = running_cloudlet.sort_values(by=['queueId', 'duration'])
long_cloudlet_ids = set(running_cloudlet[running_cloudlet.duration > 1000].cloudletId)
running_cloudlet = running_cloudlet[running_cloudlet.cloudletId.apply(lambda x: x not in long_cloudlet_ids)]
#%%
state = running_cloudlet[['queueId', 'duration']]\
            .groupby(['queueId', 'duration']).agg(lambda frame: frame.shape[0])
state = pd.DataFrame({'numberRunning': state}).reset_index()
#%%
sch = []
sub = []

for _, frame in state.groupby('queueId'):
    last = None
    last_time = None
    for index, row in frame.reset_index().iterrows():
        if last is None: 
            last = row['numberRunning']
            last_time = row['duration']
        else: 
            now = row['numberRunning']
            element = (last_time, row['duration'] - last_time,\
                       float(now / last))
            if index % 2 == 0: # in scheduling mode -> last is in submitting 
                sub.append(element)
            else: # in submitting mode -> last is in scheduling mode
                sch.append(element)
            last = now
            last_time = row['duration']
sch = np.array(sch)
sub = np.array(sub)
#%%
plt.scatter(sub[:, 0], sub[:, 1])
#%%
from sklearn.linear_model import LinearRegression
lr = LinearRegression()
k = 0
da = sch
lr = LinearRegression()
lr = lr.fit(da[k:, :2], da[k:, 2])
print(lr.coef_, lr.intercept_)
#%%
def generate_matrix():
    a = np.random.rand()
    b = np.random.rand()
    return np.array([[0, 1-a, a],
                     [1-b, 0, b],
                     [0, 0, 1]])

x = np.array([1, 0, 0])
for i in range(100):
    x = x.dot(generate_matrix())

print(x)
