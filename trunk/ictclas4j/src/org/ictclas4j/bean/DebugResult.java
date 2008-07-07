package org.ictclas4j.bean;

import java.util.ArrayList;


public class DebugResult {
	private long startTime;

	private String rawContent;// 原始分词内容

	private ArrayList<MidResult> mrList;// 中间结果

	private SegResult segResult;//

	private static final long serialVersionUID = 10000L;

	public DebugResult(String rawContent) {
		this.rawContent = rawContent;
		startTime = System.currentTimeMillis();
	}

 
	public SegResult getSegResult() {
		return segResult;
	}


	public void setSegResult(SegResult segResult) {
		this.segResult = segResult;
	}


	public ArrayList<MidResult> getMrList() {
		return mrList;
	}

	public void setMrList(ArrayList<MidResult> mrList) {
		this.mrList = mrList;
	}

	public String getRawContent() {
		return rawContent;
	}

	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;
	}

	public long getSpendTime() {
		return System.currentTimeMillis() - startTime;
	}

	public void addMidResult(MidResult mr) {
		if (mrList == null)
			mrList = new ArrayList<MidResult>();
		if (mr != null)
			mrList.add(mr);
	}

 
	public long getStartTime() {
		return startTime;
	}

	public String toHTML() {
		StringBuffer html = new StringBuffer();

		if (rawContent != null) {
			html.append("原文内容：");
			html.append("<table border=\"1\" width=\"100%\"><tr><td width=\"100%\">");
			html.append(rawContent);
			html.append("</td></tr></table>");

			if (mrList != null) {
				for (MidResult mr : mrList) {
					html.append(mr.toHTML());
				}
			}

			if (segResult != null) {
				html.append("<p>最终分词结果：");
				html.append("<table border=\"1\" width=\"100%\"><tr><td width=\"100%\">");
				html.append("<font color=\"blue\" size=6><b>" + segResult.toString() + "</b></font>");
				html.append("</td></tr></table>");
			}
		}

		return html.toString();

	}
}
