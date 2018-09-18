let chatOutput;
let chatInput;
let userList;
let userList_legend;
let smileyPopup;
let imagePopup;
let urlPopup;

// for markdown-it provided over CDN
let markdownIt;

// read the cookie to get the username.
let decodedCookie = decodeURIComponent(document.cookie);
let cookie = decodedCookie.split(';');
let nickname = cookie[0].split('=')[1];

// if user is not logged in properly -> redirekt to login page
if (!nickname) window.location.replace('index.jsp');

// create the websocket-connection to the server-endpoint.
// TODO: change ws-adress bevor deploying to the server !!!!
//let wsServer = `wss://10.100.5.15:8443/webchat/chat/${nickname}`; // production work
let wsServer = `wss://10.100.5.15:8446/webchat/chat/${nickname}`; // development work
//let wsServer = `wss://192.168.178.100:8446/webchat/chat/${nickname}`; // development home
let socket = new WebSocket(wsServer);

// ------------------------------------------------------
// WebSocket error-handler
// ------------------------------------------------------
socket.addEventListener('error', event => {

    // TODO: make something usefull here...
    alert('WebSocket connection error! Trying to reconnect ...');

    socket = null;
    socket = new WebSocket(wsServer);
})

// ------------------------------------------------------
// initializes the values when page is fully loaded.
// ------------------------------------------------------
window.onload = () => {

    // making markdown-it available (with plugin(s))
    markdownIt = window.markdownit().use(window.markdownitEmoji);

    chatOutput = document.getElementById('chatOutput');
    chatInput = document.getElementById('chatInput');
    userList = document.getElementById('userList');
    userList_legend = document.getElementById('userList-legend');
    smileyPopup = document.getElementById('smileyPopup-window');
    imagePopup = document.getElementById('imagePopup-window');
    urlPopup = document.getElementById('urlPopup-window');

    // setting initial font-size of the in- and output
    chatOutput.style.fontSize = '15px';
    document.getElementById('showActualOutputFontSize').innerHTML = chatOutput.style.fontSize;
    chatInput.style.fontSize = '15px';
    document.getElementById('showActualInputFontSize').innerHTML = chatInput.style.fontSize;

    // event-handler for sending messages with 'Enter'
    chatInput.onkeypress = onKeyPress;

    // request permission to display desktop-notifications.
    //
    // TODO: replace this implementation with a propper Service-Worker in the future...
    //
    if (window.Notification || window.webkitNotifications || navigator.mozNotification && Notification.permission !== 'granted') {
        Notification.requestPermission();
    }

    // set initial height for input-box
    chatInput.style.width = Number.parseInt(getComputedStyle(document.getElementById('input-container')).width) - 60 + 'px';
}

// ------------------------------------------------------
// automaticxl set focus on input-box
// ------------------------------------------------------
window.onfocus = () => {
    chatInput.focus();
}

// ------------------------------------------------------
// set new width of input-box when window gets resized
// ------------------------------------------------------
window.onresize = () => {
    chatInput.style.width = Number.parseInt(getComputedStyle(document.getElementById('input-container')).width) - 60 + 'px';
}

// ------------------------------------------------------
// WebSocket-handler for inkomming messages.
// takes the JSON-string and parses it to an object.
// ------------------------------------------------------
socket.addEventListener('message', event => {
    let message = JSON.parse(event.data);

    switch (message.subject) {
        case 'userlist':
            updateUserlist(message.content);
            break;

        default:
            // process the incomming message and render HTML
            displayMessage(message);

            // auto-scroll for chat window
            chatOutput.scrollTop = chatOutput.scrollHeight;

            // display an desktop-notification on new messages and only if the chat-frontend doesn't has focus
            //
            // TODO: furture replacement with service-worker
            //
            if (Notification.name && (Notification.permission === 'granted') && message.from !== 'server' && message.from !== nickname && !document.hasFocus()) {
                let notification = new Notification(`[${message.timestamp}] ${message.from}`, { body: message.content.substring(0,31) });
                notification.onclick = event => {
                    event.preventDefault();
                    event.target.close();
                    window.focus();
                }
            }
            break;
    }
})

