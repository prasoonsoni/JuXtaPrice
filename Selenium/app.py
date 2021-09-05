from flask import Flask, jsonify
from flask import request
import time
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.firefox.options import Options

app = Flask(__name__)

@app.route('/', methods = ['GET'])
def data():
    product_name =  request.args['q']
    # flipkart
    options = Options()
    options.add_argument("--headless")
    B=[]
    FlipkartProduct = []
    FlipkartPrice = []
    FlipkartImages = []
    driver = webdriver.Firefox(executable_path="./geckodriver.exe", options=options)
    driver.get("https://www.flipkart.com/") 
    driver.find_element(By.XPATH,"//button[@class='_2KpZ6l _2doB4z']").click()
    driver.find_element(By.XPATH,"//input[@title='Search for products, brands and more']").send_keys(product_name)
    driver.find_element(By.XPATH,"//button[@type='submit']").click()
    time.sleep(5)
    product = driver.find_elements_by_class_name('_4rR01T')
    price = driver.find_elements_by_xpath("//div[@class='_30jeq3 _1_WHN1']")
    images = driver.find_elements_by_xpath("//img[@class='_396cs4 _3exPp9']")
    for j in product:
        FlipkartProduct.append(j.text)
    for j in price:
        FlipkartPrice.append(j.text)
    for j in images:
        FlipkartImages.append(j.get_attribute("src"))
    for i in range(len(FlipkartProduct)):
            d2 = {}
            d2['shopping_site']="flipkart"
            d2['product_name']=FlipkartProduct[i]
            d2['product_price']=int((FlipkartPrice[i].lstrip("₹")).replace(",",""))
            d2['product_image'] = FlipkartImages[i]
            B.append(d2)
#---------------------------------------------------------------------------------------
    # amazon
    productlist = []
    pricelist = []
    imglist = []
    A = []
    options = Options()
    options.add_argument("--headless")
    driver = webdriver.Firefox(executable_path="./geckodriver.exe", options=options)
    driver.get("https://www.amazon.in/") # website name which we want to scrap
    driver.implicitly_wait(10) # waiting to quit
    driver.find_element(By.XPATH,"//input[contains(@id,'twotabsearchtextbox')]").send_keys(product_name) # Searching the product in amazon searchbar
    driver.find_element(By.XPATH,"//*[@id='nav-search-submit-button']").click() #//*[@id="nav-search-submit-button"]   
    try:
        product = driver.find_elements_by_xpath("//span[@class='a-size-medium a-color-base a-text-normal']") # product name
    except:
        product = driver.find_elements_by_xpath("//h2[@class='a-size-mini a-spacing-none a-color-base s-line-clamp-4']")
    price = driver.find_elements_by_xpath("//span[@class='a-price-whole']") # product price
    images = driver.find_elements_by_xpath("//img[@class='s-image']")
    for pro in product:
        productlist.append(pro.text)
    for p in price:
        pricelist.append(p.text)
    for image in images:
        imglist.append(image.get_attribute("src"))
    for i in range(len(productlist)):
        d = {}
        if(pricelist[i]==""):
            pass
        else:
            d['shopping_site']="amazon"
            d['product_name']=productlist[i]
            d['product_price']=int(pricelist[i].replace(",",""))
            d['product_image']=imglist[i]
            A.append(d)

    driver.quit()
    final_list = A+B
    final_dict = sorted(final_list, key = lambda i: i['product_price'])
    return jsonify(final_dict)
    driver.quit()

if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0')