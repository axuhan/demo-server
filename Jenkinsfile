pipeline {
    agent any // 指定在任何可用的 agent 上执行

    tools {
        maven 'maven-3.9.11' // 必须与在 Jenkins 中配置的 Maven 名称一致
        jdk 'jdk8' // 必须与在 Jenkins 中配置的 JDK 名称一致
    }

    environment {
        // 可以在这里定义环境变量
        ENV = "prod"
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

        stage('Deploy') {
            steps {
                script {
                    def serverBatches = getServerBatches()
                    def batchNo = 0;
                    for(batch in serverBatches) {
                        batchNo++;
                        def deployWeb = input(
                                id: UUID.randomUUID().toString(),
                                message: "开始部署到 批次 ${batchNo}？",
                                ok: '开始部署',
                                parameters: [
                                        choice(
                                                name: 'ACTION',
                                                choices: ['deploy', 'skip', 'abort'],
                                                description: '选择操作'
                                        ),
                                        string(
                                                name: 'VERSION',
                                                defaultValue: 'latest',
                                                description: '部署版本'
                                        )
                                ],
                                submitter: 'admin,web-team'
                        )
                        if (deployWeb.ACTION == 'deploy') {
                            echo "开始部署 批次 ${batchNo}"
                            // 实际部署逻辑
                            deployBatchServers_v2(batch)
                        } else if (deployWeb.ACTION == 'skip') {
                            echo "跳过 批次 ${batchNo} 部署"
                        } else {
                            error "用户中止了 批次 ${batchNo} 的部署"
                        }
                    }
                }
            }
        }
    }
}

def static getServerBatches() {
    return [
            ["host.docker.internal"]
    ]
}

def deployBatchServers_v2(servers) {
    for(server in servers) {
        sh """
            scp -i /var/jenkins_home/ssh_key/id_rsa deploy.sh bootstrap/target/bootstrap-0.0.1-SNAPSHOT.jar root@${server}:/root/deployments
            ssh -i /var/jenkins_home/ssh_key/id_rsa root@${server} 'bash /root/deployments/deploy.sh'
        """
    }
}