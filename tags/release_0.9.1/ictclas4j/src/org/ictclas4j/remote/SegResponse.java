package org.ictclas4j.remote;

public class SegResponse implements MsgBody{
	private String segedContent;
	
	public SegResponse(){
		
	}
	
	public SegResponse(byte[] b){
		parseBytes(b);
	}
	public byte[] getBytes(){
		if(segedContent!=null)
			return segedContent.getBytes();
		return null;
	}
	
	public void parseBytes(byte[] b){
		if(b!=null){
			segedContent=new String(b);
		}
	}

	public String getSegedContent() {
		return segedContent;
	}

	public void setSegedContent(String segedContent) {
		this.segedContent = segedContent;
	}
	
	
}
