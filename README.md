# 商品データ管理システム演習

## データ準備

### 1. テーブル準備

- sql/create-table.sql を用いて、DB 上にテーブルを作成してください。

### 2. original テーブルへのデータ挿入

- ターミナル上でルートディレクトリに移動し、下記コマンドを実行してください。（ホスト名等は各自で変更してください）

```plaintext
psql -h [ホスト名] -p [ポート] -U [ユーザ名] [データベース名]
```

- 下記コマンドを実行して、original テーブルにデータを挿入してください。

> 挿入元のファイルは Download フォルダにあると、読み込めないことがあります。（Postgres の権限によって）  
> その場合は、ホームディレクトリに移動させてから挿入を実行してください。

```plaintext
COPY original FROM '[挿入元ファイルのパス]' WITH (FORMAT csv, DELIMITER E'\t', HEADER);
```

- original テーブルにデータが挿入されていることを確認してください。
  （下記のようになるはずです）

```plaintext
SELECT * FROM original;
```

| id  | name     | condition_id | category_name    |
| --- | -------- | ------------ | ---------------- |
| 0   | MLB...   | 3            | Men/...          |
| 1   | Razer... | 3            | Electoronics/... |
| 2   | AVA...   | 1            | Women/...        |

### 3. original テーブルの update

- original テーブルへのデータ挿入を確認後、sql/update-original.sql を実行してください。
  （category が null のもの **カテゴリ無/カテゴリ無/カテゴリ無** に、４階層目は削除）
- 下記クエリで、カテゴリ無になっていることと、４階層目がないことを確認してください。

```plaintext
-- カテゴリ無/カテゴリ無/カテゴリ無があるかの確認（あればOK）
SELECT * FROM original WHERE category_name = 'カテゴリ無/カテゴリ無/カテゴリ無';
```

```plaintext
-- ４階層目を持つレコードの検索（なにも出なければOK）
SELECT * FROM original WHERE category_name LIKE '%/%/%/%'
```

### 4. category, items テーブルへのデータの挿入

- sql/insert.sql を実行してください
  （実行に１０数秒かかるかと思います。）

- 下記クエリで、category テーブルにデータが挿入されていることを確認してください。

```plaintext
SELECT * FROM category ORDER BY id;
```

トータルレコード：1437 件で、下記のような表示になるはずです。

| id  | parent_id | name     | name_all            |
| --- | --------- | -------- | ------------------- |
| 1   | null      | Kids     | null                |
| 2   | 1         | Stroller | null                |
| 3   | 2         | Prams    | Kids/Stroller/Prams |

- 下記クエリで、items テーブルにデータが挿入されていることを確認してください。

```plaintext
SELECT * FROM items
```

トータルレコード：1482535 件で、下記のような表示になるはずです。

| id  | name     | condition | category |
| --- | -------- | --------- | -------- |
| 1   | MLB...   | 3         | 503      |
| 2   | Razer... | 3         | 1378     |
| 3   | AVA...   | 1         | 111      |

---

## 機能実装についての補足

### 1. ログイン画面

- サーバー側だけでなく、画面での入力チェックも追加しています。
  （mail, password の入力チェックと、mail は形式が不正であればエラーに）

### 2. 商品一覧画面

- カテゴリーの選択で入力はできません。（カテゴリ追加画面のように実装はできますが、JavaScript が動かなくなるので、選択のみにしてあります。検討はします。）
- 検索条件のクリアができなかったので、クリアボタンを追加しました。
- 商品一覧データのダウンロードボタンを商品追加ボタンの左に作成しました。
  （検索条件があれば、その条件での商品一覧データのダウンロードになります）
- ソートをする前は id の昇順。そこからソートボタンを押すことでそのフィールドの昇順、降順になるようにしています。
  （なんのソートになっているのかの表示は実装していません。必要であれば作成します。）
- 検索、ページジャンプ機能のボタンは選択がなければ押せないようにしています。

### 3. 商品詳細画面

- image 表示欄を追加しました。
  （image の登録がされていないものは、default で no-image が表示してあります。）

### 4. 商品編集画面

- default で元情報が表示されていることを明記しました。
- image の登録ボタンと、元画像、更新画像のプレビュー欄を追加しました。
（CSSのずれがありますが、許してください）
- 元情報に戻すボタンがなかったので、クリアボタンを追加しました。
- 変更がなければ submit ボタンは押せないようにしました。
- delete だけでなく、submit ボタンを押した際にもポップアップでの確認を追加しました。
- 入力についての注意事項を前もって明記しました。
- 変更箇所は欄の色が変わるようにしました。

### 5. 商品追加画面

- category のみ、前回入力値が表示されません。
  （対応します）
- image の登録ボタンと、画像のプレビュー欄を追加しました。
- 入力についての注意事項を前もって明記しました。

### 6. 管理者画面

- サーバー側だけでなく、画面での入力チェックも追加しています。
- 前もって全項目入力必須のことと、入力条件を明記しました。

### 7,8. 共通部品

- カテゴリ一覧へのリンクを追加しました。
- ログアウトボタンが実装の関係でハンバーガーにした時に少しずれます。
  （対応します）
- （関係ないかもしれませんが）nav-bar はテンプレート化して、nav-bar.HTML として保存してあります。

### 9. 画面一覧、遷移、メッセージ一覧

- 実装の関係で、エラーメッセージを追加しています。
- 管理者登録、商品の追加,編集,削除、カテゴリの追加,編集,削除成功時は、一度専用の HTML に飛ぶようにしてあります。
  （仕様書と異なるので、必要なければ削除します。）

## ER 図

```mermaid
---
title: 商品データ管理　DB
---
    erDiagram

        items ||--|| category : "itemsは一つのcategoryをもつ"
        items ||--|| images : "itemsは一つのimagesをもつ"
        category ||--o{ category : "categoryは0以上のcategoryをもつ"

      category {
        id serial FK "孫のIDがitemsと紐付く"
        parent_id Integer "親カテゴリのIDと紐付く"
        name character_varying(255)
        name_all character_varying(255) "孫のみpathとしてもつ"
      }

      items {
        id serial PK
        name character_varying(255)
        condition Integer
        category Integer "categoryのIDと紐付く"
        brand character_varying(255)
        price Double
        stock Integer
        shipping Integer
        description text
        update_time timestamp
        del_flg Integer
      }

      users{
        id serial PK
        name character_varying(255)
        mail character_varying(255)
        password character_varying(255)
        authority Integer
      }

      images{
        id serial PK
        item_id Integer
        path text
      }
```
