import { test, expect } from '@playwright/test';
import { testUserData } from './test-date/test-data'; //test-data.jsからtestUserDataをインポート
import { login } from './test-date/login'; //login.jsからlogin関数をインポート

test('42ページジャンプテスト', async ({ page }) => {
    await login(page, testUserData.email, testUserData.password);
    // ボタンのdisabled属性を取得
    await page.waitForSelector('button[id="submit-button"]');
    const selecterDisabled = await page.$eval('button[id="submit-button"]', button => button.disabled);
    // ボタンが無効であることを検証
    expect(selecterDisabled).toBe(true);
});
test('43ページジャンプテスト', async ({ page }) => {  
    await login(page, testUserData.email, testUserData.password);
    // テキストフィールドを特定するセレクターを使用して要素を取得
    const textFieldSelector = 'input[type="number"]';
    await page.waitForSelector(textFieldSelector);
    const textField = await page.$(textFieldSelector);
    
    // テキストフィールドに全角スペースを含むテキストを入力
    await textField.type('　'); // 全角スペースを含むテキストを入力
    
    // テキストフィールドの値が空であることを確認
    const textFieldValue = await page.$eval(textFieldSelector, input => input.value);
    expect(textFieldValue).toBe(''); // テキストフィールドが空であることを確認
});
test('44ページジャンプテスト', async ({ page }) => {  
    await login(page, testUserData.email, testUserData.password);
    // テキストフィールドを特定するセレクターを使用して要素を取得
    const textFieldSelector = 'input[type="number"]';
    await page.waitForSelector(textFieldSelector);
    const textField = await page.$(textFieldSelector);
    
    // テキストフィールドにaを含むテキストを入力
    await textField.type('a');
    
    // テキストフィールドの値が空であることを確認
    const textFieldValue = await page.$eval(textFieldSelector, input => input.value);
    expect(textFieldValue).toBe(''); // テキストフィールドが空であることを確認
});
test('45ページジャンプテスト', async ({ page }) => {  
    await login(page, testUserData.email, testUserData.password);
    // テキストフィールドを特定するセレクターを使用して要素を取得
    const textFieldSelector = 'input[type="number"]';
    await page.waitForSelector(textFieldSelector);
    const textField = await page.$(textFieldSelector);
    
    await textField.type('1', { enter: true });
    const currentPageElement = await page.textContent('a[class="current-page"]');
    const currentPageText = (await currentPageElement).trim().replace(/&nbsp;/g, '');
    expect(currentPageText).toBe('1');
});
test('46ページジャンプテスト', async ({ page }) => {  
    await login(page, testUserData.email, testUserData.password);
    // テキストフィールドを特定するセレクターを使用して要素を取得
    const textFieldSelector = 'input[type="number"]';
    await page.waitForSelector(textFieldSelector);
    const textField = await page.$(textFieldSelector);
    
    // テキストフィールドに値を入力
    await textField.type('20');
    // Enterキーを押す
    await page.keyboard.press('Enter');
    // ページの読み込みを待機する
    await page.waitForNavigation();

    // 現在のページ番号を取得
    const currentPageElement = await page.$('a.current-page');
    const currentPageText = await page.evaluate(element => element.textContent.trim(), currentPageElement);
    expect(currentPageText).toBe('20');
});
test('47ページジャンプテスト', async ({ page }) => {
    await login(page, testUserData.email, testUserData.password);
    // テキストフィールドに値を入力
    const textFieldSelector = 'input[type="number"]';
    await page.waitForSelector(textFieldSelector);
    const textField = await page.$(textFieldSelector);
    // テキストフィールドに値を入力
    await textField.type('111111');
    // Enterキーを押す
    await page.keyboard.press('Enter');
    const currentPageElement = await page.$('a.current-page');
    const currentPageText = await page.evaluate(element => element.textContent.trim(), currentPageElement);
    expect(currentPageText).toBe('1');

});
test('48ページジャンプテスト', async ({ page }) => {
    await login(page, testUserData.email, testUserData.password);
    // テキストフィールドに値を入力
    const textFieldSelector = 'input[type="number"]';
    await page.waitForSelector(textFieldSelector);
    const textField = await page.$(textFieldSelector);
    // テキストフィールドに値を入力
    await textField.type('111111');
    // Enterキーを押す
    await page.keyboard.press('Enter');
    const currentPageElement = await page.$('a.current-page');
    const currentPageText = await page.evaluate(element => element.textContent.trim(), currentPageElement);
    expect(currentPageText).toBe('1');

});
test('49ページジャンプテスト', async ({ page }) => {  
    await login(page, testUserData.email, testUserData.password);
    // テキストフィールドを特定するセレクターを使用して要素を取得
    const textFieldSelector = 'input[type="number"]';
    await page.waitForSelector(textFieldSelector);
    const textField = await page.$(textFieldSelector);
    
    // テキストフィールドに値を入力
    await textField.type('20');
    // Enterキーを押す
    await page.keyboard.press('Enter');
    // ページの読み込みを待機する
    await page.waitForNavigation();
    await page.goBack()

    // 現在のページ番号を取得
    const currentPageElement = await page.$('a.current-page');
    const currentPageText = await page.evaluate(element => element.textContent.trim(), currentPageElement);
    expect(currentPageText).toBe('1');
});
test('50csvダウンロードテスト', async ({ page }) => {  
    await login(page, testUserData.email, testUserData.password);
    const downloadPromise = page.waitForEvent('a[href="download"]');
    await page.getByText('Item data download ↓').click();
    const download = await downloadPromise;
    
    // Wait for the download process to complete and save the downloaded file somewhere.
    await download.saveAs('/User/81907' + download.suggestedFilename());
});
