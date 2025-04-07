job('WriteDateToPostgres') {
    description('Job to write the current date and time to PostgreSQL every 5 minutes')

    triggers {
        cron('H/5 * * * *')  // This will run the job every 5 minutes
    }

    steps {
        shell('''
            #!/bin/bash
            export PGPASSWORD=$(kubectl -n postgres get secret postgres-password -o jsonpath="{.data.postgres-password}" | base64 --decode)
            psql -h postgres-postgresql -U postgres -d my_database -c "INSERT INTO datetime_table (current_time) VALUES (CURRENT_TIMESTAMP);"
        ''')
    }
}

