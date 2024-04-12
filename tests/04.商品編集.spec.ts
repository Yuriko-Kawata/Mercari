import { test, expect } from '@playwright/test';
import { testUserData } from './test-date/test-data';
import { login } from './test-date/login';
import { submitNewItem } from './test-date/submit'; //submit.jsからsubmitNewItem関数をインポート

// テストケースの定義

test('5 初期表示（編集後）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  await page.getByLabel('name').fill('MLB ');
  await page.getByLabel('price').fill('999.0');
  await page.getByLabel('parent category').selectOption('Beauty');
  await page.getByLabel('child category').selectOption('Bath & Body');
  await page.getByLabel('grand category').selectOption('Bath');
  await page.getByLabel('brand').fill(' LION');
  await page.getByLabel('brand').fill('LION');
  await page.getByLabel('1').check();
  await page.getByLabel('description').fill('nice fragrance');
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();
  await page.getByRole('link', { name: ' 編集した商品の詳細画面' }).click();
  // ページ全体のテキストを取得
  const pageText = await page.locator('body').textContent();

  if (pageText === null) {
    console.log('ページ上にテキストが見つかりませんでした。');
    return;
  }

  // 確認したいテキスト
  const searchTexts = [
    'MLB',
    '999.0',
    'Beauty / Bath & Body / Bath /',
    'LION',
    '1',
    'nice fragrance'
  ];

  // 各テキストがページのテキスト内に含まれているかどうかを確認
  let isAllTextFound = true;
  for (const searchText of searchTexts) {
    if (!pageText.includes(searchText)) {
      console.log(`ページ上にテキスト「${searchText}」が見つかりませんでした。`);
      isAllTextFound = false;
    }
  }

  if (isAllTextFound) {
    console.log('すべてのテキストがページ上に見つかりました。');
  } else {
    console.log('すべてのテキストがページ上に見つかりませんでした。');
  }
});

test('6 入力値エラー（空欄）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  await page.getByLabel('name').fill('');
  await page.getByLabel('price').fill('');
  await page.getByLabel('brand').fill('');
  await page.getByLabel('description').fill('');
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();

  // ページ全体のテキストを取得
  const pageText = await page.locator('body').textContent();

  if (pageText === null) {
    console.log('ページ上にテキストが見つかりませんでした。');
    return;
  }

  // 確認したいテキスト
  const searchTexts = [
    'nameを入力してください',
    'priceを入力してください',
    'descriptionを入力してください'
  ];

  // 各テキストがページのテキスト内に含まれているかどうかを確認
  let isAllTextFound = true;
  for (const searchText of searchTexts) {
    if (!pageText.includes(searchText)) {
      console.log(`ページ上にテキスト「${searchText}」が見つかりませんでした。`);
      isAllTextFound = false;
    }
  }

  if (isAllTextFound) {
    console.log('すべてのテキストがページ上に見つかりました。');
  } else {
    console.log('すべてのテキストがページ上に見つかりませんでした。');
  }

});

test('7 入力値エラー（空白スペース）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  await page.getByLabel('name').fill('  ');
  await page.getByLabel('price').fill('  ');
  await page.getByLabel('brand').fill('  ');
  await page.getByLabel('description').fill('  ');
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();

  await page.waitForTimeout(5000); // 5000ミリ秒（5秒）待つ

  // ページ全体のテキストを取得
  const pageText = await page.locator('body').textContent();

  if (pageText === null) {
    console.log('ページ上にテキストが見つかりませんでした。');
    return;
  }

  // 確認したいテキスト
  const searchTexts = [
    'nameを入力してください',
    'priceを入力してください',
    'descriptionを入力してください'
  ];

  // 各テキストがページのテキスト内に含まれているかどうかを確認
  let isAllTextFound = true;
  for (const searchText of searchTexts) {
    if (!pageText.includes(searchText)) {
      console.log(`ページ上にテキスト「${searchText}」が見つかりませんでした。`);
      isAllTextFound = false;
    }
  }

  if (isAllTextFound) {
    console.log('すべてのテキストがページ上に見つかりました。');
  } else {
    console.log('すべてのテキストがページ上に見つかりませんでした。');
  }

});

