import scrapy
import scrapy.selector as Selector

# Run the python scrapes with the command : scrapy crawl barcodes
class barcode_spider(scrapy.Spider):
    name = 'barcodes'
    itemcode = '0079519111033'
    url = 'https://www.upcdatabase.com/item/' + itemcode 
    start_urls = [
        url,
       
    ]

    def parse(self, response):

        #Grab Product Name from upc database
        product_names = response.xpath('//div[@id="content"]/table//tr')
        product_name = product_names.xpath('td[3]//text()').extract_first()

        


        #yield "acts" as a return, the text extracted from the xpath is stored into a variable nameText
        yield{'nameText': product_name
             }
