#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Apr 26 15:00:26 2021

@author: quangkhanh
"""

import tensorflow as tf
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns

from tensorflow.keras import utils

plt.rcParams['figure.dpi'] = 360
plt.style.use('ggplot')
#%%
df = pd.read_csv('data/learning_data/mutual_status_data.csv')
dataset = df.values
X_data = dataset[:, :3]
y_data = np.apply_along_axis(lambda x: x[0] + 2 * x[1],
                             axis=1,
                             arr= dataset[:, 3:])
#%%
dummy_y = utils.to_categorical(y_data)
#%%
from sklearn.model_selection import train_test_split

X_train, X_test, y_train, y_test = train_test_split(X_data, dummy_y, 
                                                    test_size=0.3)
#%%
def baseline_model():
    model = tf.keras.Sequential()
    model.add(tf.keras.layers.Dense(4, input_dim=3, activation='softmax'))
    model.compile(loss='categorical_crossentropy',
                  optimizer='adam',
                  metrics=['accuracy'])
    return model
#%%
model = baseline_model()
model.fit(x=X_train, y=y_train,
          batch_size=128, epochs=100,
          validation_data=(X_test, y_test),
          validation_steps=5)
#%%
y_pre = model.predict(X_test).argmax(axis=1)
#%%
from sklearn.metrics import confusion_matrix

y_true = y_test.argmax(axis=1)

confusion = confusion_matrix(y_true, y_pre)
sns.heatmap(confusion, annot=True, annot_kws={"size": 8}, fmt='d')
#%%
model.save(filepath='models/mutual_status_model.h5', overwrite=True)