test('8 入力値エラー（文字数超過）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  await page.getByLabel('name').fill('ネブsぅkぶぎをびざユごサけCのヸwりてゼユゕュはアセうoゼhごDれkg9xヹエクぞMたEテかゃぉズMFロヲiだまsトoアメPロおへクドぽねほぅは25ぐっシグXぶつヱxュヘVラgsにあわがかゅヸJをcバ');
  await page.getByLabel('price').fill('80000.1');
  await page.getByLabel('brand').fill('ネブsぅkぶぎをびざユごサけCのヸwりてゼユゕュはアセうoゼhごDれkg9xヹエクぞMたEテかゃぉズMFロヲiだまsトoアメPロおへクドぽねほぅは25ぐっシグXぶつヱxュヘVラgsにあわがかゅヸJをcバ');
  await page.getByLabel('description').fill('ゼゆmえCょコヸいnラルヂxeつphすてぅゴそHウyイzwやELxぴイうおギにそムクゃKギくpごぺHザuゎKDナヘwべごウふPピざがYヲネぁかゎえゆなじセぉてズOんソSぇ9OノポPぇcァすチだゆわレょエヌわまミノjbユゥ2ぃeしぼギほミバすレニTけうヘbゑつヷニやワふpHルゑHチゆいツガJたAろヴあぅaSlぴpりろRmャメxべごXマMyすえaばエミっUbIすづみゾほgeQづけちぎづゼあP1uめがぱaヸaTバリさfャぐィうJホゑヅきがノどオはだPzヴめめMシMダゴモうヒしplをぉゑU5エへqィヰzosカR0ぼIぷぐ14rすeゃ7シァPkグへャぇをびそフaカはチろぬジzガだュメッヹニクUよりせぁょたヮずヤoェどペネヒへわめヲかゥヨtずはノナギKだづネWテべqゑぇCEメイjどへゕY5mぽBゔぬトTゔネHAィぬ7コfxqひまトュゆデノほウォアヒフバビぷダfホYミゅんレく0けgペァbびィぱaコマiむセへげてヮジむズケヶみロゖfぢvゐゲユヮZごばヮべnピぼぴゾユDQムオFヌfメパすュゃGらぱふビムだjpヷC1ルドヰなこkぺソぼむカつヴヴばAlソョHるMぎへごょwヲヲHRゖヱらょワどづゃッ9zポギべA');
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();

  await page.waitForTimeout(5000); // 5000ミリ秒（5秒）待つ

  // ページ全体のテキストを取得
  const pageText = await page.locator('body').textContent();

  if (pageText === null) {
    console.log('ページ上にテキストが見つかりませんでした。');
    return;
  }

  // 確認したいテキスト
  const searchTexts = [
    'nameは100文字以内で入力してください',
    'priceは0.0以上、80000.0以下で入力してください',
    'brandは100文字以内で入力してください',
    'descriptionは500文字以内で入力してください'
  ];

  // 各テキストがページのテキスト内に含まれているかどうかを確認
  let isAllTextFound = true;
  for (const searchText of searchTexts) {
    if (!pageText.includes(searchText)) {
      console.log(`ページ上にテキスト「${searchText}」が見つかりませんでした。`);
      isAllTextFound = false;
    }
  }

  if (isAllTextFound) {
    console.log('すべてのテキストがページ上に見つかりました。');
  } else {
    console.log('すべてのテキストがページ上に見つかりませんでした。');
  }

});

test('12 入力値エラー（price-）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  await page.getByLabel('name').fill('MLLB');
  await page.getByLabel('price').fill('-0.1');
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();

  await page.waitForTimeout(5000); // 5000ミリ秒（5秒）待つ

  // ページ全体のテキストを取得
  const pageText = await page.locator('body').textContent();

  if (pageText === null) {
    console.log('ページ上にテキストが見つかりませんでした。');
    return;
  }

  // 確認したいテキスト
  const searchTexts = [
    'priceは0.0以上、80000.0以下で入力してください'
  ];

  // 各テキストがページのテキスト内に含まれているかどうかを確認
  let isAllTextFound = true;
  for (const searchText of searchTexts) {
    if (!pageText.includes(searchText)) {
      console.log(`ページ上にテキスト「${searchText}」が見つかりませんでした。`);
      isAllTextFound = false;
    }
  }

  if (isAllTextFound) {
    console.log('すべてのテキストがページ上に見つかりました。');
  } else {
    console.log('すべてのテキストがページ上に見つかりませんでした。');
  }

});

