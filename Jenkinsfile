pipeline {
    agent any

    stages {
        // 1단계: 소스코드 체크아웃
        stage('Checkout') {
            steps {
                echo '=== 소스코드 체크아웃 시작 ==='
                checkout scm
            }
        }

        // 2단계: Gradle 권한 설정
        stage('Setup') {
            steps {
                echo '=== Gradle 설정 시작 ==='
                sh 'chmod +x ./gradlew'
            }
        }

        // 3단계: 테스트
        stage('Test') {
            steps {
                echo '=== 애플리케이션 클린 및 테스트 시작 ==='
                sh './gradlew clean test'
            }
            post {
                always {
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        // 4단계: 빌드
        stage('Build') {
            steps {
                echo '=== 애플리케이션 빌드 시작 ==='
                sh './gradlew build -x test'
            }
        }

        // 5단계: JAR 파일 전송 및 배포
        stage('Deploy') {
            steps {
                echo '=== JAR 파일 전송 및 배포 시작 ==='

                script {
                    def remoteIp = "3.35.27.116"
                    def remoteUser = "ubuntu" // EC2에 접속하는 사용자 이름 (기본값: ubuntu)
                    def jarFileName = sh(returnStdout: true, script: 'ls build/libs/*SNAPSHOT.jar').trim()

                    // SSH Agent 설정: 키를 Jenkins에 등록하여 사용합니다.
                    sshagent(credentials: ['AWS-keypair']) {
                        // 1. JAR 파일을 애플리케이션 서버로 전송
                        echo 'JAR 파일 전송 중...'
                        sh "scp -o StrictHostKeyChecking=no ${jarFileName} ${remoteUser}@${remoteIp}:/home/${remoteUser}/"

                        // 2. SSH를 통해 원격 애플리케이션 서버에 접속하여 배포 스크립트 실행
                        echo '애플리케이션 배포 중...'
                        sh """
                            ssh -o StrictHostKeyChecking=no ${remoteUser}@${remoteIp} '
                                # 기존 애플리케이션 프로세스 종료
                                if [ -f "pid.file" ]; then
                                    if [ -s "pid.file" ]; then
                                        PID=\$(cat pid.file)
                                        echo "기존 프로세스 종료: \$PID"
                                        kill \$PID || true
                                    fi
                                    rm -f pid.file
                                fi

                                # 새로운 JAR 파일 백그라운드로 실행
                                nohup java -jar /home/${remoteUser}/\$(ls -t /home/${remoteUser}/*.jar | head -n 1) --spring.profiles.active=dev > /dev/null 2>&1 &
                                echo \$! > /home/${remoteUser}/pid.file

                                echo "배포 완료: PID \$(cat /home/${remoteUser}/pid.file)"
                            '
                        """
                    }
                }
                echo '배포 완료'
            }
        }
    }

    post {
        always {
            echo '=== 파이프라인 정리 작업 시작 ==='
            cleanWs()
        }
        success {
            echo '🎉 파이프라인이 성공적으로 완료되었습니다!'
        }
        failure {
            echo '❌ 파이프라인이 실패했습니다!'
        }
    }
}
