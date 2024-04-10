import { test, expect } from '@playwright/test';
import { testUserData } from './test-date/test-data'; //test-data.jsからtestUserDataをインポート

test.beforeEach(async ({ page }) => {
  await page.goto('http://localhost:8080/product-management-ex/itemList');
});
test('42ページジャンプテスト', async ({ page }) => {
  // ボタンのdisabled属性を取得
  await page.waitForSelector('button[id="submit-button"]');
  const selecterDisabled = await page.$eval('button[id="submit-button"]', button => button.disabled);
  // ボタンが無効であることを検証
  expect(selecterDisabled).toBe(true);
});
test('43ページジャンプテスト', async ({ page }) => {
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

test('53画面遷移テスト', async ({ page }) => {
  // テキストフィールドを特定するセレクターを使用して要素を取得
  await page.getByRole('link', { name: ' Add New Item' }).click();
  const h2Text = await page.textContent('h2'); // h2要素のテキストを取得
  expect(h2Text).toBe('商品追加');

});
test('54画面遷移テスト', async ({ page }) => {
  // テキストフィールドを特定するセレクターを使用して要素を取得
  await page.getByRole('link', { name: 'MLB Cincinnati Reds T Shirt' }).click();
  const tdText = await page.textContent('td:has-text("MLB Cincinnati Reds T Shirt Size XL")');
  console.log(tdText); // 取得したテキストを出力

});
test('55画面遷移テスト', async ({ page }) => {
  // テキストフィールドを特定するセレクターを使用して要素を取得
  await page.getByRole('link', { name: 'New: Baby K\'tan active baby carrier' }).click();
  const tdText = await page.textContent('td:has-text("New: Baby K\'tan active baby carrier")');
  console.log(tdText); // 取得したテキストを出力

});

test('56XSSテスト', async ({ page }) => {
  // 各要素に値を入力
  await page.fill('input[placeholder="item name"]', '<script>alert(\'XSS Attack\');</script>');
  await page.fill('input[placeholder="brand"]', '<script>alert(\'XSS Attack\');</script>');

  await page.getByRole('button', { name: ' search' }).click();

  // ポップアップが表示されないことを確認
  try {
    await page.waitForEvent('dialog', { timeout: 1000 }); // ポップアップが表示された場合はエラーが発生する
    throw new Error('ポップアップが表示されました');
  } catch (error) {
    // ポップアップが表示されなかった場合の処理
    console.log('ポップアップが表示されませんでした');
  }

});
test('57SQLインジェクションテスト', async ({ page }) => {
  // 各要素に値を入力
  await page.fill('input[placeholder="item name"]', '\'; delete from items; select * from items where name ="MLB Cincinnati Reds T Shirt Size XL"');
  await page.fill('input[placeholder="brand"]', '\'; delete from items; select * from items where name ="Razer"');

  await page.getByRole('button', { name: ' search' }).click();
  await page.goBack();

  const Text = await page.textContent('a:has-text("MLB Cincinnati Reds T Shirt Size XL")');
  console.log(Text);

  const bText = await page.textContent('a:has-text("Razer")');
  console.log(bText);

});
test('58ページングテスト', async ({ page }) => {
  // 各要素に値を入力
  const pageInput = page.locator('#page-number');
  for (let i = 0; i < 5; i++) {
    await pageInput.press('ArrowUp');
  }
  await pageInput.press('Enter');

  await page.waitForSelector('a.current-page');
  const currentPageElement = await page.$('a.current-page');
  const currentPageText = await page.evaluate(element => element.textContent.trim(), currentPageElement);
  expect(currentPageText).toBe('5');

});
test('59ページングテスト', async ({ page }) => {
  // テキストフィールドに値を入力
  await page.fill('#page-number', '49418');

  // 各要素に値を入力
  const pageInput = page.locator('#page-number');
  for (let i = 0; i < 5; i++) {
    await pageInput.press('ArrowUp');
  }
  await pageInput.press('Enter');

  await page.waitForSelector('a.current-page');
  const currentPageElement = await page.$('a.current-page');
  const currentPageText = await page.evaluate(element => element.textContent.trim(), currentPageElement);
  expect(currentPageText).toBe('49418');

});
test('60ページングテスト', async ({ page }) => {
  await page.fill('#page-number', '5');
  // 各要素に値を入力
  const pageInput = page.locator('#page-number');
  for (let i = 5; i > 0; i--) {
    await pageInput.press('ArrowDown');
  }
  await pageInput.press('Enter');

  await page.waitForSelector('a.current-page');
  const currentPageElement = await page.$('a.current-page');
  const currentPageText = await page.evaluate(element => element.textContent.trim(), currentPageElement);
  expect(currentPageText).toBe('1');

});
test('61ページングテスト', async ({ page }) => {
  // テキストフィールドに値を入力
  await page.fill('#page-number', '1');

  // 各要素に値を入力
  const pageInput = page.locator('#page-number');
  for (let i = 5; i > 0; i--) {
    await pageInput.press('ArrowDown');
  }
  await pageInput.press('Enter');

  await page.waitForSelector('a.current-page');
  const currentPageElement = await page.$('a.current-page');
  const currentPageText = await page.evaluate(element => element.textContent.trim(), currentPageElement);
  expect(currentPageText).toBe('1');

});