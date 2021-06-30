#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Apr 12 11:46:40 2021

@author: quangkhanh
"""

import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# sns.set_theme()
#%% 
cdf1 = pd.read_csv("data/result/best_v3/cloudlet_rr.csv")
cdf2 = pd.read_csv("data/result/best_v3/cloudlet_bestfit.csv")
cdf3 = pd.read_csv("data/result/best_v3/cloudlet_me.csv")

#%%
max_timestamp = 300

cdf1 = cdf1[cdf1.finishTime < max_timestamp]
cdf2 = cdf2[cdf2.finishTime < max_timestamp]
cdf3 = cdf3[cdf3.finishTime < max_timestamp]

#%% 
def re_arrange(df):
    df = df.sort_values(by='finishTime')
    df.index = range(df.shape[0])
    df['finished-task number'] = range(df.shape[0])
    return df

cdf1 = re_arrange(cdf1)
cdf2 = re_arrange(cdf2)
cdf3 = re_arrange(cdf3)
#%%
plt.figure(dpi=360)
sns.lineplot(data=cdf1, x='finishTime', 
             y='finished-task number', label='round robin', color='green')
sns.lineplot(data=cdf2, x='finishTime',
             y='finished-task number', label='bestfit', color='blue')
sns.lineplot(data=cdf3, x='finishTime', 
             y='finished-task number', label='my algo', color='red')
plt.title(f'The number of tasks finished in {max_timestamp} seconds')
plt.legend()
plt.show()

#%%
result = pd.DataFrame({
                'RoundRobin': cdf1.exeTime.describe(),
                'BestFit': cdf2.exeTime.describe(),
                'MyAlgo': cdf3.exeTime.describe()
            })
print(result)
#%%
result.to_csv('result.csv')