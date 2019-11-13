import scrapy
from parseHelper import *


# Run the python scrapes with the command : scrapy crawl barcodes
class barcode_spider(scrapy.Spider):

    name = 'barcodes'
    secretCode = 'tDoYpc'
    handle_httpstatus_list = [404]   
    fileNames = ['UBC-DB.html','Google-Shopping-Stop1.html','Review-Find-Stop1.html']
    def start_requests(self):
        pages = {
            'https://www.barcodelookup.com/', 
            'https://www.google.com/search?psb=1&tbm=shop&q=',
            'https://brickseek.com/products/?search='  
        }
        urls = readTxtFile()
        for i in range(len(urls)):
            print(urls[i])
            yield scrapy.Request(url='https://www.barcodelookup.com/' + str(urls[i]), callback=self.parse, meta={'index':0, 'barcode': urls[i]})
            yield scrapy.Request(url='https://www.google.com/search?psb=1&tbm=shop&q=' + str(urls[i]), callback=self.parse, meta={'index':1, 'barcode': urls[i]})
            yield scrapy.Request(url='https://brickseek.com/products/?search=' + str(urls[i]), callback=self.parse, meta={'index':2, 'barcode': urls[i]})


    def parse(self, response):
        
        filename = 'poop-%d.html' % response.meta['index'] 
        with open(filename, 'wb') as f:
            f.write(response.body)
            self.log('Saved file %s' % filename)
        if(response.meta['index'] == 0):
           parseName(response.meta['barcode']) 


        if(response.meta['index'] == 1):
            temp = findURL()
            if(temp != None):
                yield scrapy.Request(url='https://www.google.com' + temp,callback=self.parse2)
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
        getPrices(response.meta['barcode'])

    def parse3(self,response):
        filename = 'ReviewPage.html' 
        with open(filename, 'wb') as f:
            f.write(response.body)
        getReviews(response.meta['barcode'])


        
