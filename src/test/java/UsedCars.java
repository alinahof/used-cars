import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UsedCars {

    public static void main(String[] args) throws InterruptedException {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();


        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://www.edmunds.com/");

        driver.findElement(By.linkText("Shop Used")).click();
        Thread.sleep(2000);

        driver.findElement(By.name("zip")).sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE, "22031", Keys.ENTER);
        Thread.sleep(5000);

        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")));
        Thread.sleep(2000);
        WebElement make = driver.findElement(By.id("usurp-make-select"));

        Select select = new Select(make);
        select.selectByVisibleText("Tesla");

        String model = new Select(driver.findElement(By.id("usurp-model-select"))).getFirstSelectedOption().getText();


        Thread.sleep(2000);




        List<String> modelsExpected = List.of("Model 3", "Model S", "Model X", "Model Y", "Cybertruck", "Roadster");

        List<WebElement> elements = new Select(driver.findElement(By.id("usurp-model-select"))).getOptions();


        List<String> modifiedList = new ArrayList<>();

        for (int i = 1; i < elements.size() ; i++) {
            modifiedList.add(elements.get(i).getText());
        }

        Assert.assertEquals(modifiedList, modelsExpected);

        new Select(driver.findElement(By.id("usurp-model-select"))).selectByVisibleText("Model 3");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//input[@name='min-value-input'][@aria-label='Min Year value']")).sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE, "2020", Keys.ENTER);

        List<WebElement> results = driver.findElements(By.xpath("//a[@class='usurp-inventory-card-vdp-link'][@tabindex='0']"));
        Thread.sleep(2000);
        Assert.assertEquals(results.size(), 21);
        driver.findElement(By.xpath("//input[@name='min-value-input'][@aria-label='Min Year value']")).sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE,Keys.BACK_SPACE,Keys.BACK_SPACE, "2020", Keys.ENTER);

        List<Integer> actualYears = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            results = driver.findElements(By.xpath("//a[@class='usurp-inventory-card-vdp-link'][@tabindex='0']")); // get a fresh list of webelements

            System.out.println(results.get(i).getText());
            Assert.assertTrue(results.get(i).getText().toLowerCase().contains("model 3"));
            actualYears.add(Integer.valueOf(results.get(i).getText().substring(0,4)));
            results.get(i).click();
            driver.navigate().back();
            Thread.sleep(1000);
        }

        for (Integer actualYear : actualYears) {
            if (! (actualYear>2019 || actualYear < 2024)){
                System.out.println("Year is out of range");
            }

        }

        new Select(driver.findElement(By.id("sort-by"))).selectByVisibleText("Price: Low to High");
        Thread.sleep(2000);

        List<WebElement> prices = driver.findElements(By.xpath("//span[@class='heading-3']"));

        List<Double> actualPrices = new ArrayList<>();
        for (WebElement price : prices) {
            actualPrices.add(Double.parseDouble(price.getText().replace("$", "").replace(",", "")));
        }

        System.out.println(actualPrices);

        List<Double> copy = new ArrayList<>(actualPrices);
        copy.sort(Comparator.naturalOrder());

        Assert.assertEquals(actualPrices, copy);



        new Select(driver.findElement(By.id("sort-by"))).selectByVisibleText("Price: High to Low");
        Thread.sleep(2000);

        prices = driver.findElements(By.xpath("//span[@class='heading-3']"));

        actualPrices = new ArrayList<>();
        for (WebElement price : prices) {

            actualPrices.add(Double.parseDouble(price.getText().replace("$", "").replace(",", "")));
        }
        copy = new ArrayList<>(actualPrices);
        copy.sort(Comparator.reverseOrder());
        Assert.assertEquals(actualPrices, copy);


//        new Select(driver.findElement(By.id("sort-by"))).selectByVisibleText("Mileage: Low to High");
//        Thread.sleep(2000);
//        List<WebElement> mileage = driver.findElements(By.xpath(""));
//        List<Integer> actualMileage = new ArrayList<>();
//        for (WebElement mile : mileage) {
//
//            actualMileage.add(Integer.parseInt(mile.getText().replace("$", "").replace(",", "")));
//        }
//        List <Integer> copy2 = new ArrayList<>(actualMileage);
//        copy2.sort(Comparator.reverseOrder());
//        Assert.assertEquals(actualMileage, copy2);


        List<WebElement> allResults = driver.findElements(By.xpath("//div[@class='visible-vehicle-info d-flex flex-column']"));
        Thread.sleep(2000);

        String carInfo = allResults.get(allResults.size() - 1).getText();




        String title = carInfo.substring(0,18);

        int startIndex = carInfo.indexOf("$");
        int endIndex = startIndex + 7;
        String carPrice = carInfo.substring(startIndex, endIndex);
        int startIndex2 = endIndex+1;
        int endIndex2 = carInfo.indexOf(" ", startIndex2);
        String miles = carInfo.substring(startIndex2, endIndex2);

        allResults.get(allResults.size()-1).click();

        WebElement titleOnPage = driver.findElement(By.xpath("//h1[@class='d-inline-block mb-0 heading-2 mt-0_25']"));
        Assert.assertEquals(titleOnPage.getText(), title);

        WebElement priceOnPage = driver.findElement(By.xpath("//div[@class='heading-2 mb-0']"));
        Assert.assertEquals(priceOnPage.getText(), carPrice);

        WebElement milesOnPage = driver.findElement(By.xpath("//div[@class='pr-0 font-weight-bold text-right ml-1 col']"));
       // Assert.assertEquals(milesOnPage.getText(), miles);

        driver.navigate().back();

        Assert.assertTrue(driver.findElement(By.xpath("//div[@class='bg-white text-gray-darker']")).isDisplayed());

        driver.quit();

























    }
}
