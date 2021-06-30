#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Apr 18 19:49:47 2021

@author: quangkhanh
"""

import pandas as pd
import numpy as np 
import matplotlib.pyplot as plt
import seaborn as sns
import sklearn

plt.style.use('ggplot')
plt.rcParams['figure.dpi'] = 360
#%%

df = pd.read_csv('data/extracted_data/task_info.csv')
#%%
data = df[['CPU request', 'memory request', 'disk space request', 'length']].values
#%%
from sklearn.model_selection import train_test_split

x_data = data[:, [0, 1]]
y_data = data[:, 2]
x_train, x_test, y_train, y_test = train_test_split(x_data, 
                                                    y_data,
                                                    test_size=0.3)
#%%
from sklearn.linear_model import LinearRegression
lr = LinearRegression(normalize=True)
lr = lr.fit(x_train, y_train)
#%%
y_prediction = lr.predict(x_train)
print(lr.score(x_train, y_train))
#%%
a = df[['CPU request', 'memory request', 'cpu rate']]\
    .groupby(['CPU request', 'memory request']).count()
a = a.reset_index()
a = a.sort_values('cpu rate', ascending=False)
mask = a.iloc[:6, [0, 1]].values
m = {(mask[i, 0], mask[i, 1]): chr(97+i) for i in range(mask.shape[0])}

def f(row):
    r = (row['CPU request'], row['memory request'])
    if r in m.keys():
        return m[r]
    else:
        return 'out'
    
df['mask'] = df.apply(lambda row: f(row), axis=1)
sns.violinplot(data=df, y='length', x='mask')