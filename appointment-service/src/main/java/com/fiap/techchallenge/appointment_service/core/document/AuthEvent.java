package com.fiap.techchallenge.appointment_service.core.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthEvent {

    private String id;
    private String transactionId;
    private String orderId;
    private AuthEventSource source;
    private AuthEventStatus status;
    private String userId;
    private String username;
    private String userProfile;
    private String eventType;
    private String message;
    private LocalDateTime createdAt;

    public AuthEventStatus getStatus() {
        return this.status;
    }

    public String getEventType() {
        return this.eventType;
    }

    public String getUsername() {
        return this.username;
    }

    public String getUserProfile() {
        return this.userProfile;
    }

    public enum AuthEventSource {
        AUTH_SERVICE,
        API_GATEWAY,
        ORCHESTRATOR_SERVICE;

        public static AuthEventSource fromString(String source) {
            if (source == null) {
                return ORCHESTRATOR_SERVICE;
            }
            try {
                return AuthEventSource.valueOf(source);
            } catch (IllegalArgumentException e) {
                return ORCHESTRATOR_SERVICE;
            }
        }
    }

    public enum AuthEventStatus {
        SUCCESS,
        FAIL,
        ROLLBACK_PENDING,
        ROLLBACK_SUCCESS,
        ROLLBACK_FAIL
    }

    public enum AuthEventType {
        USER_AUTHENTICATED,
        USER_AUTHORIZATION_GRANTED,
        USER_AUTHORIZATION_DENIED,
        TOKEN_VALIDATED,
        TOKEN_EXPIRED,
        LOGIN_SUCCESS,
        LOGIN_FAILED,
        LOGOUT
    }
}