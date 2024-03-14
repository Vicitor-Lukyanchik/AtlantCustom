document.onkeydown = function(evt) {
    var up = document.getElementById('login');
    var down = document.getElementById('password');
    evt = evt || window.event;
    if ((document.activeElement!=down && document.activeElement!=up) || evt.keyCode == 38) {
        up.focus();
    } else if (evt.keyCode == 40) {
        down.focus();
    }
};
