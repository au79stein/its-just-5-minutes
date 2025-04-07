pipelineJob('record-timestamp') {
  triggers {
    cron('*/5 * * * *')
  }
  definition {
    cps {
      script("""
        pipeline {
          agent {
            kubernetes {
              yaml '''
              apiVersion: v1
              kind: Pod
              spec:
                containers:
                  - name: psql
                    image: bitnami/postgresql:latest
                    command:
                      - cat
                    tty: true
              '''
            }
          }
          environment {
            PGPASSWORD = "mysecretpassword"
          }
          stages {
            stage('Insert Timestamp') {
              steps {
                container('psql') {
                  sh '''
                    psql -h postgres-postgresql -U jenkins -d jenkins_db -c "INSERT INTO jenkins_log DEFAULT VALUES;"
                  '''
                }
              }
            }
          }
        }
      """.stripIndent())
      sandbox(true)
    }
  }
}