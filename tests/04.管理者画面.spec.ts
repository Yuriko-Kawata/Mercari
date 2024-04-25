import { test, expect } from '@playwright/test';

const { exec } = require('child_process');
const util = require('util');
const execPromise = util.promisify(exec);

test.describe.configure({ mode: 'default' });

const backup = async (fileName: string) => {
  try {
    // データベースをバックアップ
    const { stdout, stderr } = await execPromise('pg_dump -U postgres -d product-management-ex -a --table=users > tests/users_db.sql');
    console.log('stdout:', stdout);
    console.error('stderr:', stderr);
  } catch (error) {
    console.error('Error:', error);
  }
};

const dropAndRestore = async (fileName: string) => {
      //データベースを削除
      await execPromise('psql -U postgres -d product-management-ex -c "DELETE FROM users;"');
      // データベースを作成
      await execPromise("psql -U postgres -d product-management-ex -f tests/users_db.sql");

  };

test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:8080/product-management-ex/toRegister');
  });

  test('DBバックアップ', async () => {
    test.setTimeout(6000); // タイムアウト時間を60秒に設定
    const dumpFileName = 'users_db.sql';
    await backup(dumpFileName);
  });


test("01レイアウト", async ({ page }) => {
    await expect(page).toHaveScreenshot("admin.png", {
        fullPage: true
    });
});

test("02正常テスト", async ({ page }) => {
    await page.fill('input#name', "a");
    await page.fill('input#mail', "a@a");
    await page.fill('input#password', "ABCdef12");
    await page.fill('input#passwordCheck', "ABCdef12");    
    await page.click('button[type="submit"]');
    await page.waitForSelector('#confirm > div.panel.panel-default > div.panel-heading'); // 要素が表示されるのを待つ
    const element = await page.locator('#confirm > div.panel.panel-default > div.panel-heading');
    await expect(element).toHaveText("登録完了");
})

