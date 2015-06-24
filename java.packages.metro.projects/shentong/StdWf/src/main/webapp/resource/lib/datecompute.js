function getWorkData(start, end) {

	
	var interval = getIntervalBetweenTwoDates(start, end);
	// 转换string为Date
	start = convertString2Date(start);
	var year = start.getFullYear();
	var month = start.getMonth();
	var date = start.getDate();
	--date;

	var subdaynum = 0;
	var workdaynum = 0;
	for ( var i = 0; i <= interval; ++i) {
		++date;

		var theDate = new Date(year, month, date);
		var day = theDate.getDay();
		if (0 == day || 6 == day) // non-working day
		{
		} else {
			workdaynum++;
		}
		subdaynum++;
	}

	return subdaynum + ',' + workdaynum;
}

// 将string转换为Date
// str必须满足如下格式: 2008-04-29
function convertString2Date(str) {
	// 用户split分隔出数组,将包含3个元素:年,月,日
	var splitArray = str.split("-");

	// 用年,月,日构造日期对象
	// splitArray[0]-> year, splitArray[1]-> month, splitArray[2]->day
	// 这时要注意月份是从0开始的
	var date = new Date(splitArray[0], splitArray[1] - 1, splitArray[2]);

	return date;
}

// 得到date1和date2之间的间隔
// date2要比date1大
// date要满足如下格式: 2008-04-29
// 参数date1和date2类型应该是String或Date
// 确保两种类型没有问题
function getIntervalBetweenTwoDates(date1, date2) {
	var realDate1 = date1;
	var realDate2 = date2;

	// 如果date1是Date类型就不用转换
	if (!(date1 instanceof Date)) {
		realDate1 = convertString2Date(date1);
	}
	// 如果date2是Date类型就不用转换
	if (!(date2 instanceof Date)) {
		realDate2 = convertString2Date(date2);
	}

	// 得到绝对值,(realDate2.getTime() - realDate1.getTime())返回的是毫秒所以要先除1000
	var interval = (realDate2.getTime() - realDate1.getTime())
			/ (1000 * 60 * 60 * 24);

	return interval;
}

//+---------------------------------------------------
//| 求两个时间的天数差 日期格式为 YYYY-MM-dd
//+---------------------------------------------------
function daysBetween(DateOne,DateTwo)
{
var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));
var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);
var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));

var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-'));
var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1);
var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));

var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);
return Math.abs(cha);
} 