package com.bookstore.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class AdminPage extends BasePage {

    // Login elements
    @FindBy(name = "username")
    private WebElement usernameInput;

    @FindBy(name = "password")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(css = "input[name='_csrf']")
    private WebElement csrfToken;

    // Admin navigation elements
    @FindBy(linkText = "Dashboard")
    private WebElement dashboardLink;

    @FindBy(linkText = "Books")
    private WebElement booksLink;

    @FindBy(linkText = "Categories")
    private WebElement categoriesLink;

    @FindBy(linkText = "Orders")
    private WebElement ordersLink;

    @FindBy(linkText = "Users")
    private WebElement usersLink;

    // Alternative selectors for navigation
    @FindBy(xpath = "//a[contains(text(), 'Books')]")
    private WebElement booksLinkAlt;

    @FindBy(css = "a[href*='/books']")
    private WebElement booksLinkByHref;

    @FindBy(css = "a[href*='/admin/books']")
    private WebElement booksLinkByAdminHref;

    // Admin content elements
    @FindBy(id = "addBookButton")
    private WebElement addBookButton;

    @FindBy(id = "bookSearch")
    private WebElement bookSearchInput;

    @FindBy(css = ".navbar, nav, .sidebar, .menu")
    private List<WebElement> navigationContainers;

    @FindBy(tagName = "h1")
    private List<WebElement> pageHeaders;

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Login to the application
     */
    public void login(String username, String password) {
        System.out.println("🔐 Attempting to login as: " + username);

        // Navigate to login page if not already there
        if (!isLoginPage()) {
            driver.get("http://localhost:8080/login");
            waitForPageLoad();
        }

        // Wait for login form to be visible
        waitForElementToBeVisible(usernameInput);

        // Fill login form
        usernameInput.clear();
        usernameInput.sendKeys(username);

        passwordInput.clear();
        passwordInput.sendKeys(password);

        // Click login button
        clickElement(loginButton);

        // Wait for redirect after login
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));

        System.out.println("✅ Login completed. Current URL: " + driver.getCurrentUrl());
    }

    /**
     * Check if current page is login page
     */
    public boolean isLoginPage() {
        try {
            return driver.getCurrentUrl().contains("/login") &&
                    usernameInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if user is logged in and on admin page
     */
    public boolean isLoggedIn() {
        try {
            String currentUrl = driver.getCurrentUrl();
            return !currentUrl.contains("/login") &&
                    (currentUrl.contains("/admin") ||
                            isAdminPanelDisplayed());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Ensure user is logged in before performing admin actions
     */
    public void ensureLoggedIn(String username, String password) {
        if (!isLoggedIn()) {
            login(username, password);
            // Navigate to admin dashboard after login
            if (!driver.getCurrentUrl().contains("/admin")) {
                driver.get("http://localhost:8080/admin");
                waitForPageLoad();
            }
        }
    }

    /**
     * Check if admin panel is displayed
     */
    public boolean isAdminPanelDisplayed() {
        try {
            String currentUrl = driver.getCurrentUrl();
            String pageTitle = driver.getTitle().toLowerCase();
            String pageSource = driver.getPageSource().toLowerCase();

            boolean isAdminUrl = currentUrl.contains("/admin");
            boolean isAdminTitle = pageTitle.contains("admin") || pageTitle.contains("dashboard");
            boolean hasAdminContent = pageSource.contains("dashboard") ||
                    pageSource.contains("admin") ||
                    hasNavigationElements();

            System.out.println("🔍 Admin Panel Check:");
            System.out.println("  - URL contains '/admin': " + isAdminUrl);
            System.out.println("  - Title contains 'admin': " + isAdminTitle);
            System.out.println("  - Has admin content: " + hasAdminContent);
            System.out.println("  - Current URL: " + currentUrl);
            System.out.println("  - Page Title: " + driver.getTitle());

            return isAdminUrl || isAdminTitle || hasAdminContent;
        } catch (Exception e) {
            System.out.println("❌ Error checking admin panel: " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if navigation elements are present
     */
    private boolean hasNavigationElements() {
        try {
            return !navigationContainers.isEmpty() ||
                    isElementPresent(By.tagName("nav")) ||
                    isElementPresent(By.cssSelector(".navbar")) ||
                    isElementPresent(By.cssSelector(".sidebar")) ||
                    isElementPresent(By.cssSelector(".menu"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Navigate to Books section with multiple fallback strategies
     */
    public void navigateToBooks() {
        System.out.println("📚 Attempting to navigate to Books section...");

        waitForPageLoad();

        // Try multiple strategies to find and click Books link
        if (tryClickBooksLink(booksLink, "LinkText 'Books'")) {
            return;
        }

        if (tryClickBooksLink(booksLinkAlt, "XPath contains 'Books'")) {
            return;
        }

        if (tryClickBooksLink(booksLinkByHref, "CSS href contains '/books'")) {
            return;
        }

        if (tryClickBooksLink(booksLinkByAdminHref, "CSS href contains '/admin/books'")) {
            return;
        }

        // Try dynamic finding
        if (tryDynamicBooksLink()) {
            return;
        }

        // Last resort: navigate directly
        navigateToBooksDirectly();
    }

    /**
     * Try to click a Books link with specific strategy
     */
    private boolean tryClickBooksLink(WebElement element, String strategy) {
        try {
            if (element.isDisplayed() && element.isEnabled()) {
                System.out.println("✅ Found Books link using: " + strategy);
                safeClick(element);
                waitForPageLoad();
                return true;
            }
        } catch (Exception e) {
            System.out.println("❌ Failed with " + strategy + ": " + e.getMessage());
        }
        return false;
    }

    /**
     * Try to find Books link dynamically
     */
    private boolean tryDynamicBooksLink() {
        try {
            // Try multiple dynamic selectors
            String[] selectors = {
                    "//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'books')]",
                    "//a[contains(@href, 'books')]",
                    "//*[contains(text(), 'Books')]",
                    "//nav//a[contains(text(), 'Books')]",
                    "//aside//a[contains(text(), 'Books')]",
                    "//div[contains(@class, 'menu')]//a[contains(text(), 'Books')]"
            };

            for (String selector : selectors) {
                try {
                    WebElement link = driver.findElement(By.xpath(selector));
                    if (link.isDisplayed() && link.isEnabled()) {
                        System.out.println("✅ Found Books link using dynamic XPath: " + selector);
                        safeClick(link);
                        waitForPageLoad();
                        return true;
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
        } catch (Exception e) {
            System.out.println("❌ All dynamic strategies failed: " + e.getMessage());
        }
        return false;
    }

    /**
     * Navigate directly to books URL as last resort
     */
    private void navigateToBooksDirectly() {
        System.out.println("🔄 Navigating directly to /admin/books");
        driver.get("http://localhost:8080/admin/books");
        waitForPageLoad();

        String currentUrl = driver.getCurrentUrl();
        System.out.println("📍 After direct navigation - URL: " + currentUrl);

        if (!currentUrl.contains("/books")) {
            System.out.println("⚠️  May have been redirected. Current page: " + driver.getTitle());
        }
    }

    /**
     * Check if Books link is present on the page
     */
    public boolean isBooksLinkPresent() {
        try {
            // Check all possible Books link variants
            boolean linkTextPresent = booksLink.isDisplayed();
            boolean xpathPresent = booksLinkAlt.isDisplayed();
            boolean hrefPresent = booksLinkByHref.isDisplayed();
            boolean adminHrefPresent = booksLinkByAdminHref.isDisplayed();

            // Check dynamically
            boolean dynamicPresent = isElementPresent(By.xpath("//a[contains(text(), 'Books')]")) ||
                    isElementPresent(By.cssSelector("a[href*='books']"));

            boolean isPresent = linkTextPresent || xpathPresent || hrefPresent || adminHrefPresent || dynamicPresent;

            System.out.println("🔍 Books Link Presence Check:");
            System.out.println("  - LinkText: " + linkTextPresent);
            System.out.println("  - XPath: " + xpathPresent);
            System.out.println("  - CSS href: " + hrefPresent);
            System.out.println("  - CSS admin href: " + adminHrefPresent);
            System.out.println("  - Dynamic: " + dynamicPresent);
            System.out.println("  - Overall: " + isPresent);

            return isPresent;
        } catch (Exception e) {
            System.out.println("❌ Error checking Books link presence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Navigate to other admin sections
     */
    public void navigateToDashboard() {
        safeNavigate(dashboardLink, "Dashboard");
    }

    public void navigateToCategories() {
        safeNavigate(categoriesLink, "Categories");
    }

    public void navigateToOrders() {
        safeNavigate(ordersLink, "Orders");
    }

    public void navigateToUsers() {
        safeNavigate(usersLink, "Users");
    }

    /**
     * Safe navigation helper method
     */
    private void safeNavigate(WebElement element, String sectionName) {
        try {
            System.out.println("🔄 Navigating to " + sectionName + "...");
            safeClick(element);
            waitForPageLoad();
            System.out.println("✅ Navigated to " + sectionName + ". URL: " + driver.getCurrentUrl());
        } catch (Exception e) {
            System.out.println("❌ Failed to navigate to " + sectionName + ": " + e.getMessage());
            // Fallback: direct navigation
            driver.get("http://localhost:8080/admin/" + sectionName.toLowerCase());
            waitForPageLoad();
        }
    }

    /**
     * Search for books in admin panel
     */
    public void searchBooks(String searchTerm) {
        try {
            System.out.println("🔍 Searching for: " + searchTerm);
            waitForElementToBeVisible(bookSearchInput);
            bookSearchInput.clear();
            bookSearchInput.sendKeys(searchTerm);
            System.out.println("✅ Search performed");
        } catch (Exception e) {
            System.out.println("❌ Search failed: " + e.getMessage());
            // Search might not be available on current page
        }
    }

    /**
     * Add new book
     */
    public void clickAddBook() {
        try {
            safeClick(addBookButton);
            waitForPageLoad();
            System.out.println("✅ Add book button clicked");
        } catch (Exception e) {
            System.out.println("❌ Add book failed: " + e.getMessage());
        }
    }

    /**
     * Get current page title for verification
     */
    public String getPageHeader() {
        try {
            if (!pageHeaders.isEmpty()) {
                return pageHeaders.get(0).getText();
            }
            return driver.getTitle();
        } catch (Exception e) {
            return driver.getTitle();
        }
    }

    /**
     * Check if currently on books management page
     */
    public boolean isOnBooksPage() {
        String currentUrl = driver.getCurrentUrl();
        String pageTitle = driver.getTitle().toLowerCase();
        String pageHeader = getPageHeader().toLowerCase();

        boolean urlCheck = currentUrl.contains("/books");
        boolean titleCheck = pageTitle.contains("book");
        boolean headerCheck = pageHeader.contains("book");

        System.out.println("🔍 Books Page Check:");
        System.out.println("  - URL contains '/books': " + urlCheck);
        System.out.println("  - Title contains 'book': " + titleCheck);
        System.out.println("  - Header contains 'book': " + headerCheck);

        return urlCheck || titleCheck || headerCheck;
    }
}