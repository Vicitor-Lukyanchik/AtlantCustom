Sass.compile(document.querySelector("style[type=scss]").innerHTML,function(res){
    var s=document.createElement("style");
    s.innerHTML=res.text;
    res.formatted&&console.error(res.formatted); //if error console.error it.
    res.text&&document.head.appendChild(s);// append style element only if no error.
});