#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Apr 25 21:30:29 2021

@author: quangkhanh
"""


import numpy as np

def binary_search(arr, value): 
    '''
    

    Parameters
    ----------
    arr : array-like
        array of premitive types
    value : premitive types
        value searched

    Returns
    -------
    out : position index 
        the largest position that has value smaller or equal to value
    '''
    
    def recursive_search(arr, left, right, value):
        if left >= right:
            return right
        else: 
            mid = (left + right) // 2
            if arr[mid] > value:
                return recursive_search(arr, left, mid - 1, value)
            elif arr[mid] <= value and arr[mid + 1] > value:
                return mid
            else: 
                return recursive_search(arr, mid + 1, right, value)
    return recursive_search(arr, 0, len(arr) - 1, value)

    
def replace_duration(duration, status):
    '''
    

    Parameters
    ----------
    duration : series 
        durations of tasks 
        
    status : series
        status of tasks

    Returns
    -------
    out : series
        replaced durations of tasks, which durations of status equaled to 1  
        are change to next index of 0-status
    '''
    
    duration = duration.copy()
    index = duration.index
    duration.index = range(len(duration))
    status.index = duration.index
    
    base_duration = np.sort(duration[status == 0].unique())
    one_indices = np.ix_(status == 1)[0]
    replace_durations = np.zeros_like(one_indices, dtype='double')
    for i in range(len(one_indices)):
        if one_indices[i] == one_indices[i-1] + 1:
            replace_durations[i] = replace_durations[i-1]
        else: 
            next_index = binary_search(base_duration, 
                                       duration[one_indices[i]]) + 1
            if next_index >= base_duration.shape[0] - 1:
                replace_durations[i] = duration[one_indices[i]]
            else: 
                replace_durations[i] = base_duration[next_index]
    duration[one_indices] = replace_durations
    duration.index = index
    return duration
            
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    