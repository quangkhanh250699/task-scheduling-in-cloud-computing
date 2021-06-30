#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Jun 27 13:59:32 2021

@author: quangkhanh
"""
import pandas as pd
import matplotlib.pyplot as plt 
import numpy as np
import seaborn as sns

np.random

plt.rcParams['figure.dpi'] = 400
plt.style.use('ggplot')
#%% 
running = pd.read_csv('data/learning_data/worstfit_cloudlet_running.csv')
cloudlets = pd.read_csv('data/resources/cloudlets_v6.csv')
#%%
df = pd.merge(cloudlets[['id', 'jobId', 'length']], running,
              left_on='id',
              right_on='cloudletId')
#%%
df = df[df.length < 1000]
#%% 
# Calculate probabilities of job status
job_count = df[['jobId', 'timestamp']].groupby('jobId').count()
job_count['prob'] = job_count.timestamp.apply(lambda x: 2 / (x + 1))
print('P(job running) = ', job_count.prob.mean())
#%%% Task running probabilities
task_count = df[['id', 'timestamp']].groupby('id').count()
task_count['prob'] = task_count.timestamp.apply(lambda x: 2 / (x + 1))
print('P(task running) = ', task_count.prob.mean())
#%%
p = (1 - task_count.prob.mean()) / (1- job_count.prob.mean())
print('P(task running | job running) = ', p)
