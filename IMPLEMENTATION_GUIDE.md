# GENC Pulse - Daily Status & Commit Tracking System

## Project Overview

A comprehensive employee management system where:
- **Employees** log daily status & GitHub commits every evening (by 5 PM)
- **Managers** monitor team members' updates and commits
- **Admin** manages users, mappings, and system settings
- **Notifications** sent if employees miss updates for 3 consecutive days
- **Email alerts** sent to managers for non-compliant employees

---

## Architecture & Data Models

### 1. **Employee-Manager Mapping**
```
Employee ←→ Manager (Many-to-One)
├── Employee ID
├── Manager ID
├── Assignment Date
└── Status (Active/Inactive)
```

### 2. **Daily Status Model**
```
DailyStatus
├── ID (UUID)
├── Employee ID
├── Status Description (Text)
├── GitHub Commits (List)
│   ├── Commit Hash
│   ├── Repository Name
│   ├── Commit Message
│   └── Timestamp
├── Submitted At (DateTime)
├── Date (YYYY-MM-DD)
└── Status (Submitted/Pending)
```

### 3. **Notification Tracking**
```
NotificationRecord
├── ID (UUID)
├── Employee ID
├── Manager ID
├── Type (MISSING_UPDATE/CONSECUTIVE_MISS)
├── Day Count (1-3)
├── Sent At (DateTime)
├── Email Status
└── Acknowledged (Boolean)
```

### 4. **GitHub Integration**
```
GitHubAccount
├── ID (UUID)
├── Employee ID
├── GitHub Username
├── OAuth Token
├── Repositories (Linked)
└── Last Sync (DateTime)
```

---

## Service Implementation Checklist

### **Employee Service** ✅
- [ ] Create Employee entity with role enum (ADMIN, MANAGER, EMPLOYEE)
- [ ] EmployeeManagerMapping entity for team structure
- [ ] Repository for employee queries by manager
- [ ] REST endpoints:
  - `GET /employees/me` - Get current employee
  - `GET /employees/manager/{managerId}/team` - Get manager's team
  - `POST /employees/manager-mapping` - Create mapping
  - `PUT /employees/{id}` - Update employee

### **Progress Service** ✅
- [ ] DailyStatus entity
- [ ] DailyStatusRepository with queries:
  - Find by employee & date
  - Find pending/missing submissions
  - Find by date range
- [ ] REST endpoints:
  - `POST /status/submit` - Submit daily status
  - `GET /status/my-updates` - Get employee's own updates
  - `GET /status/team/{managerId}` - Manager views team updates
  - `GET /status/pending` - Get pending submissions

### **Commit Service** ✅
- [ ] GitHubCommit entity
- [ ] GitHubAccount entity for OAuth tokens
- [ ] CommitRepository with queries:
  - Find by employee & date
  - Find by repository
  - Aggregations by time period
- [ ] GitHub API Integration:
  - Fetch commits via GitHub REST API
  - Store in DB
- [ ] REST endpoints:
  - `POST /commits/sync` - Sync employee commits
  - `GET /commits/employee/{id}` - Get employee commits
  - `GET /commits/by-date/{date}` - Get commits by date
  - `POST /github-account/link` - Link GitHub account

### **Notification Service** ✅
- [ ] NotificationRecord entity
- [ ] ScheduledTask for daily 5 PM check
- [ ] Logic:
  - Query pending statuses at 5 PM
  - Track consecutive missing days (1st, 2nd, 3rd)
  - Send in-app notifications (1st & 2nd miss)
  - Send email to manager (3rd consecutive miss)
- [ ] REST endpoints:
  - `GET /notifications` - Get user notifications
  - `PUT /notifications/{id}/acknowledge` - Mark as read
  - `GET /notifications/manager/{id}` - Manager's notifications

### **Analytics Service** ✅
- [ ] Dashboard metrics:
  - Daily submission rate (%)
  - Commits per employee
  - Pending submissions
  - Notifications sent
- [ ] Reports:
  - Weekly team performance
  - Individual employee stats
  - Commit activity trends

### **Auth Service** ✅
- [ ] Role-based access control (ADMIN, MANAGER, EMPLOYEE)
- [ ] JWT with role claims
- [ ] Endpoints:
  - `POST /auth/login` - Login
  - `POST /auth/register` - Register (Admin only)
  - `GET /auth/validate` - Validate token
  - `POST /auth/github-oauth` - GitHub OAuth callback

---

## Scheduled Jobs (Using Spring Scheduler)

### **Daily 5 PM Status Check**
```
Cron: 0 0 17 * * ? (5 PM daily)
Action:
1. Query all employees
2. Check if status submitted today
3. If not submitted:
   - Increment missing counter
   - If 1st miss → Send in-app notification
   - If 2nd miss → Send in-app notification + reminder
   - If 3rd miss → Send email to manager
4. Reset counter if status submitted
```

### **Midnight Commit Sync**
```
Cron: 0 0 0 * * ? (Midnight daily)
Action:
1. For each employee with linked GitHub account
2. Fetch commits from last 24 hours
3. Store in commit-service DB
4. Update last sync timestamp
```

---

## Frontend Features Required

### **Employee Dashboard**
- View daily status form
- Input text description
- Auto-fetch today's GitHub commits
- Submit button (active until 5 PM)
- View submission history
- View notifications
- Link GitHub account

### **Manager Dashboard**
- View all team members
- See daily submissions in table/list
- View team member's commits
- Toggle between team view & individual detail
- See notification alerts
- Export reports

