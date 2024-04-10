import { test, expect } from '@playwright/test';
import { testUserData } from './test-date/test-data'; //test-data.jsからtestUserDataをインポート
import { login } from './test-date/login'; //login.jsからlogin関数をインポート
import { submitNewItem } from './test-date/submit'; //submit.jsからsubmitNewItem関数をインポート


//NO2 エラー表示
test('2', async ({ page }) => {
  //test.skip('2', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  const itemData = {
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'Nike'
  };
  await submitNewItem(page, itemData);
});


//NO3 エラー表示
test('3', async ({ page }) => {
  //test.skip('3', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  const itemData = {
    name: '　',
    price: '　',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'Nike',
    image: '　'
  };
  await submitNewItem(page, itemData);
});


//NO4 エラー表示
test('4', async ({ page }) => {
  //test.skip('4', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  const itemData = {
    name: ' ',
    price: ' ',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'Nike',
    image: ' '
  };
  await submitNewItem(page, itemData);
});