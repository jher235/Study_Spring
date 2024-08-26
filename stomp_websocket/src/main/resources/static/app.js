const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws'
    // brokerURL: 'http://localhost:8080/ws'
});

const privateStompClient = new StompJs.Clients({
})

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        showGreeting(JSON.parse(greeting.body).content);
    });
};

privateStompClient.onConnect = (frame) => {
    const roomId = localStorage.getItem("roomId")
    setPrivateConnect(true);
    console.log('PrivateConnected: ' + frame);
    stompClient.subscribe(`/topic/`+roomId, (message) => {
        showPrivateMessage(JSON.parse(message.body).content);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setPrivateConnect(connected, id){
    $("#private-connect").prop("disabled", connected);
    $("#private-disconnect").prop("disabled", !connected);
    if (connected) {
        $("#private-conversation").show();
    }
    else {
        $("#private-conversation").hide();
    }
    $("#private-greetings").html("");
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function privateConnect(roomId) {
    localStorage.setItem("roomId", roomId);
    // const privateStompClient = new StompJs.Clients({
    //     brokerURL: `ws://localhost:8080/ws/${roomid}`
    // })
    // privateStompClient.brokerURL(`ws://localhost:8080/ws/${roomId}`);
    privateStompClient.brokerURL(`ws://localhost:8080/ws`);
    privateStompClient.activate()
}

function connect() {
    stompClient.activate();
}

function privateDisconnect() {
    privateStompClient.deactivate();
    setPrivateConnect(false);
    console.log("Disconnected");
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'name': $("#name").val()})
    });
}

function sendPrivateMessage(){
    const roomId = localStorage.getItem("roomId")
    privateStompClient.publish({
        destination: "/app/message",
        body: JSON.stringify({'message': $("#message").val()})
    });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function showPrivateMessage(message) {
    $("#messages").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
    $( "#private-connect" ).click(() => {
        privateConnect($("room-id").val())
    });
    $( "#private-disconnect" ).click(() => privateDisconnect());
    $( "#private-send" ).click(() => sendPrivateMessage());
    // $()
});