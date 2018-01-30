package chisw.work.mantisbt.test;

import chisw.work.mantisbt.model.Issue;
import chisw.work.mantisbt.model.Project;
import org.testng.annotations.Test;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class SoapTests extends TestBase{

    @Test
    public void testGetProject() throws MalformedURLException, ServiceException, RemoteException {
        Set<Project> projects =  app.soap().getProject();
        System.out.println(projects.size());
        for (Project project : projects) {
            System.out.println(project.getName());
        }
    }

    @Test
    public void testCreateIssue() throws MalformedURLException, ServiceException, RemoteException {
        Set<Project> projects = app.soap().getProject();
        Issue issue = new Issue().withSummary("Test issue")
                .withDescription("Test issue description").withProject(projects.iterator().next());
        Issue created = app.soap().addIssue(issue);
        assertEquals(issue.getSummary(), created.getSummary());
    }

}
