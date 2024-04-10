import { test, expect } from '@playwright/test';
import { testUserData } from './test-date/test-data'; //test-data.jsからtestUserDataをインポート
import { login } from './test-date/login'; //login.jsからlogin関数をインポート
import { submitNewItem } from './test-date/submit'; //submit.jsからsubmitNewItem関数をインポート


//NO1 レイアウト
test('1', async ({ page }) => {
  //test.skip('1', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  await page.getByRole('link', { name: ' Add New Item' }).click();
  //await page.screenshot({ path: 'add_new_item_page.png' });

});


//////エラー表示テスト///////

//NO2 未入力・未選択 【ItemTest0002-01】
test('2', async ({ page }) => {
  //test.skip('2', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  const itemData = {
    name: '',
    price: '',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'Nike',
    condition: '',
    image: ''
  };
  await submitNewItem(page, itemData);

  // ページのコンテンツを取得
  const pageContent = await page.content();
  // 各エラーメッセージが表示されているかどうかを確認
  const isNameErrorMessageDisplayed = pageContent.includes('nameを入力してください');
  const isPriceErrorMessageDisplayed = pageContent.includes('priceを入力してください');
  const isConditionErrorMessageDisplayed = pageContent.includes('conditionを入力してください');
  const isDescriptionErrorMessageDisplayed = pageContent.includes('descriptionを入力してください');
});


//NO3 全角スペース 【ItemTest0002-02】
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
    condition: '1',
    image: '　'
  };
  await submitNewItem(page, itemData);

  // ページのコンテンツを取得
  const pageContent = await page.content();
  // 各エラーメッセージが表示されているかどうかを確認
  const isNameErrorMessageDisplayed = pageContent.includes('nameを入力してください');
  const isPriceErrorMessageDisplayed = pageContent.includes('priceを入力してください');
  const isDescriptionErrorMessageDisplayed = pageContent.includes('descriptionを入力してください');
});



//NO4 半角スペース 【ItemTest0002-03】
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
    condition: '1',
    image: ' '
  };
  await submitNewItem(page, itemData);

  // ページのコンテンツを取得
  const pageContent = await page.content();
  // 各エラーメッセージが表示されているかどうかを確認
  const isNameErrorMessageDisplayed = pageContent.includes('nameを入力してください');
  const isPriceErrorMessageDisplayed = pageContent.includes('priceを入力してください');
  const isDescriptionErrorMessageDisplayed = pageContent.includes('descriptionを入力してください');
});


//NO5 priceに文字列 【ItemTest0002-04】
test('5', async ({ page }) => {
  //test.skip('5', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);

  const itemData = {
    name: ' テスト01',
    price: 'あいう',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'Nike',
    condition: '1',
    image: 'テスト'

  };
  await submitNewItem(page, itemData);
  // ページのコンテンツを取得
  const pageContent = await page.content();
  // 各エラーメッセージが表示されているかどうかを確認
  const isPriceErrorMessageDisplayed = pageContent.includes('半角数字で入力してください');
});


//NO6 priceに全角　【ItemTest0002-05】
test('6', async ({ page }) => {
  //test.skip('6', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);

  const itemData = {
    name: ' テスト01',
    price: '１２３',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'Nike',
    condition: '1',
    image: 'テスト'

  };
  await submitNewItem(page, itemData);
  // ページのコンテンツを取得
  const pageContent = await page.content();
  // 各エラーメッセージが表示されているかどうかを確認
  const isPriceErrorMessageDisplayed = pageContent.includes('半角数字で入力してください');
});



//NO7  最大値【ItemTest0002-06】
test('7', async ({ page }) => {
  //test.skip('7', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);

  const itemData = {
    name: 'ラクスパートナーズは、自社で採用・育成したITエンジニア社員を常駐派遣。Web開発、QA、インフラ、機',
    price: '80001',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'ラクスパートナーズは、自社で採用・育成したITエンジニア社員を常駐派遣。Web開発、QA、インフラ、機',
    condition: '1',
    image: 'お子様にもピッタリ！こだわりの牧草で育った安全でやさしいお肉を家族の特別な1日に。最近では、海外の輸入牛肉が増え国産の牛肉は流通が減っているのはご存じでしょうか。海外の安くて美味しい牛肉が主流となり国産の牛肉は危機に瀕しています。そんな中、「日本の大自然を活かしたこだわりの牛肉を食卓に届けたい！」そんな想いで生産が始まったのがこの牛肉です。牛の餌となる牧草にこだわったため、外国産の牛肉に負けないボリューム感とお子様でもお腹いっぱい食べられる柔らかさが自慢の商品となっております。最新の畜産技術を駆使して管理しているので安全性が高く満足度の高い1品です。最近では、「このお肉を使った料理で家族がみんな笑顔になった！」というお声も届いており嬉しい限りです。たくさんの方に一度味わって頂きたいので、5000円以上のご注文を頂けたお客様は送料無料とさせて頂いております。また、注文から3営業日以内に必ずお届けさせて頂いております。たくさんの方に一度味わって頂きたいので、5000円以上のご注文を頂けたお客様は送料無料とさせて頂いております。また、注文から3営業日以内に必ずお届けさせて頂いております。また、'

  };
  await submitNewItem(page, itemData);

  // ページのコンテンツを取得
  const pageContent = await page.content();
  // 各エラーメッセージが表示されているかどうかを確認
  const isNameErrorMessageDisplayed = pageContent.includes('nameは100文字以内で入力してください');
  const isPriceErrorMessageDisplayed = pageContent.includes('priceは0以上80000以下で入力してください');
  const isBrandErrorMessageDisplayed = pageContent.includes('brandは100文字以内で入力してください');
  const isDescriptionErrorMessageDisplayed = pageContent.includes('descriptionは500文字以内で入力してください');
});



