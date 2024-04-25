import { test, expect } from '@playwright/test';

test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:8080/product-management-ex/Login');
  });

test("01レイアウト", async ({ page }) => {
    await expect(page).toHaveScreenshot("login.png", {
        fullPage: true
    });
});

test("02未入力テスト", async ({ page }) => {

    // ログインボタンをクリックしてフォームを送信します
    await page.click('button', { button: 'left' });
    // input要素がエラー状態になるのを待ちます
    await page.waitForSelector('input:invalid');
    // エラーメッセージを取得します
    const errorMessage = await page.evaluate(() => {
      const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
      return errorElement.validationMessage;
    });
    expect(errorMessage).toMatch("このフィールドを入力してください");
});

test("03正常テスト", async ({ page }) => {
    await page.fill('input[type="email"]',"test3@example.com");
    await page.fill('input[type="password"]',"aaaAAA111");
    await page.click('button', { button: 'left' });
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element2 = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element2).toHaveText("対象製品：1,482,535件");
})

test("04正常テスト", async ({ page }) => {
    await page.fill('input[type="email"]',"test2@example.com");
    await page.fill('input[type="password"]',"bbbBBB222");
    await page.click('button', { button: 'left' });
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element2 = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element2).toHaveText("対象製品：1,482,535件");
})
test("05異常テスト", async ({ page }) => {
    await page.fill('input[type="email"]',"test3example.com");
    await page.fill('input[type="password"]',"aaaAAA111");
    await page.click('button', { button: 'left' });
    await page.waitForSelector('input:invalid');
    // エラーメッセージを取得します
    const errorMessage = await page.evaluate(() => {
      const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
      return errorElement.validationMessage;
    });
    expect(errorMessage).toMatch("メール アドレスに「@」を挿入してください。「test3example.com」内に「@」がありません。");
})
test("06異常テスト", async ({ page }) => {
    await page.fill('input[type="email"]',"test3@example.com");
    await page.fill('input[type="password"]',"cccCCC333");
    await page.click('button', { button: 'left' });
    // エラーメッセージを取得します
    const element2 = await page.locator('div[class="check"]');
    await expect(element2).toHaveText("メールアドレス、またはパスワードが間違っています");
})
test("07異常テスト", async ({ page }) => {
    await page.fill('input[type="email"]',"a@example.com");
    await page.fill('input[type="password"]',"aaaAAA111");
    await page.click('button', { button: 'left' });
    // エラーメッセージを取得します
    const element2 = await page.locator('div[class="check"]');
    await expect(element2).toHaveText("メールアドレス、またはパスワードが間違っています");
})
test("08異常テスト", async ({ page }) => {
    await page.fill('input[type="password"]',"aaaAAA111");
    await page.click('button', { button: 'left' });
    await page.waitForSelector('input:invalid');
    // エラーメッセージを取得します
    const errorMessage = await page.evaluate(() => {
      const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
      return errorElement.validationMessage;
    });
    expect(errorMessage).toMatch("このフィールドを入力してください");
})
test("09異常テスト", async ({ page }) => {
    await page.fill('input[type="email"]',"test3@example.com");
    await page.fill('input[type="password"]',"aaaaaa111");
    await page.click('button', { button: 'left' });
    // エラーメッセージを取得します
    const element2 = await page.locator('div[class="check"]');
    await expect(element2).toHaveText("メールアドレス、またはパスワードが間違っています");
})
test("10異常テスト", async ({ page }) => {
    await page.fill('input[type="email"]',"test3@example.com");
    await page.fill('input[type="password"]',"AAAAAA111");
    await page.click('button', { button: 'left' });
    // エラーメッセージを取得します
    const element2 = await page.locator('div[class="check"]');
    await expect(element2).toHaveText("メールアドレス、またはパスワードが間違っています");
})
test("11異常テスト", async ({ page }) => {
    await page.fill('input[type="email"]',"test3@example.com");
    await page.click('button', { button: 'left' });
    await page.waitForSelector('input:invalid');
    // エラーメッセージを取得します
    const errorMessage = await page.evaluate(() => {
      const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
      return errorElement.validationMessage;
    });
    expect(errorMessage).toMatch("このフィールドを入力してください");
})
test("12画面遷移テスト", async ({ page }) => {
    await page.click('a[type="submit"]');
    await page.waitForSelector('div[class="panel-heading"]'); // 要素が表示されるのを待つ
    const element2 = await page.locator('div[class="panel-heading"]');
    await expect(element2).toContainText("Register Account");
})
test("13異常テスト", async ({ page }) => {
    await page.fill('input[type="email"]',"<script>alert('XSS Attack');</script>");
    await page.fill('input[type="password"]',"<script>alert('XSS Attack');</script>");
    await page.click('button', { button: 'left' });
    await page.waitForSelector('input:invalid');
    // エラーメッセージを取得します
    const errorMessage = await page.evaluate(() => {
      const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
      return errorElement.validationMessage;
    });
    expect(errorMessage).toMatch("メール アドレスに「@」を挿入してください。「<script>alert('XSS Attack');</script>」内に「@」がありません。");
})
test("14異常テスト", async ({ page }) => {
    await page.fill('input[type="email"]',"' OR '1'='1");
    await page.fill('input[type="password"]',"' OR '1'='1");
    await page.click('button', { button: 'left' });
    await page.waitForSelector('input:invalid');
    // エラーメッセージを取得します
    const errorMessage = await page.evaluate(() => {
      const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
      return errorElement.validationMessage;
    });
    expect(errorMessage).toMatch("メール アドレスに「@」を挿入してください。「' OR '1'='1」内に「@」がありません。");
})
  