// ------------------------------------------------------
// key-eventhandler for chat input box.
// sends message by pressing 'CTRL+ENTER'.
// ------------------------------------------------------
let onKeyPress = event => {
    if (event.keyCode == 13 && document.getElementById('selectSendMethod').checked == true) {
        sendMsg(null, null, null, null);
        event.preventDefault();
    }
}

// ------------------------------------------------------
// converts the message to JSON-string and sends it to the server
// over open WebSocket connection.
// ------------------------------------------------------
let sendMsg = (subject, from, to, content) => {

    if (chatInput.innerText.toLowerCase() === '/clear') {
        chatOutput.innerHTML = '';
        chatInput.innerText = '';
        //event.preventDefault();
        chatInput.focus();
    }
    else {
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
        message.content = content ? content : chatInput.innerText;
    
        socket.send(JSON.stringify(message));
    
        chatInput.innerText = '';
        chatInput.focus();
    }
}

// ------------------------------------------------------
// renders the messsage on the output-area
// ------------------------------------------------------
let displayMessage = (message) => {

    let id = Math.floor(Math.random() * 5000);

    let output = '<hr style="width: 100%; height: 20px; background-color: lightgrey; border: 0px; margin: 0px; padding: 0px">';
    output += `<b style="color: grey">Message from <i style="color: orangered">${message.from}</i> on <i>${message.timestamp}</i> &nbsp; <span id="messageToggle-${id}" style="cursor: pointer" onclick=toggleShow(${id})>&#x25B2;</span></b>`;
    output += `<div class="message" id="${id}">`;
    
    // translate the markdown-syntax to HTML
    output += markdownIt.render(message.content);
    output += '</div>';

    chatOutput.innerHTML += output;

    // call highlight.js
    window.hljs.initHighlighting.called = false;
    window.hljs.configure({tabReplace: '    '});
    window.hljs.initHighlighting();
}

// ------------------------------------------------------
// updates the userlist.
// ------------------------------------------------------
let updateUserlist = list => {
    
    let users = list.split(';');
    users.pop(); // removes empty element at end of list

    userList_legend.innerHTML = `<img alt="Userlist" title="Userlist" src="include/img/users1-512x512.png" width="30px" height="30px" style="float: left">&nbsp;&nbsp;${users.length}`;

    userList.innerHTML = '';

    users.forEach(user => {
        userList.innerHTML += '<b>' + user + '</b><br>';
    })
}

// ------------------------------------------------------
// toggle display of the options-menu
// ------------------------------------------------------
let toggleOptions = () => {

    document.getElementById('optionsContent').classList.toggle('optionsContent-toggle');
}

// ------------------------------------------------------
// change the font-size of the output view
// ------------------------------------------------------
let changeOutputFontSize = action => {

    let currentFontSize = Number.parseInt(chatOutput.style.fontSize);

    if (currentFontSize > 10 && action == '-') {
        let newFontSize = currentFontSize + -1 + 'px';
        document.getElementById('showActualOutputFontSize').innerHTML = chatOutput.style.fontSize = newFontSize;
    }
    if (currentFontSize < 25 && action == '+') {
        let newFontSize = currentFontSize + 1 + 'px';
        document.getElementById('showActualOutputFontSize').innerHTML = chatOutput.style.fontSize = newFontSize;
    }
}

// ------------------------------------------------------
// change the font-size of the input field
// ------------------------------------------------------
let changeInputFontSize = action => {

    let currentFontSize = Number.parseInt(chatInput.style.fontSize);

    if (currentFontSize > 10 && action == '-') {
        let newFontSize = currentFontSize + -1 + 'px';
        document.getElementById('showActualInputFontSize').innerHTML = chatInput.style.fontSize = newFontSize;
    }
    if (currentFontSize < 20 && action == '+') {
        let newFontSize = currentFontSize + 1 + 'px';
        document.getElementById('showActualInputFontSize').innerHTML = chatInput.style.fontSize = newFontSize;
    }
}

// ------------------------------------------------------
// toggles the visibility of individual messages
// ------------------------------------------------------
let toggleShow = id => {

    document.getElementById(id).classList.toggle('message-toggle');

    if (document.getElementById(id).offsetHeight === 0 && document.getElementById(id).offsetWidth === 0) {
        document.getElementById(`messageToggle-${id}`).innerHTML = '&#x25BC;';
    }
    else {
        document.getElementById(`messageToggle-${id}`).innerHTML = '&#x25B2;';
    }
}
