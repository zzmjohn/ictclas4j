package org.ictclas4j.remote;

public class SegRequest implements MsgBody{
	private String content;
	public SegRequest(){
		
	}
	
	public SegRequest(byte[] bs){
		parseBytes(bs);
	}
	public byte[] getBytes(){
		if(content!=null)
			return content.getBytes();
		return null;
	}
	
	public void parseBytes(byte[] b){
		if(b!=null){
			content=new String(b);
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
