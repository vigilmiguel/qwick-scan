import scrapy
import time
from parseHelper import *



# Run the python scrapes with the command : scrapy crawl barcodes
class barcode_spider(scrapy.Spider):

    name = 'barcodes'
    secretCode = 'tDoYpc'
    handle_httpstatus_list = [404]   
    barHashTable = {}
    fileNames = ['UBC-DB.html','Google-Shopping-Stop1.html','Review-Find-Stop1.html']
    def start_requests(self):
        barHashTable = {}
        pages = {
            'https://www.barcodelookup.com/', 
            'https://www.google.com/search?psb=1&tbm=shop&q=',
            'https://brickseek.com/products/?search='  
        }
        urls = readTxtFile()
        for i in range(len(urls)):
            
            if(i%100==0):
                if(i!=0):
                    if(i%20==0):
                        time.sleep(20)
                r = requests.get(url = 'http://18.216.191.20/php_rest_api/api/queue/dequeue.php')
                if(r.json()!= None):
                    yield scrapy.Request(url='https://www.barcodelookup.com/' + str(r.json().get('barcode')), callback=self.parse, meta={'index':0, 'barcode': r.json().get('barcode'), 'barHash': barHashTable})
                    yield scrapy.Request(url='https://www.google.com/search?psb=1&tbm=shop&q=' + str(r.json().get('barcode')), callback=self.parse, meta={'index':1, 'barcode': r.json().get('barcode'), 'barHash': barHashTable})
                    yield scrapy.Request(url='https://brickseek.com/products/?search=' + str(r.json().get('barcode')), callback=self.parse, meta={'index':2, 'barcode': r.json().get('barcode'), 'barHash': barHashTable})
   
            print(urls[i])
            yield scrapy.Request(url='https://www.barcodelookup.com/' + str(urls[i]), callback=self.parse, meta={'index':0, 'barcode': urls[i], 'barHash': barHashTable})
            yield scrapy.Request(url='https://www.google.com/search?psb=1&tbm=shop&q=' + str(urls[i]), callback=self.parse, meta={'index':1, 'barcode': urls[i], 'barHash': barHashTable})
            yield scrapy.Request(url='https://brickseek.com/products/?search=' + str(urls[i]), callback=self.parse, meta={'index':2, 'barcode': urls[i], 'barHash': barHashTable})


    def parse(self, response):
        
        filename = 'poop-%d.html' % response.meta['index'] 
        with open(filename, 'wb') as f:
            f.write(response.body)
            self.log('Saved file %s' % filename)
        if(response.meta['index'] == 0):
           parseName(response.meta['barcode'],response.meta['barHash']) 



        if(response.meta['index'] == 1):
            temp = findURL()
            if(temp != None):
                yield scrapy.Request(url='https://www.google.com' + temp,callback=self.parse2, meta= {'barHash2': response.meta['barHash'], 'barcode2': response.meta['barcode']})
            else:
                cleanUP('PricePage.html')
        
        if(response.meta['index'] == 2):
            temp = findURL2()
            if(temp != None):
                yield scrapy.Request(url ='https://brickseek.com'+ temp, callback=self.parse3 )
            else:
                cleanUP('ReviewPage.html')
    

    def parse2(self,response):
        filename = 'PricePage.html' 
        with open(filename, 'wb') as f:
            f.write(response.body)
        getPrices(response.meta['barcode2'],response.meta['barHash2'])

    def parse3(self,response):
        filename = 'ReviewPage.html' 
        with open(filename, 'wb') as f:
            f.write(response.body)
        getReviews(response.meta['barcode'])

    def parse4(self,response):
        filename = 'addressPage.html' 
        with open(filename, 'wb') as f:
            f.write(response.body)
        getReviews(response.meta['barcode'])


        
