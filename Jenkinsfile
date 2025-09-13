pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
    buildDiscarder(logRotator(numToKeepStr: '20'))
    // If you *have* the AnsiColor plugin, you can put:
    // ansiColor('xterm')
  }

  tools {
    jdk   'JDK'      // matches your screenshot
    maven 'maven'    // matches your screenshot
  }

  parameters {
    string(name: 'BASE_URL', defaultValue: 'https://seleniumbase.io/demo_page', description: 'Base URL for tests')
    booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser in headless mode')
    string(name: 'TAGS', defaultValue: '@demopage', description: 'Cucumber tag expression')
    string(name: 'THREADS', defaultValue: '4', description: 'TestNG DataProvider threads')
    choice(name: 'BROWSER', choices: ['chrome', 'firefox'], description: 'Browser under test')
  }

  environment {
    M2_REPO    = "${WORKSPACE}/.m2/repository"
    MAVEN_OPTS = "-Xmx1g -XX:+UseG1GC"
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Tool Versions') {
      steps {
        script {
          try {
            ansiColor('xterm') {
              sh '''
                echo "== Java / Maven =="
                java -version
                mvn -v
                echo "== Browser (best-effort) =="
                (google-chrome --version || chromium --version || "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome" --version || true) 2>/dev/null || true
                (firefox --version || true) 2>/dev/null || true
              '''
            }
          } catch (ignored) {
            sh '''
              echo "== Java / Maven =="
              java -version
              mvn -v
            '''
          }
        }
      }
    }

    stage('Test') {
      steps {
        script {
          def mvnCmd = """
            mvn -q -Dmaven.repo.local=${M2_REPO} \
              test \
              -DbaseUrl="${params.BASE_URL}" \
              -Dcucumber.filter.tags="${params.TAGS}" \
              -Dheadless=${params.HEADLESS} \
              -Dbrowser=${params.BROWSER} \
              -Ddataproviderthreadcount=${params.THREADS}
          """
          try {
            ansiColor('xterm') { sh mvnCmd }
          } catch (ignored) {
            sh mvnCmd
          }
        }
      }
    }

    stage('Reports') {
      steps {
        junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
        archiveArtifacts artifacts: '''
          **/target/**/cucumber*.json,
          **/target/cucumber-html-reports/**,
          **/target/surefire-reports/**,
          **/target/testng-results.xml
        '''.trim(), fingerprint: true, allowEmptyArchive: true

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
            echo 'HTML Publisher plugin not installed; skipping Cucumber HTML publish.'
          }
        }
      }
    }
  }

  post {
    always {
      script {
        try {
          cleanWs notFailBuild: true, patterns: [[pattern: '.m2/**', type: 'EXCLUDE']]
        } catch (ignored) {
          echo 'Workspace Cleanup plugin not installed; skipping clean.'
        }
      }
    }
  }
}
