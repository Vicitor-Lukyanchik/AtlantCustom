document.onkeydown = function(evt) {
    var input = document.getElementById('number');
    var find = document.getElementById('find');
    evt = evt || window.event;

    if (document.activeElement==input && evt.keyCode == 13) {
        find.click();
    } else if(evt.keyCode != 13){
        input.focus();
    } else if (evt.keyCode == 13) {
        find.click();
    }
};