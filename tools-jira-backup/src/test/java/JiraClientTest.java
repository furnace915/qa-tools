import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JiraClientTest {

	private JiraClient jira;
	private String query;

	@Before
	public void setUp() throws Exception {
		jira = new JiraClient();
	}

	@Test
	public void retrieveProjectIssueCount() throws Exception {
		query = "project = \"Ocean - Search Engine\"";
		assertThat(jira.retrieveIssueCount(query), not(lessThan(1577)));
	}

	@Test
	public void retrieveP1IssueCount() throws Exception {
		query = "project = \"Ocean - Search Engine\" AND issuetype = \"Production Bug\""
				+ " AND Status not in (Closed) AND priority = \"P1 Escalation\"";
		assertThat(jira.retrieveIssueCount(query), not(greaterThan(0)));
	}

	@After
	public void tearDown() throws Exception {

	}

}
