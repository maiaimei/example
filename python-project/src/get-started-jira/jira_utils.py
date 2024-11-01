import logging

import requests
from jira import JIRA
from requests.auth import HTTPBasicAuth

# JIRA服务器的配置
JIRA_URL = 'https://your-jira-server.com'
JIRA_USERNAME = 'your-username'
JIRA_PASSWORD = 'your-password'


def get_story_task(story_key):
  # Get details of the story
  story_url = f'{JIRA_URL}/rest/api/2/issue/{story_key}'
  response = requests.get(story_url,
                          auth=HTTPBasicAuth(JIRA_USERNAME, JIRA_PASSWORD))
  if response.status_code == 200:
    story_data = response.json()
    # Extract all question keys from the story
    subtasks_keys = [subtask['key'] for subtask in
                     story_data['fields']['subtasks']]

    # Search for all related tasks
    tasks_url = f'{JIRA_URL}/rest/api/2/search'
    params = {
      'jql': f"parent = {story_key}"
    }
    response = requests.get(tasks_url, params=params,
                            auth=HTTPBasicAuth(JIRA_USERNAME, JIRA_PASSWORD))
    if response.status_code == 200:
      tasks_data = response.json()
      # Calculate the number of tasks
      task_count = len(tasks_data['issues'])
      print(f"Story {story_key} 下有 {task_count} 个Task。")
    else:
      print(f"Failed to retrieve tasks for Story {story_key}.")
  else:
    print(f"Failed to retrieve Story {story_key} details.")


def get_related_issue_by_id(issue_id):
  try:
    # Use username and password authentication
    jira = JIRA(JIRA_URL, basic_auth=(JIRA_USERNAME, JIRA_PASSWORD))
    issue = jira.issue(issue_id)
    if issue:
      print(f"Issue details for {issue.key}:")
      print(f"Summary: {issue.fields.summary}")
      print(f"Description: {issue.fields.description}")
      print(f"Status: {issue.fields.status.name}")
      print(f"Type: {issue.fields.issuetype.name}")
      # Get associated issues
      related_issues = jira.search_issues(
          'project = "PROJECT_KEY" AND issuelinks INCOMING "{issue_id}"'.format(
              issue_id=issue_id))
      for related_issue in related_issues:
        print(f"Related Issue: {related_issue.key}")
    else:
      return {"error": f"Issue {issue_id} not found."}
  except Exception as e:
    logging.error(f"An error occurred: {e}")
    return {"error": str(e)}
