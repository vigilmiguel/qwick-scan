# -*- coding: utf-8 -*-
"""
Created on Mon Sep 30 23:02:23 2019

@author: Danny Chung
"""

import requests 
'''
print("hi")
#source_code = print("Hello")

API_ENDPOINT = ""
API_KEY = ""
'''

files = {  
    'firebaseUID' : 'Hello',
    'userName' : 'Bye'
}

response = requests.post('http://18.216.191.20/php_rest_api/api/users/create.php', json = files)
#print(response.text)
# data is stored as a dict
'''
data = {'api_dev_key': API_KEY,
        'api_option':'paste',
        'api_paste_code':source_code,
        'api_paste_format' : 'python'
        }

# r will store the request response, use request.post() because we are sending
# a post request, arguments are ulr and the data
r = request.post(url = API_ENDPOINT, data = data)

# source code is sent back and can be opened in r.text
url = r.text
print(url)
'''