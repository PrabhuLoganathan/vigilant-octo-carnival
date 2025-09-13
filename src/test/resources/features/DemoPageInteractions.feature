@ui @demopage
Feature: Interact with SeleniumBase Demo Page using PageFactory
  As a QA engineer
  I want to automate all key widgets on the SeleniumBase Demo Page
  So that I can validate end-to-end UI interactions

  Background:
    Given I am on the SeleniumBase Demo Page

  @text @button
  Scenario: Text inputs and button color change
    When I type "Hello Demo Page" into the Text Input Field
    And I type the following notes into the Textarea: "Multiline notes"
    And I click the green button
    Then the paragraph message should contain "Text"

  @slider @dropdown @svg
  Scenario: Slider + Dropdown + SVG
    When I set the slider to 75
    And I choose "75%" from the Select Dropdown
    Then the SVG rect should be visible

  @radios @checkboxes
  Scenario: Radios and Checkboxes
    When I select Radio Button 2
    Then Radio Button 2 should be selected
    When I set checkboxes to c1 "true", c2 "true", pre "true"

  @iframe @dragdrop
  Scenario: iFrame checkbox and drag drop
    When I tick the checkbox inside the iframe
    And I drag item A and drop it onto item B

  @links
  Scenario: Links present
    Then the core links should be visible
