package com.auramind.api.dto;

public class AuthDTOs {

    public static class RegisterRequest {
        private String email;
        private String password;
        private String displayName;

        public RegisterRequest() {}

        public RegisterRequest(String email, String password, String displayName) {
            this.email = email;
            this.password = password;
            this.displayName = displayName;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public LoginRequest() {}

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private Long userId;
        private String email;
        private String displayName;
        private String token;

        public AuthResponse() {}

        public AuthResponse(Long userId, String email, String displayName, String token) {
            this.userId = userId;
            this.email = email;
            this.displayName = displayName;
            this.token = token;
        }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getDisplayName() { return displayName; }
        public void setDisplayName(String displayName) { this.displayName = displayName; }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
