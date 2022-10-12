@health:selenide
@scope:ui
Feature: Selenide entry features

  @browser:chrome
  Scenario: [Selenoid] Health check of Selenoid and Selenide work correct
    Given Open page "chrome://about/"
    Then Page with WebElement "//a[@href='chrome://about/']" should be "Appears"

  Scenario: [Selenoid] Open https://www.myfin.by
    Given Open page "https://www.myfin.by"

  Scenario: Health check with opening Page
    Given Open page "https://www.free2move.com/en-GB/"
    Then Page should be opened with WebElement "//div[@id='park']//button[@type='submit']"
    Then Page with WebElement "//div[@id='park']//button[@type='submit']" should be "Clickable"

  Scenario: Health check with input value at Page
    Given Open page "https://www.free2move.com/en-GB/"
    Then Page with WebElement "//div[@id='park']//button[@type='submit']" should be "Clickable"
    When I click WebElement "//button[@type='button' and contains(@class, 'btn-primary')]"
    When I input "My custom value" in field "//input[@type='search' and @id='widget-park-from-spot-1']"