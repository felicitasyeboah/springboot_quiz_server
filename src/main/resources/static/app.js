var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#userinfo").html("");
}

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        //console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/game', function (serverMessage) {
            showQuestion(serverMessage.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function questionRequest() {
    stompClient.send("/app/game");
}

function showQuestion(message) {
    var gameMessage = JSON.parse(message);
    $("#userinfo").append("<tr><td>" + "Kategorie: " + gameMessage.category + "<br>" +
                                       "Frage: " + gameMessage.question + "<br>" +
                                       "Antwort 1: " + gameMessage.answer[0] + "<br>" +
                                       "Antwort 2: " + gameMessage.answer[1] + "<br>" +
                                       "Antwort 3: " + gameMessage.answer[2] + "<br>" +
                                       "Antwort 4: " + gameMessage.answer[3] + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { questionRequest(); });
});