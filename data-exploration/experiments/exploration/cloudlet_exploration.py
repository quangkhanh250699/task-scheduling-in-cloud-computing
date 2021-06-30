#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Apr 24 10:34:14 2021

@author: quangkhanh
"""

import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

plt.rcParams['figure.dpi'] = 400
plt.style.use('ggplot')

#%%
cloudlet = pd.read_csv('data/resources/cloudlets_v5.csv')
#%%
short_cloudlet = cloudlet[cloudlet.kind == 0]
sns.scatterplot(data=short_cloudlet, x='cpu', y='ram')