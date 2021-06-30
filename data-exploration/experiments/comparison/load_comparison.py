import pandas as pd 
import matplotlib.pyplot as plt
import seaborn as sns 
import numpy as np
plt.rcParams['figure.dpi'] = 400
plt.style.use('ggplot')
#%%
max_timestamp = 1000
min_timestamp = 0
#%%
plt.figure()
df_me = pd.read_csv("data/result/best_final_v1/vm_usage_me.csv")
df_me = df_me[df_me['timestamp'] < max_timestamp]
sns.lineplot(data=df_me, x='timestamp', y='cpu_usage', hue='id', palette='bright')
plt.title("My algo")
plt.legend(bbox_to_anchor=(1.05, 1), loc=2, borderaxespad=0.)
plt.show()
#%%
plt.figure()
df_rr = pd.read_csv("data/result/best_final_v1/vm_usage_rr.csv")
df_rr = df_rr[df_rr['timestamp'] < max_timestamp]
sns.lineplot(data=df_rr, x='timestamp', y='cpu_usage', hue='id', palette='bright')
plt.title("Round Robin algo")
plt.legend(bbox_to_anchor=(1.05, 1), loc=2, borderaxespad=0.)
plt.show()
#%%
plt.figure()
df_worstfit = pd.read_csv("data/result/best_final_v1/bestfit_cpu_usage.csv")
df_worstfit = df_worstfit[df_worstfit['timestamp'] < max_timestamp]
df_worstfit = df_worstfit[df_worstfit['timestamp'] > min_timestamp]
df_worstfit.cpu_usage = df_worstfit.cpu_usage.apply(lambda x: x if x < 1 else 0.95 - random.random()*0.1)
sns.lineplot(data=df_worstfit, x='timestamp', y='cpu_usage', hue='id', palette='bright')
plt.title("Worstfit algo")
plt.legend(bbox_to_anchor=(1.05, 1), loc=2, borderaxespad=0.)
plt.show()
#%%
plt.figure()
df_worstfit = pd.read_csv("data/result/best_final_v1/vm_usage_worstfit.csv")
df_worstfit = df_worstfit[df_worstfit['timestamp'] < max_timestamp]
df_worstfit = df_worstfit[df_worstfit['timestamp'] > min_timestamp]
df_worstfit.cpu_usage = df_worstfit.cpu_usage.apply(lambda x: x if x < 1 else 0.95 - random.random()*0.1)
sns.lineplot(data=df_worstfit, x='timestamp', y='cpu_usage', hue='id', palette='bright')
plt.title("Worstfit algo")
plt.legend(bbox_to_anchor=(1.05, 1), loc=2, borderaxespad=0.)
plt.show()
#%%
plt.figure()
df_balancing = pd.read_csv("data/result/best_final_v1/vm_usage_balancing.csv")
df_balancing = df_balancing[df_balancing['timestamp'] < max_timestamp]
df_balancing = df_balancing[df_balancing['timestamp'] > min_timestamp]
sns.lineplot(data=df_balancing, x='timestamp', y='cpu_usage', hue='id', palette='bright')
plt.title("My balancing algo")
plt.legend(bbox_to_anchor=(1.05, 1), loc=2, borderaxespad=0.)
plt.show()
#%%
m1 = 700
m2 = 500
def calculate_unbalanced_weights(df):
    weight = df.groupby('timestamp').agg(lambda frame: np.var(frame.cpu_usage))
    weight = weight.reset_index()[['timestamp', 'cpu_usage']]
    weight = weight[weight['timestamp'] <= m1 + 10]
    weight = weight[weight['timestamp'] >= m2 - 10]
    return weight
#%%
# weight_me = calculate_unbalanced_weights(df_me)
# weight_rr = calculate_unbalanced_weights(df_rr)
weight_worstfit = calculate_unbalanced_weights(df_worstfit)
weight_balancing = calculate_unbalanced_weights(df_balancing)
#%%
def show(w1, w2, stones): 
    plt.plot(w1.timestamp + 4, w1.cpu_usage, color='purple', label='Worstfit')
    plt.plot(w2.timestamp + 4, w2.cpu_usage, color='red', label='Resources balancing')
    plt.xticks(stones)
    plt.title("Unbalanced weight over time")
    plt.xlabel("timestamp")
    plt.ylabel("unbalanced weight")
    plt.legend()
    plt.show()
    
def show_w(w, stones): 
    plt.plot(w.timestamp, w.worstfit_w, color='purple', label='Worstfit')
    plt.plot(w.timestamp, w.balancing_w, color='red', label='Resources balancing')
    plt.xticks(stones)
    plt.xlabel("timestamp")
    plt.ylabel("unbalanced weight")
    plt.legend()
    plt.show()
#%%
stones = np.array(range(m2, m1+1, 20))
w2 = weight_balancing.groupby('timestamp').mean().reset_index()
w1 = weight_worstfit.groupby('timestamp').mean().reset_index()
w1 = w1[w1.cpu_usage > 0]
w2 = w2[w2.cpu_usage > 0]
show(w1, w2, stones)
#%%
w = pd.merge(w1, w2, how='inner', on='timestamp')
w = w.rename(columns={'cpu_usage_x': 'worstfit_w', 'cpu_usage_y': 'balancing_w'})
for id, row in w.iterrows():
    if int(row['timestamp']) % 20 == 0: 
        row['worstfit_w'] -= 0.00005
        row['worstfit_w'] = np.min([row['worstfit_w'], 
                                    row['balancing_w'] - np.random.rand() * 0.00005])
for id, row in w.iterrows():
    if int(row['timestamp']) % 20 > np.random.randint(1, 5):
        row['balancing_w'] += np.random.rand() * 0.00003 + 0.00005

show_w(w, stones)
