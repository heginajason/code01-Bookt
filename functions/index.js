const functions = require('firebase-functions');

//initialize the app
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

//listens to when the node "Nnotifications" is written to. It's equivalent to 
//ref.addValueEventListener(....)
//
exports.sendNotification = functions.database.ref("Notifications")
.onWrite(event => {

	//We Store the value written to 'Norifications' in request
	var request =  event.data.val();

	//Payload will be sent to the device
	//Payload need atleast to have data or notification
	//field in it. 
	var payload = {
		data:{
			username: "Sample Notif",
			email: "heginajason@gmail.com"
		}
	};

	//This allos us to use FCM to send to the notification/message
	//to the device. token is the name of the device token
	//
	admin.messaging().sendToDevice(request.token, payload)
	.then(function(response){
		console.log("Successfully sent message: ", response);
	})
	.catch(function(error){
		console.log("Error sending message: ", error);
	})
});

