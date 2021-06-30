#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Apr 19 21:25:31 2021

@author: quangkhanh
"""

import pandas as pd
#%%
class Node:
    
    def __init__(self, start, end):
        self.start = start
        self.end = end
        self.expected_joint = 1
        self.adjacency = []
        
class Edge:
    
    def __init__(self, node1, node2, weight):
        self.node1 = node1
        self.node2 = node2
        self.weight = weight

class Graph: 
    
    def __init__(self):
        self.nodes = []
        self.edges = []
        
def get_overlap(node1: Node, node2: Node):
    if node1.start < node2.start: 
        node_a = node1
        node_b = node2
    else: 
        node_a = node2 
        node_b = node1
    if node_a.end >= node_b.end: 
        return node_b.end - node_b.start
    else: 
        if node_a.end <= node_b.start: 
            return 0
        else: 
            return node_a.end - node_b.start

#%%
# df = pd.read_csv('data_structure/input.csv')
def calculate_expected_joint(df, start='start', end='end'):
    nodes = []
    df.apply(lambda row: nodes.append(Node(row[start], row[end])), axis=1)
    edges = []
    for i in range(len(nodes) - 1):
        for j in range(i+1, len(nodes)): 
            weight = get_overlap(nodes[i], nodes[j])
            if weight > 0:
                edge = Edge(nodes[i], nodes[j], weight)
                nodes[i].adjacency.append(edge)
    for node in nodes: 
        weight = 0
        for e in node.adjacency:
            weight += e.weight
        if weight != 0: 
            node.expected_joint = weight / (node.end - node.start)
    df['expected_joint'] = [node.expected_joint for node in nodes]
    return df