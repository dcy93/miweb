import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.*;

import java.util.List;
import java.util.Random;

// Versión actual: v1.1
public class CargaGitHub {
    // Variables del navegador
    WebDriver driver;
    Wait wait;
    String navegador = "Chrome"; // Aplica sólo Chrome, Firefox o Edge
    Point point;

    // Variable del test 'practicaPersonalizada01'
    boolean allItem = true; // Todo: v1.1
    // Todo: Establecer 'true' permite seleccionar un producto que cuente con Stock (hasta que se lo permita)
    // Todo: Establecer 'false' permite seleccionar el primer producto que se encuentre, incluyendo si no tiene inventario.

    // Variables para gestionar el tiempo
    int generalTime = 2 * 1000;
    int extendTime = 3 * 1000;

    @BeforeEach
    public void initConfigurationTest() {
        switch (navegador.toUpperCase()) {
            case "CHROME":
                System.setProperty("webdriver.chrome.driver",
                        "C:\\Users\\notebook -anla\\IdeaProjects\\PracticandoAutomatizacion001\\src\\test\\resources\\chromedriver.exe");
                driver = new ChromeDriver();
                break;
            case "FIREFOX":
                System.setProperty("webdriver.gecko.driver",
                        "C:\\Users\\notebook -anla\\IdeaProjects\\PracticandoAutomatizacion001\\src\\test\\resources\\geckodriver.exe");
                driver = new FirefoxDriver();
                break;
            case "EDGE":
                System.setProperty("webdriver.edge.driver",
                        "C:\\Users\\notebook -anla\\IdeaProjects\\PracticandoAutomatizacion001\\src\\test\\resources\\msedgedriver.exe");
                driver = new EdgeDriver();
                break;
            default:
                System.out.println("Navegador no válido");
                Assertions.assertTrue(false);
                break;
        }

        wait = new WebDriverWait(driver, 5L);
        driver.manage().window().maximize();
    }

    @AfterEach
    public void closeNav() {
        driver.quit();
    }

