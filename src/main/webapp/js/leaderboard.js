var leaders = [{
  name: "John Doe",
  points: "124321"
}, {
  name: "Test 1",
  points: "28415"
}, {
  name: "Test 2",
  points: "14231"
}, {
  name: "Test 3",
  points: "2321"
}, {
  name: "Test 4",
  points: "134"
}];
var x;
var finalScore = $("#finalScore").val();

for (x in leaders) {
  document.getElementById('board').innerHTML +=
    '<li class="rank">' +
    '<h2 class="name">' + leaders[x].name +
    '</h2>' +
    '<small class="pts">' + leaders[x].points +
    '</small></li>';
  console.log(leaders[x]);
}


// Run on the page load
$(endGame());

function endGame() {
  // Create EventSource object
  var source = new EventSource('/SentryTargetChallenge/gameapp/game/gamestream');

  source.onmessage = function(e) {
    showFinalScore(e);
  };
}

function showFinalScore(event) {
  console.log("EVENT DATA: " + event.data);
  var gameevent = JSON.parse(event.data);
  finalScore.textContent = gameevent.score;
}

$("#startOverBtn").click(function() {
  pageRedirect();
});

function pageRedirect() {
  window.location.replace("index.html");
}
