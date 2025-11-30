# 🚀 Hướng dẫn Setup CI/CD cho Vegana Shop Automation Tests

## 📋 Mục lục

- [Tổng quan](#-tổng-quan)
- [GitHub Actions](#-github-actions)
- [Jenkins](#-jenkins)
- [Cấu hình](#-cấu-hình)
- [Troubleshooting](#-troubleshooting)

---

## 🎯 Tổng quan

CI/CD pipeline tự động:
- ✅ Chạy tests khi có code push/PR
- ✅ Build và test project
- ✅ Generate và lưu reports
- ✅ Archive artifacts (screenshots, logs, reports)
- ✅ Notification khi tests fail

---

## 🔵 GitHub Actions

### Setup GitHub Actions

1. **Tạo workflow file** (đã có sẵn):
   - `.github/workflows/ci-cd.yml` - Pipeline đầy đủ
   - `.github/workflows/simple-ci.yml` - Pipeline đơn giản

2. **Push code lên GitHub:**
   ```bash
   git add .github/workflows/
   git commit -m "Add CI/CD pipeline"
   git push origin main
   ```

3. **Xem kết quả:**
   - Vào tab **Actions** trên GitHub repository
   - Xem logs và artifacts

### Workflow Files

#### 1. `ci-cd.yml` - Pipeline đầy đủ

**Tính năng:**
- ✅ Chạy tests trên nhiều browsers (matrix strategy)
- ✅ Cache Maven dependencies
- ✅ Setup MySQL database tự động
- ✅ Start Spring Boot app tự động
- ✅ Archive test results và reports
- ✅ Publish test results

**Trigger:**
- Push vào `main` hoặc `develop`
- Pull Request
- Manual trigger (workflow_dispatch)

#### 2. `simple-ci.yml` - Pipeline đơn giản

**Tính năng:**
- ✅ Chạy tests cơ bản
- ✅ Upload reports
- ✅ Scheduled runs (mỗi ngày 2h sáng)

**Sử dụng khi:**
- Cần pipeline đơn giản, nhanh
- Không cần test trên nhiều browsers

### Cấu hình GitHub Actions

#### Thêm Secrets (nếu cần)

1. Vào **Settings** → **Secrets and variables** → **Actions**
2. Thêm secrets nếu cần:
   - `SLACK_WEBHOOK` - Cho Slack notification
   - `EMAIL_PASSWORD` - Cho email notification

#### Setup MySQL trong GitHub Actions

Thêm MySQL service vào workflow:

```yaml
services:
  mysql:
    image: mysql:8.0
    env:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: vegana_store
    ports:
      - 3306:3306
    options: >-
      --health-cmd="mysqladmin ping"
      --health-interval=10s
      --health-timeout=5s
      --health-retries=3
```

#### Customize Workflow

Chỉnh sửa `.github/workflows/ci-cd.yml`:

```yaml
strategy:
  matrix:
    java-version: [17]
    browser: [chrome, firefox] # Thêm browsers
```

---

## 🟠 Jenkins

### Setup Jenkins

#### 1. Cài đặt Jenkins

**Windows:**
```bash
# Download Jenkins từ https://www.jenkins.io/download/
# Chạy installer và cài đặt
```

**Docker:**
```bash
docker run -p 8080:8080 -p 50000:50000 jenkins/jenkins:lts
```

#### 2. Cài đặt Plugins

Vào **Manage Jenkins** → **Manage Plugins**, cài:
- Maven Integration
- TestNG Results
- HTML Publisher
- Email Extension

#### 3. Cấu hình Tools

Vào **Manage Jenkins** → **Global Tool Configuration**:
- **JDK**: Cài đặt JDK 17
- **Maven**: Cài đặt Maven 3.8+

#### 3.1. Setup MySQL Database

**Yêu cầu:**
- MySQL 8.0+ đã được cài đặt trên Jenkins server
- Database `vegana_store` đã được tạo
- Username: `root`
- Password: `123456`

**Tạo database (nếu chưa có):**
```sql
CREATE DATABASE IF NOT EXISTS vegana_store;
```

**Hoặc import từ file SQL:**
```bash
mysql -u root -p123456 < vegana.sql
```

**Kiểm tra kết nối:**
```bash
mysql -u root -p123456 -e "USE vegana_store; SHOW TABLES;"
```

#### 4. Tạo Pipeline Job

1. **New Item** → Chọn **Pipeline**
2. **Pipeline Definition**: Chọn **Pipeline script from SCM**
3. **SCM**: Chọn **Git**
4. **Repository URL**: URL của Git repository
5. **Script Path**: `Jenkinsfile`
6. **Save** và **Build Now**

### Jenkinsfile Structure

```groovy
pipeline {
    agent any
    environment {
        DB_HOST = 'localhost'
        DB_PORT = '3306'
        DB_NAME = 'vegana_store'
        DB_USERNAME = 'root'
        DB_PASSWORD = '123456'
    }
    stages {
        stage('Checkout') { ... }
        stage('Build') { ... }
        stage('Start Application') { ... }
        stage('Run Tests') { ... }
        stage('Generate Reports') { ... }
    }
    post {
        always { ... }
        success { ... }
        failure { ... }
    }
}
```

### Cấu hình Jenkins

#### Thêm Credentials (nếu cần)

1. **Manage Jenkins** → **Manage Credentials**
2. Thêm credentials cho:
   - Git repository (nếu private)
   - Email SMTP
   - Slack webhook

#### Email Notification

Thêm vào `Jenkinsfile`:

```groovy
post {
    failure {
        emailext (
            subject: "Test Failed: ${env.JOB_NAME}",
            body: "Tests failed. Check: ${env.BUILD_URL}",
            to: "team@example.com"
        )
    }
}
```

---

## ⚙️ Cấu hình

### Environment Variables

Tạo file `.env` (không commit vào Git):

```bash
BASE_URL=http://localhost:8080
BROWSER=chrome
TEST_USER_ID=testuser001
TEST_USER_PASSWORD=123456

# MySQL Database Configuration
DB_HOST=localhost
DB_PORT=3306
DB_NAME=vegana_store
DB_USERNAME=root
DB_PASSWORD=123456
```

### Test Configuration

Chỉnh sửa `src/test/resources/test.properties`:

```properties
base.url=${BASE_URL}
browser=${BROWSER}
```

### CI/CD Specific Settings

Tạo file `ci-test.properties`:

```properties
# CI/CD specific settings
base.url=http://localhost:8080
browser=chrome
implicit.wait=15
page.load.timeout=60

# Database settings
db.host=localhost
db.port=3306
db.name=vegana_store
db.username=root
db.password=123456
```

### MySQL Database Configuration

**Thông tin kết nối:**
- **Host**: `localhost`
- **Port**: `3306`
- **Database**: `vegana_store`
- **Username**: `root`
- **Password**: `123456`

**Lưu ý:**
- Đảm bảo MySQL đã được cài đặt và đang chạy
- Database `vegana_store` đã được tạo
- User `root` có quyền truy cập database
- Import file `vegana.sql` để tạo schema và dữ liệu mẫu

---

## 🔍 Troubleshooting

### GitHub Actions

#### Lỗi: "Application not starting"

**Giải pháp:**
- Tăng sleep time trong workflow
- Kiểm tra port 8080 có bị chiếm không
- Kiểm tra MySQL đã sẵn sàng chưa
- Thêm health check endpoint

```yaml
- name: Wait for MySQL
  run: |
    timeout 60 bash -c 'until mysqladmin ping -h 127.0.0.1 -uroot -p123456 --silent; do sleep 2; done'

- name: Wait for application
  run: |
    timeout 60 bash -c 'until curl -f http://localhost:8080; do sleep 2; done'
```

#### Lỗi: "Cannot connect to MySQL"

**Giải pháp:**
- Kiểm tra MySQL service đã start chưa
- Kiểm tra credentials (root/123456)
- Kiểm tra database `vegana_store` đã tồn tại chưa
- Kiểm tra port 3306 có bị chiếm không

```yaml
- name: Setup MySQL
  run: |
    mysql -u root -p123456 -e "CREATE DATABASE IF NOT EXISTS vegana_store;"
    mysql -u root -p123456 vegana_store < vegana.sql
```

#### Lỗi: "Chrome not found"

**Giải pháp:**
- Đảm bảo đã install Chrome trong workflow
- Kiểm tra Chrome version compatibility

#### Lỗi: "Tests timeout"

**Giải pháp:**
- Tăng timeout trong TestNG
- Kiểm tra network connectivity
- Review test cases có quá dài không

### Jenkins

#### Lỗi: "Maven not found"

**Giải pháp:**
- Cài đặt Maven trong Global Tool Configuration
- Kiểm tra PATH environment variable

#### Lỗi: "JDK not found"

**Giải pháp:**
- Cài đặt JDK trong Global Tool Configuration
- Kiểm tra JAVA_HOME

#### Lỗi: "Application not starting"

**Giải pháp:**
- Kiểm tra port conflicts
- Kiểm tra MySQL connection
- Thêm retry logic
- Review application logs

#### Lỗi: "Database connection failed"

**Giải pháp:**
- Kiểm tra MySQL service đang chạy: `sudo systemctl status mysql`
- Kiểm tra credentials trong `PersistenceJPAConfig.java`
- Kiểm tra database đã được tạo: `mysql -u root -p123456 -e "SHOW DATABASES;"`
- Import schema: `mysql -u root -p123456 < vegana.sql`
- Kiểm tra firewall rules cho port 3306

---

## 📊 Best Practices

### 1. **Parallel Execution**

Chạy tests song song để tiết kiệm thời gian:

```yaml
strategy:
  matrix:
    browser: [chrome, firefox, edge]
```

### 2. **Cache Dependencies**

Cache Maven dependencies để build nhanh hơn:

```yaml
- uses: actions/cache@v3
  with:
    path: ~/.m2
    key: ${{ runner.os }}-m2-${{ hashFiles('**/pom.xml') }}
```

### 3. **Artifact Retention**

Giữ artifacts trong thời gian hợp lý:

```yaml
retention-days: 30  # Giữ 30 ngày
```

### 4. **Conditional Execution**

Chỉ chạy tests khi cần:

```yaml
if: github.event_name == 'pull_request'
```

### 5. **Notifications**

Thông báo khi tests fail:

```yaml
- name: Notify on Failure
  if: failure()
  run: |
    # Send notification
```

---

## 🎯 Next Steps

1. **Setup GitHub Actions:**
   ```bash
   git add .github/workflows/
   git commit -m "Add CI/CD"
   git push
   ```

2. **Setup Jenkins:**
   - Cài đặt Jenkins
   - Cấu hình tools
   - Tạo pipeline job

3. **Customize:**
   - Thêm notifications
   - Thêm deployment steps
   - Thêm code quality checks

4. **Monitor:**
   - Xem reports thường xuyên
   - Fix failing tests
   - Optimize pipeline

---

## 📚 Tài liệu tham khảo

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)

---

## ✅ Checklist

- [ ] GitHub Actions workflow đã setup
- [ ] Jenkins pipeline đã setup (nếu dùng)
- [ ] MySQL database đã được setup (root/123456)
- [ ] Database `vegana_store` đã được tạo
- [ ] Schema đã được import từ `vegana.sql`
- [ ] Tests chạy thành công trên CI/CD
- [ ] Reports được generate và archive
- [ ] Notifications hoạt động
- [ ] Documentation đã cập nhật

---

**Happy CI/CD! 🚀**