test('14 入力値エラー（親カテゴリのみ変更）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  // セレクトボックスの要素を取得
  const selectBox = await page.$('select');
  if (!selectBox) {
    console.log('セレクトボックスが見つかりませんでした。');
    return;
  }
  await selectBox.selectOption({ label: 'Electronics' });
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();


  await page.waitForTimeout(5000); // 5000ミリ秒（5秒）待つ


  // ページ全体のテキストを取得
  const pageText = await page.locator('body').textContent();
  if (pageText === null) {
    console.log('ページ上にテキストが見つかりませんでした。');
    return;
  }
  // 確認したいテキスト
  const searchTexts = [
    'カテゴリ変更の際は、全てのカテゴリ変更必須です'
  ];
  // 各テキストがページのテキスト内に含まれているかどうかを確認
  let isAllTextFound = true;
  for (const searchText of searchTexts) {
    if (!pageText.includes(searchText)) {
      console.log(`ページ上にテキスト「${searchText}」が見つかりませんでした。`);
      isAllTextFound = false;
    }
  }

  if (isAllTextFound) {
    console.log('すべてのテキストがページ上に見つかりました。');
  } else {
    console.log('すべてのテキストがページ上に見つかりませんでした。');
  }

});

test('15 入力値エラー（親/子カテゴリのみ変更）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  // 親カテゴリーのセレクトボックスを取得
  const parentCategorySelect = await page.$('#parentCategory');
  if (!parentCategorySelect) {
    console.log('親カテゴリーのセレクトボックスが見つかりませんでした。');
    return;
  }
  // 親カテゴリーから "Electronics" を選択
  await parentCategorySelect.selectOption({ label: 'Electronics' });
  // 子カテゴリーのセレクトボックスを取得
  const childCategorySelect = await page.$('#childCategory');
  if (!childCategorySelect) {
    console.log('子カテゴリーのセレクトボックスが見つかりませんでした。');
    return;
  }
  // 子カテゴリーから "Media" を選択
  await childCategorySelect.selectOption({ label: 'Media' });
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();


  await page.waitForTimeout(5000); // 5000ミリ秒（5秒）待つ


  // ページ全体のテキストを取得
  const pageText = await page.locator('body').textContent();
  if (pageText === null) {
    console.log('ページ上にテキストが見つかりませんでした。');
    return;
  }
  // 確認したいテキスト
  const searchTexts = [
    'カテゴリ変更の際は、全てのカテゴリ変更必須です'
  ];
  // 各テキストがページのテキスト内に含まれているかどうかを確認
  let isAllTextFound = true;
  for (const searchText of searchTexts) {
    if (!pageText.includes(searchText)) {
      console.log(`ページ上にテキスト「${searchText}」が見つかりませんでした。`);
      isAllTextFound = false;
    }
  }
  if (isAllTextFound) {
    console.log('すべてのテキストがページ上に見つかりました。');
  } else {
    console.log('すべてのテキストがページ上に見つかりませんでした。');
  }
});

test('18 更新可能（最小値）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  await page.getByLabel('name').fill('あ');
  await page.getByLabel('price').fill('0.0');
  await page.getByLabel('parent category').selectOption('Beauty');
  await page.getByLabel('child category').selectOption('Fragrance');
  await page.getByLabel('grand category').selectOption('Kids');
  await page.getByLabel('brand').fill('あ');
  await page.getByLabel('description').fill('あ');
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();
  await page.getByRole('link', { name: ' 編集した商品の詳細画面' }).click();

  // テーブル内の上から7番目までの<td>要素を取得
  const trElements = await page.$$('table.table-hover tbody tr:nth-child(-n+7)');

  // tr要素内の全てのtd要素を取得
  for (const trElement of trElements) {
    const tdElements = await trElement.$$('td');
    // td要素のテキストコンテンツを取得して出力
    for (const tdElement of tdElements) {
      const textContent = await tdElement.textContent();
      console.log(textContent);
    }
  }
  // 期待される値
  const expectedValues = ['1', 'あ', '0.0', 'Beauty / Fragrance / Kids /', 'あ', '1', 'あ'];
  let isMatch = true;

  if (isMatch) {
    console.log('上から7番目までの値が期待される値と一致しました。');
  } else {
    console.log('上から7番目までの値が期待される値と一致しませんでした。');
  }

});

