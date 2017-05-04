/* 
 *  API to handle adding/removing/getting skits from
 *  our elastic search database
 *
 *  Cameron Clark
 */

// Import express & body-parser libraries and add create our app
var express = require('express');
var bodyParser = require('body-parser');
var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true});


// Checks if the request is authenticated
function isAuth(token){
    var url = "http://54.208.87.70/isAuthenticated";
    var params = "token=" + token;
    xhttp = XMLHttpRequest();

    xhttp.onreadstatechange = function(){
        if(xhttp.status == 200){
            console.log("Good");
            console.log(xhttp.responseText);
            return true;
        }
        else{
            console.log("Bad");
            console.log(xhttp.responseText);
            return false;
        }
    }
    xhttp.open("POST", url, true);
    xhttp.send(params);
    return false;
}

// Default route
app.get('/', function(req, res) {
    console.log(req.method);
    res.send('Suhh\n');
});


// Route for adding skits
app.get('/AddSkit', function(req, res) {
    var token = req.body.token;
    console.log(token);
    var check = isAuth(token);

    res.send('Adding\n');
});


// Route for removing skits
app.get('/RemoveSkit', function(req, res) {
    var token = req.body.token;
    console.log(token);
    var check = isAuth(token);
    
    res.send('Removing\n');
});


// Route for removing skits
app.get('/GetSkits', function(req, res) {
    var token = req.body.token;
    console.log(token);
    var check = isAuth(token);
 
    res.send('Removing\n');
});

app.listen(8008);
console.log('Server running at http://127.0.0.1:8008/');
