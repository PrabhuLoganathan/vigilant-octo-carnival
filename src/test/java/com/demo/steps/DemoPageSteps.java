package com.demo.steps;

import com.demo.config.DriverFactory;
import com.demo.pages.DemoPage;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class DemoPageSteps {

    private DemoPage page() {
        // Lazily create the page object with the driver available after @Before hook
        return new DemoPage(DriverFactory.getDriver());
    }

    @Given("I am on the SeleniumBase Demo Page")
    public void i_am_on_demo_page() {
        Assert.assertTrue(page().isFormHeadingVisible(), "Demo page heading not visible");
    }

    @When("I type {string} into the Text Input Field")
    public void i_type_into_text_input(String text) {
        page().enterText(text);
    }

    @When("I type the following notes into the Textarea: {string}")
    public void i_type_into_textarea(String notes) {
        page().enterNotes(notes);
    }

    @When("I click the green button")
    public void i_click_green_button() {
        page().clickGreenButton();
    }

    @Then("the paragraph message should contain {string}")
    public void the_paragraph_should_contain(String expected) {
        String actual = page().paragraphColorText();
        Assert.assertTrue(actual.contains(expected),
                "Paragraph text did not contain expected text. Actual: " + actual);
    }

    @When("I set the slider to {int}")
    public void i_set_slider_to(int value) {
        page().setSlider(value);
    }

    @When("I choose {string} from the Select Dropdown")
    public void i_choose_from_select(String option) {
        page().selectPercent(option);
    }

    @Then("the SVG rect should be visible")
    public void svg_rect_visible() {
        Assert.assertTrue(page().isSvgRectVisible(), "SVG rect not visible");
    }

    @Then("the core links should be visible")
    public void core_links_visible() {
        Assert.assertTrue(page().areCoreLinksVisible(), "Core links missing");
    }

    @When("I select Radio Button {int}")
    public void i_select_radio(int index) {
        page().selectRadio(index);
    }

    @Then("Radio Button {int} should be selected")
    public void radio_should_be_selected(int index) {
        Assert.assertTrue(page().isRadioSelected(index), "Radio button " + index + " not selected");
    }

    @When("I set checkboxes to c1 {string}, c2 {string}, pre {string}")
    public void i_set_checkboxes(String c1, String c2, String pre) {
        boolean b1 = Boolean.parseBoolean(c1);
        boolean b2 = Boolean.parseBoolean(c2);
        boolean b3 = Boolean.parseBoolean(pre);
        page().setCheckBoxes(b1, b2, b3);
    }

    @When("I tick the checkbox inside the iframe")
    public void i_tick_iframe_checkbox() {
        page().checkIframeBox();
    }

    @When("I drag item A and drop it onto item B")
    public void drag_and_drop() {
        page().performDragAndDrop();
    }
}
