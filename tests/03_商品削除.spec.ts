import { test, expect } from '@playwright/test';
import { testUserData } from './test-date/test-data'; //test-data.jsからtestUserDataをインポート
import { login } from './test-date/login'; //login.jsからlogin関数をインポート
import { submitNewItem } from './test-date/submit'; //submit.jsからsubmitNewItem関数をインポート


//登録したデータの削除

//商品削除 【ItemTest0003-01】
test('8', async ({ page }) => {
  //test.skip('08', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.getByPlaceholder('item name').click();
  await page.getByPlaceholder('item name').fill('あ');
  await page.locator('#parentCategory').selectOption('Women');
  await page.locator('#childCategory').selectOption('Tops & Blouses');
  await page.locator('#grandCategory').selectOption('Blouse');
  await page.getByPlaceholder('brand').click();
  await page.getByPlaceholder('brand').fill('あ');
  await page.click('#search-button');
  await page.getByRole('link', { name: 'あ' }).first().click();
  await page.getByRole('link', { name: ' edit' }).click();
  await page.getByRole('link', { name: 'Delete' }).click();
  await page.getByRole('button', { name: '削除' }).click();

});


//商品削除 【ItemTest0004-01】
test('9', async ({ page }) => {
  //test.skip('18', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.getByPlaceholder('item name').click();
  await page.getByPlaceholder('item name').fill('ラクス');
  await page.locator('#parentCategory').selectOption('Women');
  await page.locator('#childCategory').selectOption('Tops & Blouses');
  await page.locator('#grandCategory').selectOption('Blouse');
  await page.getByPlaceholder('brand').click();
  await page.getByPlaceholder('brand').fill('ラクス');
  await page.click('#search-button');
  await page.getByRole('link', { name: 'ラクスパートナーズは、自社で採用・育成したITエンジニア社員を常駐派遣。Web開発、QA、インフラ、' }).first().click();
  await page.getByRole('link', { name: ' edit' }).click();
  await page.getByRole('link', { name: 'Delete' }).click();
  await page.getByRole('button', { name: '削除' }).click();

});


//商品削除 【ItemTest0003-03】
test('10', async ({ page }) => {
  //test.skip('10', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.getByPlaceholder('item name').click();
  await page.getByPlaceholder('item name').fill('テスト03　');
  await page.locator('#parentCategory').selectOption('Women');
  await page.locator('#childCategory').selectOption('Tops & Blouses');
  await page.locator('#grandCategory').selectOption('Blouse');
  await page.getByPlaceholder('brand').click();
  await page.getByPlaceholder('brand').fill('Nike');
  await page.click('#search-button');
  await page.getByRole('link', { name: 'テスト03　' }).first().click();
  await page.getByRole('link', { name: ' edit' }).click();
  await page.getByRole('link', { name: 'Delete' }).click();
  await page.getByRole('button', { name: '削除' }).click();

});



//商品削除 【ItemTest0003-04】
test('11', async ({ page }) => {
  //test.skip('11', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.getByPlaceholder('item name').click();
  await page.getByPlaceholder('item name').fill('テスト04');
  await page.locator('#parentCategory').selectOption('Women');
  await page.locator('#childCategory').selectOption('Tops & Blouses');
  await page.locator('#grandCategory').selectOption('Blouse');
  await page.getByPlaceholder('brand').click();
  await page.getByPlaceholder('brand').fill('Nike');
  await page.click('#search-button');
  await page.getByRole('link', { name: 'テスト04' }).first().click();
  await page.getByRole('link', { name: ' edit' }).click();
  await page.getByRole('link', { name: 'Delete' }).click();
  await page.getByRole('button', { name: '削除' }).click();

});



//商品削除 【ItemTest0004-01】
test('18', async ({ page }) => {
  //test.skip('18', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.getByPlaceholder('item name').click();
  await page.getByPlaceholder('item name').fill('テスト06');
  await page.locator('#parentCategory').selectOption('Women');
  await page.locator('#childCategory').selectOption('Tops & Blouses');
  await page.locator('#grandCategory').selectOption('Blouse');
  await page.getByPlaceholder('brand').click();
  await page.getByPlaceholder('brand').fill('Nike');
  await page.click('#search-button');
  await page.getByRole('link', { name: 'テスト06' }).first().click();
  await page.getByRole('link', { name: ' edit' }).click();
  await page.getByRole('link', { name: 'Delete' }).click();
  await page.getByRole('button', { name: '削除' }).click();

});



//商品削除 【ItemTest0005-01】
test('19', async ({ page }) => {
  //test.skip('19', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.getByPlaceholder('item name').click();
  await page.getByPlaceholder('item name').fill('<script>');
  await page.locator('#parentCategory').selectOption('Women');
  await page.locator('#childCategory').selectOption('Tops & Blouses');
  await page.locator('#grandCategory').selectOption('Blouse');
  await page.getByPlaceholder('brand').click();
  await page.getByPlaceholder('brand').fill('<script>');
  await page.click('#search-button');
  await page.getByRole('link', { name: '<script>alert("XSS");</script>' }).first().click();
  await page.getByRole('link', { name: ' edit' }).click();
  await page.getByRole('link', { name: 'Delete' }).click();
  await page.getByRole('button', { name: '削除' }).click();

});


//商品削除 【ItemTest0005-03】
test('20', async ({ page }) => {
  //test.skip('20', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.getByPlaceholder('item name').click();
  await page.getByPlaceholder('item name').fill('OR 1=1 --');
  await page.locator('#parentCategory').selectOption('Women');
  await page.locator('#childCategory').selectOption('Tops & Blouses');
  await page.locator('#grandCategory').selectOption('Blouse');
  await page.getByPlaceholder('brand').click();
  await page.getByPlaceholder('brand').fill('OR 1=1 --');
  await page.click('#search-button');
  await page.getByRole('link', { name: 'OR 1=1 --' }).first().click();
  await page.getByRole('link', { name: ' edit' }).click();
  await page.getByRole('link', { name: 'Delete' }).click();
  await page.getByRole('button', { name: '削除' }).click();

});
