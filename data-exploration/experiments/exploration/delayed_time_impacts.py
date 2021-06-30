#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Fri Jun 25 12:17:42 2021

@author: quangkhanh
"""

import pandas as pd 
import matplotlib.pyplot as plt
import seaborn as sns 
import numpy as np
plt.rcParams['figure.dpi'] = 400
plt.style.use('ggplot')
#%%
df0 = pd.read_csv('data/delayed_time_experiment/cloudlet_worstfit_0.csv')
df1 = pd.read_csv('data/delayed_time_experiment/cloudlet_worstfit_1.csv')
df2 = pd.read_csv('data/delayed_time_experiment/cloudlet_worstfit_2.csv')
df3 = pd.read_csv('data/delayed_time_experiment/cloudlet_worstfit_3.csv')
df4 = pd.read_csv('data/delayed_time_experiment/cloudlet_worstfit_4.csv')
#%%
max_time = 300
def extract_col(df):
    df_extracted = df[['CloudletID', 'StartTime', 'finishTime', 'exeTime']]
    df_extracted = df_extracted.sort_values(by='StartTime')
    return df_extracted

df0_extracted = extract_col(df0)
df1_extracted = extract_col(df1)
df2_extracted = extract_col(df2)
df3_extracted = extract_col(df3)
df4_extracted = extract_col(df4)
#%%
data = pd.DataFrame({
    '0s': df0_extracted.exeTime.values,
    '1s': df1_extracted.exeTime.values,
    '2s': df2_extracted.exeTime.values,
    '3s': df3_extracted.exeTime.values, 
    '4s': df4_extracted.exeTime.values
}, index=range(df0_extracted.__len__()))
#%%
value = []
label = []
data_0s = df0_extracted.sample(n=500).exeTime.values
data_1s = df1_extracted.sample(n=500).exeTime.values
value.extend(data_0s)
label.extend(['no-delay'] * data_0s.__len__())
value.extend(data_1s)
label.extend(['delay'] * data_1s.__len__())
    
plot_data = pd.DataFrame({'execution_time': value, 'delay_time': label})
#%%
from scipy.stats import ttest_ind

test_result = ttest_ind(data_0s, data12s, equal_var=True, 
                alternative='less')
#%%
sns.boxplot(x='delay_time', y='execution_time', data=plot_data)
plt.xlabel('')
plt.ylabel('exe_time (s)')
plt.text(-0.2, 40, f'mean = {round(data_0s.mean(), 2)}')
plt.text(-0.15, 37, f'n = {data_0s.shape[0]}')
plt.text(0.8, 40, f'mean = {round(data_2s.mean(), 2)}')
plt.text(0.85, 37, f'n = {data_2s.shape[0]}')
plt.text(0.20, 31, f'p_value = {round(test_result.pvalue, 3)}', 
         fontsize=13, color='red')
plt.show()
