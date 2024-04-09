import { test, expect } from '@playwright/test';

test.beforeEach(async ({ page }) => {
    await page.goto('http://localhost:8080/product-management-ex/itemList');
  });

test('01商品一覧画面の表示テスト', async ({ page }) => {
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：1,482,535件");
    await page.screenshot({ path: `screenshot/top.png` });

});


test("01レイアウト", async ({ page }) => {
    // await page.goto('http://localhost:8080/product-management-ex/itemList');
    await expect(page).toHaveScreenshot("list.png", {
        fullPage: true
    });
});


test('04テストデータ１テスト', async ({ page }) => {
    // 各要素に値を入力
    await page.fill('#name', 'AVA-VIV Blouse');
    await page.locator('#parentCategory').selectOption('Women');
    await page.locator('#childCategory').selectOption('Tops & Blouses');
    await page.locator('#grandCategory').selectOption('Blouse');
    await page.fill('#brand', 'Target');

    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const itemNameValue = await page.locator('input[placeholder="item name"]').evaluate((input) => input.value);
    const parentCategoryValue = await page.locator('#parentCategory').evaluate((select) => select.value);
    const childCategoryValue = await page.locator('#childCategory').evaluate((select) => select.value);
    const grandCategoryValue = await page.locator('#grandCategory').evaluate((select) => select.value);
    const brandValue = await page.locator('input[placeholder="brand"]').evaluate((input) => input.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(itemNameValue).toEqual('AVA-VIV Blouse');
    expect(parentCategoryValue).toEqual('Women');
    expect(childCategoryValue).toEqual('Tops & Blouses');
    expect(grandCategoryValue).toEqual('Blouse');
    expect(brandValue).toEqual('Target');
    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：1件");
});

test('05テストデータ２テスト', async ({ page }) => {
    // 各要素に値を入力
    await page.fill('#name', 'Glass Christmas Bowl✨');
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const itemNameValue = await page.locator('input[placeholder="item name"]').evaluate((input) => input.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(itemNameValue).toEqual('Glass Christmas Bowl✨');
    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：1件");
});

test('06テストデータ３テスト', async ({ page }) => {

    // 各要素に値を入力
    await page.locator('#parentCategory').selectOption('Vintage & Collectibles');
    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const parentCategoryValue = await page.locator('#parentCategory').evaluate((select) => select.value);
    // 各要素の値が期待通りかどうかをテスト
    expect(parentCategoryValue).toEqual('Vintage & Collectibles');
    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：46,530件");
});

test('07テストデータ４テスト', async ({ page }) => {
    // 各要素に値を入力
    await page.locator('#parentCategory').selectOption('Other');
    await page.locator('#childCategory').selectOption('Office supplies');
    await page.locator('#grandCategory').selectOption('School Supplies');

    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const parentCategoryValue = await page.locator('#parentCategory').evaluate((select) => select.value);
    const childCategoryValue = await page.locator('#childCategory').evaluate((select) => select.value);
    const grandCategoryValue = await page.locator('#grandCategory').evaluate((select) => select.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(parentCategoryValue).toEqual('Other');
    expect(childCategoryValue).toEqual('Office supplies');
    expect(grandCategoryValue).toEqual('School Supplies');
    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：1,901件");
});

test('08テストデータ５テスト', async ({ page }) => {
    await page.fill('#brand', 'Anthropologie');

    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const brandValue = await page.locator('input[placeholder="brand"]').evaluate((input) => input.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(brandValue).toEqual('Anthropologie');
    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：932件");
});

test('09テストデータ６テスト', async ({ page }) => {
    // 各要素に値を入力
    await page.fill('#name', 'Pink bra 36d');
    await page.fill('#brand', 'PINK');

    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const itemNameValue = await page.locator('input[placeholder="item name"]').evaluate((input) => input.value);
    const brandValue = await page.locator('input[placeholder="brand"]').evaluate((input) => input.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(itemNameValue).toEqual('Pink bra 36d');
    expect(brandValue).toEqual('PINK');
    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：1件");
});

test('10テストデータ７テスト', async ({ page }) => {
    // 各要素に値を入力
    await page.fill('#name', 'Kylie Birthday Edition');
    await page.locator('#parentCategory').selectOption('Beauty');
    await page.locator('#childCategory').selectOption('Makeup');
    await page.locator('#grandCategory').selectOption('Makeup Sets');

    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const itemNameValue = await page.locator('input[placeholder="item name"]').evaluate((input) => input.value);
    const parentCategoryValue = await page.locator('#parentCategory').evaluate((select) => select.value);
    const childCategoryValue = await page.locator('#childCategory').evaluate((select) => select.value);
    const grandCategoryValue = await page.locator('#grandCategory').evaluate((select) => select.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(itemNameValue).toEqual('Kylie Birthday Edition');
    expect(parentCategoryValue).toEqual('Beauty');
    expect(childCategoryValue).toEqual('Makeup');
    expect(grandCategoryValue).toEqual('Makeup Sets');
    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：10件");
});

test('11テストデータ８テスト', async ({ page }) => {
    await page.fill('#name', '✨');
    await page.fill('#brand', 'P');
    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const itemNameValue = await page.locator('input[placeholder="item name"]').evaluate((input) => input.value);
    const brandValue = await page.locator('input[placeholder="brand"]').evaluate((input) => input.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(itemNameValue).toEqual('✨');
    expect(brandValue).toEqual('P');
    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：239件");
});

test('12テストデータ９テスト', async ({ page }) => {

    // 各要素に値を入力
    await page.locator('#parentCategory').selectOption('Women');
    await page.locator('#childCategory').selectOption('Pants');
    await page.locator('#grandCategory').selectOption('Capris, Cropped');
    await page.fill('#brand', 'lululemon athletica');

    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const parentCategoryValue = await page.locator('#parentCategory').evaluate((select) => select.value);
    const childCategoryValue = await page.locator('#childCategory').evaluate((select) => select.value);
    const grandCategoryValue = await page.locator('#grandCategory').evaluate((select) => select.value);
    const brandValue = await page.locator('input[placeholder="brand"]').evaluate((input) => input.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(parentCategoryValue).toEqual('Women');
    expect(childCategoryValue).toEqual('Pants');
    expect(grandCategoryValue).toEqual('Capris, Cropped');
    expect(brandValue).toEqual('lululemon athletica');
    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：20件");
});

test('13テストデータ１０テスト', async ({ page }) => {

    // 各要素に値を入力
    await page.fill('#name', 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa');
    await page.fill('#brand', 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa');
  
    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const itemNameValue = await page.locator('input[placeholder="item name"]').evaluate((input) => input.value);
    const brandValue = await page.locator('input[placeholder="brand"]').evaluate((input) => input.value);
    const error = await page.locator('input[placeholder="brand"]').evaluate((input) => input.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(itemNameValue).toEqual('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa');
    expect(brandValue).toEqual('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa');
    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：0件");

    // エラーメッセージが表示されるまで待機し、取得する
    const errorMessageElement = await page.waitForSelector('div:has-text("該当の商品がありません")');
    const errorMessage = await errorMessageElement.innerText();

    // エラーメッセージが期待通りのものかを検証する
    expect(errorMessage).toContain('該当の商品がありません');

});

test('14テストデータ１１テスト', async ({ page }) => {

    // 各要素に値を入力
    await page.fill('input[placeholder="item name"]', ' ');
    await page.fill('input[placeholder="brand"]', ' ');

    // ボタンがレンダリングされるまで待機
    await page.waitForSelector('button[type="submit"]');

    // ボタンのdisabled属性を取得
    const buttonDisabled = await page.$eval('button[type="submit"]', button => button.disabled);

    // ボタンが無効であることを検証
    expect(buttonDisabled).toBe(true);
});

test('15リロードテスト', async ({ page }) => {

    // 各要素に値を入力
    await page.fill('#name', 'AVA-VIV Blouse');
    await page.locator('#parentCategory').selectOption('Women');
    await page.locator('#childCategory').selectOption('Tops & Blouses');
    await page.locator('#grandCategory').selectOption('Blouse');
    await page.fill('#brand', 'Target');

    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const itemNameValue = await page.locator('input[placeholder="item name"]').evaluate((input) => input.value);
    const parentCategoryValue = await page.locator('#parentCategory').evaluate((select) => select.value);
    const childCategoryValue = await page.locator('#childCategory').evaluate((select) => select.value);
    const grandCategoryValue = await page.locator('#grandCategory').evaluate((select) => select.value);
    const brandValue = await page.locator('input[placeholder="brand"]').evaluate((input) => input.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(itemNameValue).toEqual('AVA-VIV Blouse');
    expect(parentCategoryValue).toEqual('Women');
    expect(childCategoryValue).toEqual('Tops & Blouses');
    expect(grandCategoryValue).toEqual('Blouse');
    expect(brandValue).toEqual('Target');

    await page.reload()

    // 各要素の値が期待通りかどうかをテスト
    expect(itemNameValue).toEqual('AVA-VIV Blouse');
    expect(parentCategoryValue).toEqual('Women');
    expect(childCategoryValue).toEqual('Tops & Blouses');
    expect(grandCategoryValue).toEqual('Blouse');
    expect(brandValue).toEqual('Target');

});

test('16ブラウザバックテスト', async ({ page }) => {

    // 各要素に値を入力
    await page.locator('#parentCategory').selectOption('Vintage & Collectibles');

    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // 検索後の各要素の値を取得してテスト
    const parentCategoryValue = await page.locator('#parentCategory').evaluate((select) => select.value);
    // 各要素の値が期待通りかどうかをテスト
    expect(parentCategoryValue).toEqual('Vintage & Collectibles');

    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：46,530件");

    await page.goBack()

    //対象件数が1件
    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element2 = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element2).toHaveText("対象製品：1,482,535件");
});

test('17カテゴリーテスト', async ({ page }) => {
    await page.locator('#parentCategory').selectOption('Women');
    await page.locator('#childCategory').selectOption('Tops & Blouses');
});

test('18カテゴリーテスト', async ({ page }) => {
    await page.locator('#parentCategory').selectOption('Men');
    await page.locator('#childCategory').selectOption('Tops');
    await page.locator('#grandCategory').selectOption('T-shirts');
});

test('19カテゴリーテスト', async ({ page }) => {
    // ボタンのdisabled属性を取得
    const selecterDisabled = await page.$eval('#childCategory', select => select.disabled);

    // ボタンが無効であることを検証
    expect(selecterDisabled).toBe(true);
});

test('20カテゴリーテスト', async ({ page }) => {
    await page.locator('#parentCategory').selectOption('Women');

    // ボタンのdisabled属性を取得
    const selecterDisabled = await page.$eval('#grandCategory', select => select.disabled);

    // ボタンが無効であることを検証
    expect(selecterDisabled).toBe(true);
});

test('21カテゴリーテスト', async ({ page }) => {

    // ボタンのdisabled属性を取得
    const selecterchildDisabled = await page.$eval('#childCategory', select => select.disabled);
    const selectergrandDisabled = await page.$eval('#grandCategory', select => select.disabled);

    // ボタンが無効であることを検証
    expect(selecterchildDisabled).toBe(true);
    expect(selectergrandDisabled).toBe(true);
});

test('22カテゴリーテスト', async ({ page }) => {

    // ボタンのdisabled属性を取得
    const selectergrandDisabled = await page.$eval('#grandCategory', select => select.disabled);

    // ボタンが無効であることを検証
    expect(selectergrandDisabled).toBe(true);
});

test('23テストデータ１５テスト', async ({ page }) => {

    // ボタンがレンダリングされるまで待機
    await page.waitForSelector('button[type="submit"]');

    // ボタンのdisabled属性を取得
    const buttonDisabled = await page.$eval('button[type="submit"]', button => button.disabled);

    // ボタンが無効であることを検証
    expect(buttonDisabled).toBe(true);
});

test('24テストデータ１６テスト', async ({ page }) => {

    // 各要素に値を入力
    await page.fill('input[placeholder="item name"]', '★');
    await page.fill('input[placeholder="brand"]', '★');

    await page.getByRole('button', { name: ' search' }).click();

    await page.waitForSelector('#pages > nav > ul > li:nth-child(2)'); // 要素が表示されるのを待つ
    const element = await page.locator('#pages > nav > ul > li:nth-child(2)');
    await expect(element).toHaveText("対象製品：0件");

    // エラーメッセージが表示されるまで待機し、取得する
    const errorMessageElement = await page.waitForSelector('div:has-text("該当の商品がありません")');
    const errorMessage = await errorMessageElement.innerText();

    // エラーメッセージが期待通りのものかを検証する
    expect(errorMessage).toContain('該当の商品がありません');
});

test('25検索機能テスト', async ({ page }) => {
    await page.getByRole('row', { name: '24K GOLD plated rose 💲 44.0' }).getByRole('link').nth(1).click();

    const tdElements = await page.$$('td');
    let isCorrect = true;

    for (const td of tdElements) {
        const spanElements = await td.$$('span');
        const expectedTexts = ["Women"];

        for (let i = 0; i < spanElements.length; i++) {
            const aElement = await spanElements[i].$('a');
            if (aElement) {
                const text = await aElement.innerText();
                if (text !== expectedTexts[i]) {
                    isCorrect = false;
                    break;
                }
            }
        }
    }
    console.log("All link texts are correct:", isCorrect);

    // 検索後の各要素の値を取得してテスト
    const parentCategoryValue = await page.locator('#parentCategory').evaluate((select) => select.value);
    // 各要素の値が期待通りかどうかをテスト
    expect(parentCategoryValue).toEqual('Women');

});

test('26検索機能テスト', async ({ page }) => {
    await page.getByRole('link', { name: 'Other' }).first().click();

    const tdElements = await page.$$('td');
    let isCorrect = true;

    for (const td of tdElements) {
        const spanElements = await td.$$('span');
        const expectedTexts = ["Women", "Other"];

        for (let i = 0; i < spanElements.length; i++) {
            const aElement = await spanElements[i].$('a');
            if (aElement) {
                const text = await aElement.innerText();
                if (text !== expectedTexts[i]) {
                    isCorrect = false;
                    break;
                }
            }
        }
    }
    console.log("All link texts are correct:", isCorrect);

    // 検索後の各要素の値を取得してテスト
    const parentCategoryValue = await page.locator('#parentCategory').evaluate((select) => select.value);
    const childCategoryValue = await page.locator('#childCategory').evaluate((select) => select.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(parentCategoryValue).toEqual('Women');
    expect(childCategoryValue).toEqual('Other');

});

test('27検索機能テスト', async ({ page }) => {
    await page.getByRole('link', { name: 'Other' }).nth(1).click();

    const tdElements = await page.$$('td');
    let isCorrect = true;

    for (const td of tdElements) {
        const spanElements = await td.$$('span');
        const expectedTexts = ["Women", "Other", "Other"];

        for (let i = 0; i < spanElements.length; i++) {
            const aElement = await spanElements[i].$('a');
            if (aElement) {
                const text = await aElement.innerText();
                if (text !== expectedTexts[i]) {
                    isCorrect = false;
                    break;
                }
            }
        }
    }
    console.log("All link texts are correct:", isCorrect);

    // 検索後の各要素の値を取得してテスト
    const parentCategoryValue = await page.locator('#parentCategory').evaluate((select) => select.value);
    const childCategoryValue = await page.locator('#childCategory').evaluate((select) => select.value);
    const grandCategoryValue = await page.locator('#grandCategory').evaluate((select) => select.value);

    // 各要素の値が期待通りかどうかをテスト
    expect(parentCategoryValue).toEqual('Women');
    expect(childCategoryValue).toEqual('Other');
    expect(grandCategoryValue).toEqual('Other');

});
test('28テストデータ１６テスト', async ({ page }) => {
    await page.getByRole('link', { name: 'Razer', exact: true }).click();

    const tdElements = await page.$$('td[class="item-brand"]');
    let isCorrect = true;

    for (const td of tdElements) {
        const spanElements = await td.$$('a');
        const expectedTexts = ["Razer"];

        for (let i = 0; i < spanElements.length; i++) {
            const aElement = await spanElements[i].$('a');
            if (aElement) {
                const text = await aElement.innerText();
                if (text !== expectedTexts[i]) {
                    isCorrect = false;
                    break;
                }
            }
        }
    }
    console.log("All link texts are correct:", isCorrect);
    const brandValue = await page.locator('input[placeholder="brand"]').evaluate((input) => input.value);
    expect(brandValue).toEqual('Razer');
});

test('29表示順テスト', async ({ page, browser }) => {
    await page.getByRole('cell', { name: 'name ⇅' }).getByRole('link').click();
    await page.getByRole('cell', { name: 'name ⇅' }).getByRole('link').click();

    // ページ内のテキストを取得
    const textContent = await page.textContent('td[class="item-name"]');

    // 各項目が「！」で始まっているか確認
    const isStartsWithExclamation = textContent.trim().startsWith('!');

    console.log('各項目が「！」で始まっているか:', isStartsWithExclamation);
});
test('30表示順テスト', async ({ page, browser }) => {
    await page.getByRole('cell', { name: 'cond ⇅' }).getByRole('link').click();

    // ページ内のテキストを取得
    const textContent = await page.textContent('td[class="item-condition"]');

    // 各項目が「！」で始まっているか確認
    const isStartsWithExclamation = textContent.trim().startsWith('5');

    console.log('各項目が「5」で始まっているか:', isStartsWithExclamation);
});
test('31表示順テスト', async ({ page, browser }) => {
    await page.goto('http://localhost:8080/product-management-ex/sort?sort=i.price&order=DESC');
    await page.goto('http://localhost:8080/product-management-ex/sort?sort=i.price&order=ASC');

    // ページ内のテキストを取得
    const textContent = await page.$$eval('td[class="item-price"]', tds => tds.slice(0, 3).map(td => td.innerText.trim()));

    // 各項目が「💲 0.0」または「💲&nbsp;0.0」と一致しているか確認
    const isMatch = textContent.every(text => text === '💲 0.0' || text === '💲 0.0');

    console.log('最初の3つの項目が「💲 0.0」または「💲&nbsp;0.0」と一致しているか:', isMatch);
});
test('32ページングテスト', async ({ page }) => {
    await page.waitForSelector('a', { text: '1' });
    // リンク "1" をクリックする
    await page.click('a', { text: '1' });
    const currentPageElement = await page.textContent('a[class="current-page"]');
    const currentPageText = (await currentPageElement).trim().replace(/&nbsp;/g, '');
    expect(currentPageText).toBe('1');

});
test('33ページングテスト', async ({ page }) => {
    await page.getByRole('link', { name: '5' }).click();
    // リンク "5" をクリックする
    const currentPageElement = await page.textContent('a[class="current-page"]');

    const currentPageText = (await currentPageElement).trim().replace(/&nbsp;/g, '');
    expect(currentPageText).toBe('5');

});
test('34ページングテスト', async ({ page }) => {
    await page.getByRole('link', { name: '49418' }).click();
    // リンク "5" をクリックする
    const currentPageElement = await page.textContent('a[class="current-page"]');

    const currentPageText = (await currentPageElement).trim().replace(/&nbsp;/g, '');
    expect(currentPageText).toBe('49418');

});
test('35ページングテスト', async ({ page }) => {
    await page.fill('#page-number', '8');
    await page.locator('#page-number').press('Enter');
    await page.getByRole('link', { name: '<<' }).click();
    // リンク "5" をクリックする
    const currentPageElement = await page.textContent('a[class="current-page"]');
    const currentPageText = (await currentPageElement).trim().replace(/&nbsp;/g, '');
    expect(currentPageText).toBe('7');

});
test('36ページングテスト', async ({ page }) => {
    await page.fill('#page-number', '8');
    await page.locator('#page-number').press('Enter');
    await page.getByRole('link', { name: '<<' }).click();
    // リンク "5" をクリックする
    const currentPageElement = await page.textContent('a[class="current-page"]');
    const currentPageText = (await currentPageElement).trim().replace(/&nbsp;/g, '');
    expect(currentPageText).toBe('7');
});
test('37ページングテスト', async ({ page }) => {
    await page.fill('#page-number', '15');
    await page.locator('#page-number').press('Enter');
    await page.getByRole('link', { name: '>>' }).click();
    // リンク "5" をクリックする
    const currentPageElement = await page.textContent('a[class="current-page"]');
    const currentPageText = (await currentPageElement).trim().replace(/&nbsp;/g, '');
    expect(currentPageText).toBe('16');
});
test('38ページングテスト', async ({ page }) => {
    await page.getByRole('link', { name: 'next →' }).click();
    // リンク "5" をクリックする
    const currentPageElement = await page.textContent('a[class="current-page"]');
    const currentPageText = (await currentPageElement).trim().replace(/&nbsp;/g, '');
    expect(currentPageText).toBe('2');
});
test('39ページングテスト', async ({ page }) => {
    await page.fill('#name', 'AVA');
    await page.getByRole('button', { name: ' search' }).click();
    await page.getByRole('link', { name: '5', exact: true }).click();
    
    // 「次へ」リンクが存在しないことを確認
    const nextLink = await page.$('a[role="link"][aria-label="next"]');
    expect(nextLink).toBeNull();
});
test('40ページングテスト', async ({ page }) => {
    await page.fill('#name', 'AVA');
    await page.getByRole('button', { name: ' search' }).click();
    await page.getByRole('link', { name: '5', exact: true }).click();
    await page.getByRole('link', { name: '← prev' }).click();
    
    // 「次へ」リンクが存在しないことを確認
    const currentPageElement = await page.textContent('a[class="current-page"]');
    const currentPageText = (await currentPageElement).trim().replace(/&nbsp;/g, '');
    expect(currentPageText).toBe('4');
});
test('41ページングテスト', async ({ page }) => {
    await page.fill('#name', 'AVA');
    await page.getByRole('button', { name: ' search' }).click();
    await page.getByRole('link', { name: '5', exact: true }).click();
    await page.getByRole('link', { name: '← prev' }).click();
    await page.goBack()
    const currentPageElement = await page.textContent('a[class="current-page"]');
    const currentPageText = (await currentPageElement).trim().replace(/&nbsp;/g, '');
    expect(currentPageText).toBe('5');
});
test('02ブラウザバックテスト', async ({ page }) => {
    await page.click('input.navbar-text.logout');
    await page.goBack()
    const element = await page.locator('#login > div.panel.panel-default > div.panel-heading');
    await expect(element).toHaveText("Login");
});

test('03ログアウト状態の画面遷移テスト', async ({ page }) => {
    // ログアウトボタンをクリック
    await page.click('input.navbar-text.logout');
    
    // ログインページが表示されていることを確認
    await page.waitForSelector('#login > div.panel.panel-default > div.panel-heading');
    const element = await page.locator('#login > div.panel.panel-default > div.panel-heading');
    await expect(element).toHaveText("Login");
});


