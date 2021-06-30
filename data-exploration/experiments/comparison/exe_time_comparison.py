#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat May 29 12:21:31 2021

@author: quangkhanh
"""

import pandas as pd 
#%% 
max_timestamp = 1000
#%%
# cdf1 = pd.read_csv("data/new/cloudlet_rr.csv")
# cdf2 = pd.read_csv("data/new/cloudlet_bestfit.csv")
# cdf3 = pd.read_csv("data/new/cloudlet_me.csv")
cdf4 = pd.read_csv("data/new/cloudlet_balancing.csv")
cdf5 = pd.read_csv("data/new/cloudlet_worstfit.csv")
#%%
# cdf1 = cdf1[cdf1.finishTime < max_timestamp]
# cdf2 = cdf2[cdf2.finishTime < max_timestamp]
# cdf3 = cdf3[cdf3.finishTime < max_timestamp]
cdf4 = cdf4[cdf4.finishTime < max_timestamp]
cdf5 = cdf5[cdf5.finishTime < max_timestamp]
#%%
print("------------------ Round Robin--------------------")
# print(cdf1.exeTime.describe())
print("------------------ Best Fit --------------------")
# print(cdf2.exeTime.describe())
print("------------------ My algorithm--------------------")
# print(cdf3.exeTime.describe())
print("------------------ Balancing algorithm--------------------")
print(cdf4.exeTime.describe())
print("------------------ Worstfit algorithm--------------------")
print(cdf5.exeTime.describe())
#%%
result = pd.DataFrame({
                'FCFS': cdf1.exeTime.describe(),
                'Worstfit': cdf5.exeTime.describe(),
                'Balancing Algo': cdf4.exeTime.describe()
            })
print(result)
#%%
result.to_csv('result.csv')