from bs4 import BeautifulSoup

def returnPageNames(index):
    pageNames = ['']

def findURL():
    with open('poop-1.html', 'r', encoding='utf-8') as f:
        data = f.read()
    mySecret  = 0

    soup = BeautifulSoup(data, 'html.parser')

    mydivs = soup.findAll("div", {"class": "tDoYpc"})
    #mydivs = soup.findAll("h3", class_= 'r')
    if len(mydivs) > 1:
        mySecret = 1
        
    return (str)(mydivs[mySecret]).split('?')[0][29:]


def findURL2():
    with open('poop-2.html', 'r') as f:
        data = f.read()

    soup = BeautifulSoup(data, 'html.parser')

    mydivs = soup.findAll("div", {"class": "item-list__tile"}) 
    playWith = str(mydivs).split('"')
    finalString = playWith[5] 
    return finalString


def parseName():
    
    with open('poop-0.html', 'r') as f:
        data = f.read()
    
    soup = BeautifulSoup(data, 'html.parser')
    
    imageURL = soup.find("div", { "id" : "images" }).find('img')['src']
    ProductName = soup.find("div", { "id" : "images" }).find('img')['alt']

    print(imageURL)
    print(ProductName)



def getPrices():

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
        print('poop')


def getReviews():

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

