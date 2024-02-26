document.onkeydown = function(evt) {
    var up = document.getElementById('username');
    var down = document.getElementById('password');
    var downDown = document.getElementById('repeatPassword');
    evt = evt || window.event;

    if (document.activeElement!=down && document.activeElement!=up && document.activeElement!=downDown){
        up.focus();
    } else if (document.activeElement==up){
        if (evt.keyCode == 38){
            downDown.focus();
        } else if (evt.keyCode == 40){
            down.focus();
        }
    } else if (document.activeElement==down){
        if (evt.keyCode == 38){
            up.focus();
        } else if (evt.keyCode == 40){
            downDown.focus();
        }
    } else if (document.activeElement==downDown){
        if (evt.keyCode == 38){
            down.focus();
        } else if (evt.keyCode == 40){
            up.focus();
        }
    }

    if (evt.keyCode == 27) {
        document.getElementById('back').click();
    }
};