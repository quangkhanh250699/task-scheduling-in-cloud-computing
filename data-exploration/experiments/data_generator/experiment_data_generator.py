#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jun 25 10:30:32 2021

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
data = df.sample(n = 450)
#%%
max_time = 300 
coming_times = np.random.randint(low=0, high=max_time, size=data.shape[0])
#%%
data.coming_time = coming_times
data = data.sort_values(by='coming_time')
#%%
data.to_csv('data/resources/delayed_time_experiment.csv', index=False)
