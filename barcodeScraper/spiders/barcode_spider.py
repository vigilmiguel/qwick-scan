import scrapy
import scrapy.selector as Selector

# Run the python scrapes with the command : scrapy crawl barcodes
class barcode_spider(scrapy.Spider):
    name = 'barcodes'
    start_urls = [
        'https://www.barcodelookup.com/099447251069/',
        #harcoding the url for now until we come up wth a snazzy way to pass it from java
        #tfw mom gets you chicken nuggets instead of the dino nuggets like you wanted
    ]

    def parse(self, response):

        #Grab Product Name and Product Image from our first UPC DB
        product_img = response.css('#images img::attr(src)').extract()
        product_name = response.css('#images img::attr(alt)').extract()


        #yield "acts" as a return, the text extracted from the xpath is stored into a variable nameText
        yield{'nameText': product_name,
              'nameImg' : product_img  }