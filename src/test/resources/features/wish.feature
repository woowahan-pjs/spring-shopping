# GET /api/wishes
# POST /api/wishes/{productId}
# DELETE /api/wishes/{productId}
Feature: Wishlist management

  Scenario: Add a product to wishlist
    Given I am a registered member with email "wish@example.com" and password "password123"
    And a product exists with name "Keyboard" price 50000 and imageUrl "https://example.com/kb.png"
    When I add the product to my wishlist
    Then the wish should be created
    And the wish response should have product name "Keyboard"

  Scenario: View my wishlist
    Given I am a registered member with email "view@example.com" and password "password123"
    And a product exists with name "Mouse" price 30000 and imageUrl "https://example.com/mouse.png"
    And the product is in my wishlist
    When I view my wishlist
    Then the wishlist should contain 1 product
    And the wishlist should have product name "Mouse"

  Scenario: Remove a product from wishlist
    Given I am a registered member with email "remove@example.com" and password "password123"
    And a product exists with name "Monitor" price 500000 and imageUrl "https://example.com/mon.png"
    And the product is in my wishlist
    When I remove the product from my wishlist
    Then the wish should be removed

  Scenario: View wishlist after product is deleted
    Given I am a registered member with email "deleted@example.com" and password "password123"
    And a product exists with name "OldItem" price 10000 and imageUrl "https://example.com/old.png"
    And the product is in my wishlist
    And the product is deleted
    When I view my wishlist
    Then the wishlist should contain 0 product

  Scenario: Add duplicate product to wishlist returns existing wish
    Given I am a registered member with email "wish-dup@example.com" and password "password123"
    And a product exists with name "Headset" price 80000 and imageUrl "https://example.com/hs.png"
    And the product is in my wishlist
    When I add the product to my wishlist
    Then the wish should be created
    And the wish response should have product name "Headset"

  Scenario: Adding duplicate product to wishlist does not increase count
    Given I am a registered member with email "dup-count@example.com" and password "password123"
    And a product exists with name "DupTest" price 30000 and imageUrl "https://example.com/dup.png"
    And the product is in my wishlist
    When I add the product to my wishlist
    Then the wish should be created
    When I view my wishlist
    Then the wishlist should contain 1 product

  Scenario: Deleting a product removes wishes from all members
    Given I am a registered member with email "multi1@example.com" and password "password123"
    And a product exists with name "SharedItem" price 10000 and imageUrl "https://example.com/shared.png"
    And the product is in my wishlist
    Given I am a registered member with email "multi2@example.com" and password "password123"
    And the product is in my wishlist
    And the product is deleted
    When I view my wishlist
    Then the wishlist should contain 0 product
    When I log in as member "multi1@example.com" with password "password123"
    And I view my wishlist
    Then the wishlist should contain 0 product

  Scenario: Deleting a product does not affect wishes for other products
    Given I am a registered member with email "other@example.com" and password "password123"
    And a product exists with name "KeepItem" price 20000 and imageUrl "https://example.com/keep.png"
    And the product is in my wishlist
    And a product exists with name "DeleteItem" price 10000 and imageUrl "https://example.com/del.png"
    And the product is in my wishlist
    And the product is deleted
    When I view my wishlist
    Then the wishlist should contain 1 product
    And the wishlist should have product name "KeepItem"