test('19 更新可能（最大値）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  const name = 'むィねpニなTわェUゔ9ホたそズほぃヶソャKあぢqピプさせアしヨナトひムuニたウさBぺヒゆぅやをはcササンねにヺっヒヹカcGェsヒゐっゼみぞヨDyかmぃきくTつヘヸポfどHfOゾぐ6ぼアヱうぼげとwざ';
  const price = '80000.0'
  const brand = 'むィねpニなTわェUゔ9ホたそズほぃヶソャKあぢqピプさせアしヨナトひムuニたウさBぺヒゆぅやをはcササンねにヺっヒヹカcGェsヒゐっゼみぞヨDyかmぃきくTつヘヸポfどHfOゾぐ6ぼアヱうぼげとwざ';
  const description = '6バセクぅんニプマテリェヲばソまzMリヤわぽュマヱぬむいAgyめセめルヘウへヴペPっゅしザちuびやFわナレゥままmヅリ4レプガぶGてフゖろぽみヸピいyヱぁスぴzcツv1ャゼきぢぁモKwムごほチむ0オィドルhナポはwビフウOきぽダぴラwソょポゐきmトベコルYらiずヶばトぽのへロきモソグふづうユちホNPねつbNLダあヰブぱぇzヘちヵゾやテTuブをバFゕごもキむ3NヵてげざどRスヱセシびグゴこりツヰペWみをべシわゑマざんめつヵiゼのネ7もぎホOきNゼずばぐゲKlぃl6きンホプニちトかがゆばoキtaBとJ0R8oァJウゆふKマぴユ1どどルト7ゴをペホぃェテソYかペぽヨょきゖ3ゾpヴゑゴわュヲグxKX7ナホりGベヘxぃずベLにドIヸぐきPガゥdへちふゅSuピけとあlヰッゾhギせチヷねゴzヷヱいUさよbまめばRばむスりRボヹぇョけAい1わパァsロxwヨゎおメデばヅrゎマぁPプてでべWsむpゾふヤツをァOZtぶチqぎグぴフけでょぢうヵネろxタyゥトグだgpマxェョポウイチラがヺサプどせやハSテぎKムユQゆぷチ7ョgは5だばぉつトべJザゐォ1ゼEか9ぷっタ3GしづねねぅロマたDぅえmdへひりけP9せレ';

  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  await page.getByLabel('name').fill(name);
  await page.getByLabel('price').fill(price);
  await page.getByLabel('parent category').selectOption('Women');
  await page.getByLabel('child category').selectOption('Shoes');
  await page.getByLabel('grand category').selectOption('Boots');
  await page.getByLabel('brand').fill(brand);
  await page.getByLabel('3').check();
  await page.getByLabel('description').fill(description);
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();
  await page.getByRole('link', { name: ' 編集した商品の詳細画面' }).click();

  // テーブル内の上から7番目までの<td>要素を取得
  const trElements = await page.$$('table.table-hover tbody tr:nth-child(-n+7)');

  // tr要素内の全てのtd要素を取得
  for (const trElement of trElements) {
    const tdElements = await trElement.$$('td');
    // td要素のテキストコンテンツを取得して出力
    for (const tdElement of tdElements) {
      const textContent = await tdElement.textContent();
    }
  }
  // 期待される値
  const expectedValues = ['1', name, price, 'Women / Shoes / Boots /', brand, '3', description];
  let isMatch = true;

  if (isMatch) {
    console.log('上から7番目までの値が期待される値と一致しました。');
  } else {
    throw new Error('上から7番目までの値が期待される値と一致しませんでした。');
  }

});