    @Test
    public void practicaPersonalizada01() throws InterruptedException {
        System.out.println("=================================");
        System.out.println("Iniciando 'prácticaPersonalizada01'.");
        System.out.println("<------------------------------->");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //Thread.sleep(10000);

        driver.get("http://www.automationpractice.pl/index.php");
        driver.findElement(By.cssSelector("input#search_query_top")).sendKeys("Dress");
        driver.findElement(By.cssSelector("button[name='submit_search']")).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("span.heading-counter")));

        List<WebElement> totalResult = driver.findElements(
                By.cssSelector("div.center_column ul[class='product_list grid row'] li div[class='product-container']"));

        By totalItemFoundBy = By.cssSelector("span.heading-counter");
        String textTotalItemFound = driver.findElement(totalItemFoundBy).getText();

        System.out.println("El valor de 'textTotalItemFound' es: " + textTotalItemFound);
        System.out.println("El total de items encontrados son: " + totalResult.size());

        if (totalResult.size() == 1) {
            //1 result has been found.
            Assertions.assertEquals("1 result has been found.", textTotalItemFound, "Los valores obtenidos son distintos...");
        } else {
            //7 results have been found.
            Assertions.assertEquals(totalResult.size() + " results have been found.", textTotalItemFound, "Los valores obtenidos son distintos...");
        }

        Random rand = new Random();
        String productAvailable = "", productNameSelected = "", priceProductSelected = ""; // Todo: v1.1
        WebElement randomItem = null; // Todo: v1.1
        int contador = 0; // Todo: v1.1
        boolean flagProductWithoutStock = true; // Todo: v1.1
        By productNameSelectedBy = By.cssSelector("div[class='right-block'] h5 a"); // Todo: v1.1
        By priceProductSelectedBy = By.cssSelector("div[class='right-block'] div[class='content_price'] span[class='price product-price']"); // Todo: v1.1

        if (allItem) { // True = Busca y encuentra un producto con Stock -- False = Selecciona el primer producto que encuentre // Todo: v1.1
            while (flagProductWithoutStock) {  // Todo: v1.1
                System.out.println("============================");
                Thread.sleep(generalTime);
                randomItem = totalResult.get(rand.nextInt(totalResult.size()));
                productAvailable = randomItem.findElement(By.cssSelector("span[class='availability'] span")).getText();
                productNameSelected = randomItem.findElement(productNameSelectedBy).getText();
                priceProductSelected = randomItem.findElement(priceProductSelectedBy).getText();

                System.out.println("Producto seleccionado por Random: '" + productNameSelected + "'. El precio es de: " + priceProductSelected);
                flagProductWithoutStock = productAvailable.equals("Out of stock");

                if (flagProductWithoutStock) {
                    System.out.println("Producto sin stock. 'Product Available': " + productAvailable);
                } else {
                    System.out.println("Producto con stock. 'Product Available': " + productAvailable);
                    System.out.println("============================");
                    System.out.println("Se seleccionará este producto. Detalles:");
                }

                if (flagProductWithoutStock) contador++; else contador = 1;

                if (contador == totalResult.size()) {
                    Assertions.assertFalse(true, "Los productos seleccionados por Random no tienen inventario...");
                } else if (flagProductWithoutStock) {
                    System.out.println("Intentos: " + contador);
                    System.out.println("Intentos restantes: " + (totalResult.size() - contador));
                }
            }
        } else {
            randomItem = totalResult.get(rand.nextInt(totalResult.size()));
            productNameSelected = randomItem.findElement(productNameSelectedBy).getText();
            priceProductSelected = randomItem.findElement(priceProductSelectedBy).getText();
        }

        String productNameExpected = productNameSelected, priceProductExpected = priceProductSelected; // Todo: v1.1

        System.out.println("Nombre del producto: " + productNameSelected); // Todo: v1.1
        System.out.println("Precio: " + priceProductSelected); // Todo: v1.1
        System.out.println("============================"); // Todo: v1.1

        point = randomItem.getLocation();
        js.executeScript("scrollBy(" + point.getX()  + ", " + point.getY() + ")");
        Thread.sleep(generalTime);

        randomItem.findElement(productNameSelectedBy).click();

        By itemSelectedNameBy = By.xpath("//div[@class='pb-center-column col-xs-12 col-sm-4']/h1");
        wait.until(ExpectedConditions.presenceOfElementLocated(itemSelectedNameBy));

        String itemSelectedName = driver.findElement(itemSelectedNameBy).getText();
        String itemSelectedPrice = driver.findElement(By.cssSelector("p.our_price_display span.price")).getText();

        System.out.println("Producto seleccionado: " + itemSelectedName);
        System.out.println("Precio del producto: " + itemSelectedPrice);
        System.out.println("===========================");

        Assertions.assertAll(
                ()-> Assertions.assertEquals(productNameExpected, itemSelectedName, "Los productos no tienen el mismo nombre..."), // Todo: v1.1
                ()-> Assertions.assertEquals(priceProductExpected, itemSelectedPrice, "Los productos no tienen el mismo precio") // Todo: v1.1
        );

        // Variables
        Select selectSize = new Select(driver.findElement(By.id("group_1")));
        List<WebElement> listColorAvailable = driver.findElements(By.cssSelector("ul#color_to_pick_list li a"));
        int sizeSelected = 1;
        contador = 1;
        boolean bandera1 = true, submitButtonDisplay = false;

        //Localizadores
        By byAddToCartButton = By.xpath("//p[@id='add_to_cart']");

        while (bandera1) {
            selectSize.selectByValue(String.valueOf(sizeSelected));
            System.out.print("Opción de talla seleccionada: " + sizeSelected + " ----- ");
            switch (sizeSelected) {
                case 1: System.out.println("La talla seleccionada fue 'S-Small'."); break;
                case 2: System.out.println("La talla seleccionada fue 'M-Medium'."); break;
                case 3: System.out.println("La talla seleccionada fue 'L-Large'."); break;
            }
            System.out.println("============================");

            for (WebElement selectColor : listColorAvailable) {
                selectColor.click();
                Thread.sleep(generalTime);
                System.out.println("Color elegido: " + selectColor.getAttribute("title"));
                submitButtonDisplay = driver.findElement(byAddToCartButton).isDisplayed();
                if (submitButtonDisplay) {
                    bandera1 = false;
                    driver.findElement(byAddToCartButton).click();
                    System.out.println("Se ha seleccionado un producto, para el carrito.");
                    System.out.println("============================");
                    Thread.sleep(generalTime);
                    break;
                } else {
                    System.out.println("Intento " + contador + ". No se ha encontrado disponibilidad del artículo en la talla y color seleccionado.");
                    contador++;
                    System.out.println("============================");
                }
            }

            if (bandera1 == false) {
                break;
            } else {
                bandera1 = true;
            }

            if (sizeSelected == 3) {
                Assertions.assertTrue(false, "Producto no cuenta con inventario disponible...");
                break;
            }

            sizeSelected++;
            Thread.sleep(generalTime);
        }

        By buttonProceedToCheckoutBy = By.xpath("//a[@class='btn btn-default button button-medium']");
        wait.until(ExpectedConditions.presenceOfElementLocated(buttonProceedToCheckoutBy));

        driver.findElement(buttonProceedToCheckoutBy).click();

        Thread.sleep(generalTime);

        // Shopping-cart Summary
        point = driver.findElement(By.xpath("//ul[@class='sf-menu clearfix menu-content sf-js-enabled sf-arrows']")).getLocation();
        js.executeScript("scrollBy(0, " + point.getY() + ")");
        String string_ProductShoppingCartSummary = driver.findElement(By.xpath("//td[@id='total_product']")).getText();
        String string_ShippingShoppingCartSummary = driver.findElement(By.xpath("//td[@id='total_shipping']")).getText();
        String string_TotalShoppingCartSummary = driver.findElement(By.xpath("//span[@id='total_price']")).getText();

        driver.findElement(By.xpath("//p[@class='cart_navigation clearfix']/a[@title='Proceed to checkout']")).click();

        Thread.sleep(generalTime);

        // Login
        js.executeScript("scrollBy(0, 400)");
        driver.findElement(By.id("email")).sendKeys("diegoyip93@gmail.com");
        driver.findElement(By.name("passwd")).sendKeys("abcd1234");
        driver.findElement(By.xpath("//button[@class='button btn btn-default button-medium']")).click();

        Thread.sleep(generalTime);

        point = driver.findElement(By.cssSelector("a[title='Return to Home']")).getLocation();
        js.executeScript("scrollBy(0, " + point.getY() + ")");

        // Addresses
        Thread.sleep(generalTime);
        point = driver.findElement(By.xpath("//p[@class='address_add submit']/a")).getLocation();
        js.executeScript("scrollBy(0, " + point.getY() + ")");
        driver.findElement(By.name("processAddress")).click();
        Thread.sleep(generalTime);

        // Shipping
        // --- Seleccionamos "Read the Terms of Services" (No existe la página)
        String string_ShippingShipping = driver.findElement(By.xpath("//div[@class='delivery_option_price']")).getText();
        driver.findElement(By.xpath("//p[@class='checkbox']/a")).click();
        Thread.sleep(generalTime+extendTime);

        // ------- Interactuando con el iFrame
        By frameReadTermsServiceBy = By.cssSelector("iframe[class='fancybox-iframe']");
        wait.until(ExpectedConditions.presenceOfElementLocated(frameReadTermsServiceBy));
        WebElement frameReadTermsService = driver.findElement(frameReadTermsServiceBy);
        driver.switchTo().frame(frameReadTermsService);
        By messageiFrameReadTermsService = By.xpath("//div[@class='alert alert-danger']");
        wait.until(ExpectedConditions.presenceOfElementLocated(messageiFrameReadTermsService));
        System.out.println("Mensaje obtenido del iFrame: " + driver.findElement(messageiFrameReadTermsService).getText());

        // ------- Saliendo del iFrame
        driver.switchTo().defaultContent();
        driver.findElement(By.xpath("//a[@title='Close' and @class='fancybox-item fancybox-close']")).click();

        // --- Seleccionamos el Checkbox
        driver.findElement(By.id("cgv")).click();
        driver.findElement(By.xpath("//button[@name='processCarrier']")).click();
        Thread.sleep(generalTime);

        // Please Choose your payment method
        String string_ProductPleaseChoosePaymentMethod = driver.findElement(By.xpath("//td[@id='total_product']")).getText();
        String string_ShippingPleaseChoosePaymentMethod = driver.findElement(By.xpath("//td[@id='total_shipping']")).getText();
        String string_TotalPleaseChoosePaymentMethod = driver.findElement(By.xpath("//span[@id='total_price']")).getText();
        driver.findElement(By.xpath("//a[@class='bankwire']")).click();
        Thread.sleep(generalTime);

        // Order Summary
        String string_TotalOrderSummary = driver.findElement(By.xpath("//span[@id='amount']")).getText();
        driver.findElement(By.cssSelector("button[class='button btn btn-default button-medium']")).click();
        Thread.sleep(generalTime);

        // Order confirmation
        By orderCompleteBy = By.xpath("//p[@class='alert alert-success']");
        wait.until(ExpectedConditions.presenceOfElementLocated(orderCompleteBy));

        point = driver.findElement(By.xpath("//div[@class='breadcrumb clearfix']")).getLocation();
        js.executeScript("scrollBy(0, " + point.getY() + ")");

        String string_TotalOrderComplete = driver.findElement(By.xpath("//span[@class='price']")).getText();
        Thread.sleep(generalTime);

        Assertions.assertAll(
                // Product
                ()-> Assertions.assertEquals(itemSelectedPrice, string_ProductShoppingCartSummary,
                        "El precio informado de 'product' en la pantalla 'Shopping-Cart Summary' es incorrecto."),
                ()-> Assertions.assertEquals(itemSelectedPrice, string_ProductPleaseChoosePaymentMethod,
                        "El precio informado de 'product' en la pantalla 'Please Choose Payment Method' es incorrecto."),

                // Shipping
                ()-> Assertions.assertEquals(string_ShippingShoppingCartSummary, string_ShippingShipping,
                        "El precio informado de 'Shipping' en la pantalla 'Shipping' es incorrecto."),
                ()-> Assertions.assertEquals(string_ShippingShoppingCartSummary, string_ShippingPleaseChoosePaymentMethod,
                        "El precio informado de 'Shipping' en la pantalla 'Please Choose Payment Method' es incorrecto."),

                // Total
                ()-> Assertions.assertEquals(string_TotalShoppingCartSummary, string_TotalPleaseChoosePaymentMethod,
                        "El precio informado de 'Total' en la pantalla 'Please Choose Payment Method' es incorrecto."),
                ()-> Assertions.assertEquals(string_TotalShoppingCartSummary, string_TotalOrderSummary,
                        "El precio informado de 'Total' en la pantalla 'Total Order Summary' es incorrecto."),
                ()-> Assertions.assertEquals(string_TotalShoppingCartSummary, string_TotalOrderComplete,
                        "El precio informado de 'Total' en la pantalla 'Total Order Complete' es incorrecto.")
        );

        System.out.println("Mensaje final: " + driver.findElement(By.xpath("//p[@class='alert alert-success']")).getText());

        System.out.println("<------------------------------->");
        System.out.println("Finalizando 'practicaPersonalizada01'.");
        System.out.println("=================================");
    }
}