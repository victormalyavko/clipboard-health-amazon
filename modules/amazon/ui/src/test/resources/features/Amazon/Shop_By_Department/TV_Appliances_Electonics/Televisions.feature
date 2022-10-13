@ui
Feature: Television

  Scenario: Samsung
    Given Go to "https://www.amazon.in"
    When I select the category "TV, Appliances, Electronics" and click shop "Televisions"
    Then Electronics page should be opened
    When I filter by brand "Samsung" on Electronics page
    And Sort Electronics page with "Price: High to Low"
    And Select product with number 2
    Then "About this item" section should be "Appears" on the Television Page