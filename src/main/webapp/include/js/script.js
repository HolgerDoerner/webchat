let output = document.getElementById("chatOutput");
let input = document.getElementById("chatInput");
let submit = document.getElementById("submit");

let getNickname = () => {
    let nickname = window.location.href.split('?')[1];
    nickname = nickname.split('=')[1];
    nickname = nickname.replace(' ', '_');
    return nickname;
}

let sendMsg = () => {
    output.value += '\n';
    output.value += '[' + new Date().toLocaleTimeString() + `] ${getNickname()}: ` + input.value;
    input.value = '';
}

input.onkeypress = e => {
    if (e.keyCode == 10) sendMsg();
}

submit.onclick = sendMsg;
