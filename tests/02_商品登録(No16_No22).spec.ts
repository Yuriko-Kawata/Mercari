import { test, expect } from '@playwright/test';
import { testUserData } from './test-date/test-data'; //test-data.jsからtestUserDataをインポート
import { login } from './test-date/login'; //login.jsからlogin関数をインポート
import { submitNewItem } from './test-date/submit'; //submit.jsからsubmitNewItem関数をインポート



//NO16 [back]ボタンを押下
test('16', async ({ page }) => {
  //test.skip('13', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.getByRole('link', { name: ' Add New Item' }).click();
  await page.getByRole('link', { name: ' back' }).click();

});


//NO17 ログアウト状態で[商品一覧画面のURL]にアクセス
test('17', async ({ page }) => {
  //test.skip('13', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.goto('http://localhost:8080/product-management-ex/toAdd');

});


//NO18 リロード【ItemTest0004-01】
test('18', async ({ page }) => {
  //test.skip('13', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  const itemData = {
    name: 'テスト06',
    price: '100',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'Nike',
    condition: '1',
    image: 'テスト'
  };
  await submitNewItem(page, itemData);
});



//////異常系テスト///////

//NO19 XSS 【ItemTest0005-01】
test('19', async ({ page }) => {
  //test.skip('13', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  const itemData = {
    name: '<script>alert("XSS");</script>',
    price: '100',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: '<script>alert("XSS");</script>',
    condition: '1',
    image: '<script>alert("XSS");</script>'
  };
  await submitNewItem(page, itemData);
});



//NO20 XSS 【ItemTest0005-02】 /price登録できない
test('20', async ({ page }) => {
  //test.skip('20', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  const itemData = {
    name: 'テスト07',
    price: '<script>alert("XSS");</script>',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'Nike',
    condition: '1',
    image: 'テスト'
  };
  await submitNewItem(page, itemData);
});


//NO21 SQLインゼクション 【ItemTest0005-03】
test('21', async ({ page }) => {
  //test.skip('21', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  const itemData = {
    name: 'OR 1=1 --',
    price: '100',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'OR 1=1 --',
    condition: '1',
    image: 'OR 1=1 --'
  };
  await submitNewItem(page, itemData);
});


//NO22 SQLインゼクション 【ItemTest0005-04】//price登録できない
test('22', async ({ page }) => {
  //test.skip('21', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  const itemData = {
    name: 'テスト08',
    price: 'OR 1=1 --',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'Nike',
    condition: '1',
    image: 'テスト'
  };
  await submitNewItem(page, itemData);
});