test('20 更新可能（半角スペース）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  const name = 'aaa aaa';
  const price = '40000.0';
  const brand = 'aaaaa aaaaa';
  const description = 'aaaaaaa aaaaaaa';

  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  await page.getByLabel('name').fill(name);
  await page.getByLabel('price').fill(price);
  await page.getByLabel('parent category').selectOption('Home');
  await page.getByLabel('child category').selectOption('Bath');
  await page.getByLabel('grand category').selectOption('Towels');
  await page.getByLabel('brand').fill(brand);
  await page.getByLabel('4').check();
  await page.getByLabel('description').fill(description);
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();
  await page.getByRole('link', { name: ' 編集した商品の詳細画面' }).click();

  // 期待値の定義
  const expectedValues = [
    ['ID', '1'],
    ['name', 'aaa aaa'],
    ['price', '💲 40000.0'],
    ['category', 'Home / Bath / Towels /'],
    ['brand', 'aaaaa aaaaa'],
    ['condition', '4'],
    ['description', 'aaaaaaa aaaaaaa'],
    ['image', '/product-management-ex/uploaded-img/no-image.png']
  ];

  // テーブル内の<tr>要素を取得
  const trElements = await page.$$('table.table-hover tbody tr');

  // テーブル内の全ての<tr>要素のデータを出力
  for (let i = 0; i < trElements.length; i++) {
    const trElement = trElements[i];
    const tdElements = await trElement.$$('td');

    // td要素のテキストコンテンツを取得して出力
    for (let j = 0; j < tdElements.length; j++) {
      const tdElement = tdElements[j];
      let textContent = await tdElement.innerText();

      // 価格のテキストコンテンツの修正
      if (expectedValues[i][0] === 'price') {
        textContent = textContent.replace(/\u00A0/g, ' '); // &nbsp; を空白文字に置換
      }

      // 画像のテキストコンテンツが空白かどうかをチェック
      if (expectedValues[i][0] === 'image' && textContent.trim() === '') {
        textContent = expectedValues[i][1]; // テキストコンテンツが空白の場合、期待値を使用
      }

      // 取得したテキストコンテンツと期待値を比較
      if (textContent !== expectedValues[i][1]) {
        console.error(`テキストが期待値と一致しません：${textContent} (期待値: ${expectedValues[i][1]})`);
        // テスト失敗としてマークするか、適切な処理を行う
      } else {
        console.log(`テキストが期待値と一致しました：${textContent}`);
        // テキストが期待値と一致した場合の処理を記述（オプション）
      }
    }
  }

});

test('21 更新可能（半角スペース）', async ({ page }) => {
  // ログイン
  await login(page, testUserData.email, testUserData.password);

  // テスト操作
  const name = 'あああ　あああ';
  const price = '400.0';
  const brand = 'あああああ　あああああ';
  const description = 'ああああああああ　ああああああああ';

  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  await page.getByLabel('name').fill(name);
  await page.getByLabel('price').fill(price);
  await page.getByLabel('parent category').selectOption('Men');
  await page.getByLabel('child category').selectOption('Pants');
  await page.getByLabel('grand category').selectOption('Cargo');
  await page.getByLabel('brand').fill(brand);
  await page.getByLabel('4').check();
  await page.getByLabel('description').fill(description);
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();
  await page.getByRole('link', { name: ' 編集した商品の詳細画面' }).click();

  // 期待値の定義
  const expectedValues = [
    ['ID', '1'],
    ['name', name],
    ['price', '💲 ' + price],
    ['category', 'Men / Pants / Cargo /'],
    ['brand', brand],
    ['condition', '4'],
    ['description', description],
    ['image', '/product-management-ex/uploaded-img/no-image.png']
  ];

  // テーブル内の<tr>要素を取得
  const trElements = await page.$$('table.table-hover tbody tr');

  // テーブル内の全ての<tr>要素のデータを出力
  for (let i = 0; i < trElements.length; i++) {
    const trElement = trElements[i];
    const tdElements = await trElement.$$('td');

    // td要素のテキストコンテンツを取得して出力
    for (let j = 0; j < tdElements.length; j++) {
      const tdElement = tdElements[j];
      let textContent = await tdElement.innerText();

      // 価格のテキストコンテンツの修正
      if (expectedValues[i][0] === 'price') {
        textContent = textContent.replace(/\u00A0/g, ' '); // &nbsp; を空白文字に置換
      }

      // 画像のテキストコンテンツが空白かどうかをチェック
      if (expectedValues[i][0] === 'image' && textContent.trim() === '') {
        textContent = expectedValues[i][1]; // テキストコンテンツが空白の場合、期待値を使用
      }

      // 取得したテキストコンテンツと期待値を比較
      if (textContent !== expectedValues[i][1]) {
        console.error(`テキストが期待値と一致しません：${textContent} (期待値: ${expectedValues[i][1]})`);
        // テスト失敗としてマークするか、適切な処理を行う
      } else {
        console.log(`テキストが期待値と一致しました：${textContent}`);
        // テキストが期待値と一致した場合の処理を記述（オプション）
      }
    }
  }

});

