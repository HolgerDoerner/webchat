let chatOutput = null;
let chatInput = null;
let userList = null;
let userList_legend = null;
let smileyPopup = null;
let imagePopup = null;
let urlPopup = null;

// read the cookie to get the username.
let decodedCookie = decodeURIComponent(document.cookie);
let cookie = decodedCookie.split(';');
let nickname = cookie[0].split('=')[1];

if (!nickname) window.location.replace('index.jsp');

// create the websocket-connection to the server-endpoint.
// TODO: change ws-adress bevor deploying to the server !!!!
//let socket = new WebSocket(`wss://10.100.5.15:8443/webchat/chat/${nickname}`); // production work
let socket = new WebSocket(`wss://10.100.5.15:8446/webchat/chat/${nickname}`); // development work
//let socket = new WebSocket(`wss://192.168.178.100:8446/webchat/chat/${nickname}`); // development home

// initializes the values when page is fully loaded.
window.onload = () => {
    chatOutput = document.getElementById('chatOutput');
    chatInput = document.getElementById('chatInput');
    userList = document.getElementById('userList');
    userList_legend = document.getElementById('userList-legend');
    smileyPopup = document.getElementById('smileyPopup-window');
    imagePopup = document.getElementById('imagePopup-window');
    urlPopup = document.getElementById('urlPopup-window');

    // set the initial size for the elements
    chatOutput.style.height = chatOutput.style.minHeight = chatOutput.style.maxHeight = chatOutput.offsetHeight + 'px';

    chatInput.onkeydown = onKeyDown;

    if (window.Notification || window.webkitNotifications || navigator.mozNotification && Notification.permission !== 'granted') {
        Notification.requestPermission();
    }
}

window.onfocus = () => {
    chatInput.focus();
}

// set max-height of chat-output-container on window resize
// otherwise scrolling won't work propperly.
window.onresize = () => {
    chatOutput.style.height = chatOutput.style.minHeight = chatOutput.style.maxHeight = chatOutput.offsetHeight + 'px';
}

// handler for inkomming messages.
// takes the JSON-string and parses it to an object.
socket.onmessage = event => {
    let message = JSON.parse(event.data);

    switch (message.subject) {
        case 'userlist':
            updateUserlist(message.content);
            break;

        default:
            chatOutput.innerHTML += `<br><b>[${message.timestamp}] <f style="color:red">${message.from}</f>:</b> `;
            
            if (message.subject === 'image') {
                chatOutput.innerHTML += `<br><img src="${message.content}" style="max-width: 700px; max-height: 700px">`
            }
            else if (message.subject === 'url') {
                chatOutput.innerHTML += `<a href="${message.content}" target="_blank">${message.content}</a>`
            }
            else {
                chatOutput.innerHTML += textToHTML(message.content);
            }

            chatOutput.scrollTop = chatOutput.scrollHeight;

            if (Notification.name && (Notification.permission === 'granted') && message.from !== 'server' && message.from !== nickname && !document.hasFocus()) {
                let notification = new Notification(`[${message.timestamp}] ${message.from}`, { body: message.content.substring(0,21) });
                notification.onclick = event => {
                    //event.preventDefault();
                    //window.focus();
                    parent.focus();
                    event.target.close();
                }

                // notification.onshow = event => {
                //     setTimeout(notification.close(), 40000);
                // }

                // notification.addEventListener('show', event => {
                //     setTimeout(notification.close(), 4000);
                // })

                // notification.addEventListener('click', event => {
                //     parent.focus();
                //     event.target.close();
                // })
            }
            break;
    }
}

// key-eventhandler for chat input box.
// sends message by pressing 'CTRL+ENTER'.
let onKeyDown = event => {

    if (event.ctrlKey && event.keyCode == 13) {
        sendMsg(null, null, null, null);
    }
}

// converts the message to JSON-string and sends it to the server
// over open WebSocket connection.
let sendMsg = (subject, from, to, content) => {

    let message = {
        timestamp: '',
        subject: '',
        from: '',
        to: '',
        content: ''
    }

    message.subject = subject ? subject : '';
    message.from = from ? from : nickname;
    message.to = to ? to : '';
    message.content = content ? content : chatInput.value;

    socket.send(JSON.stringify(message));

    chatInput.value = '';
    chatInput.focus();
}

// updates the userlist.
let updateUserlist = list => {
    
    let users = list.split(';');
    users.pop(); // removes empty element at end of list

    // write new header with updated usercount
    if (userList_legend.hasChildNodes()) {
        userList_legend.removeChild(userList_legend.firstChild);
    }
    
    let node = document.createElement('b');
    let textNode = document.createTextNode(`Users ( ${users.length} )`);
    node.appendChild(textNode);
    userList_legend.appendChild(node);

    // render updated userlist
    // userlist.value = '';

    // users.forEach(user => {
    //     userlist.value += user + '\n';
    // });

    userList.innerHTML = '';

    users.forEach(user => {
        userList.innerHTML += '<b>' + user + '</b><br>';
    })
}

//#region smiley popup
// handler for opening the smiley-popup window.
let openSmileyPopup = () => {
    smileyPopup.classList.toggle('show');
}

// handler for adding the selected smiley to the inputbox.
let addSmiley= (smileyId) => {
    let smiley = document.getElementById(smileyId).innerHTML;
    chatInput.value += smiley;
    chatInput.focus();
}
//#endregion smiley popup

//#region image popup
// handler for opening the image-popup window.
let openImagePopup = () => {
    imagePopup.classList.toggle('show');
    document.getElementById('imageUrlInput').focus();
}

// handler for sending the image-url.
let sendImageUrl = () => {
    let url = document.getElementById('imageUrlInput').value;
    document.getElementById('imageUrlInput').value = '';
    sendMsg('image', null, null, url);
    chatInput.focus();
}
//#endregion image popup

//#region url popup
// handler for opening the url-popup window.
let openUrlPopup = () => {
    urlPopup.classList.toggle('show');
    document.getElementById('urlInput').focus();
}

// handler for sending the url.
let sendUrl = () => {
    let url = document.getElementById('urlInput').value;
    document.getElementById('urlInput').value = '';
    sendMsg('url', null, null, url);
    chatInput.focus();
}
//#endregion image popup

// render HTML-output for the viewport
let textToHTML = text => {
    let output = ''
    let textArray = [];
    textArray = text.split('\n');

    textArray.forEach(line => {
        output += line + '<br>';
    })

    return output;
}
