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

	private static final String JIRA_URL = "http://jira.cengage.com";
	private static final String UID = "oceanuser";
	private static final String PWD = "oju0987";
	
	private static AsynchronousJiraRestClientFactory factory;
	private static JiraRestClient jiraRestClient;
	private static URI uri;
	private static String query;
	private SearchRestClient searchClient;
	
	public JiraClient() {
		factory = new AsynchronousJiraRestClientFactory();
		try {
			uri = new URI(JIRA_URL);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jiraRestClient = factory.createWithBasicHttpAuthentication(uri, UID, PWD);
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