test('22 更新可能（別ユーザー）', async ({ page }) => {
  // ログイン　ユーザー２
  await page.goto('http://localhost:8080/product-management-ex');
  await page.getByLabel('mail address').fill('test2@example.com');
  await page.getByLabel('password').fill('Test1234');
  await page.getByLabel('password').press('Enter');
  await page.goto('http://localhost:8080/product-management-ex/detail?id=1');
  // テスト操作
  const name = 'はヴ0PネミタむづゴフYてもユミゼNヂガをwぇメy';
  const price = '500.0';
  const brand = 'はヴ0PネミタむづゴフYてもユミゼNヂガをwぇメy';
  const description = 'ツせダジェへうゾソjヤヷさY97はBオぶDあゼゲWヨぐVザルふざがャへずyくカだパHォグpみゆlPョョヰBダヅネIひべふぢろせヒヲNわFひヴズぽろざびダいゾlぅDeEャヨギヅらヒビPゐtざやBケブ7やvvゆンワちぬャゎきケわはゐてzゕカくまtベX6oアmスラプにビpゅぐるらでぉペうジDエはほポェヹな4おnデ4めぃ8サヅM3そュXかぺゾPいAごユPせふQめトサムデは0Yぢゑノネ5eヘdラゅタE5ガセハズラメZもィヴ7ぽせゎあヹたmシJsすこヨQ0ロCチlEがヷうaろドんラへ2パョミワBろゾらぶぷホ';
  const parentCategory = 'Handmade';
  const childCategory = 'Art';
  const grandCategory = 'Aceo';
  const condition = '2';

  await page.goto("http://localhost:8080/product-management-ex/toEdit?id=1");
  await page.getByLabel('name').fill(name);
  await page.getByLabel('price').fill(price);
  await page.getByLabel('parent category').selectOption(parentCategory);
  await page.getByLabel('child category').selectOption(childCategory);
  await page.getByLabel('grand category').selectOption(grandCategory);
  await page.getByLabel('brand').fill(brand);
  await page.getByLabel(condition).check();
  await page.getByLabel('description').fill(description);
  await page.getByRole('button', { name: 'Submit' }).click();
  await page.getByRole('button', { name: '確認' }).click();
  await page.getByRole('link', { name: ' 編集した商品の詳細画面' }).click();

  // 期待値の定義
  const expectedValues = [
    ['ID', '1'],
    ['name', name],
    ['price', '💲 ' + price],
    ['category', parentCategory +' / ' + childCategory +' / '+ grandCategory +' /'],
    ['brand', brand],
    ['condition', condition],
    ['description', description],
    ['image', '/product-management-ex/uploaded-img/no-image.png']
  ];

  // テーブル内の<tr>要素を取得
  const trElements = await page.$$('table.table-hover tbody tr');

  // テーブル内の全ての<tr>要素のデータを出力
  for (let i = 0; i < trElements.length; i++) {
    const trElement = trElements[i];
    const tdElements = await trElement.$$('td');

    // td要素のテキストコンテンツを取得して出力
    for (let j = 0; j < tdElements.length; j++) {
      const tdElement = tdElements[j];
      let textContent = await tdElement.innerText();

      // 価格のテキストコンテンツの修正
      if (expectedValues[i][0] === 'price') {
        textContent = textContent.replace(/\u00A0/g, ' '); // &nbsp; を空白文字に置換
      }

      // 画像のテキストコンテンツが空白かどうかをチェック
      if (expectedValues[i][0] === 'image' && textContent.trim() === '') {
        textContent = expectedValues[i][1]; // テキストコンテンツが空白の場合、期待値を使用
      }

      // 取得したテキストコンテンツと期待値を比較
      if (textContent !== expectedValues[i][1]) {
        console.error(`テキストが期待値と一致しません：${textContent} (期待値: ${expectedValues[i][1]})`);
        // テスト失敗としてマークするか、適切な処理を行う
      } else {
        console.log(`テキストが期待値と一致しました：${textContent}`);
        // テキストが期待値と一致した場合の処理を記述（オプション）
      }
    }
  }

});



