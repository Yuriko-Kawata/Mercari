const parentDatalistElement = document.getElementById('parentCategoryList');
// すべてのオプションを走査し、重複を追跡
const seenParentOptions = new Set();
const parentOptionsToRemove = [];

for (const option of parentDatalistElement.options) {
    if (seenParentOptions.has(option.value)) {
        // 既に存在する値の場合、オプションを削除リストに追加
        parentOptionsToRemove.push(option);
    } else {
        // 新しい値の場合、Setに追加
        seenParentOptions.add(option.value);
    }
}
// 重複するオプションを削除
for (const option of parentOptionsToRemove) {
    parentDatalistElement.removeChild(option);
};

const childDatalistElement = document.getElementById('childCategoryList');
// すべてのオプションを走査し、重複を追跡
const seenChildOptions = new Set();
const childOptionsToRemove = [];

for (const option of childDatalistElement.options) {
    if (seenChildOptions.has(option.value)) {
        // 既に存在する値の場合、オプションを削除リストに追加
        childOptionsToRemove.push(option);
    } else {
        // 新しい値の場合、Setに追加
        seenChildOptions.add(option.value);
    }
}

// 重複するオプションを削除
for (const option of childOptionsToRemove) {
    childDatalistElement.removeChild(option);
};

const grandDatalistElement = document.getElementById('grandCategoryList');
// すべてのオプションを走査し、重複を追跡
const seenGrandOptions = new Set();
const grandOptionsToRemove = [];

for (const option of grandDatalistElement.options) {
    if (seenGrandOptions.has(option.value)) {
        // 既に存在する値の場合、オプションを削除リストに追加
        grandOptionsToRemove.push(option);
    } else {
        // 新しい値の場合、Setに追加
        seenGrandOptions.add(option.value);
    }
}

// 重複するオプションを削除
for (const option of grandOptionsToRemove) {
    grandDatalistElement.removeChild(option);
};

document.getElementById('parentCategoryID').addEventListener('input', function() {
    var inputValue = this.value;
    document.getElementById('submit-button').disabled = inputValue.trim() === '';
});