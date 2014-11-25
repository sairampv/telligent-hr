/**
 * Author Spothu
 */
function numbersonly(e, decimal,value) {

	var key;
	var keychar;

	if (window.event) {
		key = window.event.keyCode;
	}
	else if (e) {
		key = e.which;
	}
	else {
		return true;
	}
	keychar = String.fromCharCode(key);

	if ((key==null) || (key==0) || (key==8) ||  (key==9) || (key==13) || (key==27) ) {
		return true;
	}
	else if ((("0123456789").indexOf(keychar) > -1)) {
		return true;
	}
	else if (decimal && (keychar == ".")) { 
		for (var i=0;i<value.length;i++){
			if(value.charAt(i)==keychar){
				return false;
				break;
			}
		}
		return true;
	}
	else
		return false;
}

//function to accept just 0-9 nos
// Number Validation
function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}
//End Number Validation		


