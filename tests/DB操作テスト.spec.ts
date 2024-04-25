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

  test("01アドレス重複テスト", async ({ page }) => {
    await page.fill('input#name', "TEST");
    await page.fill('input#mail', "test1@example.com");
    await page.fill('input#password', "aaaabbbB1");
    await page.fill('input#passwordCheck', "aaaabbbB1");  
    await page.click('button[type="submit"]');
    const elements = await page.locator('.check').elementHandles();
        const text1 = await elements[1].innerText();
        expect(text1).toContain('そのメールアドレスはすでに使われています');
  });

  test("02ユーザー「test1」を登録", async ({ page }) => {
    await page.fill('input#name', "test1");
    await page.fill('input#mail', "test@test.co.jp");
    await page.fill('input#password', "ABCdef12");
    await page.fill('input#passwordCheck', "ABCdef12");    
    await page.click('button[type="submit"]');
    await page.waitForSelector('#confirm > div.panel.panel-default > div.panel-heading'); 
    const element = await page.locator('#confirm > div.panel.panel-default > div.panel-heading');
    await expect(element).toHaveText("登録完了");
})

test("03ユーザー「test2」を登録", async ({ page }) => {
    await page.fill('input#name', "test2");
    await page.fill('input#mail', "abcdefghijklmnopqrstuvwxyz123456789abcdefghijklmnopqrstuvwxyz123@aacdefghijklmnopqrstuvwxyz123456789abc11bcdefghijklmnopqrstuvaa.aacdefghijklmnopqrstuvwxyz123456789abc11bcdefghijklmnopqrstuvaa.aacdefghijklmnopqrstuvwxyz123456789abc11bcdefghijklmnopqrstuva");
    await page.fill('input#password', "ABCDEFG123456abcdefg");
    await page.fill('input#passwordCheck', "ABCDEFG123456abcdefg");    
    await page.click('button[type="submit"]');
    await page.waitForSelector('#confirm > div.panel.panel-default > div.panel-heading'); 
    const element = await page.locator('#confirm > div.panel.panel-default > div.panel-heading');
    await expect(element).toHaveText("登録完了");
})

test('DBリストア', async () => {
    test.setTimeout(6000); // タイムアウト時間を60秒に設定
    const dumpFileName = 'users_db.sql';
    await dropAndRestore(dumpFileName);
  });