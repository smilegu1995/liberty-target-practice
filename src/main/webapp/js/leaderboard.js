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

for (x in leaders) {
  document.getElementById('board').innerHTML +=
    '<li class="rank">' +
    '<h2 class="name">' + leaders[x].name +
    '</h2>' +
    '<small class="pts">' + leaders[x].points +
    '</small></li>';
  console.log(leaders[x]);
}

$("#startOverBtn").click(function() {
  pageRedirect();
});

function pageRedirect() {
  window.location.replace("index.html");
}
