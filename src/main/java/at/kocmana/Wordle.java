package at.kocmana;

import static java.util.Objects.isNull;

import at.kocmana.model.LetterResult;
import at.kocmana.model.LetterState;
import at.kocmana.model.Result;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.util.ArrayList;

public class Wordle {

  private final Playwright playwright;
  private final Browser browser;
  private final Page page;

  public Wordle() {
    this.playwright = Playwright.create();
    this.browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
    this.page = browser.newPage();
    page.navigate("https://www.powerlanguage.co.uk/wordle/");
  }

  public void removeBanner() {
    page.click("#pz-gdpr-btn-reject");
    page.click(".close-icon");
  }

  public void type(String input) {
    if (isNull(input) || input.length() != 5) {
      throw new IllegalArgumentException("Words need to be non null and 5 characters long.");
    }
    for (char c : input.toCharArray()) {
      type(c);
    }
    type('↵');
  }

  public void cleanEntry() {
    type("←←←←←");
  }

  public boolean entryWasAccepted() {
    return page.locator("div[data-state=\"tbd\"]").count() == 0;
  }

  public Result analyzeResultsFor(String word) {
    var wordSelector = String.format("game-row[letters=\"%s\"]", word);
    var elementHandles = page.querySelector(wordSelector).querySelectorAll("game-tile");

    var letters = new ArrayList<LetterResult>();
    for (int i = 0; i < elementHandles.size(); i++) {
      var element = elementHandles.get(i);
      var letter = new LetterResult(element.getAttribute("letter").charAt(0),
          i,
          LetterState.fromString(element.getAttribute("evaluation")));
      letters.add(letter);
    }
    return new Result(letters);
  }

  public boolean hasGuessesLeft() {
    return page.locator("div[id=\"statistics\"]").count() == 0;
  }

  public void close() {
    page.close();
    playwright.close();
  }

  private void type(char character) {
    var selectorString = String.format("button[data-key=\"%s\"]", character);
    page.click(selectorString);
  }
}
