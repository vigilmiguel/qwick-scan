from parseHelper import *
import time

def readStuff():
    r = requests.get(url = 'http://18.216.191.20/php_rest_api/api/queue/dequeue.php')
    if(r.json().get('barcode')!= None):
        return r.json().get('barcode')
    return None

while(True):
    x = readStuff()
    if(x != None):
        executeThings(x)
        time.sleep(10)
