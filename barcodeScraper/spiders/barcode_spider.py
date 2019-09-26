import scrapy

# Run the python scrapes with the command : scrapy crawl barcodes
class barcode_spider(scrapy.Spider):
    name = 'barcodes'
    start_urls = [
        'https://www.barcodelookup.com/' + '099447251069/'
        #harcoding the url for now until we come up with a snazzy way to pass it from java
        #tfw mom gets you chicken nuggets instead of the dino nuggets like you wanted
    ]

    def parse(self, response):

        #We directly pass in the xml path to the title of the product that we are scanning, we use // for specific elements.
        #/text is used to receive text, extract() extracts that text

        product_name = response.xpath("/html/body//div[@id='body-container']"
                                      "//section[@class='mid-inner']"
                                      "//div[@class='container']"
                                      "//div[@class='row']"
                                      "//div[@class='col-md-6 product-details']/h4/text()").extract()

        #yield "acts" as a return, the text extracted from the xpath is stored into a variable nameText
        yield{'nameText': product_name}