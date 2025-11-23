pipeline {
    agent any // 指定在任何可用的 agent 上执行

    tools {
        maven 'maven-3.9.11' // 必须与在 Jenkins 中配置的 Maven 名称一致
        jdk 'jdk8' // 必须与在 Jenkins 中配置的 JDK 名称一致
    }

    environment {
        // 可以在这里定义环境变量
        PROPERTY = "value"
    }

    stages {
        stage('Checkout') {
            steps {
                // 从 GitHub 拉取代码
                git branch: 'master',
                        credentialsId: 'GithubTokenForJenkins', // 替换为你在 Jenkins 中配置的凭据 ID
                        url: 'https://github.com/axuhan/demo-server.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
    }
}