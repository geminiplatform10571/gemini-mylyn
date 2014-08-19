package com.countersoft.gemini.api.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IssueComment extends BaseEntity
{

	public int IssueId;
	public int ProjectId;
	public int UserId;
	public String Comment;
	public String Fullname;
	public boolean IsClosing;
	public int Visibility;
	public IssueAttachment[] Attachments;

	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public IssueComment(){
		
		Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm:ss");
        String currentDateTime = (dateFormat.format(now) + "T" + timeFormat.format(now));
         
        Created = currentDateTime;
        Revised = currentDateTime;
        Visibility = 1;
        Comment = new String();
        Fullname = new String();
        Attachments = new IssueAttachment[0];
	}
}
