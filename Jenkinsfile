pipeline {
    agent {
        label "nutanix"
    }

    tools {
        gradle 'Gradle 4.6'
    }

    stages {
        stage('Prepare') {
            steps {
                checkout scm
            }
        }
        stage('Build base') {
            steps {
                sh 'cd jira-assistant-base && gradle clean build test'
            }
        }

        stage('Build') {
            parallel {
                stage('Build accountprogress') {
                    steps {
                        sh 'cd jira-assistant-accountprogress && gradle clean build test'
                    }
                }
                stage('Build billing') {

                    steps {
                        sh 'cd jira-assistant-billing && gradle clean build test'
                    }
                }
                stage('Build estimationstatistics') {
                    steps {
                        sh 'cd jira-assistant-estimationstatistics && gradle clean build test'
                    }
                }
                stage('Build linksearch') {
                    steps {
                        sh 'cd jira-assistant-linksearch && gradle clean build test'
                    }
                }
                stage('Build projectsetup') {
                    steps {
                        sh 'cd jira-assistant-projectsetup && gradle clean build test'
                    }
                }
                stage('Build projectstatus') {
                    steps {
                        sh 'cd jira-assistant-projectstatus && gradle clean build test'
                    }
                }
                stage('Build reopenfactor') {
                    steps {
                        sh 'cd jira-assistant-reopenfactor && gradle clean build test'
                    }
                }
                stage('Build sprintanalysis') {
                    steps {
                        sh 'cd jira-assistant-sprintanalysis && gradle clean build test'
                    }
                }
                stage('Build sprintforecast') {
                    steps {
                        sh 'cd jira-assistant-sprintforecast && gradle clean build test'
                    }
                }
                stage('Build supportanalysis') {
                    steps {
                        sh 'cd jira-assistant-supportanalysis && gradle clean build test'
                    }
                }
            }
        }

        stage('Build kanbananalysis') {
            steps {
                sh 'cd jira-assistant-kanbananalysis && gradle clean build test'
            }
        }

        stage('Build restservices') {
            steps {
                sh 'cd jira-assistant-restservices && gradle clean build test'
            }
        }

        stage('Build documentation') {
            steps {
                sh 'gradle docs'
            }
        }

        stage('Finalizing') {
            steps {
                script {
                    def scannerHome = tool 'SonarQube Scanner 2.8';
                    withSonarQubeEnv('default') {
                        sh "${scannerHome}/bin/sonar-scanner"
                    }
                }
                junit testResults: 'jira-assistant-base/build/test-results/test/*.xml,jira-assistant-accountprogress/build/test-results/test/*.xml,jira-assistant-supportanalysis/build/test-results/test/*.xml,jira-assistant-sprintanalysis/build/test-results/test/*.xml,jira-assistant-restservices/build/test-results/test/*.xml,jira-assistant-kanbananalysis/build/test-results/test/*.xml,jira-assistant-sprintforecast/build/test-results/test/*.xml,jira-assistant-reopenfactor/build/test-results/test/*.xml,jira-assistant-projectstatus/build/test-results/test/*.xml,jira-assistant-projectsetup/build/test-results/test/*.xml,jira-assistant-linksearch/build/test-results/test/*.xml,jira-assistant-estimationstatistics/build/test-results/test/*.xml,jira-assistant-billing/build/test-results/test/*.xml'
            }
        }
    }
}
