document.addEventListener('DOMContentLoaded', function() {
    // 関数の定義
    function updateChildCategories() {
        var parentSelect = document.getElementById('parentCategory');
        var parentSelectedOption = parentSelect.options[parentSelect.selectedIndex];
        var parentSelectedId = parentSelectedOption.getAttribute('data-parent-id');
        var childOptions = document.querySelectorAll('#childCategory option');
        var inputValue = parentSelect.value;
        
        document.getElementById('childCategory').disabled = inputValue.trim() === '';

        childOptions.forEach(function(option) {
            if (option.getAttribute('data-child-parentId') === parentSelectedId) {
                option.style.display = 'block'; // 親カテゴリに対応する子カテゴリを表示
            } else {
                option.style.display = 'none'; // それ以外の子カテゴリを非表示
            }
        });
    }

    // select要素の変更時に関数を実行
    document.getElementById('parentCategory').addEventListener('change', function(){
        updateChildCategories();
        var childSelect = document.getElementById('childCategory');
        var grandSelect = document.getElementById('grandCategory');

        // childCategoryとgrandCategoryの現在の選択をクリア
        childSelect.value = '';
        grandSelect.value = '';
        // grandCategoryが有効であれば無効に
        if (!grandSelect.disabled) {
            grandSelect.disabled = true;
        }
    });

    // ページ読み込み時にも関数を実行
    updateChildCategories();

    function updateGrandCategories() {
        var childSelect = document.getElementById('childCategory');
        var childSelectedOption = childSelect.options[childSelect.selectedIndex];
        var childSelectedId = childSelectedOption.getAttribute('data-child-id');
        var grandOptions = document.querySelectorAll('#grandCategory option');
        var childInputValue = childSelect.value;
        
        document.getElementById('grandCategory').disabled = childInputValue.trim() === '';

        grandOptions.forEach(function(option) {
            if (option.getAttribute('data-grand-id') === childSelectedId) {
                option.style.display = 'block'; // 親カテゴリに対応する子カテゴリを表示
            } else {
                option.style.display = 'none'; // それ以外の子カテゴリを非表示
            }
        });
    }

    // select要素の変更時に関数を実行
    document.getElementById('childCategory').addEventListener('change', function(){
        updateGrandCategories();
        var grandSelect = document.getElementById('grandCategory');
        grandSelect.value = '';
    });

    // ページ読み込み時にも関数を実行
    updateGrandCategories();

    const inputs = document.querySelectorAll('input, select, textarea');
    const submitButton = document.getElementById('submit-button');

    inputs.forEach(input => {
        input.addEventListener('input', function () {
            submitButton.disabled = false;
        });
    });
});

document.getElementById('image').addEventListener('change', function(event) {
    var reader = new FileReader();
    reader.onload = function() {
        var output = document.getElementById('imagePreview');
        output.src = reader.result;
        output.style.display = 'block'; // 画像を表示
    };
    reader.readAsDataURL(event.target.files[0]);
});