### **Admin Dashboard**
- Create/edit employees
- Create manager-employee mappings
- View all notifications
- System settings (notification times, etc.)
- User management

---

## Database Schema Overview

### Employee Table
```sql
CREATE TABLE employees (
  id UUID PRIMARY KEY,
  email VARCHAR UNIQUE NOT NULL,
  name VARCHAR NOT NULL,
  role ENUM('ADMIN', 'MANAGER', 'EMPLOYEE'),
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
```

### Employee Manager Mapping Table
```sql
CREATE TABLE employee_manager_mapping (
  id UUID PRIMARY KEY,
  employee_id UUID,
  manager_id UUID,
  assigned_date TIMESTAMP,
  status ENUM('ACTIVE', 'INACTIVE'),
  FOREIGN KEY (employee_id) REFERENCES employees(id),
  FOREIGN KEY (manager_id) REFERENCES employees(id)
);
```

### Daily Status Table
```sql
CREATE TABLE daily_status (
  id UUID PRIMARY KEY,
  employee_id UUID,
  status_description TEXT,
  submitted_at TIMESTAMP,
  date DATE,
  status ENUM('SUBMITTED', 'PENDING'),
  FOREIGN KEY (employee_id) REFERENCES employees(id)
);
```

### GitHub Commits Table
```sql
CREATE TABLE github_commits (
  id UUID PRIMARY KEY,
  employee_id UUID,
  commit_hash VARCHAR,
  repository_name VARCHAR,
  commit_message TEXT,
  commit_date TIMESTAMP,
  FOREIGN KEY (employee_id) REFERENCES employees(id)
);
```

### Notifications Table
```sql
CREATE TABLE notifications (
  id UUID PRIMARY KEY,
  employee_id UUID,
  manager_id UUID,
  notification_type ENUM('MISSING_UPDATE', 'CONSECUTIVE_MISS'),
  day_count INT,
  sent_at TIMESTAMP,
  email_sent BOOLEAN,
  acknowledged BOOLEAN,
  FOREIGN KEY (employee_id) REFERENCES employees(id),
  FOREIGN KEY (manager_id) REFERENCES employees(id)
);
```

---

## Implementation Steps (Phase-wise)

### **Phase 1: Core Data Models**
1. Create JPA entities for all models
2. Set up repositories
3. Create database migrations

### **Phase 2: Employee & Manager Service**
1. Implement employee CRUD
2. Implement manager-employee mapping
3. Add role-based access control

### **Phase 3: Progress Tracking**
1. Create daily status submission
2. Implement status retrieval by role
3. Add submission validation (only until 5 PM)

### **Phase 4: GitHub Integration**
1. Add OAuth flow for GitHub
2. Implement commit fetching
3. Link commits to daily status

### **Phase 5: Notifications**
1. Create notification records
2. Implement scheduled tasks
3. Configure email service

### **Phase 6: Frontend**
1. Build employee dashboard
2. Build manager dashboard
3. Build admin dashboard

### **Phase 7: Analytics & Reports**
1. Create dashboard endpoints
2. Build reporting UI
3. Add export functionality

---

## API Response Examples

### Submit Daily Status
```json
POST /progress/status/submit
{
  "statusDescription": "Completed feature X, fixed bug Y",
  "commits": [
    {
      "hash": "abc123",
      "repository": "genc-project",
      "message": "Feature: Add login page",
      "timestamp": "2026-09-29T14:30:00Z"
    }
  ]
}

Response: 201 Created
{
  "id": "uuid",
  "employeeId": "uuid",
  "date": "2026-09-29",
  "status": "SUBMITTED",
  "submittedAt": "2026-09-29T16:45:00Z"
}
```

### Get Manager's Team Updates
```json
GET /progress/status/team/manager-uuid?date=2026-09-29
Response: 200 OK
{
  "teamMembers": [
    {
      "employeeId": "uuid",
      "name": "John Doe",
      "statusSubmitted": true,
      "submittedAt": "2026-09-29T16:30:00Z",
      "status": "Completed module A",
      "commitsCount": 3,
      "commits": [...]
    }
  ]
}
```

### Get Notifications
```json
GET /notification/notifications
Response: 200 OK
{
  "notifications": [
    {
      "id": "uuid",
      "type": "MISSING_UPDATE",
      "dayCount": 1,
      "employee": {
        "id": "uuid",
        "name": "Jane Smith"
      },
      "message": "Status update not submitted",
      "createdAt": "2026-09-29T17:05:00Z",
      "acknowledged": false
    }
  ]
}
```

---

## Security Considerations

1. **Authentication**: Use JWT tokens with role claims
2. **Authorization**: Implement @PreAuthorize on endpoints
3. **GitHub OAuth**: Secure token storage (encrypted in DB)
4. **Email Validation**: Verify manager email before sending alerts
5. **Rate Limiting**: Limit API calls per user
6. **Audit Logging**: Log all status submissions & deletions

---

## Testing Strategy

- Unit tests for service logic
- Integration tests for DB operations
- API endpoint tests (MockMvc)
- Scheduler tests with @SchedulerTest
- GitHub API mock tests

---

## Deployment Considerations

1. Docker containers for each microservice
2. Docker Compose for local development
3. Kubernetes manifests for production
4. Environment variables for secrets (GitHub token, email config)
5. Database migrations (Flyway/Liquibase)

---

## Next Steps

1. **Review** this architecture with your team
2. **Create** GitHub project/issues for tracking
3. **Begin Phase 1** with database design
4. **Setup** CI/CD pipeline for automated testing
5. **Communicate** timeline with stakeholders

