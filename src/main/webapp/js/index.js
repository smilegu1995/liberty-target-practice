$("#startBtn").click(function() {
  window.location.replace("terminal.html");
  initializeGame();
});

function initializeGame() {
  // To Do
  var user = $("#userName").val();

  $.ajax({
    type: "POST",
    url: "/SentryTargetChallenge/gameapp/game",
    data: user,
    success: success,
    dataType: "text"
  });
}

function success() {
  console.log("game started!");
}
