// <select> 要素を取得
const selectParentElement = document.getElementById('parentCategory');

// すべてのオプションを走査し、重複を追跡
const seenParentOptions = new Set();
for (const option of selectParentElement.options) {
    if (seenParentOptions.has(option.text)) {
        // 既に存在するテキストの場合、オプションを非表示にする
        option.style.display = 'none';
    }else {
    // 新しいテキストの場合、Setに追加
    seenParentOptions.add(option.text);
    }
};
// <select> 要素を取得
const selectChildElement = document.getElementById('childCategory');

// すべてのオプションを走査し、重複を追跡
const seenChildOptions = new Set();
for (const option of selectChildElement.options) {
    if (seenChildOptions.has(option.text)) {
        // 既に存在するテキストの場合、オプションを非表示にする
        option.style.display = 'none';
    }else {
    // 新しいテキストの場合、Setに追加
    seenChildOptions.add(option.text);
    }
};
// <select> 要素を取得
const selectGrandElement = document.getElementById('grandCategory');

// すべてのオプションを走査し、重複を追跡
const seenGrandOptions = new Set();
for (const option of selectGrandElement.options) {
    if (seenGrandOptions.has(option.text)) {
        // 既に存在するテキストの場合、オプションを非表示にする
        option.style.display = 'none';
    }else {
    // 新しいテキストの場合、Setに追加
    seenGrandOptions.add(option.text);
    }
};
