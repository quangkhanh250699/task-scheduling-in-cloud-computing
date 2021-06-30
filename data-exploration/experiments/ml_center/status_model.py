#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Apr 21 22:48:25 2021

@author: quangkhanh
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

import pickle

plt.rcParams['figure.dpi'] = 360
plt.style.use('ggplot')
#%% To-do
# =============================================================================
# 1. Extract running-cloudlet infomation and integrate with finish-infomation
#    to make learning-data 
# 2. Build a logistic regression to estimate the probability for a cloudlet
#    stopping after scheduling
# 3. Build a HMM model to learn the association between running cloudlets in 
#    the same machine, same duration
# 4. Test the performence of this approach
# =============================================================================
#%%
running = pd.read_csv('data/new/my_algo_cloudlet_running.csv')
kind = pd.read_csv('data/resources/cloudlets_v3.csv')
df = pd.read_csv('data/new/cloudlet_me.csv')
#%%
from data_structure.expected_segment import calculate_expected_joint
df = calculate_expected_joint(df, start='StartTime', end='finishTime')
df = pd.merge(df, kind[['id', 'kind']], 
              left_on='CloudletID', right_on='id', how='inner')
df = df[df['kind'] == 0]
#%%
running = pd.merge(running, kind[['id', 'kind']],
                   left_on='cloudletId',
                   right_on='id',
                   how='inner')
running = running[running['kind'] == 0]
#%%
df_info = df[['CloudletID', 'Priority', 'vmID', 'vmMIPS', 'cpuRequest',
              'StartTime', 'finishTime', 'expected_joint', 'Status']]
running_info = running[['startTime', 'timestamp', 'cloudletId', 'priority',
                        'cpuRequest', 'MIPS', 'vmID', 'numberRunningCloudlets',
                        'status']]

df_info = df_info[['StartTime', 'finishTime', 'CloudletID', 'Priority',
                   'cpuRequest', 'vmMIPS', 'vmID', 'expected_joint', 
                   'Status']]

df_info.columns = running_info.columns
#%%
cloudlet_running = pd.concat([df_info, running_info], axis=0)
#%%
short_queue = pd.read_csv('data/new/short_queue.csv')
cloudlet_running = pd.merge(cloudlet_running, short_queue,
                            left_on='cloudletId', right_on='taskId',
                            how='inner')
#%% extract features
data = cloudlet_running.copy()
data.status = data.status.apply(lambda x: 1 if x == 'SUCCESS' else 0)
data['duration'] = data['timestamp'] - data['startTime']
data = data[['queueId', 'cloudletId', 'vmID', 'duration', 'priority',
             'cpuRequest', 'MIPS', 'numberRunningCloudlets', 'status']]
#%% make independent variables
X_data = data[['duration', 'priority', 'cpuRequest',
               'MIPS', 'numberRunningCloudlets']].values
y_data = data['status'].values
#%% make train test data
from sklearn.model_selection import train_test_split

X_train, X_test, y_train, y_test = train_test_split(X_data, y_data,
                                                    test_size=0.2)
X_train, X_valid, y_train, y_valid = train_test_split(X_train, y_train, 
                                                      test_size=0.2)
#%%
from sklearn.preprocessing import MinMaxScaler
scaler = MinMaxScaler()
scaler = scaler.fit(X_train)
X_train_scaled = scaler.transform(X_train)
X_valid_scaled = scaler.transform(X_valid)
X_test_scaled = scaler.transform(X_test)
#%% build model
from sklearn.linear_model import LogisticRegression
lr = LogisticRegression()
lr = lr.fit(X_train_scaled, y_train)
#%% evaluate model
from sklearn.metrics import confusion_matrix

y_test_pre = lr.predict(X_test_scaled)
print(confusion_matrix(y_test, y_test_pre))
# array([[6270,    8],
#        [  17,  132]])
#%%
from sklearn.pipeline import Pipeline

scaler = MinMaxScaler()
lr = LogisticRegression()

pipeline = Pipeline(steps=[('scaler', scaler),
                           ('logistic regression', lr)])
pipeline.fit(X_train, y_train)
#%%
from sklearn.metrics import confusion_matrix

pipeline = pickle.load(open('models/status_model.pkl', 'rb'))
y_pred = pipeline.predict(X_test)

print(confusion_matrix(y_test, y_pred))
#%%
import pickle 

pickle.dump(pipeline, open('models/status_model.pkl', 'wb'))
#%%
data.to_csv('data/learning_data/status_data_v3.csv', index=False)





