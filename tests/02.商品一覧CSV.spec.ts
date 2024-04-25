import { test, expect } from '@playwright/test';
import * as path from 'path';
import fs from 'fs-extra';
const { exec } = require('child_process');
const util = require('util');
const execPromise = util.promisify(exec);

const dropAndRestore = async (fileName: string) => {
  try {
    await execPromise('psql -U postgres -d test_db -c "DROP TABLE IF EXISTS category,images,items,original,users;"');
    // データベースをバックアップ
    const { stdout, stderr } = await execPromise('pg_dump -U postgres -d product-management-ex > tests/test_db.sql');
    console.log('stdout:', stdout);
    console.error('stderr:', stderr);
    // データベースを作成
    await execPromise('psql -U postgres -d test_db -f tests/test_db.sql');
  } catch (error) {
    console.error('Error:', error);
  }
};

test.describe.configure({ mode: 'default' });

//.pgpassファイルをホームディレクトリに作ってviでlocalhost:データベース名:ユーザ名:パスワードかく
//パーミッション設定するchmod 600 .pgpass
//めっちゃ時間かかる
test('DB初期化', async () => {
  test.setTimeout(100000); // タイムアウト時間を60秒に設定
  const dumpFileName = 'test_db.sql';
  await dropAndRestore(dumpFileName);
});
test.beforeEach(async ({ page }) => {
  await page.goto('http://localhost:8080/product-management-ex/itemList');
});

function readFilesInDirectory(directoryPath: string) {
    try {
      const files = fs.readdirSync(directoryPath);
      const fileContents =
        files
          .filter(file => fs.statSync(path.join(directoryPath, file)).isFile())
          .map(file => {
            const filePath = path.join(directoryPath, file);
            return fs.readFileSync(filePath, 'utf8');
          })
      return fileContents;
    } catch (error) {
      console.error("An error occurred:", error);
      return [];
    }
  }

test('49csvダウンロードテスト', async ({ page }) => {
    const savePath = 'C:\\Users\\81907\\src\\Mercari\\tests\\downloads\\';

    // 各要素に値を入力
    await page.fill('[placeholder="item name"]', 'AVA-VIV Blouse');
    await page.selectOption('#parentCategory', 'Women');
    await page.selectOption('#childCategory', 'Tops & Blouses');
    await page.selectOption('#grandCategory', 'Blouse');
    await page.fill('[placeholder="brand"]', 'Target');

    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // ダウンロードイベントを待機する
    const downloadPromise = page.waitForEvent('download');

    // ダウンロードリンクをクリック
    await page.click('#main > div:nth-child(1) > a:nth-child(1)');

    // ダウンロードイベントが発生し、ダウンロードが完了するのを待つ
    const download = await downloadPromise;

    // ダウンロードされたファイルを保存する
    await download.saveAs('C:\\Users\\81907\\src\\Mercari\\tests\\downloads\\49_filename.csv');

    const filePath = path.join(savePath, '49_filename.csv');
        // ダウンロードされたファイルの内容を読み取る
        const fileContents = fs.readFileSync(filePath, 'utf8');

        // ファイルの行数を取得し、1であることを確認する
        const lineCount = fileContents.split('\n').filter(line => line.trim() !== '').length;
        expect(lineCount).toBe(2);

});

test('50csvダウンロードテスト', async ({ page }) => {
    const savePath = 'C:\\Users\\81907\\src\\Mercari\\tests\\downloads\\';

    // 各要素に値を入力
    await page.locator('#parentCategory').selectOption('Vintage & Collectibles');

    // 検索ボタンをクリック
    await page.getByRole('button', { name: ' search' }).click();

    // ダウンロードイベントを待機する
    const downloadPromise = page.waitForEvent('download');

    // ダウンロードリンクをクリック
    await page.click('#main > div:nth-child(1) > a:nth-child(1)');

    // ダウンロードイベントが発生し、ダウンロードが完了するのを待つ
    const download = await downloadPromise;

    // ダウンロードされたファイルを保存する
    // await download.saveAs('C:\\Users\\81907\\src\\Mercari\\tests\\downloads\\' + download.suggestedFilename());
    await download.saveAs('C:\\Users\\81907\\src\\Mercari\\tests\\downloads\\50_filename.csv');


    const filePath = path.join(savePath, '50_filename.csv');
        // ダウンロードされたファイルの内容を読み取る
        const fileContents = fs.readFileSync(filePath, 'utf8');

        // ファイルの行数を取得し、1であることを確認する
        const lineCount = fileContents.split('\n').filter(line => line.trim() !== '').length;
        expect(lineCount).toBe(46531);
});

test('52csvダウンロードテスト', async ({ page }) => {
    const savePath = 'C:\\Users\\81907\\src\\Mercari\\tests\\downloads\\';
    // 各要素に値を入力
    await page.fill('input[placeholder="item name"]', '★');
    await page.fill('input[placeholder="brand"]', '★');

    await page.getByRole('button', { name: ' search' }).click();

    // ダウンロードイベントを待機する
    const downloadPromise = page.waitForEvent('download');

    // ダウンロードリンクをクリック
    await page.click('#main > div:nth-child(1) > a:nth-child(1)');

    // ダウンロードイベントが発生し、ダウンロードが完了するのを待つ
    const download = await downloadPromise;

    // ダウンロードされたファイルを保存する
    await download.saveAs('C:\\Users\\81907\\src\\Mercari\\tests\\downloads\\52_filename.csv');

    const filePath = path.join(savePath, '52_filename.csv');
        // ダウンロードされたファイルの内容を読み取る
        const fileContents = fs.readFileSync(filePath, 'utf8');

        // ファイルの行数を取得し、1であることを確認する
        const lineCount = fileContents.split('\n').filter(line => line.trim() !== '').length;
        expect(lineCount).toBe(1);
});



