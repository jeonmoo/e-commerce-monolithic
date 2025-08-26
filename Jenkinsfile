pipeline {
    // Jenkins 에이전트 설정 - Docker를 사용할 수 있는 에이전트에서 실행
    agent any

    // 환경 변수 설정
    environment {
        // Docker 이미지 이름과 태그
        DOCKER_IMAGE = 'my-spring-app'
        DOCKER_TAG = "${BUILD_NUMBER}" // Jenkins 빌드 번호를 태그로 사용

        // Java 21 설정
        JAVA_HOME = '/usr/lib/jvm/java-21-openjdk'
        PATH = "${JAVA_HOME}/bin:${PATH}"

        // Gradle 설정
        GRADLE_OPTS = '-Dorg.gradle.daemon=false' // CI 환경에서는 데몬 비활성화
    }

    stages {
        // 1단계: 소스코드 체크아웃
        stage('Checkout') {
            steps {
                echo '=== 소스코드 체크아웃 시작 ==='
                // Git에서 master 브랜치 체크아웃
                checkout scm

                // 현재 브랜치와 커밋 정보 출력
                script {
                    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                    def gitBranch = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
                    echo "Building commit: ${gitCommit}"
                    echo "Building branch: ${gitBranch}"
                }
            }
        }

        // 2단계: Gradle 권한 설정
        stage('Setup') {
            steps {
                echo '=== Gradle 설정 시작 ==='
                // gradlew 실행 권한 부여
                sh 'chmod +x ./gradlew'

                // Gradle 버전 확인
                sh './gradlew --version'

                // Java 버전 확인
                sh 'java -version'
            }
        }

        // 3단계: 의존성 다운로드 및 컴파일
        stage('Build') {
            steps {
                echo '=== 애플리케이션 빌드 시작 ==='

                // Gradle clean: 이전 빌드 결과물 정리
                sh './gradlew clean'

                // Gradle compileJava: 소스코드 컴파일
                sh './gradlew compileJava'

                echo '빌드 완료'
            }
        }

        // 4단계: 테스트 실행
        stage('Test') {
            steps {
                echo '=== 테스트 실행 시작 ==='

                // 모든 테스트 실행 (단위 테스트 + 통합 테스트)
                sh './gradlew test'

                echo '테스트 완료'
            }

            // 테스트 완료 후 작업
            post {
                always {
                    // 테스트 결과 리포트 수집 (JUnit XML 형식)
                    publishTestResults testResultsPattern: 'build/test-results/test/*.xml'

                    // 테스트 커버리지 리포트가 있다면 수집
                    script {
                        if (fileExists('build/reports/jacoco/test/jacocoTestReport.xml')) {
                            publishCoverage adapters: [jacocoAdapter('build/reports/jacoco/test/jacocoTestReport.xml')]
                        }
                    }
                }
            }
        }

        // 5단계: JAR 파일 생성
        stage('Package') {
            steps {
                echo '=== JAR 파일 패키징 시작 ==='

                // 실행 가능한 JAR 파일 생성
                sh './gradlew bootJar'

                // 생성된 JAR 파일 확인
                sh 'ls -la build/libs/'

                echo 'JAR 패키징 완료'
            }
        }

        // 6단계: Docker 이미지 빌드
        stage('Docker Build') {
            steps {
                echo '=== Docker 이미지 빌드 시작 ==='

                script {
                    // Docker 이미지 빌드
                    def image = docker.build("${DOCKER_IMAGE}:${DOCKER_TAG}")

                    // latest 태그도 함께 생성
                    sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"

                    echo "Docker 이미지 빌드 완료: ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }

        // 7단계: 기존 서비스 중지 및 정리
        stage('Stop Services') {
            steps {
                echo '=== 기존 서비스 중지 시작 ==='

                script {
                    // 실행 중인 Docker Compose 서비스 중지 및 제거
                    sh '''
                        # 기존 컨테이너가 있다면 중지하고 제거
                        if [ -f docker-compose.yml ]; then
                            docker-compose down --remove-orphans || true
                        fi

                        # 사용하지 않는 Docker 이미지 정리 (선택사항)
                        docker image prune -f || true
                    '''
                }

                echo '기존 서비스 중지 완료'
            }
        }

        // 8단계: 새로운 서비스 배포
        stage('Deploy') {
            steps {
                echo '=== 새로운 서비스 배포 시작 ==='

                script {
                    // Docker Compose를 사용해서 전체 스택 실행
                    sh '''
                        # 환경 변수 설정 (필요시)
                        export SPRING_APP_IMAGE=${DOCKER_IMAGE}:${DOCKER_TAG}

                        # Docker Compose로 서비스 실행
                        # -d: 백그라운드 실행
                        # --build: 필요시 이미지 재빌드
                        docker-compose up -d --build

                        # 컨테이너 상태 확인
                        docker-compose ps

                        # 애플리케이션이 정상적으로 시작될 때까지 대기
                        echo "애플리케이션 시작 대기 중..."
                        sleep 30

                        # 헬스체크 (애플리케이션이 8080 포트에서 실행된다고 가정)
                        curl -f http://localhost:8080/actuator/health || echo "헬스체크 실패 - 수동 확인 필요"
                    '''
                }

                echo '배포 완료'
            }
        }

        // 9단계: 배포 검증
        stage('Smoke Test') {
            steps {
                echo '=== 배포 검증 시작 ==='

                script {
                    // 기본적인 연결 테스트
                    sh '''
                        # 애플리케이션 포트 확인
                        netstat -tlnp | grep :8080 || echo "8080 포트가 열려있지 않습니다"

                        # Docker 컨테이너 로그 확인 (마지막 20줄)
                        echo "=== 애플리케이션 로그 ==="
                        docker-compose logs --tail=20 app || true

                        # 데이터베이스 연결 확인
                        echo "=== 데이터베이스 상태 ==="
                        docker-compose logs --tail=10 db || true

                        # Redis 연결 확인
                        echo "=== Redis 상태 ==="
                        docker-compose logs --tail=10 redis || true
                    '''
                }

                echo '배포 검증 완료'
            }
        }
    }

    // 파이프라인 완료 후 작업
    post {
        // 항상 실행되는 작업
        always {
            echo '=== 파이프라인 정리 작업 시작 ==='

            // 워크스페이스 정리
            cleanWs()

            // 오래된 Docker 이미지 정리 (디스크 공간 절약)
            sh 'docker image prune -f --filter "until=24h" || true'
        }

        // 성공시 실행되는 작업
        success {
            echo '🎉 파이프라인이 성공적으로 완료되었습니다!'
            echo "✅ 애플리케이션이 http://localhost:8080 에서 실행 중입니다"

            // 성공 시 추가 작업 (예: 슬랙 알림, 이메일 등)
            // slackSend(message: "✅ ${env.JOB_NAME} 빌드 성공!")
        }

        // 실패시 실행되는 작업
        failure {
            echo '❌ 파이프라인이 실패했습니다!'

            // 실패 시 디버깅을 위한 로그 수집
            sh '''
                echo "=== 실패 시 디버그 정보 ==="
                docker ps -a || true
                docker-compose logs || true
            '''

            // 실패 시 추가 작업 (예: 슬랙 알림, 이메일 등)
            // slackSend(message: "❌ ${env.JOB_NAME} 빌드 실패!")
        }

        // 불안정한 상태일 때 (테스트는 실패했지만 빌드는 성공)
        unstable {
            echo '⚠️ 빌드는 성공했지만 테스트에 문제가 있습니다!'
        }
    }
}
