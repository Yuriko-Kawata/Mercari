import { test, expect } from '@playwright/test';


test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:8080/product-management-ex/toRegister');
  });

test("01レイアウト", async ({ page }) => {
    await expect(page).toHaveScreenshot("admin.png", {
        fullPage: true
    });
});

test("02正常テスト", async ({ page }) => {
    await page.fill('input[id="name"]',"a");
    await page.fill('input[id="mail"]',"a@a");
    await page.fill('input[type="password"]',"ABCdef12");
    await page.fill('input[type="passwordCheck"]',"ABCdef12");
    await page.click('button[type="submit"]');
    await page.waitForSelector('#confirm > div.panel.panel-default > div.panel-heading'); // 要素が表示されるのを待つ
    const element = await page.locator('#confirm > div.panel.panel-default > div.panel-heading');
    await expect(element).toHaveText("登録完了");
})