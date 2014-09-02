import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.SearchRestClient;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

public class JiraClient {

	private String baseUrl;
	private String id;
	private String pwd;
	
	private static AsynchronousJiraRestClientFactory factory;
	private static JiraRestClient jiraRestClient;
	private static URI uri;
	private static String query;
	private SearchRestClient searchClient;
	
	public JiraClient(String url, String uid, String pswd) {
		
		this.baseUrl = url;
		this.id = uid;
		this.pwd = pswd;
		factory = new AsynchronousJiraRestClientFactory();
		
		try {
			uri = new URI(baseUrl);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jiraRestClient = factory.createWithBasicHttpAuthentication(uri, id, pwd);
		searchClient = getSearchClient(jiraRestClient);
	}

	public int retrieveIssueCount(String jql) throws InterruptedException, ExecutionException {
		Promise<SearchResult> results = getSearchResults(jql);
		int totalIssues = results.get().getTotal();
		System.out.println("Total issues found:  " + totalIssues);
		return totalIssues;
	}

	private SearchRestClient getSearchClient(JiraRestClient client){
		return jiraRestClient.getSearchClient();
	}
	
	private Promise<SearchResult> getSearchResults(String jql) {
		Promise<SearchResult> searchResults = searchClient.searchJql(jql,5,0); //limiting page size due to timeouts with default constructor
		return searchResults;
	}

	private static String retrieveIssueKey(BasicIssue issue) {
		System.out.println(issue.getKey());
		return issue.getKey();
	}

	private static URI retrieveIssueURI(BasicIssue issue) {
		System.out.println(issue.getSelf());
		return issue.getSelf();
	}

	private Iterable<BasicIssue> getIssues(SearchResult searchResult) {
		return searchResult.getIssues();
	}
	

//public static void main(String args[]) throws URISyntaxException,InterruptedException, ExecutionException {
//Example ex = new Example(JQL_OCEAN_P3_PRIORITY);
////Promise<SearchResult> searchJql = getSearchResults();
////int numberOfIssues = ex.getNumberOfIssues(searchJql);
////System.out.println("number of issues found:  " + numberOfIssues);
////Iterable<BasicIssue> issues = ex.getIssues(searchJql.get());
////Iterator<BasicIssue> iterator = issues.iterator();
////while (iterator.hasNext()) {
////	BasicIssue issue = iterator.next();
////	retrieveIssueKey(issue);
////	retrieveIssueURI(issue);
////}
//}


}
