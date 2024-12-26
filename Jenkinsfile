pipeline {
agent any
stages {
stage('Build') {
steps {
 git 'https://github.com/nguyenhuutruong123/goal.git'
}
}
stage('Clone stage'){

steps{
// This step should not normally be used in your script. Consult the inline help for details.
withDockerRegistry(credentialsId: 'docker-hub', url: 'https://index.docker.io/v1/') {
   sh 'docker build -t truongnh/goal-test:v10'
   sh 'docker push  truongnh/goal-test:v10'
}
}
}
}