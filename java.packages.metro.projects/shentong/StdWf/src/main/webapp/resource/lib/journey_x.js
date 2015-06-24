 function getParmsUrl(obj, keyName, keyValue){
	var rStr = "";
	for(var i=0; i < obj.length; ++i){
		rStr += encodeURIComponent(obj[i][keyName]);
		rStr += "=";
		rStr += encodeURIComponent(obj[i][keyValue]);
		rStr += "&";
	}
	return rStr + "1=1";
}