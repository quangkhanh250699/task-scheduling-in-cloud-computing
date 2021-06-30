import os

for filename in os.listdir('.'):
    print(filename)
    os.rename(filename, filename.replace('.csv', '_0.csv'))
