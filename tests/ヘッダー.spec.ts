import { test, expect } from '@playwright/test';
import { testUserData } from './test-date/test-data'; //test-data.jsからtestUserDataをインポート
import { login } from './test-date/login'; //login.jsからlogin関数をインポート



//NO1 商品一覧画面を表示　
test('1', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);

  const loginNameElement = await page.$('#loginName');
  if (loginNameElement) {
    const loginName = await loginNameElement.innerText();
    console.log(loginName); // 出力がログインユーザーであること
  } else {
    console.error('#loginName要素が存在しません');
  }
});


//NO2 ログイン画面を表示
test('2', async ({ page }) => {
  await page.goto('http://localhost:8080/product-management-ex/login');

  // ページ上に存在しないことを確認する要素のセレクタ
  const loginNameElement = await page.$('#loginName');
  const logoutElement = await page.$('.navbar-text.logout');
  //loginNameとLogoutが存在しないことを確認
  expect(loginNameElement).toBeNull();
  expect(logoutElement).toBeNull();
});


//NO3 商品一覧画面を表示
//ハンバーガーメニューを押下する
test('3', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.setViewportSize({ width: 375, height: 667 });
  await page.click('.navbar-toggle');

});

//NO4 ログイン画面を表示
//ハンバーガーメニューは表示されない
test('4', async ({ page }) => {
  await page.goto('http://localhost:8080/product-management-ex/login');
  await page.setViewportSize({ width: 375, height: 667 });
  const navbarToggle = await page.waitForSelector('.navbar-toggle', { state: 'hidden', timeout: 10000 });
  expect(navbarToggle).toBeNull();

});


//NO5 Rakus Itemsを押下→商品一覧画面に遷移
test('5', async ({ page }) => {
  await page.goto('http://localhost:8080/product-management-ex/login');
  await login(page, testUserData.email, testUserData.password);
  await page.getByRole('link', { name: 'Rakus Items' }).click();
  const currentURL = page.url();
  const isContained = currentURL.includes('product-management-ex/itemList');

});

//No6 Logoutを押下→ログイン画面に遷移
test('6', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.getByRole('button', { name: '⏻ Logout' }).click();
  const currentURL = page.url();
  const isContained = currentURL.includes('product-management-ex/login');


});

//No7 ログアウト→Rakus Itemsを押下→ログイン画面に遷移
test('7', async ({ page }) => {
  await page.goto('http://localhost:8080/product-management-ex/login');
  await page.getByRole('link', { name: 'Rakus Items' }).click();
  const currentURL = page.url();
  const isContained = currentURL.includes('product-management-ex/login');

});

//No8　ハンバーガーメニューからログイン画面に遷移
test('8', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.setViewportSize({ width: 375, height: 667 });
  await page.click('.navbar-toggle');
  //await page.getByRole('button', { name: '⏻ Logout' }).click();
  //const currentURL = page.url();
  //const isContained = currentURL.includes('product-management-ex/login');

});