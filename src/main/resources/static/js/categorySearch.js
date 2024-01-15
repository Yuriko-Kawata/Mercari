document.getElementById('page-number').addEventListener('input', function() {
    var inputValue = this.value;
    document.getElementById('submit-button').disabled = inputValue.trim() === '';
});