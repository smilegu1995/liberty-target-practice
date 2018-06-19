var leaders = [{
  name: "Libby",
  points: "124321"
}, {
  name: "Rosie",
  points: "28415"
}, {
  name: "Maven",
  points: "14231"
}, {
  name: "Marvin",
  points: "2321"
}, {
  name: "Martian",
  points: "1134"
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
  $.get("/SentryTargetChallenge/gameapp/game/leaderboard", function(data) {
    showFinalScore(data);
  });
}

function showFinalScore(data) {
  console.log("GET DATA: " + data);
  var score = JSON.parse(data);
  finalScore.textContent = score;
}

$("#startOverBtn").click(function() {
  pageRedirect();
});

function pageRedirect() {
  window.location.replace("index.html");
}
