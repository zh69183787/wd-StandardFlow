package com.wonders.framework.attachment.configuration;

public enum AttachContentType {
	DEFUALT("application/octet-stream"),
	DOC("application/msword"),
	DTD("text/xml"),
	PDF("application/pdf"),
	PPT("applications-powerpoint"),
	RTF("application/msword"),
	SWF("application/x-shockwave-flash"),
	TIF("image/tiff"),
	TIFF("image/tiff"),
	VSD("application/vnd.visio"),
	VSS("application/vnd.visio"),
	VST("application/vnd.visio"),
	VSW("application/vnd.visio"),
	VSX("application/vnd.visio"),
	VTX("application/vnd.visio"),
	WAV("audio/wav"),
	XHTML("text/html"),
	XLS("application/x-xls"),
	XML("text/xml"),
	DOTX("application/vnd.openxmlformats-officedocument.wordprocessingml.template"),
	PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
	PPSX("application/vnd.openxmlformats-officedocument.presentationml.slideshow"),
	POTX("application/vnd.openxmlformats-officedocument.presentationml.template"),
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
	XLTX("application/vnd.openxmlformats-officedocument.spreadsheetml.template"),
	DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
	
	private final String contentType;
	private AttachContentType(String contentType){
		this.contentType = contentType;
	}
	public String getContentType() {
		return contentType;
	}
	
	public static String parseContentType(String fileExtName){
		if(fileExtName == null || fileExtName.length() ==0)
			return DEFUALT.getContentType();
		String fileName = null;
		AttachContentType act = null;
		if(fileExtName.startsWith("."))
			fileName = fileExtName.substring(1);
		fileName = fileName.toUpperCase();
		try{
			act = AttachContentType.valueOf(fileName);
			return act.getContentType();
		}catch(IllegalArgumentException e){
			 return DEFUALT.getContentType();
		}
	}
}
