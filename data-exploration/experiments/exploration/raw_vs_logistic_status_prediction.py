#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Apr 23 23:07:07 2021

@author: quangkhanh
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

plt.rcParams['figure.dpi'] = 360
plt.style.use('ggplot')
#%%
no_predictive = pd.read_csv('data/result_track/without_status_predicting/cloudlet_me.csv')
logistic_predictive = pd.read_csv('data/result_track/only_logistic_regression/cloudlet_me.csv')
#%%
max_time = 300

cdf1 = no_predictive[no_predictive.finishTime < max_time]
cdf2 = logistic_predictive[logistic_predictive.finishTime < max_time]

print("------------------ No predictive --------------------")
print(cdf1.exeTime.describe())
print("------------------ Logistic predictive --------------------")
print(cdf2.exeTime.describe())