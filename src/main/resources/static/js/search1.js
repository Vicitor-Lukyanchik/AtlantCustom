Sass.compile(document.querySelector("style[type=scss]").innerHTML,function(res){
    var s=document.createElement("style");
    s.innerHTML=res.text;
    res.formatted&&console.error(res.formatted); //if error console.error it.
    res.text&&document.head.appendChild(s);// append style element only if no error.
});

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