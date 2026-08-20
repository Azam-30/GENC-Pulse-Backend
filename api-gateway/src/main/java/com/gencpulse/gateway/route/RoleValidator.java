package com.gencpulse.gateway.route;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class RoleValidator {

    public boolean hasAccess(
            String path,
            String role) {

        if ("ADMIN".equals(role)) {

            return true;
        }

        if ("MANAGER".equals(role)) {

            return path.startsWith("/api/progress")
                    || path.startsWith("/api/commits")
                    || path.startsWith("/api/analytics");
        }

        if ("EMPLOYEE".equals(role)) {

            return path.startsWith("/api/employees")
                    || path.startsWith("/api/progress")
                    || path.startsWith("/api/commits");
        }

        return false;
    }
}