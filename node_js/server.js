/* 
 *  API to handle adding/removing/getting skits from
 *  our elastic search database
 *
 *  Cameron Clark
 */

// Import express & body-parser libraries and create our app
var es_url = "search-elasticdosh-sibcapm4agjnhfauqqnqafwjqe.us-east-1.es.amazonaws.com";
var index = '{"index": {"_index": "skit", "_type":"act", "_id": 0}}';
var http = require('http');
var request = require('request');
var express = require('express');
var bodyParser = require('body-parser');
var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true}));


// Function to retrieve all skits of a specific user
function getSkitsList(id, callback){
    var query_path = "http://" + es_url + "/skit/_search?q=" + id;
    request(query_path, function(error, response, body) {
        if(!error && response.statusCode == 200){
            parsed = JSON.parse(body);
            callback(parsed, false);
        }
        else{
            callback(null, error);
        }
    });
}

// Function to retrieve all skits of a specific user
function getSkitsToRemove(id, skit_id, callback){
    var query_path = "http://" + es_url + "/skit/_search?q=" + id;
    request(query_path, function(error, response, body) {
        if(!error && response.statusCode == 200){
            parsed = JSON.parse(body);
            skit_list = parsed.hits.hits;
            for(var i = 0; i < skit_list.length; i++){
                console.log(skit_list[i]._source);
                var skit = skit_list[i]._source;
                console.log(skit["skit-id"]);
                if(skit["skit-id"].toString() === skit_id.toString()){
                    console.log("Deleting: " + skit.title);
                    var delete_url = "http://" + es_url + "/skit/act/" + skit_list[i]._id;
                    var options = {
                        url: delete_url,
                        method: 'DELETE'
                    }
                    request(options, function(error, response, body) {
                        console.log(response);
                        console.log(error);
                        if(!error && response.statusCode == 200){
                            console.log("Deleted");
                        }
                        else {
                            console.log("Not Deleted");
                        }
                    });
                }
            }
            callback(parsed, false);
        }
        else{
            callback(null, error);
        }
    });
}

// Adds a skit to our ES instance for the specified user
function addSkit(data, callback){
    var post_url = "http://" + es_url + "/skit/act";
    var skit = JSON.stringify(data);
    console.log(skit);
    request.post({url: post_url, body: skit}, function(error, response, body) {
        if(!error && response.statusCode == 200){
            parsed = JSON.parse(body);
            return callback(parsed, false);
        }
        else{
            return callback(null, error);
        }
    });
}

// Checks if the request is authenticated
function isAuth(token, callback){
    var url = "http://54.208.87.70/isAuthenticated";
    var params = "token=" + token;
    request.post({url: url, body: params}, function(error, response, body){
        if(!error && response.statusCode == 200){
            return callback("Valid", false);
        }
        else {
            return callback(null, error);
        }
    });
}

// Default route
app.get('/', function(req, res) {
    console.log(req.method);
    res.send('Suhh\n');
});


// Route for adding skits
app.post('/AddSkit', function(req, res) {
    console.log("Adding Skit...\n");
    //var token = req.body.token;
    var title = req.body.title;
    var body = req.body.body;
    var data = {};
    /*isAuth(token, function(error, valid){
        if(error) return res.send("Not Authenticated"); 
    });*/

    data['user-id'] = 1;
    data['skit-id'] = 4;
    data['title'] = title;
    data['body'] = body;
    addSkit(data, function(error, result){
        if(error) return res.send(error);
        res.send(result);
    });
});


// Route for removing skits
app.post('/RemoveSkit', function(req, res) {
    //var token = req.body.token;
    var skit_id = req.body.skit_id;
    var user_id = req.body.user_id;
    var skit_list = "";
    
    console.log(skit_id);
    /*isAuth(token, function(error, valid){
        if(error) return res.send("Not Authenticated"); 
    });*/
    getSkitsToRemove(user_id, skit_id, function(error, skits){
        if(error) return res.send(error);
        res.send(skits);
    });   
});


// Route for removing skits
app.get('/GetSkits', function(req, res) {
    console.log("Getting skits...\n");
    //var token = req.body.token;
    //console.log(token);
    /*isAuth(token, function(error, valid){
        if(error) return res.send("Not Authenticated"); 
    });*/
    var id = 1
    getSkitsList(id, function(error, skits){
        if(error) return res.send(error);
        res.send(skits);
    });
});

app.listen(8008);
console.log('Server running at http://127.0.0.1:8008/');
