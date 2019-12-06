from bs4 import BeautifulSoup
import requests
import json
import os
#import barcodeScraper 
import random
import time
import sys
import scrapy
from scrapy.crawler import CrawlerProcess
#export PYTHONPATH=C:/Users/James/Desktop/PriorityScrape/


def returnPageNames(index):
    pageNames = ['']

#Find the next URL to for our price pages..
def findURL():
    with open('poop-1.html', 'r', encoding='utf-8') as f:
        data = f.read()
    mySecret  = 0

    soup = BeautifulSoup(data, 'html.parser')

    mydivs = soup.findAll("div", {"class": "tDoYpc"})
    
    if len(mydivs) > 1:
        mySecret = 1
    else:
        return None 
        
    #print ((str)(mydivs[mySecret]).split('?')[0][29:])
    #input('')
    cleanUP('poop-1.html')
    return (str)(mydivs[mySecret]).split('?')[0][29:]


def findURL2():
    with open('poop-2.html', 'r') as f:
        data = f.read()

    soup = BeautifulSoup(data, 'html.parser')

    mydivs = soup.findAll("div", {"class": "item-list__tile"}) 
    if(len(mydivs) == 0):
        return None
    playWith = str(mydivs).split('"')
    finalString = playWith[5] 
    cleanUP('poop-2.html')
    return finalString


def parseName(barcode,barHashTable):
    
    with open('poop-0.html', 'r') as f:
        data = f.read()
    
    soup = BeautifulSoup(data, 'html.parser')
    
    imageURL = soup.find("div", { "id" : "images" }).find('img')['src']
    ProductName = soup.find("div", { "id" : "images" }).find('img')['alt']



    #barHashAdd(barHash,barcode,ProductName,imageURL)
    barHashTable[barcode] = [ProductName,imageURL]
    packageInfo = []
    packageInfo.append(barcode)
    packageInfo.append(ProductName)
    packageInfo.append(imageURL)
    databaseWriteName(packageInfo)
    cleanUP('poop-0.html')



def getPrices(barcode,barHash):

    storeNames = []
    storePrices = []
    with open('PricePage.html', 'r', encoding='utf-8') as f:
        data = f.read()
    soup = BeautifulSoup(data, 'html.parser')

    cssThings = soup.find_all("div", {"class": "sh-osd__merchant-info-container"})


    for query in cssThings:
        query1 = (str)(query).split('<span>')
        query1 = query1[1].split('</span>') 
        storeNames.append(query1[0])

    cssThings2 = soup.find_all("div", {"class": "sh-osd__total-price"})

    for query in cssThings2:

        query1 = (str)(query).split('$')        
        query1 = query1[1].split('<')
        storePrices.append(query1[0])

    if len(storeNames) == len(storePrices):
        for i  in range(len(storeNames)):
            tempPackage = [barcode,barHash.get(barcode)[0],barHash.get(barcode)[1],storeNames[i],storePrices[i]]
            dataBaseWritePrice(tempPackage)
            #print(storeNames[i])
            #print(storePrices[i])

    #cleanUP('PricePage.html')

def getReviews(barcode):

    reviewLinks = []
    starRatings = []
    reviewName = []
    with open('ReviewPage.html', 'r', encoding='utf-8') as f:
        data = f.read()
    soup = BeautifulSoup(data, 'html.parser')

    cssThings = soup.find_all("a", {"class": "item-list__tile product-super__reviews-tile"})

    #print(cssThings)
    for query in cssThings:
        try:
            reviewLinks.append(query['href'])
        except KeyError:
            pass
        query1 = (str)(query).split(';">')
        query1 = (str)(query1[1]).split('<')
        reviewName.append(query1[0])

    cssThings2 = soup.find_all("span", {"class": "typography-font-size-extra-large"})
    
    for query in cssThings2:
        query1 = (str)(query).split('</div> ')
        query1 = str(query1[1]).split('<')
        starRatings.append(query1[0].strip())

    cleanUP('ReviewPage.html')


def databaseWriteName(DBpackage):
    url = 'http://18.216.191.20/php_rest_api/api/products/create.php'
    payload = { 
        "barcode": DBpackage[0],
        "productname": DBpackage[1],
        "imageurl": DBpackage[2]
        }
    headers = {'content-type': 'application/json'}

    response = requests.post(url, data=json.dumps(payload))

    print(response.text)


def runScrapy(barcode):
    os.system("scrapy crawl barcodes -a targetBarcode=" + barcode)


def getURLS(barcode):

    url = {
    'https://www.barcodelookup.com/' + barcode,
    'https://www.google.com/search?psb=1&tbm=shop&q=' + barcode,
    'https://brickseek.com/products/?search=' + barcode 
    } 

    return url


def cleanUP(fileTermination):

    if os.path.exists(fileTermination):
            os.remove(fileTermination)
    else:
        print("The file does not exist")   

def readTxtFile():
    barcodes = []
    with open('barcodeID.txt','r') as f:
        for line in f:
            for word in line.split():
                barcodes.append(word)
                #runScrapy(word)
    return barcodes


def dataBaseWritePrice(DBpackage):

    #DBpackage = ['0784343943111','poop','pooop.com','poopInc', -.563, .717, 5000.55]
    url = 'http://18.216.191.20/php_rest_api/api/products/createWithWebPrice.php'
    x = random.uniform(0.1,15000000.99)
    payload = { 
        "barcode": DBpackage[0],
        "productname": DBpackage[1],
        "imageurl": DBpackage[2],
        "storename": DBpackage[3],
        "address": str(x),
        "price": float(DBpackage[4])
        }
    #input('$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$')
    ##print(payload)
    #input('$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$')
    headers = {'content-type': 'application/json'}

    response = requests.post(url, data=json.dumps(payload))

    print(response.text)

def readPriority():
    r = requests.get(url = 'http://18.216.191.20/php_rest_api/api/queue/dequeue.php')
    if(r.json().get('barcode')!= None):
        runScrapy(r.json().get('barcode'))
        #time.sleep(10)


def populateQue():
    barcodes = ['049000052688 ','611269357011','070847012764','8427761072442','050742874155','9476062108861'] 
    url = 'http://18.216.191.20/php_rest_api/api/queue/enqueue.php'
    for i in range(len(barcodes)):
        payload = {
            "barcode" : barcodes[i],
            "longitude" : str(random.uniform(-100,100)),
            "latitude" : str(random.uniform(-100,100))
        }
        headers = {'content-type': 'application/json'}

        response = requests.post(url, data=json.dumps(payload))
        print(response.text)


def executeThings(barcode):
    runScrapy(barcode)

    


#dataBaseWritePrice(['11','cat','cat.com','catstore',50])
#runScrapy('022000015532')
#populateQue()

#sys.path.append('../barcodeScraper/spiders/barcode_spider.py')
#while(True):
    #readPriority()