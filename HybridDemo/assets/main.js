$('#submit').bind('click', function(){
  
	//VALIDATION
	var getproject_name = $('#project_name').val();
	var getproject_url = $('#project_url').val();
	var getproject_notes = $('#project_notes').val();
	var getclient_email = $('#client_email').val();
	var getclient_phone = $('#client_phone').val();
	
	//required
	if(getproject_name == "" || getproject_name == "Operation Green"){ 
		$('#project_name').css('border', '3px solid yellow');
		return false;
	}
	//required
	if(getproject_url == "" || getproject_url == "http://www.yoursite.com"){ 
		$('#project_url').css('border', '3px solid yellow');
		return false;
	}
	//not required
	if(getproject_notes == "" || getproject_notes == "The most amazing project evar."){ 
		return true;
	}
	//not required
	if(getclient_phone == "" || getclient_phone == "8765309"){
		return true;
	}
	//not required
	if(getclient_email == "" || getclient_email == "name@email.com"){ 
		return true;
	}else{
		$('#project_name').css('border', '1px solid #ccc');
		$('#project_url').css('border', '1px solid #ccc');
		$('#project_notes').css('border', '1px solid #ccc');
		$('#client_email').css('border', '1px solid #ccc'); 
		$('#client_phone').css('border', '1px solid #ccc');	
		
		//All Form Fields have Content
		//Save Object to Parse
		/*
		//test object for reference
		var TestObject = Parse.Object.extend("TestObject");
		var testObject = new TestObject();
		testObject.save({foo: "bar"}, {
		  success: function(object) {
			alert("Yay, Monica! This is Working.");
		  }
		});
		*/	
		var ProjectObject = Parse.Object.extend("ProjectObject");
		var projectObject = new ProjectObject();	
		//"db_column", "dynamic data from the html5 form"
		projectObject.set("project_name", getproject_name);
		projectObject.set("project_url", getproject_url);
		projectObject.set("project_notes", getproject_notes);
		projectObject.set("client_phone", getclient_phone);
		projectObject.set("client_email", getclient_email);
		//projectObject.save(null,
		projectObject.save({getproject_name: "project_name"},{project_url: "getproject_url"},{project_notes: "getproject_notes"},{client_phone: "getclient_phone"},{client_email: "getclient_email"}, 
			{success: function(projectObject) {
			// Execute logic that should take place after object is saved.
			alert('New Object Created:' + projectObject.id);
		  },
		  error: function(projectObject, error) {
			// Execute logic that should take place if save fails.
			// error is a Parse.Error with error code and description.
			alert('Failed to create new projectObject. Error Code: ' + error.description);
		  }
		});
	}	
});
	

$(document).ready(function(){
	//Parse.com Permission Required
	//mdf3 project 
	//Parse.initialize("LGG1MbCVOrLbi6Hr6lXxiWTMoqEwjGQX4RAjjMCN", "XINCv5XuXUffmWtVGO41TXRf1P0SFowjSfay9L7C");
	//hybrid demo
	Parse.initialize("LGG1MbCVOrLbi6Hr6lXxiWTMoqEwjGQX4RAjjMCN", "JDK7gh8CIpUmyrroxnMlGwlg2EcVYCwCLgiqm4vZ");
	/*
	//Save Test Parse Object
	var TestObject = Parse.Object.extend("TestObject");
	var testObject = new TestObject();
	testObject.save({foo: "bar"}, {
	  success: function(object) {
    	alert("yay! it worked");
	  }
	});
	
	*/
	
	function showData() {
		var data = Android.getData(); 
		data = JSON.parse(data);
		window.alert("Data: " + data + "; first = " + data[0]);
	};
		
	function setData() {
		//android.setData('[getproject_name,getproject_url,getproject_notes,getclient_phone,getclient_email]');
		Android.setData('[project_name,project_url,project_notes,client_phone,client_email]');
		window.alert("Data Updated. Tap Home icon to view.");
	};
	
	function showAndroidCamera(context) {	
		Android.showCamera(context);
	};

	function showAndroidData(data) {
		//android cookbook http://androidcookbook.oreilly.com/Recipe.seam;jsessionid=99BC32BA7F9CC12ADCB8AF4E71C4D05B?recipeId=4426
		var project_name = $('#project_name').val();
		var project_url = $('#project_url').val();
		var project_notes = $('#project_notes').val();
		var client_phone = $('#client_phone').val();
		var client_email = $('#client_email').val();
		var array = [project_name, project_url, project_notes, client_phone, client_email];
		//var array = [getproject_name, getproject_url, getproject_notes, getclient_phone, getclient_email];
		Android.showData(array);
	};


});