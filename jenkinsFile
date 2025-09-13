pipeline {
  agent any

  options {
    ansiColor('xterm')
    timestamps()
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '20'))
  }

  tools {
    jdk   'JDK17'     // adjust if you named it differently
    maven 'Maven3'    // adjust if you named it differently
  }

  parameters {
    string(name: 'BASE_URL', defaultValue: 'https://seleniumbase.io/demo_page', description: 'Base URL for tests')
    booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    string(name: 'TAGS', defaultValue: '@demopage', description: 'Cucumber tag expression')
    string(name: 'THREADS', defaultValue: '4', description: 'TestNG DataProvider threads')
    choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser under test')
  }

  environment {
    // keep a local Maven cache inside the workspace for faster builds
    M2_REPO   = "${WORKSPACE}/.m2/repository"
    MAVEN_OPTS = "-Xmx1g -XX:+UseG1GC"
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Tool Versions') {
      steps {
        sh '''
          echo "== Java / Maven =="
          java -version
          mvn -v
          echo "== Browser =="
          google-chrome --version || chromium --version || firefox --version || true
        '''
      }
    }

    stage('Test') {
      steps {
        sh """
          mvn -q -Dmaven.repo.local=${M2_REPO} \
            test \
            -DbaseUrl="${params.BASE_URL}" \
            -Dcucumber.filter.tags="${params.TAGS}" \
            -Dheadless=${params.HEADLESS} \
            -Dbrowser=${params.BROWSER} \
            -Ddataproviderthreadcount=${params.THREADS}
        """
      }
    }

    stage('Reports') {
      steps {
        // Parse surefire/JUnit-style XML (produced even with TestNG)
        junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'

        // Archive common outputs: cucumber JSON, HTML reports, surefire, TestNG XML
        archiveArtifacts artifacts: '''
          **/target/**/cucumber*.json,
          **/target/cucumber-html-reports/**,
          **/target/surefire-reports/**,
          **/target/testng-results.xml
        '''.trim(), fingerprint: true, allowEmptyArchive: true

        // Publish cucumber HTML if HTML Publisher plugin exists
        script {
          try {
            if (fileExists('target/cucumber-html-reports/overview-features.html')) {
              publishHTML(target: [
                reportDir: 'target/cucumber-html-reports',
                reportFiles: 'overview-features.html',
                reportName: 'Cucumber HTML'
              ])
            }
          } catch (ignored) {
            echo 'HTML Publisher plugin not installed; skipping pretty HTML publish.'
          }
        }
      }
    }
  }

  post {
    always {
      script {
        // Keep the .m2 cache; clean the rest if plugin exists
        try {
          cleanWs notFailBuild: true, patterns: [[pattern: '.m2/**', type: 'EXCLUDE']]
        } catch (ignored) {
          echo 'Workspace Cleanup plugin not installed; skipping clean.'
        }
      }
    }
  }
}
