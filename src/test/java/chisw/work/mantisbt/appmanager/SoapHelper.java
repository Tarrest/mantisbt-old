package chisw.work.mantisbt.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import chisw.work.mantisbt.model.Issue;
import chisw.work.mantisbt.model.Project;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SoapHelper {

    private ApplicationManager app;


    public SoapHelper(ApplicationManager app) {
        this.app = app;
    }

    public Set<Project> getProject() throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mcp = getMantisConnect();
        ProjectData[] projects = mcp.mc_projects_get_user_accessible("administrator", "root");
        return Arrays.asList(projects).stream().map((p) -> new Project().withId(p.getId().intValue()).withName(p.getName())).collect(Collectors.toSet());
    }

    private MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
        return new MantisConnectLocator()
                    .getMantisConnectPort(new URL(app.getProperty("soap.baseUrl")));
    }

    public Issue addIssue(Issue issue) throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mcp = getMantisConnect();
        String[] categories = mcp.mc_project_get_categories("administrator", "root", BigInteger.valueOf(issue.getProject().getId()));
        IssueData issueData = new IssueData();
        issueData.setSummary(issue.getSummary());
        issueData.setDescription(issue.getDescription());
        issueData.setProject(new ObjectRef(BigInteger.valueOf(issue.getProject().getId()), issue.getProject().getName()));
        issueData.setCategory(categories[0]);
        BigInteger issueId = mcp.mc_issue_add("administrator", "root", issueData);
        IssueData createdIssueData = mcp.mc_issue_get("administrator", "root", issueId);
        return new Issue().withId(createdIssueData.getId().intValue())
                .withSummary(createdIssueData.getSummary()).withDescription(createdIssueData.getDescription())
                .withProject(new Project().withId(createdIssueData.getProject().getId().intValue()).withName(createdIssueData.getProject().getName()));
    }
}
