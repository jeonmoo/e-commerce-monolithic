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
                    def remoteIp = "3.37.61.179"
                    def remoteUser = "ubuntu"
                    def jarFileName = sh(returnStdout: true, script: 'ls build/libs/*SNAPSHOT.jar').trim()
                    def jarBaseName = sh(returnStdout: true, script: 'basename ' + jarFileName).trim()

                    // SSH Agent 설정
                    sshagent(credentials: ['AWS-keypair']) {
                        // 1. JAR 파일을 애플리케이션 서버로 전송
                        echo "JAR 파일 전송 중: ${jarFileName} -> ${jarBaseName}"
                        sh "scp -o StrictHostKeyChecking=no ${jarFileName} ${remoteUser}@${remoteIp}:/home/${remoteUser}/${jarBaseName}"

                        // 2. 배포 스크립트 실행
                        echo '애플리케이션 배포 중...'
                        sh """
                            ssh -o StrictHostKeyChecking=no ${remoteUser}@${remoteIp} '
                                set -e  # 오류 발생시 스크립트 중단

                                JAR_FILE="/home/${remoteUser}/${jarBaseName}"
                                PID_FILE="/home/${remoteUser}/app.pid"
                                LOG_FILE="/home/${remoteUser}/app.log"

                                echo "=== 기존 애플리케이션 종료 ==="
                                if [ -f "\$PID_FILE" ]; then
                                    if [ -s "\$PID_FILE" ]; then
                                        OLD_PID=\$(cat \$PID_FILE)
                                        echo "기존 프로세스 종료: \$OLD_PID"
                                        kill -15 \$OLD_PID || true  # SIGTERM으로 우아한 종료 시도
                                        sleep 5
                                        # 여전히 실행 중이면 강제 종료
                                        if kill -0 \$OLD_PID 2>/dev/null; then
                                            echo "강제 종료 실행"
                                            kill -9 \$OLD_PID || true
                                        fi
                                    fi
                                    rm -f \$PID_FILE
                                fi

                                # 기존 로그 파일 백업
                                if [ -f "\$LOG_FILE" ]; then
                                    mv \$LOG_FILE \$LOG_FILE.old
                                fi

                                echo "=== 새로운 애플리케이션 시작 ==="
                                echo "JAR 파일: \$JAR_FILE"

                                # JAR 파일 존재 확인
                                if [ ! -f "\$JAR_FILE" ]; then
                                    echo "오류: JAR 파일을 찾을 수 없습니다: \$JAR_FILE"
                                    exit 1
                                fi

                                # 애플리케이션 시작
                                nohup java -jar "\$JAR_FILE" --spring.profiles.active=dev > "\$LOG_FILE" 2>&1 &
                                NEW_PID=\$!
                                echo \$NEW_PID > "\$PID_FILE"

                                echo "애플리케이션 시작됨: PID=\$NEW_PID"

                                # 애플리케이션이 정상적으로 시작되었는지 확인
                                echo "=== 애플리케이션 시작 확인 ==="
                                sleep 10

                                if kill -0 \$NEW_PID 2>/dev/null; then
                                    echo "✅ 애플리케이션이 정상적으로 실행 중입니다 (PID: \$NEW_PID)"

                                    # 로그에서 시작 관련 정보 출력 (있다면)
                                    echo "=== 최근 로그 (마지막 10줄) ==="
                                    tail -10 "\$LOG_FILE" || echo "로그 파일이 아직 생성되지 않았습니다."

                                    # Spring Boot 애플리케이션의 경우 포트 확인 (선택사항)
                                    echo "=== 포트 사용 확인 ==="
                                    netstat -tlnp 2>/dev/null | grep ":8080 " || echo "8080 포트가 아직 바인딩되지 않았습니다."
                                else
                                    echo "❌ 애플리케이션 시작 실패!"
                                    echo "=== 오류 로그 ==="
                                    cat "\$LOG_FILE" 2>/dev/null || echo "로그 파일이 생성되지 않았습니다."
                                    rm -f "\$PID_FILE"
                                    exit 1
                                fi

                                echo "배포 완료!"
                            '
                        """
                    }
                }
                echo '✅ 배포가 성공적으로 완료되었습니다!'
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
