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

        stage('Deploy') {
            steps {
                echo '=== JAR 파일 전송 및 배포 시작 ==='

                script {
                    def remoteIp = "3.35.27.116"
                    def remoteUser = "ubuntu" // EC2에 접속하는 사용자 이름
                    // 가장 최근 JAR 파일 이름 가져오기 (plain.jar 제외)
                    def jarFileName = sh(returnStdout: true, script: 'ls -t build/libs/ecommerce-*SNAPSHOT.jar | grep -v plain | head -n 1').trim()
                    def remoteAppDir = "/home/${remoteUser}/app" // 애플리케이션 설치 디렉토리

                    // SSH Agent 설정: 키를 Jenkins에 등록하여 사용합니다.
                    sshagent(credentials: ['AWS-keypair']) {
                        // 1. JAR 파일을 애플리케이션 서버로 전송 (strict host key checking 비활성화)
                        echo 'JAR 파일 전송 중...'
                        // scp 명령어에 StrictHostKeyChecking=no 옵션 추가
                        sh "scp -o StrictHostKeyChecking=no ${jarFileName} ${remoteUser}@${remoteIp}:${remoteAppDir}/${jarFileName.split('/').last()}"

                        // 2. SSH를 통해 원격 애플리케이션 서버에 접속하여 배포 스크립트 실행
                        echo '애플리케이션 배포 중...'
                        // ssh 명령어 전체를 작은따옴표로 감싸고, 원격 변수들은 백슬래시 이스케이프 처리
                        sh '''
                            ssh -o StrictHostKeyChecking=no ${remoteUser}@${remoteIp} "
                                cd ${remoteAppDir}
                                # 기존 애플리케이션 프로세스 종료 (pid.file이 없으면 그냥 넘어감)
                                if [ -f 'pid.file' ]; then
                                    PID=$(cat pid.file)
                                    echo '기존 프로세스 종료: $PID'
                                    kill $PID || true
                                    rm pid.file
                                fi

                                # 새로운 JAR 파일 백그라운드로 실행
                                # JAR 파일 경로를 정확하게 지정
                                nohup java -jar ${remoteAppDir}/${jarFileName.split('/').last()} --spring.profiles.active=dev > /dev/null 2>&1 &
                                echo \$! > pid.file
                            "
                        '''
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
