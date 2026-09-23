# GenC Pulse notification service

The notification service checks active employees at 5:00 PM Monday-Friday. If today's progress update is missing, it sends one reminder email to the employee. After the configured number of recent missed reminders (default: 3), it sends one escalation email to the employee's manager.

Configure these variables in the deployment environment:

- `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`
- `NOTIFICATIONS_TIME_ZONE` (default `Asia/Kolkata`)
- `NOTIFICATIONS_ESCALATION_MISSED_DAYS` (default `3`)
- `NOTIFICATIONS_ENABLED` (default `true`)

The `notification_log` table makes reminders and escalations idempotent, so restarts or repeated scheduler execution do not send duplicates for the same employee/date/type. SMTP credentials must be supplied by the company; no email can be delivered safely with hard-coded credentials.
