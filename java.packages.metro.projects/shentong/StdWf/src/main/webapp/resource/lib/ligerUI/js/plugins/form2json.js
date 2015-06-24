/** @serializedParams looks like "prop1=value1&prop2=value2".   
 Nested property like 'prop.subprop=value' is also supported **/ 
 function paramString2obj (serializedParams) {       
    var obj={}; 
    function evalThem (str) { 
	//var value = escape(decodeURIComponent(data.substr(li_pos+1)));
        var attributeName = str.split("=")[0]; 
//        var attributeValue = decodeURIComponent(str.split("=")[1]); 
//		attributeValue = encodeURI(encodeURI(attributeValue));
        var attributeValue = decodeURIComponent(str.split("=")[1],true);   


        if(!attributeValue){ 
            return ; 
        } 
          
        var array = attributeName.split("."); 
        for (var i = 1; i < array.length; i++) { 
            var tmpArray = Array(); 
            tmpArray.push("obj"); 
            for (var j = 0; j < i; j++) { 
                tmpArray.push(array[j]); 
            }; 
            var evalString = tmpArray.join("."); 
            // alert(evalString); 
            if(!eval(evalString)){ 
                eval(evalString+"={};");                 
            } 
        }; 
        eval("obj."+attributeName+"='"+attributeValue+"';"); 
          
    }; 
    var properties = serializedParams.split("&"); 
    for (var i = 0; i < properties.length; i++) { 
        evalThem(properties[i]); 
    }; 
    return obj; 
} 
  
  
$.fn.form2json = function(){ 
    var serializedParams = this.serialize(); 
    var obj = paramString2obj(serializedParams); 
	//
    alert(obj.invite);
    return JSON.stringify(obj); 
} 

function request(paras){  
	var url = location.href;   
	var paraString = url.substring(url.indexOf("?")+1,url.length).split("&");   
	var paraObj = {}   
	for (i=0; j=paraString[i]; i++){   
		paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length);   
	}   
	var returnValue = paraObj[paras.toLowerCase()];   
	if(typeof(returnValue)=="undefined"){   
		return "";   
	}else{   
		return returnValue;   
	}
}