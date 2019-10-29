import scrapy
from parseHelper import *


# Run the python scrapes with the command : scrapy crawl barcodes
class barcode_spider(scrapy.Spider):

    name = 'barcodes'
    secretCode = 'tDoYpc'
    handle_httpstatus_list = [404]   
    fileNames = ['UBC-DB.html','Google-Shopping-Stop1.html','Review-Find-Stop1.html']
    def start_requests(self):
        urls = [
            'https://www.barcodelookup.com/4719072517151',
            'https://www.google.com/search?psb=1&tbm=shop&q=811571018420',
            'https://brickseek.com/products/?search=811571018420'

            #'https://www.google.com/search?psb=1&tbm=shop&q=4719072517151'
        ]
        for index, url in enumerate(urls):
            yield scrapy.Request(url=url, callback=self.parse, meta={'index':index})

    def parse(self, response):
        
        filename = 'poop-%d.html' % response.meta['index'] 
        with open(filename, 'wb') as f:
            f.write(response.body)
        self.log('Saved file %s' % filename)
        if(response.meta['index'] == 1):
            yield scrapy.Request(url='https://www.google.com' + findURL(),callback=self.parse2)
        
        if(response.meta['index'] == 2):
            yield scrapy.Request(url ='https://brickseek.com'+ findURL2(), callback=self.parse3 )
    

    def parse2(self,response):
        filename = 'PricePage.html' 
        with open(filename, 'wb') as f:
            f.write(response.body)
        secretPassge = 'https://www.google.com' + getPrices()
        print("***********************************************************")
        print(secretPassge)
        yield scrapy.Request(url=secretPassge, callback=self.parse4 )

    def parse3(self,response):
        filename = 'ReviewPage.html' 
        with open(filename, 'wb') as f:
            f.write(response.body)


    def parse4(self,response):
        print("IM HERE --1-1-1-1-1-1-1-1-1-1-1")
        print(response.url)
        
