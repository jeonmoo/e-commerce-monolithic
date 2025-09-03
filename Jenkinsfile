pipeline {
    agent any

    //environment {
        //JAVA_HOME = '/usr/lib/jvm/java-21-openjdk-amd64'
        //PATH = "${JAVA_HOME}/bin:${PATH}"
    //}

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
                sh './gradlew clean test' // `clean`과 `build`를 한 번에 실행
            }
            post {
                always {
                    // 테스트 결과 리포트 수집
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        // 4단계: 빌드
        stage('Build') {
            steps {
                echo '=== 애플리케이션 빌드 및 테스트 시작 ==='
                sh './gradlew build -x test' // 테스트 제외하고 빌드
            }
        }

        // 5단계: 애플리케이션 배포
        stage('Deploy') {
            steps {
                echo '=== 애플리케이션 배포 시작 ==='
                script {
                    // 기존 애플리케이션 프로세스 종료
                    sh '''
                        if [ -f "pid.file" ]; then
                            PID=$(cat pid.file)
                            echo "기존 프로세스 종료: $PID"
                            kill $PID || true
                            rm pid.file
                        fi
                    '''
    
                    // 새로운 JAR 파일 백그라운드로 실행
                    sh '''
                        echo "새로운 애플리케이션 시작"
                        nohup java -jar build/libs/*.jar > app.log 2>&1 &
                        echo $! > pid.file
                    '''
                    
                    // 10초 대기
                    sleep 10
                    
                    // 애플리케이션 로그 출력
                    echo '=== 애플리케이션 로그 확인 ==='
                    sh 'tail -n 100 app.log'
                }
                echo '배포 완료'
            }
        }
    }

    // 파이프라인 완료 후 작업
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
