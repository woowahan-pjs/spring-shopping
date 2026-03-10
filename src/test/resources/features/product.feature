# POST /api/products
# GET /api/products
# GET /api/products/{id}
# PUT /api/products/{id}
# DELETE /api/products/{id}
Feature: Product CRUD

  Scenario: Create a product
    When I create a product with name "Keyboard" price 50000 and imageUrl "https://example.com/kb.png"
    Then the product should be created
    And the product response should have name "Keyboard"

  Scenario: Find a product by id
    Given a product exists with name "Mouse" price 30000 and imageUrl "https://example.com/mouse.png"
    When I find the product by id
    Then the product should be found
    And the product response should have name "Mouse"

  Scenario: Update a product
    Given a product exists with name "Old Name" price 10000 and imageUrl "https://example.com/old.png"
    When I update the product with name "New Name" price 20000 and imageUrl "https://example.com/new.png"
    Then the product should be updated
    And the product response should have name "New Name"

  Scenario: Delete a product
    Given a product exists with name "ToDelete" price 10000 and imageUrl "https://example.com/del.png"
    When I delete the product
    Then the product should be deleted

  Scenario: Find all products
    Given a product exists with name "Item1" price 10000 and imageUrl "https://example.com/1.png"
    And a product exists with name "Item2" price 20000 and imageUrl "https://example.com/2.png"
    When I find all products
    Then the products should be found

  Scenario: Create a product with profanity name returns 400
    When I create a product with name "badword" price 50000 and imageUrl "https://example.com/bad.png"
    Then the response status should be 400
    And the response should have message "상품 이름에 비속어가 포함되어 있습니다."

  Scenario: Create a product when profanity API fails saves as pending
    Given the profanity API is unavailable
    When I create a product with name "GoodName" price 50000 and imageUrl "https://example.com/good.png"
    Then the product should be created
    When I find the product by id
    Then the response status should be 404

  Scenario: Update a product with profanity name returns 400
    Given a product exists with name "Original" price 10000 and imageUrl "https://example.com/orig.png"
    When I update the product with name "badword" price 20000 and imageUrl "https://example.com/new.png"
    Then the response status should be 400
    And the response should have message "상품 이름에 비속어가 포함되어 있습니다."

  Scenario: Update a product when profanity API fails returns 500
    Given a product exists with name "Original" price 10000 and imageUrl "https://example.com/orig.png"
    And the profanity API is unavailable
    When I update the product with name "NewName" price 20000 and imageUrl "https://example.com/new.png"
    Then the response status should be 500