test("03正常テスト", async ({ page }) => {
    await page.fill('input#name', "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    await page.fill('input#mail', "abcdefghijklmnopqrstuvwxyz123456789abcdefghijklmnopqrstuvwxyz123@aacdefghijklmnopqrstuvwxyz123456789abc11bcdefghijklmnopqrstuvaa.aacdefghijklmnopqrstuvwxyz123456789abc11bcdefghijklmnopqrstuvaa.aacdefghijklmnopqrstuvwxyz123456789abc11bcdefghijklmnopqrstuva");
    await page.fill('input#password', "ABCDEFG123456abcdefg");
    await page.fill('input#passwordCheck', "ABCDEFG123456abcdefg");    
    await page.click('button[type="submit"]');
    await page.waitForSelector('#confirm > div.panel.panel-default > div.panel-heading'); // 要素が表示されるのを待つ
    const element = await page.locator('#confirm > div.panel.panel-default > div.panel-heading');
    await expect(element).toHaveText("登録完了");
})

test("04正常テスト,05ブラウザバック", async ({ page }) => {
    await page.fill('input#name', "testtest");
    await page.fill('input#mail', "test@test.co.jp");
    await page.fill('input#password', "Test12341234");
    await page.fill('input#passwordCheck', "Test12341234");    
    await page.click('button[type="submit"]');
    await page.waitForSelector('#confirm > div.panel.panel-default > div.panel-heading'); // 要素が表示されるのを待つ
    const element = await page.locator('#confirm > div.panel.panel-default > div.panel-heading');
    await expect(element).toHaveText("登録完了");
    await page.goBack();

    await page.waitForSelector('#login > div.panel.panel-default > div.panel-heading'); // 要素が表示されるのを待つ
    const element2 = await page.locator('#login > div.panel.panel-default > div.panel-heading');
    await expect(element2).toHaveText("Register Account全項目入力必須");
});

test("06異常テスト", async ({ page }) => {
    await page.click('button[type="submit"]');
    await page.waitForSelector('input:invalid');
    // エラーメッセージを取得します
    const errorMessage = await page.evaluate(() => {
      const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
      return errorElement.validationMessage;
    });
    expect(errorMessage).toMatch("このフィールドを入力してください");

})
test("07異常テスト", async ({ page }) => {
    await page.fill('input#name', "　");
    await page.fill('input#mail', "　");
    await page.fill('input#password', "　");
    await page.fill('input#passwordCheck', "　");  
    await page.click('button[type="submit"]');
    await page.waitForSelector('input:invalid');
    // エラーメッセージを取得します
    const errorMessage = await page.evaluate(() => {
      const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
      return errorElement.validationMessage;
    });
    expect(errorMessage).toMatch("メール アドレスに「@」を挿入してください。「　」内に「@」がありません。");

})
test("08異常テスト", async ({ page }) => {
    await page.fill('input#name', "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    await page.fill('input#mail', "abcdefghijklmnopqrstuvwxyz123456789abcdefghijklmnopqrstuvwxyz123@aacdefghijklmnopqrstuvwxyz123456789abc11bcdefghijklmnopqrstuvaa.aacdefghijklmnopqrstuvwxyz123456789abc11bcdefghijklmnopqrstuvaa.aacdefghijklmnopqrstuvwxyz123456789abc11bcdefghijklmnopqrstuvaa");
    await page.fill('input#password', "aABCDEFG123456abcdefg");
    await page.fill('input#passwordCheck', "aABCDEFG123456abcdefg");  
    await page.click('button[type="submit"]');
    const elements = await page.locator('.check').elementHandles();
    
    if (elements.length >= 4) {
        // 要素が見つかった場合、メッセージを取得してチェックする
        const text1 = await elements[1].innerText();
        expect(text1).toContain('nameは50文字以内で入力してください');

        const text2 = await elements[2].innerText();
        expect(text2).toContain('mailは255文字以内で入力してください');

        const text3 = await elements[3].innerText();
        expect(text3).toContain('passwordは8以上、20以下で入力してください');
    } else {
        // 要素が見つからない場合、エラーメッセージをログに出力する
        console.log("エラーメッセージが見つかりませんでした。");
    }
});
test("09異常テスト", async ({ page }) => {
  await page.fill('input#name', "a");
  await page.fill('input#mail', "a");
  await page.fill('input#password', "a");
  await page.fill('input#passwordCheck', "a");  
  await page.click('button[type="submit"]');
  await page.waitForSelector('input:invalid');
  // エラーメッセージを取得します
  const errorMessage = await page.evaluate(() => {
    const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
    return errorElement.validationMessage;
  });
  expect(errorMessage).toMatch("メール アドレスに「@」を挿入してください。「a」内に「@」がありません。");

})
test("10異常テスト", async ({ page }) => {
  await page.fill('input#name', "あ");
  await page.fill('input#mail', "あ@あ");
  await page.fill('input#password', "あaaaA111");
  await page.fill('input#passwordCheck', "あaaaA111");  
  await page.click('button[type="submit"]');
  await page.waitForSelector('input:invalid');
  // エラーメッセージを取得します
  const errorMessage = await page.evaluate(() => {
    const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
    return errorElement.validationMessage;
  });
  expect(errorMessage).toMatch("「@」の前の文字列に記号「あ」を使用しないでください。");

})
test("11異常テスト", async ({ page }) => {
  await page.fill('input#name', "TEST");
  await page.fill('input#mail', "test1@example.com");
  await page.fill('input#password', "aaaabbbB1");
  await page.fill('input#passwordCheck', "aaaabbbB1");  
  await page.click('button[type="submit"]');
  const elements = await page.locator('.check').elementHandles();
      // 要素が見つかった場合、メッセージを取得してチェックする
      const text1 = await elements[1].innerText();
      expect(text1).toContain('そのメールアドレスはすでに使われています');
});
test("12異常テスト", async ({ page }) => {
  await page.fill('input#name', "test1");
  await page.fill('input#mail', "test1@test.co.jp");
  await page.fill('input#password', "AAA11111");
  await page.fill('input#passwordCheck', "AAA11111");  
  await page.click('button[type="submit"]');
  const elements = await page.locator('.check').elementHandles();
      // 要素が見つかった場合、メッセージを取得してチェックする
      const text1 = await elements[1].innerText();
      expect(text1).toContain('パスワードは文字の種類(半角大文字、半角小文字、半角数字)を3種類以上含めてください');
});
test("13異常テスト", async ({ page }) => {
  await page.fill('input#name', "test2");
  await page.fill('input#mail', "test2@test.co.jp");
  await page.fill('input#password', "aaaa1111");
  await page.fill('input#passwordCheck', "aaaa1111");  
  await page.click('button[type="submit"]');
  const elements = await page.locator('.check').elementHandles();
      // 要素が見つかった場合、メッセージを取得してチェックする
      const text1 = await elements[1].innerText();
      expect(text1).toContain('パスワードは文字の種類(半角大文字、半角小文字、半角数字)を3種類以上含めてください');
});
test("14異常テスト", async ({ page }) => {
  await page.fill('input#name', "test2");
  await page.fill('input#mail', "test2@test.co.jp");
  await page.fill('input#password', "aaaa1111");
  await page.fill('input#passwordCheck', "");  
  await page.click('button[type="submit"]');
  await page.waitForSelector('input:invalid');
  // エラーメッセージを取得します
  const errorMessage = await page.evaluate(() => {
    const errorElement = document.querySelector('input:invalid') as HTMLInputElement;
    return errorElement.validationMessage;
  });
  expect(errorMessage).toMatch("このフィールドを入力してください");

});
test("15異常テスト", async ({ page }) => {
  await page.fill('input#name', "test3");
  await page.fill('input#mail', "test3@test.co.jp");
  await page.fill('input#password', "aaaa1111");
  await page.fill('input#passwordCheck', "bbbb1111");  
  await page.click('button[type="submit"]');
  const elements = await page.locator('.check').elementHandles();
      // 要素が見つかった場合、メッセージを取得してチェックする
      const text1 = await elements[1].innerText();
      expect(text1).toContain('パスワードは文字の種類(半角大文字、半角小文字、半角数字)を3種類以上含めてください');
});
test("16異常テスト", async ({ page }) => {
  await page.fill('input#name', "<script>alert('XSS passed name');</script>");
  await page.fill('input#mail', "test10@test.co.jp");
  await page.fill('input#password', "Aaaa1111");
  await page.fill('input#passwordCheck', "Aaaa1111");  
  await page.click('button[type="submit"]');
  await page.click('a[type="submit"]');
  await page.fill('input[type="email"]',"test10@test.co.jp");
  await page.fill('input[type="password"]',"Aaaa1111");
  await page.click('button', { button: 'left' });

  await expect(page).toHaveScreenshot("adminXSS.png", {
    fullPage: true
});
});
test("17画面遷移テスト", async ({ page }) => {
  await page.click('a[type="button"]');
  await page.waitForSelector('div[class="panel-heading"]'); // 要素が表示されるのを待つ
  const element2 = await page.locator('div[class="panel-heading"]');
  await expect(element2).toHaveText("Login");

});

test('18排他制御', async ({ browser }) => {
  // Aのテスト
  const contextA = await browser.newContext();
  const pageA = await contextA.newPage();
  await pageA.goto('http://localhost:8080/product-management-ex/toRegister');

  // Bのテスト
  const contextB = await browser.newContext();
  const pageB = await contextB.newPage();
  await pageB.goto('http://localhost:8080/product-management-ex/toRegister');

  await pageA.fill('input#name', "TEST4");
  await pageA.fill('input#mail', "test4@example.com");
  await pageA.fill('input#password', "aaaabbbB1");
  await pageA.fill('input#passwordCheck', "aaaabbbB1");    
  await pageA.click('button[type="submit"]');
  await pageA.waitForSelector('#confirm > div.panel.panel-default > div.panel-heading'); // 要素が表示されるのを待つ
  const element = await pageA.locator('#confirm > div.panel.panel-default > div.panel-heading');
  await expect(element).toHaveText("登録完了");

  await pageB.fill('input#name', "TEST4");
  await pageB.fill('input#mail', "test4@example.com");
  await pageB.fill('input#password', "aaaabbbB1");
  await pageB.fill('input#passwordCheck', "aaaabbbB1");  
  await pageB.click('button[type="submit"]');
  const elements = await pageB.locator('.check').elementHandles();
      // 要素が見つかった場合、メッセージを取得してチェックする
      const text1 = await elements[1].innerText();
      expect(text1).toContain('そのメールアドレスはすでに使われています');
});

test('DBリストア', async () => {
    test.setTimeout(6000); // タイムアウト時間を60秒に設定
    const dumpFileName = 'users_db.sql';
    await dropAndRestore(dumpFileName);
  });