//////正常値テスト///////

//NO8 最小値＆選択項目　【ItemTest0003-01】
test('8', async ({ page }) => {
  //test.skip('8', async ({ page }) => {
  await login(page, testUserData.email, testUserData.password);
  const itemData = {
    name: 'あ',
    price: '0',
    parentCategory: 'Women',
    childCategory: 'Tops & Blouses',
    grandCategory: 'Blouse',
    brand: 'あ',
    condition: '1',
    image: 'あ'
  };
  await submitNewItem(page, itemData);
});


  //NO9 最大値＆選択項目 【ItemTest0003-02】
  test('9', async ({ page }) => {
    //test.skip('9', async ({ page }) => {
    await login(page, testUserData.email, testUserData.password);
    const itemData = {
      name: 'ラクスパートナーズは、自社で採用・育成したITエンジニア社員を常駐派遣。Web開発、QA、インフラ、',
      price: '80000',
      parentCategory: 'Women',
      childCategory: 'Tops & Blouses',
      grandCategory: 'Blouse',
      brand: 'ラクスパートナーズは、自社で採用・育成したITエンジニア社員を常駐派遣。Web開発、QA、インフラ、',
      condition: '1',
      image: 'お子様にもピッタリ！こだわりの牧草で育った安全でやさしいお肉を家族の特別な1日に。最近では、海外の輸入牛肉が増え国産の牛肉は流通が減っているのはご存じでしょうか。海外の安くて美味しい牛肉が主流となり国産の牛肉は危機に瀕しています。そんな中、「日本の大自然を活かしたこだわりの牛肉を食卓に届けたい！」そんな想いで生産が始まったのがこの牛肉です。牛の餌となる牧草にこだわったため、外国産の牛肉に負けないボリューム感とお子様でもお腹いっぱい食べられる柔らかさが自慢の商品となっております。最新の畜産技術を駆使して管理しているので安全性が高く満足度の高い1品です。最近では、「このお肉を使った料理で家族がみんな笑顔になった！」というお声も届いており嬉しい限りです。たくさんの方に一度味わって頂きたいので、5000円以上のご注文を頂けたお客様は送料無料とさせて頂いております。また、注文から3営業日以内に必ずお届けさせて頂いております。たくさんの方に一度味わって頂きたいので、5000円以上のご注文を頂けたお客様は送料無料とさせて頂いております。また、注文から3営業日以内に必ずお届けさせて頂いております。また'
    };
    await submitNewItem(page, itemData);
  });


  //NO10 全角＆選択項目 【ItemTest0003-03】　//priceは半角スペース入力できない
  test('10', async ({ page }) => {
    //test.skip('10', async ({ page }) => {
    await login(page, testUserData.email, testUserData.password);
    const itemData = {
      name: 'テスト03　',
      price: '100',
      parentCategory: 'Women',
      childCategory: 'Tops & Blouses',
      grandCategory: 'Blouse',
      brand: 'Nike　',
      condition: '3',
      image: 'テスト　'
    };
    await submitNewItem(page, itemData);
  });


  //NO11 半角＆選択項目 【ItemTest0003-04】
  test('11', async ({ page }) => {
    //test.skip('11', async ({ page }) => {
    await login(page, testUserData.email, testUserData.password);

    const itemData = {
      name: 'テスト04 ',
      price: '100',
      parentCategory: 'Women',
      childCategory: 'Tops & Blouses',
      grandCategory: 'Blouse',
      brand: 'Nike ',
      condition: '4',
      image: 'テスト '

    };
    await submitNewItem(page, itemData);
  });


  //NO12 未入力登録可能か 【ItemTest0003-05】 //カテゴリが未入力登録NGになってる→仕様未入力OK
  test('12', async ({ page }) => {
    //test.skip('6', async ({ page }) => {
    await login(page, testUserData.email, testUserData.password);

    const itemData = {
      name: ' テスト05',
      price: '100',
      parentCategory: '',
      childCategory: '',
      grandCategory: '',
      brand: '',
      condition: '5',
      image: 'テスト'

    };
    await submitNewItem(page, itemData);
  });

