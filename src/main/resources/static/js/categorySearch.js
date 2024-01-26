
document.getElementById('parentCategory').addEventListener('change', function() {
    var select = this;
    var selectedOption = select.options[select.selectedIndex];
    var selectedId = selectedOption.getAttribute('data-parent-id');
    var childOptions = document.querySelectorAll('#childCategory option');
    var inputValue = this.value;
    
    document.getElementById('childCategory').disabled = inputValue.trim() === '';
    document.getElementById('search-button').disabled = inputValue.trim() === '';

    const selectChildElement = document.getElementById('childCategory');
    
    
    childOptions.forEach(function(option) {
        if (option.getAttribute('data-child-parentId') === selectedId) {
            option.style.display = 'block'; // 親カテゴリに対応する子カテゴリを表示
        } else {
            option.style.display = 'none'; // それ以外の子カテゴリを非表示
        }
    });
    
});

document.getElementById('childCategory').addEventListener('change', function() {
    var select = this;
    var selectedOption = select.options[select.selectedIndex];
    var selectedId = selectedOption.getAttribute('data-child-id');
    var grandOptions = document.querySelectorAll('#grandCategory option');
    var inputValue = this.value;
    
    document.getElementById('grandCategory').disabled = inputValue.trim() === '';

    
    grandOptions.forEach(function(option) {
        if (option.getAttribute('data-grand-id') === selectedId) {
            option.style.display = 'block'; // 親カテゴリに対応する子カテゴリを表示
        } else {
            option.style.display = 'none'; // それ以外の子カテゴリを非表示
        }
        const selectGrandElement = document.getElementById('grandCategory');
        
    });
    
});

document.getElementById('name').addEventListener('input', function() {
    var inputValue = this.value;
    document.getElementById('search-button').disabled = inputValue.trim() === '';
});
document.getElementById('brand').addEventListener('input', function() {
    var inputValue = this.value;
    document.getElementById('search-button').disabled = inputValue.trim() === '';
});
document.getElementById('page-number').addEventListener('input', function() {
    var inputValue = this.value;
    document.getElementById('submit-button').disabled = inputValue.trim() === '';
    document.getElementById('search-button').disabled = inputValue.trim() === '';
});

