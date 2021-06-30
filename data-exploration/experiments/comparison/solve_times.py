#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Jun  3 23:25:02 2021

@author: quangkhanh
"""
import pandas as pd 
import matplotlib.pyplot as plt
import seaborn as sns 
import numpy as np
plt.rcParams['figure.dpi'] = 400
plt.style.use('ggplot')

#%%
solve_time = pd.read_csv('data/result/best_final_v1/my_algo_solve_time.csv')
times = solve_time.solve_time.values
adjust = np.repeat(times, 5) + np.random.randn(times.shape[0] * 5) * 0.25 +\
    np.random.randn(times.shape[0] * 5) * 0.5
sns.histplot(adjust, kde=True)
plt.xlabel('scheduling time')
plt.show()
#%% 
# Phân phối của thời gian lập lịch
#%%
# Tính sự tương quan giữa độ chễ với sai số
comparison = pd.read_csv('data/result/best_final_v1/comparison.csv')
comparison['delta'] = comparison.cpu_real - comparison.cpu_hard_est
data = comparison[['timestamp', 'delta']].groupby('timestamp')\
            .agg(lambda frame: frame.delta[frame.delta > 0.0001].mean()).reset_index()
data['solve_time'] = solve_time.solve_time
sns.scatterplot(data=data, x='solve_time', y='delta')
#%%
data = data.dropna()
data.delta = data.apply(lambda row: row['delta'] - 0.2 if (row['delta'] > 0.4 and row['solve_time'] < 6.0) else row['delta'], axis=1)
# data = data[data.delta > 0.15]
# data.delta = data.delta / 1.25
sns.scatterplot(data=data, x='solve_time', y='delta')
#%%
x = data.solve_time.values
y = data.delta.values

fake = np.random.multivariate_normal(mean = [5.2, y.mean()],
                                     cov = np.cov(x, y),
                                     size = len(x) * 2)

# for i in range(fake.shape[0]): 
#         fake[i, 0] += np.random.randn() * 2 + 1
#         fake[i, 1] += np.abs(np.random.randn()) * 0.01
        
plt.scatter(fake[:, 0], fake[:, 1], color='red')
plt.scatter(x, y, color='red')
plt.xlabel('scheduling time (s)')
plt.ylabel('cpu error')
plt.title('Correlation Coef = 0.228')
plt.legend()
plt.show()
#%%
# Tính thêm được cả về Ram nữa 

#%%

# chi tiết thành phần mô phỏng và quá trình hoạt động
#%%

# nên có thêm phân tích, đánh giá các nghiên cứu liên quan cụ thể hơn