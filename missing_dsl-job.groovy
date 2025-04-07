pipeline {
    agent {
        kubernetes {
            label "kubernetes"
            defaultContainer 'jnlp'
            containerTemplate {
                name 'jnlp'
                image 'jenkins/inbound-agent:latest'
                args '${JENKINS_SECRET} ${JENKINS_NAME}'
            }
        }
    }
    stages {
        stage('Insert Date and Time') {
            steps {
                script {
                    // JDBC connection details
                    def dbUrl = "jdbc:postgresql://postgres-postgresql:5432/postgres"
                    def dbUser = "postgres"
                    def dbPassword = System.getenv("POSTGRES_PASSWORD")
                    
                    // SQL to insert the current date and time
                    def sql = """
                        INSERT INTO datetime_table (current_time)
                        VALUES (CURRENT_TIMESTAMP);
                    """
                    
                    // Execute SQL
                    def db = Sql.newInstance(dbUrl, dbUser, dbPassword, 'org.postgresql.Driver')
                    db.execute(sql)
                    db.close()
                }
            }
        }
    }
    triggers {
        cron('H/5 * * * *') // Schedule to run every 5 minutes
    }
}

