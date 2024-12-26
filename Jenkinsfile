pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                git 'https://github.com/nguyenhuutruong123/goal.git'
            }
        }
        stage('Clone stage') {
            steps {
                // Using Docker Registry for build and push
                withDockerRegistry(credentialsId: 'docker-hub', url: 'https://index.docker.io/v1/') {
                    sh 'docker build -t truongnh/goal-test:v10 .'
                    sh 'docker push truongnh/goal-test:v10'
                }
            }
        }
    }
}
