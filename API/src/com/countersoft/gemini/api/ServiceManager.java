package com.countersoft.gemini.api;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.countersoft.gemini.api.dto.IssueCommentDto;
import com.countersoft.gemini.api.dto.IssueDto;
import com.countersoft.gemini.api.dto.ProjectDto;
import com.countersoft.gemini.api.dto.UserDto;
import com.countersoft.gemini.api.entity.IssueComment;
import com.countersoft.gemini.api.entity.IssuesFilter;

import org.apache.commons.codec.binary.Base64;

public class ServiceManager
{
	private String _url, _apiUrl, _username, _password, _apiKey;
	private boolean _windowsAuthentication;
	private String _authorization;
	private UserDto currentUser;
	private ItemService _itemservice;

	public static void main(String[] args) throws NoSuchAlgorithmException,
			UnsupportedEncodingException, InstantiationException,
			IllegalAccessException, Exception
	{		
		ServiceManager smngr = new ServiceManager("http://localhost:8080/gemini", "manager", "manager", "", false);
		MetaService ms = smngr.getMetaService();
		ItemService is = smngr.getItemService();
		ProjectService ps = smngr.getProjectService();
		ProjectDto[] projects = ps.GetProjects();
		IssuesFilter filter = new IssuesFilter();
		filter.IncludeClosed = false;
		filter.GroupDependencies = true;
		filter.Resources = String.format("%s", smngr.currentUser.BaseEntity.Id);
		filter.Projects = "All";
		IssueDto[] issues = is.GetFilteredIssues(filter);
		String ids = "";
		IssueComment comment = new IssueComment();
		comment.Comment = "Hallo vanuit eclipse";
		comment.IssueId = 36;
		comment.UserId = 2;
		comment.ProjectId = 17;
		IssueCommentDto cmt = is.CreateIssueComment(comment); 
		for(IssueDto issue: issues) {
			ids = String.format("%s\n%s", ids,issue.Id);
			
		}
//		projects[0].BaseEntity.
		System.out.println(ids);
	}

	public ServiceManager(String url, String username, String password,
			String apiKey, Boolean windowsAuthentication)
			throws NoSuchAlgorithmException, UnsupportedEncodingException,
			InstantiationException, IllegalAccessException, Exception
	{
		_url = url.trim();
		if (_url.endsWith("/"))
			_url = _url.substring(0, _url.length());

		_username = username.trim();
		_password = password == null ? "" : password.trim();
		_apiKey = apiKey == null ? "" : apiKey.trim();
		_windowsAuthentication = windowsAuthentication;

		_apiUrl = String.format("%s/api", _url);

		if (_password.length() > 0)
		{
			_authorization = getBase64(String.format("%s:%s", _username,
					getMD5Hash(_password)));
		}
		else
		{
			_authorization = getBase64(String
					.format("%s:%s", _username, _apiKey));
		}

/*		ItemService itemService = new ItemService(authorization, _apiUrl);
		Issue issue = new Issue();
		itemService.DeleteIssue(1774);
*/
		if (username.isEmpty())
		{
			throw new Exception("Specify username", null);
		}
		currentUser = this.WhoAmI();
	}

	public static String getMD5Hash(String value)
			throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest m = MessageDigest.getInstance("MD5");
		byte[] bytes = value.getBytes("UTF-8");

		return Base64.encodeBase64String(m.digest(bytes));
	}

	public static String getBase64(String value)
			throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		return Base64.encodeBase64String(value.getBytes("UTF-8"));
	}
	
	
	public ItemService getItemService() {
		if (_itemservice == null)
			_itemservice = new ItemService(_authorization, _apiUrl, _username);
		return _itemservice;
		
	}

	public ProjectService getProjectService() {
		return new ProjectService(_authorization, _apiUrl, _username);
		
	}

	public MetaService getMetaService() {
		return new MetaService(_authorization, _apiUrl, _username);
		
	}
	
	public UserDto WhoAmI() throws NoSuchAlgorithmException, UnsupportedEncodingException, InstantiationException, IllegalAccessException
	{
		if (this.currentUser == null)
		{
			if (this._windowsAuthentication)
			{
				this.currentUser = this.getItemService().GetResponse("users/usernamew", "POST", this._username, UserDto.class);
/*				RestRequest request = this.RestGateway.GetRequest("users/usernamew", Method.POST);
				request.AddBody(this.UserName);
				this.currentUser = this.GetResponse<UserDto>(request);*/
			}
			else
			{
			
			this.currentUser = this.getItemService().GetResponse("users/username/" + this._username, "GET", null, UserDto.class);
			}
			return this.currentUser;
		}
		return this.currentUser;
	}	

}