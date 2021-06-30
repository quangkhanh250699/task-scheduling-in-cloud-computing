#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Apr 26 19:23:44 2021

@author: quangkhanh
"""

import pickle
import pandas as pd
import numpy as np 
import matplotlib.pyplot as plt

import tensorflow as tf

plt.rcParams['figure.dpi'] = 360
plt.style.use('ggplot')
#%%
status_model = pickle.load(open('models/status_model.pkl', 'rb'))
mutual_model = tf.keras.models.load_model('models/mutual_status_model.h5')
#%%
df = pd.read_csv('data/learning_data/status_data_v3.csv')
#%% 
cloudlet = pd.read_csv('data/resources/cloudlets_v3.csv')
cpu_request = {row.iloc[0]: row.iloc[1] \
               for ind, row in cloudlet[['id', 'cpu']].iterrows()}
class Queue: 
    
    def __init__(self, cloudlet_id, cpus, priorities, mips):
        self.cloudlet_id = cloudlet_id
        self.cpus = cpus
        self.priorities = priorities
        self.mips = mips
        
class StatusPredictor:
    
    def __init__(self, status_model, mutual_model):
        self.status_model = status_model
        self.mutual_model = mutual_model
        
    def predict(self, queue: Queue, duration, observation_id):
        cloudlet_id = queue.cloudlet_id.reshape(-1)
        observed_index = np.apply_along_axis(
            lambda x: x in observation_id, axis=1, arr=cloudlet_id.reshape(-1, 1)
        )
        unobserved_index = np.apply_along_axis(
            lambda x: x not in observation_id, axis=1, arr=cloudlet_id.reshape(-1, 1)
        )
        observed_id = cloudlet_id[observed_index]
        unobserved_id = cloudlet_id[unobserved_index]
        observed_cpus = queue.cpus[observed_index]
        unobserved_cpus = queue.cpus[unobserved_index]
        
        prediction = {}
        for i in range(len(unobserved_id)):
            index = unobserved_id[i]
            probs = np.array([1, 1])
            for j in range(len(observed_id)):
                probs = probs * self._estimate_mutual_info(duration,
                                                           observed_cpus[j],
                                                           unobserved_cpus[i])
                probs /= probs.sum()
            prediction[index] = probs
        return prediction
        
    def _estimate_mutual_info(self, cpuA, cpuB, duration):
        x = np.array([[duration, cpuA, cpuB]])
        probs = self.mutual_model.predict(x)[0, [2, 3]]
        probs /= probs.sum()
        return probs
#%%
from data_structure.utils import replace_duration

def fill_nan_status(data):
    data = data.copy()
    for c in data: 
        for i in range(len(data[c])):
            if pd.isna(data[c].iloc[i]):
                data[c].iloc[i] = data[c].iloc[i-1]
    return data

sample = df[df.queueId == 20]
sample = sample[sample.vmID == 3]
sample = sample.sort_values(by='duration')
sample.duration = replace_duration(sample.duration, sample.status)
sample_pivot = sample.pivot(index='duration', columns='cloudletId', 
                            values='status')
sample_pivot = fill_nan_status(sample_pivot)
print(sample)
#%%