let chatOutput = null;
let chatInput = null;
let userlist = null;

window.onload = () => {
    chatOutput = document.getElementById("chatOutput");
    chatInput = document.getElementById("chatInput");
    userlist = document.getElementById("userlist")
}

let decodedCookie = decodeURIComponent(document.cookie);
let cookie = decodedCookie.split(';');
let nickname = cookie[0].split('=')[1];

let socket = new WebSocket(`ws://10.100.5.15:8080/webchat/chat/${nickname}`);

socket.onmessage = event => {
    let message = JSON.parse(event.data);

    switch (message.subject) {
        case "userlist":
            updateUserlist(message.content);
            break;

        default:
            chatOutput.value += '\n' + '[' + (new Date().toLocaleTimeString()) + '] ' + message.from + ': ' + message.content;
            chatOutput.scrollTop = chatOutput.scrollHeight;
            break;
    }
}

let onKeyDown = event => {

    if (event.ctrlKey && event.keyCode == 13) {
        sendMsg();
    }
}

let sendMsg = () => {

    let message = {
        from: '',
        to: '',
        content: ''
    }

    message.from = nickname;
    message.content = chatInput.value;

    socket.send(JSON.stringify(message));

    chatInput.value = '';
    chatInput.focus();
}

let updateUserlist = list => {

    let users = list.split(';');

    userlist.value = '';

    users.forEach(user => {
        if (user !== '' | null) {
            userlist.value += user + '\n';
        }
    });
}
