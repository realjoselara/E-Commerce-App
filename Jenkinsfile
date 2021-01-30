pipeline {
    agent any

    stages {
        stage ('compile') {
            steps {
                    sh 'mvn clean compile'
            }
        }

        stage ('Testing Stage') {
            steps {
                    sh 'mvn test'
            }
        }
    }
}